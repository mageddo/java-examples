import * as Y from "yjs";
import { WebsocketProvider } from "y-websocket";
import { EditorState } from "@codemirror/state";
import {
  EditorView,
  keymap,
  drawSelection,
  highlightActiveLine,
  lineNumbers
} from "@codemirror/view";
import {
  history,
  historyKeymap,
  defaultKeymap
} from "@codemirror/commands";
import { defaultHighlightStyle, syntaxHighlighting } from "@codemirror/language";
import { yCollab } from "y-codemirror.next";

/** Resolve e valida elementos do DOM com mensagem clara caso não exista */
function mustGet(id) {
  const el = document.getElementById(id);
  if (!el) {
    throw new Error(`Elemento "${id}" não encontrado no DOM`);
  }
  return el;
}

const changelog = mustGet("changelog");
const participantsEl = mustGet("participants");
const titleInput = mustGet("note-title");
const tagsInput  = mustGet("note-tags");
const editorParent = mustGet("editor");

const WS_PATH = "/notes/ws";
const SNAPSHOT_MS = 180_000;
const AWARENESS_FIELD = "user";
const USER_COLOR_PALETTE = [
  "#0d6efd", "#6610f2", "#6f42c1", "#198754",
  "#20c997", "#ffc107", "#fd7e14", "#dc3545"
];

