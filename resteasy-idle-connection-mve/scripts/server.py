import itertools
import json
import socket
import select
import ssl
import sys
import threading
import time
from pathlib import Path

root = Path(sys.argv[1]).resolve()
context = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
context.load_cert_chain(root / 'cert.pem', root / 'key.pem')
context.minimum_version = ssl.TLSVersion.TLSv1_2
counter = itertools.count(1)
lock = threading.Lock()

def log(event, **details):
    with lock:
        print(json.dumps(dict(time=time.time(), event=event, **details)), flush=True)

def handle(raw):
    connection = next(counter)
    try:
        with context.wrap_socket(raw, server_side=True) as stream:
            log('accepted', connection=connection, tls=stream.version())
            pending = b''
            requests = 0
            while True:
                while b'\r\n\r\n' not in pending:
                    received = stream.recv(8192)
                    if not received:
                        log('eof', connection=connection)
                        return
                    pending += received
                header, pending = pending.split(b'\r\n\r\n', 1)
                path = header.split(b' ')[1].decode()
                requests += 1
                log('request', connection=connection, number=requests, path=path)
                if path.startswith('/silent') and requests > 1:
                    log('silent_after_reuse', connection=connection)
                    threading.Event().wait()
                    return
                body = f'connection={connection} request={requests}\n'.encode()
                advertised = b''
                if path == '/healthy-server-short':
                    advertised = b'Keep-Alive: timeout=2\r\n'
                stream.sendall(b'HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: ' + str(len(body)).encode() + b'\r\nConnection: keep-alive\r\n' + advertised + b'\r\n' + body)
                log('response', connection=connection, number=requests)
                if path.startswith('/close'):
                    time.sleep(1)
                    log('explicit_close', connection=connection)
                    return
    except Exception as error:
        log('error', connection=connection, detail=str(error))

def tunnel(raw, backend_port):
    tunnel_id = next(counter)
    with raw, socket.create_connection(('127.0.0.1', backend_port)) as backend:
        log('tunnel_open', tunnel=tunnel_id)
        last_activity = time.monotonic()
        lost = False
        while True:
            readable, _, _ = select.select([raw, backend], [], [], 15)
            if not readable:
                continue
            idle = time.monotonic() - last_activity
            if idle > 1:
                lost = True
            for source in readable:
                data = source.recv(65536)
                if not data:
                    return
                if lost:
                    log('tunnel_drop', tunnel=tunnel_id, idle_seconds=idle,
                        direction='to_server' if source is raw else 'to_client', bytes=len(data))
                else:
                    destination = backend if source is raw else raw
                    destination.sendall(data)
            last_activity = time.monotonic()

def proxy(backend_port):
    with socket.socket() as listener:
        listener.bind(('127.0.0.1', 0))
        listener.listen(20)
        (root / 'proxy_port.txt').write_text(str(listener.getsockname()[1]))
        log('proxy_listening', port=listener.getsockname()[1])
        while True:
            raw, _ = listener.accept()
            threading.Thread(target=tunnel, args=(raw, backend_port), daemon=True).start()

with socket.socket() as listener:
    listener.bind(('127.0.0.1', 0))
    listener.listen(20)
    (root / 'port.txt').write_text(str(listener.getsockname()[1]))
    log('listening', port=listener.getsockname()[1])
    threading.Thread(target=proxy, args=(listener.getsockname()[1],), daemon=True).start()
    while True:
        raw, _ = listener.accept()
        threading.Thread(target=handle, args=(raw,), daemon=True).start()
