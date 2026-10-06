import json
import re
import sys
from pathlib import Path
root = Path(sys.argv[1]).resolve()
scenario = sys.argv[2] if len(sys.argv) > 2 else None
for protocol in ['TLSv1.3', 'TLSv1.2']:
    lines = (root / f'{protocol}.log').read_text().splitlines()
    calls = [line for line in lines if line.startswith('scenario=')]
    expected_calls = 2 if scenario else 20
    if len(calls) != expected_calls:
        raise AssertionError(f'{protocol}: expected {expected_calls} calls, got {len(calls)}')
    for first, second in zip(calls[::2], calls[1::2]):
        if 'stage=first' not in first or 'status=200' not in first:
            raise AssertionError(first)
        should_timeout = any(second.startswith(f'scenario={scenario} strategy=reuse ') for scenario in ['silent', 'blackhole'])
        if should_timeout:
            if 'error=SocketTimeoutException' not in second:
                raise AssertionError(second)
        elif 'status=200' not in second:
            raise AssertionError(second)
        else:
            previous_id = re.search(r'connection=(\d+)', first).group(1)
            next_id = re.search(r'connection=(\d+)', second).group(1)
            should_reuse = first.startswith('scenario=healthy ')
            if (previous_id == next_id) != should_reuse:
                raise AssertionError(f'Unexpected connection reuse: {first} / {second}')
    print(f'{protocol}: {expected_calls} chamadas verificadas; resultados e IDs de conexao corretos')
events = [json.loads(line) for line in (root / 'server.jsonl').read_text().splitlines()]
blackhole_requests = [event for event in events if event['event'] == 'request' and event['path'] == '/blackhole']
expected_requests = 2 if scenario else 14
if len(blackhole_requests) != expected_requests or any(event['number'] != 1 for event in blackhole_requests):
    raise AssertionError(f'Unexpected backend requests through blackhole: {blackhole_requests}')
drops = [event for event in events if event['event'] == 'tunnel_drop' and event['direction'] == 'to_server']
if len(drops) < 2:
    raise AssertionError('No proxy forwarding drops captured')
print('Proxy verified: second reused HTTP request never reached backend; dropped encrypted bytes logged')
