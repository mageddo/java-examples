package com.mageddo.livecollaboration.resource;

import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArraySet;

import jakarta.inject.Singleton;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.SendResult;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

/**
 * To make it work in a distributed system with multiple instances, you have to:
 * 1. Replace NOTE_UPDATES by a centralized Database
 * 2. Every time a new message is needled to be sent to other sessions, a event must be published
 * to the other instances do the same.
 */

@Slf4j
@Singleton
@ServerEndpoint("/notes/ws/{noteId}")
public class NoteWsResource {

  /**
   * Primeiro byte da mensagem indicando "sync/update" (Yjs usa frame type no byte 0).
   */
  private static final byte SYNC_MESSAGE_TYPE = 0;

  /**
   * Retenção máxima de updates por nota (proteção contra crescimento infinito de memória).
   */
  private static final int MAX_STORED_UPDATES = 10_000;

  private static final Map<String, Set<Session>> NOTE_SESSIONS = new ConcurrentHashMap<>();

  private static final Map<String, Deque<byte[]>> NOTE_UPDATES = new ConcurrentHashMap<>();

  @OnOpen
  public void onOpen(Session session, @PathParam("noteId") String noteId) {

    this.addSession(session, noteId);
    this.replayUpdatesTo(session, noteId);

    log.info("ws=opened, noteId={}, session={}", noteId, session.getId());
  }

  private void addSession(Session session, String noteId) {
    final var sessions = this.findSessions(noteId);
    sessions.add(session);
  }

  @OnMessage
  public void onBinary(Session session, byte[] message, @PathParam("noteId") String noteId) {
    if (this.isSyncMessage(message)) {
      this.saveMessage(noteId, message);
      log.debug("ws=receivedSync, noteId={}, session={}", noteId, session.getId());
    } else {
      log.debug("ws=receivedBinaryNonSync, noteId={}, session={}, len={}",
          noteId, session.getId(), message != null ? message.length : 0);
    }
    this.broadcastToOthers(noteId, session.getId(), message);
  }

  @OnMessage
  public void onText(Session session, String text, @PathParam("noteId") String noteId) {
    if (text == null) {
      return;
    }

    if ("ping".equalsIgnoreCase(text)) {
      this.sendTextAsync(session, "pong");
      return;
    }
    log.debug("ws=ignoredText, noteId={}, session={}, text='{}'", noteId, session.getId(), text);
  }

  @OnClose
  public void onClose(Session session, @PathParam("noteId") String noteId, CloseReason reason) {
    final var sessions = this.findSessions(noteId);
    if (sessions != null) {
      sessions.remove(session);
      if (sessions.isEmpty()) {
        NOTE_UPDATES.remove(noteId);
        log.info("ws=lastSessionClosed, noteId={}", noteId);
      }
    }
    log.info("ws=closed, noteId={}, session={}, reason={}", noteId, session.getId(), reason);
  }

  @OnError
  public void onError(Session session, Throwable e, @PathParam("noteId") String noteId) {
    final var sessionId = session != null ? session.getId() : "n/a";
    log.error("ws=error, noteId={}, session={}, msg={}", noteId, sessionId, e.getMessage(), e);
  }

  private Set<Session> findSessions(String noteId) {
    NOTE_SESSIONS.computeIfAbsent(noteId, __ -> new CopyOnWriteArraySet<>());
    return NOTE_SESSIONS.get(noteId);
  }

  private Deque<byte[]> findUpdates(String noteId) {
    return NOTE_UPDATES.computeIfAbsent(noteId, __ -> new ConcurrentLinkedDeque<>());
  }

  private void replayUpdatesTo(Session session, String noteId) {
    final var updates = NOTE_UPDATES.get(noteId);
    if (updates == null || updates.isEmpty()) {
      return;
    }

    for (final var update : updates) {
      this.sendBinaryAsync(session, update);
    }
  }

  private void saveMessage(String noteId, byte[] message) {
    if (message == null || message.length == 0) {
      return;
    }

    final var deque = this.findUpdates(noteId);
    deque.addLast(message.clone());

    while (deque.size() > MAX_STORED_UPDATES) {
      deque.pollFirst();
    }
  }

  private void broadcastToOthers(String noteId, String senderSessionId, byte[] message) {
    final var sessions = this.findSessions(noteId);
    if (sessions.isEmpty()) {
      return;
    }

    for (final var current : sessions) {
      if (!current.isOpen()) {
        continue;
      }
      if (Objects.equals(current.getId(), senderSessionId)) {
        continue;
      }

      this.sendBinaryAsync(current, message);
      log.debug("ws=sentToClient, noteId={}, toSession={}", noteId, current.getId());
    }
  }

  private boolean isSyncMessage(byte[] message) {
    if (message == null || message.length == 0) {
      return false;
    }
    // compara como unsigned para evitar problemas de sinal
    return Byte.toUnsignedInt(message[0]) == SYNC_MESSAGE_TYPE;
  }

  private void sendBinaryAsync(Session session, byte[] payload) {
    if (session == null || payload == null) {
      return;
    }
    session
        .getAsyncRemote()
        .sendBinary(ByteBuffer.wrap(payload), this::logSendOutcome);
  }

  private void sendTextAsync(Session session, String text) {
    if (session == null || text == null) {
      return;
    }
    session
        .getAsyncRemote()
        .sendText(text, this::logSendOutcome);
  }

  private void logSendOutcome(SendResult result) {
    if (result.isOK()) {
      return;
    }
    final var ex = result.getException();
    log.warn("ws=sendFailed, msg={}", ex != null ? ex.getMessage() : "unknown", ex);
  }
}
