A quick bash healthcheck to make sure the station is still sending data

```bash
curl http://192.168.1.24:8080/weather-monitor/temperature/getLatest | python -c 'import json,sys,datetime; obj=json.load(sys.stdin);  print obj["timestamp"];  start=datetime.datetime.strptime(obj["timestamp"], "%Y-%m-%dT%H:%M:%S"); sys.exit(13) if (datetime.datetime.now() - start) > datetime.timedelta(minutes=1) else sys.exit(0)' || echo $?
```

Just note that the echo $? at the end won't print anything when the exit code is 0, so that line is rather point less.
