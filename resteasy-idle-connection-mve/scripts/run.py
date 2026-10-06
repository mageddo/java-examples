import os
from pathlib import Path
import subprocess
import sys
import time

scripts = Path(__file__).resolve().parent
output = Path(sys.argv[1]).resolve()
classpath = Path(sys.argv[2]).resolve().read_text().strip()
java = sys.argv[3]
idle = int(sys.argv[4])
read = int(sys.argv[5])
scenario = sys.argv[6]
if idle <= 2000 or read <= 0:
    raise ValueError('idleMillis precisa ser >2000ms; readMillis precisa ser >0ms')
if scenario not in ['matrix', 'blackhole', 'keepalive']:
    raise ValueError('scenario precisa ser matrix, blackhole ou keepalive')
if scenario == 'keepalive' and idle <= 5000:
    raise ValueError('keepalive precisa de idleMillis >5000ms para comparar expiracao')
output = output / scenario
output.mkdir(parents=True, exist_ok=True)
os.chdir(output)
subprocess.run([
    'openssl', 'req', '-x509', '-newkey', 'rsa:2048', '-nodes',
    '-keyout', 'key.pem', '-out', 'cert.pem', '-days', '2',
    '-subj', '/CN=localhost', '-addext', 'subjectAltName=DNS:localhost'
], check=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
for filename in ['port.txt', 'proxy_port.txt']:
    (output / filename).unlink(missing_ok=True)
with (output / 'server.jsonl').open('w') as log:
    server = subprocess.Popen([sys.executable, '-u', scripts / 'server.py', str(output)],
                              stdout=log, stderr=subprocess.STDOUT)
    try:
        deadline = time.monotonic() + 5
        while not all((output / name).exists() for name in ['port.txt', 'proxy_port.txt']):
            if server.poll() is not None:
                raise RuntimeError(f'Servidor terminou durante startup; veja {output / "server.jsonl"}')
            if time.monotonic() >= deadline:
                raise RuntimeError(f'Servidor nao ficou pronto em5s; veja {output / "server.jsonl"}')
            time.sleep(0.05)
        for protocol in ['TLSv1.3', 'TLSv1.2']:
            command = [java, '-cp', classpath, 'IdleProbe', protocol, str(idle), str(read)]
            if scenario != 'matrix':
                command.append(scenario)
            with (output / f'{protocol}.log').open('w') as client_log:
                client = subprocess.Popen(command, text=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
                try:
                    for line in client.stdout:
                        client_log.write(line)
                        client_log.flush()
                        print(line, end='', flush=True)
                    if client.wait() != 0:
                        raise RuntimeError(f'Client {protocol} falhou; veja {client_log.name}')
                finally:
                    if client.poll() is None:
                        client.terminate()
                        try:
                            client.wait(timeout=5)
                        except subprocess.TimeoutExpired:
                            client.kill()
                            client.wait()
        command = [sys.executable, scripts / 'verify.py', str(output)]
        if scenario != 'matrix':
            command.append(scenario)
        subprocess.run(command, check=True)
        print(f'Resultados: {output}', flush=True)
    finally:
        server.terminate()
        try:
            server.wait(timeout=5)
        except subprocess.TimeoutExpired:
            server.kill()
            server.wait()
