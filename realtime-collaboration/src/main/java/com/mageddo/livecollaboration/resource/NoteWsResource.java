package com.mageddo.livecollaboration.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

@Slf4j
@Singleton
@ServerEndpoint("/notes/ws/{noteId}")
public class NoteWsResource {

  private static final Map<String, Set<Session>> NOTE_SESSIONS = new ConcurrentHashMap<>();
  private static final Map<String, List<byte[]>> NOTE_UPDATES = new ConcurrentHashMap<>();
  private static final Map<String, Map<Integer, AwarenessState>> NOTE_AWARENESS = new ConcurrentHashMap<>();
  private static final Map<Session, Set<Integer>> SESSION_CLIENTS = new ConcurrentHashMap<>();

  private static final int SYNC_MESSAGE_TYPE = 0;
  private static final int AWARENESS_MESSAGE_TYPE = 1;
  private static final int AWARENESS_QUERY_MESSAGE_TYPE = 2;

  @OnOpen
  public void onOpen(
      final Session session,
      @PathParam("noteId") final String noteId
  ) throws IOException {
    final var sessions = NOTE_SESSIONS.computeIfAbsent(noteId, k -> new CopyOnWriteArraySet<>());
    sessions.add(session);
    SESSION_CLIENTS.put(session, ConcurrentHashMap.newKeySet());

    this.sendStoredUpdates(session, noteId);
    this.sendAwarenessSnapshot(session, noteId);
    this.requestAwarenessRefresh(noteId, session);

    log.info("Session {} joined note {}", session.getId(), noteId);
  }

  @OnMessage
  public void onBinary(
      final Session session,
      final byte[] message,
      @PathParam("noteId") final String noteId
  ) {
    if (message == null || message.length == 0) {
      log.debug("status=ignoreEmptyMessage, session={}", session.getId());
      return;
    }
    final var messageType = message[0] & 0xFF;
    switch (messageType) {
      case SYNC_MESSAGE_TYPE -> this.handleSyncMessage(session, noteId, message);
      case AWARENESS_MESSAGE_TYPE -> this.handleAwarenessMessage(session, noteId, message);
      case AWARENESS_QUERY_MESSAGE_TYPE -> log.debug("status=ignoreClientQuery, session={}", session.getId());
      default -> log.warn("status=unknownMessageType, session={}, type={}", session.getId(), messageType);
    }
  }

  private void handleSyncMessage(
      final Session session,
      final String noteId,
      final byte[] message
  ) {
    NOTE_UPDATES
        .computeIfAbsent(noteId, k -> Collections.synchronizedList(new ArrayList<>()))
        .add(message.clone());
    log.debug("status=newUpdateStored, session={}", session.getId());
    this.broadcast(noteId, session, message);
  }

  private void handleAwarenessMessage(
      final Session session,
      final String noteId,
      final byte[] message
  ) {
    if (message.length <= 1) {
      return;
    }
    final var payload = Arrays.copyOfRange(message, 1, message.length);
    final var buffer = ByteBuffer.wrap(payload);
    final var updates = this.readVarUint(buffer);
    final var awarenessStates = NOTE_AWARENESS.computeIfAbsent(noteId, k -> new ConcurrentHashMap<>());
    final var clientIds = SESSION_CLIENTS.computeIfAbsent(session, k -> ConcurrentHashMap.newKeySet());

    for (var index = 0; index < updates; index++) {
      final var clientId = this.readVarUint(buffer);
      final var clock = this.readVarUint(buffer);
      final var stateJson = this.readVarString(buffer);
      final var existing = awarenessStates.get(clientId);
      final var isRemoval = this.isNullState(stateJson);
      final var shouldUpdate = existing == null
          || existing.clock() < clock
          || (existing.clock() == clock && isRemoval);
      if (!shouldUpdate) {
        continue;
      }
      if (isRemoval) {
        awarenessStates.remove(clientId);
        clientIds.remove(clientId);
      } else {
        awarenessStates.put(clientId, new AwarenessState(clientId, clock, stateJson));
        clientIds.add(clientId);
      }
    }

    this.broadcast(noteId, session, message);
  }

  private void sendStoredUpdates(final Session session, final String noteId) {
    final var updates = NOTE_UPDATES.get(noteId);
    if (updates == null) {
      return;
    }
    for (final var update : updates) {
      session
          .getAsyncRemote()
          .sendBinary(ByteBuffer.wrap(update), this::handleDataSend);
    }
  }

