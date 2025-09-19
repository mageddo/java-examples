package com.mageddo.livecollaboration.resource;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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

  private static final Map<String, Set<Session>> NOTE_SESSIONS = new ConcurrentHashMap<>();
  private static final Map<String, List<byte[]>> NOTE_UPDATES = new ConcurrentHashMap<>();
  private static final int SYNC_MESSAGE_TYPE = 0;

  @OnOpen
  public void onOpen(
      final Session session,
      @PathParam("noteId") final String noteId
  ) throws IOException {
    final var sessions = NOTE_SESSIONS.computeIfAbsent(noteId, k -> new CopyOnWriteArraySet<>());
    sessions.add(session);

    final var updates = NOTE_UPDATES.get(noteId);
    if (updates != null) {
      for (final var update : updates) {
        session
            .getAsyncRemote()
            .sendBinary(ByteBuffer.wrap(update), this::handleDataSend);
      }
    }
    log.info("Session {} joined note {}", session.getId(), noteId);
  }

  @OnMessage
  public void onBinary(
      final Session session,
      final byte[] message,
      @PathParam("noteId") final String noteId
  ) {
    if (this.isSyncMessage(message)) {
      NOTE_UPDATES
          .computeIfAbsent(noteId, k -> Collections.synchronizedList(new ArrayList<>()))
          .add(message.clone());
      log.debug("status=newMessage, session={}", session.getId());
    }

    final var sessions = NOTE_SESSIONS.getOrDefault(noteId, Set.of());
    for (final var current : sessions) {
      if (!Objects.equals(current.getId(), session.getId()) && current.isOpen()) {
        log.debug("status=sendToClient, session={}", current.getId());
        current
            .getAsyncRemote()
            .sendBinary(ByteBuffer.wrap(message), this::handleDataSend);
      }
    }
  }

  private void handleDataSend(SendResult sendResult) {
    if (!sendResult.isOK()) {
      final var ex = sendResult.getException();
      log.warn("status=failedToSendData, msg={}", ex.getMessage(), ex);
    }
  }

  @OnMessage
  public void onText(
      final Session session,
      final String text,
      @PathParam("noteId") final String noteId
  ) throws IOException {
    if ("ping".equalsIgnoreCase(text)) {
      session
          .getAsyncRemote()
          .sendText("pong", this::handleDataSend);
    } else {
      log.debug("Ignoring text message {} for note {}", text, noteId);
    }
  }

  @OnClose
  public void onClose(
      final Session session,
      @PathParam("noteId") final String noteId,
      final CloseReason reason
  ) {
    final var sessions = NOTE_SESSIONS.get(noteId);
    if (sessions != null) {
      sessions.remove(session);
      if (sessions.isEmpty()) {
        log.info("Closing last session for note {}", noteId);
      }
    }
    log.info("Session {} closed for note {}. Reason: {}", session.getId(), noteId, reason);
  }

  @OnError
  public void onError(
      final Session session,
      final Throwable e,
      @PathParam("noteId") final String noteId
  ) {
    log.error("WebSocket error on note {} for session {}", noteId, session != null ?
        session.getId() : "n/a", e);
  }

  private boolean isSyncMessage(final byte[] message) {
    if (message == null || message.length == 0) {
      return false;
    }
    return (message[0] & 0xFF) == SYNC_MESSAGE_TYPE;
  }
}
