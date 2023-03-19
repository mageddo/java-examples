Verify simple UDP server for development porpuses

#### Features
* Setup a UDP server
* Print all received messages as plain text


#### Running

__From docker__

```bash
docker run -p 3333:3333/udp --name udp-server --rm defreitas/udp-server:latest
```

__From binary__

Download latest version and run it

```bash
jar udp-server-*.jar 
2018-05-27T14:58:29.247 - starting at 3333
```

__Send some packets to the server__

```bash
echo "it's `date`" > /dev/udp/127.0.0.1/3333
```

#### Options

```bash
jar udp-server-*.jar <port> <bufferSizeInBytes>
``` 

### Build from source

```bash
./gradlew clean build && java -jar udp-server/build/libs/udp-server-*.jar 
```