function safeRandomUUID() {
  if (globalThis.crypto?.randomUUID) {
    return crypto.randomUUID();
  }
  // fallback RFC4122 v4 “good enough” para uso não-criptográfico
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, c => {
    const r = (Math.random() * 16) | 0;
    const v = c === "x" ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
}

function log(message) {
  const ts = new Date().toLocaleTimeString();
  changelog.textContent += `[${ts}] ${message}\n`;
  changelog.scrollTop = changelog.scrollHeight;
}

function resolveNoteId() {
  const raw = location.hash.replace("#", "").trim();
  if (raw) return raw;
  const generated = safeRandomUUID();
  if (typeof window !== "undefined" && window.history?.replaceState) {
    window.history.replaceState(null, "", `${location.pathname}#${generated}`);
  } else {
    location.hash = generated;
  }
  return generated;
}

function websocketBaseUrl() {
  const scheme = location.protocol === "https:" ? "wss" : "ws";
  return `${scheme}://${location.host}${WS_PATH}`;
}

function getOrSetSession(key, compute) {
  const existing = sessionStorage.getItem(key);
  if (existing != null) return existing;
  const val = compute();
  sessionStorage.setItem(key, val);
  return val;
}

function parseTags(raw) {
  return raw
    .split(",")
    .map(t => t.trim())
    .filter(Boolean);
}

function bindYTextToInput(yText, inputEl, label, doc) {
  const current = yText.toString();
  if (inputEl.value !== current) inputEl.value = current;

  yText.observe(event => {
    if (event.transaction?.local) return;
    const value = yText.toString();
    if (inputEl.value !== value) {
      inputEl.value = value;
      log(`${label} atualizado por outro colaborador (merge automático).`);
    }
  });

  inputEl.addEventListener("input", () => {
    const newValue = inputEl.value;
    if (newValue === yText.toString()) return;
    doc.transact(() => {
      yText.delete(0, yText.length);
      yText.insert(0, newValue);
    }, label);
  });
}

const noteId = resolveNoteId();
document.title = `Live Note ${noteId}`;
log(`Editor iniciado para a nota ${noteId}.`);

const doc = new Y.Doc();
const provider = new WebsocketProvider(websocketBaseUrl(), noteId, doc);
const awareness = provider.awareness;

const userName = getOrSetSession("live-user", () => `Usuário ${safeRandomUUID().slice(0, 4)}`);
const userColor = getOrSetSession(
  "live-user-color",
  () => USER_COLOR_PALETTE[Math.floor(Math.random() * USER_COLOR_PALETTE.length)]
);

awareness.setLocalStateField(AWARENESS_FIELD, { name: userName, color: userColor });

const yTitle   = doc.getText("title");
const yTags    = doc.getText("tags");
const yContent = doc.getText("content");

bindYTextToInput(yTitle, titleInput, "Título", doc);
bindYTextToInput(yTags,  tagsInput,  "Tags",   doc);


const editorState = EditorState.create({
  doc: yContent.toString(),
  extensions: [
    lineNumbers(),
    highlightActiveLine(),
    drawSelection(),
    history(),
    syntaxHighlighting(defaultHighlightStyle, { fallback: true }),
    keymap.of([...defaultKeymap, ...historyKeymap]),
    yCollab(yContent, awareness, { awarenessField: AWARENESS_FIELD })
  ]
});

const editorView = new EditorView({ state: editorState, parent: editorParent });
void editorView;

yContent.observe(evt => {
  if (!evt.transaction?.local) {
    log("Descrição atualizada por outro colaborador (merge automático).");
  }
});

function renderParticipants() {
  participantsEl.innerHTML = "";

  const states = Array.from(awareness.getStates().values());
  if (states.length === 0) {
    const span = document.createElement("span");
    span.className = "text-secondary";
    span.textContent = "Nenhum participante ativo.";
    participantsEl.appendChild(span);
    return;
  }

  for (const state of states) {
    const user = state[AWARENESS_FIELD];
    if (!user) continue;

    const badge = document.createElement("span");
    badge.className = "badge rounded-pill";
    badge.style.backgroundColor = user.color;
    const isMe = state === awareness.getLocalState();
    badge.textContent = `${user.name}${isMe ? " (Você)" : ""}`;
    participantsEl.appendChild(badge);
  }
}

let leaderClientId = null;
let snapshotTimer  = null;

function buildSnapshotPayload() {
  return {
    id: noteId,
    title: yTitle.toString(),
    content: yContent.toString(),
    tags: parseTags(yTags.toString())
  };
}

async function sendSnapshot() {
  const payload = buildSnapshotPayload();
  try {
    const res = await fetch(`/notes/${encodeURIComponent(noteId)}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    log("Snapshot sincronizado com o servidor.");
  } catch (err) {
    log(`Falha ao sincronizar snapshot: ${/** @type {Error} */(err).message}`);
  }
}

function startLeaderSync() {
  if (snapshotTimer != null) return;
  log("Você é o líder da edição. Enviando snapshots a cada 3 minutos.");
  snapshotTimer = setInterval(sendSnapshot, SNAPSHOT_MS);
  void sendSnapshot();
}

function stopLeaderSync() {
  if (snapshotTimer == null) return;
  clearInterval(snapshotTimer);
  snapshotTimer = null;
}

function evaluateLeadership() {
  const clientIds = Array.from(awareness.getStates().keys());
  if (clientIds.length === 0) {
    stopLeaderSync();
    leaderClientId = null;
    return;
  }
  const nextLeader = Math.min(...clientIds);
  if (leaderClientId === nextLeader) return;

  leaderClientId = nextLeader;
  if (leaderClientId === doc.clientID) {
    startLeaderSync();
  } else {
    stopLeaderSync();
    log("Outro colaborador assumiu a liderança da edição.");
  }
}

awareness.on("change", () => {
  renderParticipants();
  evaluateLeadership();
});

provider.on("status", ({ status }) => {
  log(`WebSocket ${status}.`);
});

provider.on("sync", (isSynced) => {
  log(`Documento sincronizado: ${isSynced ? "sim" : "não"}.`);
  if (isSynced) {
    // garantir inputs hidratados após sync inicial
    titleInput.value = yTitle.toString();
    tagsInput.value  = yTags.toString();
  }
});

provider.on("connection-close", (event) => {
  log(`Conexão encerrada: ${event?.code ?? "n/d"}. Tentando reconectar...`);
});

provider.on("connection-error", (event) => {
  log(`Erro de conexão: ${event?.message ?? "desconhecido"}. Tentando reconectar em background.`);
});

addEventListener("beforeunload", () => {
  stopLeaderSync();
  log("Encerrando sessão local.");
});

renderParticipants();
evaluateLeadership();