  private void sendAwarenessSnapshot(final Session session, final String noteId) {
    final var states = NOTE_AWARENESS.get(noteId);
    if (states == null || states.isEmpty()) {
      return;
    }
    final var activeStates = states
        .values()
        .stream()
        .filter(AwarenessState::hasState)
        .toList();
    if (activeStates.isEmpty()) {
      return;
    }
    final var message = this.createAwarenessMessage(activeStates);
    session
        .getAsyncRemote()
        .sendBinary(ByteBuffer.wrap(message), this::handleDataSend);
  }

  private void requestAwarenessRefresh(final String noteId, final Session session) {
    final var sessions = NOTE_SESSIONS.getOrDefault(noteId, Set.of());
    final var requestMessage = ByteBuffer.wrap(new byte[]{(byte) AWARENESS_QUERY_MESSAGE_TYPE});
    for (final var current : sessions) {
      if (!Objects.equals(current.getId(), session.getId()) && current.isOpen()) {
        current.getAsyncRemote().sendBinary(requestMessage, this::handleDataSend);
      }
    }
  }

  private void broadcast(
      final String noteId,
      final Session origin,
      final byte[] message
  ) {
    final var sessions = NOTE_SESSIONS.getOrDefault(noteId, Set.of());
    for (final var current : sessions) {
      if (!Objects.equals(current.getId(), origin.getId()) && current.isOpen()) {
        current
            .getAsyncRemote()
            .sendBinary(ByteBuffer.wrap(message), this::handleDataSend);
      }
    }
  }

  private byte[] createAwarenessMessage(final Collection<AwarenessState> states) {
    final var payload = this.buildAwarenessPayload(states);
    final var envelope = new byte[payload.length + 1];
    envelope[0] = (byte) AWARENESS_MESSAGE_TYPE;
    System.arraycopy(payload, 0, envelope, 1, payload.length);
    return envelope;
  }

  private byte[] buildAwarenessPayload(final Collection<AwarenessState> states) {
    final var output = new ByteArrayOutputStream();
    this.writeVarUint(output, states.size());
    for (final var state : states) {
      this.writeVarUint(output, state.clientId());
      this.writeVarUint(output, state.clock());
      this.writeVarString(output, state.stateJson());
    }
    return output.toByteArray();
  }

  private int readVarUint(final ByteBuffer buffer) {
    var value = 0;
    var shift = 0;
    while (buffer.hasRemaining()) {
      final var current = buffer.get() & 0xFF;
      value |= (current & 0x7F) << shift;
      if ((current & 0x80) == 0) {
        break;
      }
      shift += 7;
    }
    return value;
  }

  private String readVarString(final ByteBuffer buffer) {
    final var length = this.readVarUint(buffer);
    if (length == 0) {
      return "";
    }
    final var bytes = new byte[length];
    buffer.get(bytes);
    return new String(bytes, StandardCharsets.UTF_8);
  }

  private void writeVarUint(final ByteArrayOutputStream output, final int value) {
    var current = value;
    while ((current & ~0x7F) != 0) {
      output.write((current & 0x7F) | 0x80);
      current >>>= 7;
    }
    output.write(current & 0x7F);
  }

  private void writeVarString(final ByteArrayOutputStream output, final String value) {
    final var data = value == null ? "null" : value;
    final var bytes = data.getBytes(StandardCharsets.UTF_8);
    this.writeVarUint(output, bytes.length);
    output.writeBytes(bytes);
  }

  private boolean isNullState(final String value) {
    return value == null || "null".equals(value);
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
    this.handleSessionRemoval(session, noteId);
    log.info("Session {} closed for note {}. Reason: {}", session.getId(), noteId, reason);
  }

  private void handleSessionRemoval(final Session session, final String noteId) {
    final var clientIds = SESSION_CLIENTS.remove(session);
    if (clientIds == null || clientIds.isEmpty()) {
      return;
    }
    final var awarenessStates = NOTE_AWARENESS.get(noteId);
    if (awarenessStates == null) {
      return;
    }
    final var updates = new ArrayList<AwarenessState>();
    for (final var clientId : clientIds) {
      final var existing = awarenessStates.remove(clientId);
      final var nextClock = existing == null ? 0 : existing.clock() + 1;
      updates.add(new AwarenessState(clientId, nextClock, null));
    }
    if (updates.isEmpty()) {
      return;
    }
    final var message = this.createAwarenessMessage(updates);
    this.broadcast(noteId, session, message);
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

  private void handleDataSend(final SendResult sendResult) {
    if (!sendResult.isOK()) {
      final var ex = sendResult.getException();
      log.warn("status=failedToSendData, msg={}", ex.getMessage(), ex);
    }
  }

  private record AwarenessState(int clientId, int clock, String stateJson) {
    boolean hasState() {
      return stateJson != null && !"null".equals(stateJson);
    }
  }
}
