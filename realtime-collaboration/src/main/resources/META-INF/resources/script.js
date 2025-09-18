import * as Y from "https://esm.sh/yjs@13.6.15?target=es2022&pin=v135";
import { EditorState } from "https://esm.sh/@codemirror/state@6.4.1?target=es2022&pin=v135";
import {
    EditorView,
    keymap,
    drawSelection,
    highlightActiveLine,
    lineNumbers
} from "https://esm.sh/@codemirror/view@6.28.5?target=es2022&pin=v135";
import {
    history,
    historyKeymap,
    defaultKeymap
} from "https://esm.sh/@codemirror/commands@6.5.0?target=es2022&pin=v135";
import { defaultHighlightStyle, syntaxHighlighting } from "https://esm.sh/@codemirror/language@6.10.2?target=es2022&pin=v135";
import { yCollab } from "https://esm.sh/y-codemirror.next@0.3.1?target=es2022&pin=v135&deps=yjs@13.6.15,@codemirror/state@6.4.1,@codemirror/view@6.28.5";
import {
    Awareness,
    applyAwarenessUpdate,
    encodeAwarenessUpdate
} from "https://esm.sh/y-protocols@1.0.6/awareness?target=es2022&pin=v135&deps=yjs@13.6.15";


const changelog = document.getElementById('changelog');
const participantsContainer = document.getElementById('participants');
const titleInput = document.getElementById('note-title');
const tagsInput = document.getElementById('note-tags');

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
    const generated = crypto.randomUUID();
    window.history.replaceState(null, '', `${window.location.pathname}#${generated}`);
    return generated;
};

const noteId = resolveNoteId();
document.title = `Live Note ${noteId}`;
log(`Editor iniciado para a nota ${noteId}.`);

const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws';
const websocketUrl = `${protocol}://${window.location.host}/notes/ws/${encodeURIComponent(noteId)}`;

const doc = new Y.Doc();
const awareness = new Awareness(doc);

const SYNC_MESSAGE_TYPE = 0;
const AWARENESS_MESSAGE_TYPE = 1;
const AWARENESS_QUERY_MESSAGE_TYPE = 2;

let websocket = null;
const pendingMessages = [];
let reconnectDelay = 1000;
let reconnectHandle = null;

const queueMessage = (message) => {
    pendingMessages.push(message);
};

const flushPendingMessages = () => {
    if (!websocket || websocket.readyState !== WebSocket.OPEN) {
        return;
    }
    while (pendingMessages.length) {
        const message = pendingMessages.shift();
        websocket.send(message);
    }
};

const scheduleReconnect = () => {
    if (reconnectHandle !== null) {
        return;
    }
    reconnectHandle = window.setTimeout(() => {
        reconnectHandle = null;
        reconnectDelay = Math.min(reconnectDelay * 2, 10000);
        log('Tentando reconectar ao servidor de edição...');
        connectWebsocket();
    }, reconnectDelay);
};

const sendBinaryMessage = (type, payload = new Uint8Array(0)) => {
    const envelope = new Uint8Array(payload.length + 1);
    envelope[0] = type;
    envelope.set(payload, 1);
    if (websocket && websocket.readyState === WebSocket.OPEN) {
        websocket.send(envelope);
        return;
    }
    queueMessage(envelope);
};

const broadcastLocalAwarenessState = () => {
    const update = encodeAwarenessUpdate(awareness, [doc.clientID]);
    sendBinaryMessage(AWARENESS_MESSAGE_TYPE, update);
};

const sendAwarenessUpdate = (clients) => {
    if (!clients.length) {
        return;
    }
    const update = encodeAwarenessUpdate(awareness, clients);
    sendBinaryMessage(AWARENESS_MESSAGE_TYPE, update);
};

const handleBinaryMessage = (data) => {
    if (!data.length) {
        return;
    }
    const type = data[0];
    const payload = data.slice(1);
    switch (type) {
        case SYNC_MESSAGE_TYPE:
            Y.applyUpdate(doc, payload, 'remote');
            break;
        case AWARENESS_MESSAGE_TYPE:
            applyAwarenessUpdate(awareness, payload, 'remote');
            break;
        case AWARENESS_QUERY_MESSAGE_TYPE:
            broadcastLocalAwarenessState();
            break;
        default:
            log(`Tipo de mensagem desconhecido recebido: ${type}.`);
            break;
    }
};

const handleWebsocketMessage = (event) => {
    if (typeof event.data === 'string') {
        if (event.data === 'pong') {
            log('Servidor respondeu ao ping.');
            return;
        }
        log(`Mensagem de texto ignorada: ${event.data}.`);
        return;
    }
    const data = new Uint8Array(event.data);
    handleBinaryMessage(data);
};

const onWebsocketOpen = () => {
    reconnectDelay = 1000;
    log('WebSocket conectado.');
    flushPendingMessages();
    broadcastLocalAwarenessState();
};

const handleWebsocketClose = (event) => {
    log(`Conexão encerrada: ${event?.code ?? 'n/d'}. Tentando reconectar...`);
    scheduleReconnect();
};

const handleWebsocketError = (event) => {
    log(`Erro de conexão: ${event?.message ?? 'desconhecido'}. Tentando reconectar em background.`);
    if (websocket) {
        websocket.close();
    }
};

const connectWebsocket = () => {
    if (websocket) {
        websocket.removeEventListener('open', onWebsocketOpen);
        websocket.removeEventListener('close', handleWebsocketClose);
        websocket.removeEventListener('message', handleWebsocketMessage);
        websocket.removeEventListener('error', handleWebsocketError);
    }
    websocket = new WebSocket(websocketUrl);
    websocket.binaryType = 'arraybuffer';
    websocket.addEventListener('open', onWebsocketOpen);
    websocket.addEventListener('close', handleWebsocketClose);
    websocket.addEventListener('message', handleWebsocketMessage);
    websocket.addEventListener('error', handleWebsocketError);
};

connectWebsocket();

const userName = sessionStorage.getItem('live-user') ?? `Usuário ${crypto.randomUUID().slice(0, 4)}`;
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

const sendDocumentUpdate = (update, origin) => {
    if (origin === 'remote') {
        return;
    }
    sendBinaryMessage(SYNC_MESSAGE_TYPE, update);
};

doc.on('update', sendDocumentUpdate);

yContent.observe(event => {
    if (!event.transaction.local) {
        log('Descrição atualizada por outro colaborador (conflito resolvido automaticamente).');
    }
});

awareness.on('update', ({ added, updated, removed }, origin) => {
    if (origin === 'remote') {
        return;
    }
    const changed = [...added, ...updated, ...removed];
    sendAwarenessUpdate(changed);
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

window.addEventListener('beforeunload', () => {
    stopLeaderSync();
    awareness.setLocalState(null);
    log('Encerrando sessão local.');
});

renderParticipants();
evaluateLeadership();

