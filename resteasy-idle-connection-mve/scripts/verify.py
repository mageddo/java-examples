import json
import re
import sys
from pathlib import Path

root = Path(sys.argv[1]).resolve()
mode = sys.argv[2] if len(sys.argv) > 2 else 'matrix'
expected_calls = {'matrix': 20, 'blackhole': 2, 'keepalive': 30}[mode]
for protocol in ['TLSv1.3', 'TLSv1.2']:
    calls = [line for line in (root / f'{protocol}.log').read_text().splitlines()
             if line.startswith('scenario=')]
    if len(calls) != expected_calls:
        raise AssertionError(f'{protocol}: expected {expected_calls} calls, got {len(calls)}')
    for first, second in zip(calls[::2], calls[1::2]):
        path = re.search(r'scenario=(\S+)', first).group(1)
        strategy = re.search(r'strategy=(\S+)', first).group(1)
        if 'stage=first' not in first or 'status=200' not in first:
            raise AssertionError(first)
        if not second.startswith(f'scenario={path} strategy={strategy} stage=after_idle '):
            raise AssertionError(second)
        should_timeout = path in ['silent', 'blackhole'] and strategy == 'reuse'
        should_timeout |= path == 'blackhole' and strategy.endswith('Short')
        if should_timeout:
            if 'error=SocketTimeoutException' not in second:
                raise AssertionError(second)
        elif 'status=200' not in second:
            raise AssertionError(second)
        else:
            previous_id = re.search(r'connection=(\d+)', first).group(1)
            next_id = re.search(r'connection=(\d+)', second).group(1)
            should_reuse = path == 'healthy' and (strategy == 'reuse' or strategy.endswith('Short'))
            if (previous_id == next_id) != should_reuse:
                raise AssertionError(f'Unexpected connection reuse: {first} / {second}')
    print(f'{protocol}: {expected_calls} chamadas verificadas; resultados e IDs de conexao corretos')
events = [json.loads(line) for line in (root / 'server.jsonl').read_text().splitlines()]
blackhole_requests = [event for event in events if event['event'] == 'request' and event['path'] == '/blackhole']
expected_requests = 2 if mode == 'blackhole' else 14
if len(blackhole_requests) != expected_requests or any(event['number'] != 1 for event in blackhole_requests):
    raise AssertionError(f'Unexpected backend requests through blackhole: {blackhole_requests}')
drops = [event for event in events if event['event'] == 'tunnel_drop' and event['direction'] == 'to_server']
if len(drops) < 2:
    raise AssertionError('No proxy forwarding drops captured')
print('Proxy verified: second reused HTTP request never reached backend; dropped encrypted bytes logged')
