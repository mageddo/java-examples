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

const changelog = document.getElementById('changelog');
const participantsContainer = document.getElementById('participants');
const titleInput = document.getElementById('note-title');
const tagsInput = document.getElementById('note-tags');

function safeRandomUUID() {
  if (crypto?.randomUUID) {
    return crypto.randomUUID();
  }
   return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    const r = Math.random() * 16 | 0;
    const v = c === 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

const log = (message) => {
    const timestamp = new Date().toLocaleTimeString();
    changelog.textContent += `[${timestamp}] ${message}\n`;
    changelog.scrollTop = changelog.scrollHeight;
};

const resolveNoteId = () => {
    const raw = window.location.hash.replace('#', '').trim();
    if (raw.length > 0) {
        return raw;
    }
    const generated = safeRandomUUID();
    window.history.replaceState(null, '', `${window.location.pathname}#${generated}`);
    return generated;
};

const noteId = resolveNoteId();
document.title = `Live Note ${noteId}`;
log(`Editor iniciado para a nota ${noteId}.`);

const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws';
const websocketUrl = `${protocol}://${window.location.host}/notes/ws`;

const doc = new Y.Doc();
const provider = new WebsocketProvider(websocketUrl, noteId, doc);
const awareness = provider.awareness;

const userName = sessionStorage.getItem('live-user') ?? `Usuário ${safeRandomUUID().slice(0, 4)}`;
sessionStorage.setItem('live-user', userName);
const palette = ['#0d6efd', '#6610f2', '#6f42c1', '#198754', '#20c997', '#ffc107', '#fd7e14', '#dc3545'];
const userColor = sessionStorage.getItem('live-user-color') ?? palette[Math.floor(Math.random() * palette.length)];
sessionStorage.setItem('live-user-color', userColor);
awareness.setLocalStateField('user', { name: userName, color: userColor });

const yTitle = doc.getText('title');
const yTags = doc.getText('tags');
const yContent = doc.getText('content');

const applyTextToInput = (yText, input, description) => {
    const currentValue = yText.toString();
    if (input.value !== currentValue) {
        input.value = currentValue;
    }
    yText.observe(event => {
        if (!event.transaction.local) {
            const value = yText.toString();
            if (input.value !== value) {
                input.value = value;
            }
            log(`${description} atualizado por outro colaborador (conflito resolvido automaticamente).`);
        }
    });
    input.addEventListener('input', () => {
        const newValue = input.value;
        if (newValue === yText.toString()) {
            return;
        }
        doc.transact(() => {
            yText.delete(0, yText.length);
            yText.insert(0, newValue);
        }, description);
    });
};

applyTextToInput(yTitle, titleInput, 'Título');
applyTextToInput(yTags, tagsInput, 'Tags');

const editorParent = document.getElementById('editor');
const editorExtensions = [
    lineNumbers(),
    highlightActiveLine(),
    drawSelection(),
    history(),
    syntaxHighlighting(defaultHighlightStyle, { fallback: true }),
    keymap.of([...defaultKeymap, ...historyKeymap]),
    yCollab(yContent, awareness, { awarenessField: 'user' })
];

const editorState = EditorState.create({
    doc: yContent.toString(),
    extensions: editorExtensions
});

const editorView = new EditorView({
    state: editorState,
    parent: editorParent
});

void editorView;

yContent.observe(event => {
    if (!event.transaction.local) {
        log('Descrição atualizada por outro colaborador (conflito resolvido automaticamente).');
    }
});

const renderParticipants = () => {
    participantsContainer.innerHTML = '';
    const states = Array.from(awareness.getStates().values());
    if (!states.length) {
        const span = document.createElement('span');
        span.className = 'text-secondary';
        span.textContent = 'Nenhum participante ativo.';
        participantsContainer.appendChild(span);
        return;
    }
    states.forEach(state => {
        const user = state.user;
        if (!user) {
            return;
        }
        const badge = document.createElement('span');
        badge.className = 'badge rounded-pill';
        badge.style.backgroundColor = user.color;
        badge.textContent = `${user.name}${state === awareness.getLocalState() ? ' (Você)' : ''}`;
        participantsContainer.appendChild(badge);
    });
};

const parseTags = (raw) => raw.split(',').map(tag => tag.trim()).filter(tag => tag.length > 0);

const buildPayload = () => ({
    id: noteId,
    title: yTitle.toString(),
    content: yContent.toString(),
    tags: parseTags(yTags.toString())
});

let leaderClientId = null;
let syncIntervalId = null;

const stopLeaderSync = () => {
    if (syncIntervalId !== null) {
        clearInterval(syncIntervalId);
        syncIntervalId = null;
    }
};

const sendSnapshot = async () => {
    const payload = buildPayload();
    try {
        const response = await fetch(`/notes/${encodeURIComponent(noteId)}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        log('Snapshot sincronizado com o servidor.');
    } catch (error) {
        log(`Falha ao sincronizar snapshot: ${error.message}`);
    }
};

const startLeaderSync = () => {
    if (syncIntervalId !== null) {
        return;
    }
    log('Você é o líder da edição. Enviando snapshots a cada 3 minutos.');
    syncIntervalId = setInterval(sendSnapshot, 180000);
    void sendSnapshot();
};

const evaluateLeadership = () => {
    const clients = Array.from(awareness.getStates().keys());
    if (!clients.length) {
        stopLeaderSync();
        leaderClientId = null;
        return;
    }
    const nextLeader = Math.min(...clients);
    if (leaderClientId === nextLeader) {
        return;
    }
    leaderClientId = nextLeader;
    if (leaderClientId === doc.clientID) {
        startLeaderSync();
    } else {
        stopLeaderSync();
        log('Outro colaborador assumiu a liderança da edição.');
    }
};

awareness.on('change', () => {
    renderParticipants();
    evaluateLeadership();
});

provider.on('status', ({ status }) => {
    log(`WebSocket ${status}.`);
});

provider.on('sync', (isSynced) => {
    log(`Documento sincronizado: ${isSynced ? 'sim' : 'não'}.`);
    if (isSynced) {
        titleInput.value = yTitle.toString();
        tagsInput.value = yTags.toString();
    }
});

provider.on('connection-close', (event) => {
    log(`Conexão encerrada: ${event?.code ?? 'n/d'}. Tentando reconectar...`);
});

provider.on('connection-error', (event) => {
    log(`Erro de conexão: ${event?.message ?? 'desconhecido'}. Tentando reconectar em background.`);
});

window.addEventListener('beforeunload', () => {
    stopLeaderSync();
    log('Encerrando sessão local.');
});

renderParticipants();
evaluateLeadership();
