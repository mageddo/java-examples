package vanillajavaexamples.network;

import java.net.NetworkInterface;
import java.net.SocketException;

/**
name=veth0f553e2, isUp=true, virtual=false, loopback=false, mac=26:dd:93:43:45:7b, interfaceAddresses=[/fe80:0:0:0:24dd:93ff:fe43:457b%veth0f553e2/64 [null]], inetAddresses=[/fe80:0:0:0:24dd:93ff:fe43:457b%veth0f553e2]
name=veth3df7c92, isUp=true, virtual=false, loopback=false, mac=26:29:94:cc:a7:1e, interfaceAddresses=[/fe80:0:0:0:2429:94ff:fecc:a71e%veth3df7c92/64 [null]], inetAddresses=[/fe80:0:0:0:2429:94ff:fecc:a71e%veth3df7c92]
name=vethe77109f, isUp=true, virtual=false, loopback=false, mac=7e:4d:61:63:5c:47, interfaceAddresses=[/fe80:0:0:0:7c4d:61ff:fe63:5c47%vethe77109f/64 [null]], inetAddresses=[/fe80:0:0:0:7c4d:61ff:fe63:5c47%vethe77109f]
name=docker0, isUp=true, virtual=false, loopback=false, mac=02:42:39:ca:06:52, interfaceAddresses=[/fe80:0:0:0:42:39ff:feca:652%docker0/64 [null], /172.17.0.1/16 [/172.17.255.255]], inetAddresses=[/fe80:0:0:0:42:39ff:feca:652%docker0, /172.17.0.1]
name=br-b41b674329df, isUp=false, virtual=false, loopback=false, mac=02:42:f0:93:2e:3a, interfaceAddresses=[/192.168.176.1/20 [/192.168.191.255]], inetAddresses=[/192.168.176.1]
name=docker_gwbridge, isUp=true, virtual=false, loopback=false, mac=02:42:11:d0:c6:1e, interfaceAddresses=[/fe80:0:0:0:42:11ff:fed0:c61e%docker_gwbridge/64 [null], /192.168.48.1/20 [/192.168.63.255]], inetAddresses=[/fe80:0:0:0:42:11ff:fed0:c61e%docker_gwbridge, /192.168.48.1]
name=br-55ca16d98940, isUp=true, virtual=false, loopback=false, mac=02:42:8a:a8:45:4b, interfaceAddresses=[/fe80:0:0:0:42:8aff:fea8:454b%br-55ca16d98940/64 [null], /172.157.5.1/16 [/172.157.255.255]], inetAddresses=[/fe80:0:0:0:42:8aff:fea8:454b%br-55ca16d98940, /172.157.5.1]
name=eno1, isUp=true, virtual=false, loopback=false, mac=e0:d5:5e:b8:7a:9a, interfaceAddresses=[/fe80:0:0:0:7ad4:cdd9:f8ac:147e%eno1/64 [null], /2804:14c:22:99a3:0:0:0:1001%eno1/128 [null], /2804:14c:22:99a3:7e97:b9b5:fe24:ac18%eno1/64 [null], /2804:14c:22:99a3:1618:460:ce4d:c9d3%eno1/64 [null], /192.168.0.128/24 [/192.168.0.255]], inetAddresses=[/fe80:0:0:0:7ad4:cdd9:f8ac:147e%eno1, /2804:14c:22:99a3:0:0:0:1001%eno1, /2804:14c:22:99a3:7e97:b9b5:fe24:ac18%eno1, /2804:14c:22:99a3:1618:460:ce4d:c9d3%eno1, /192.168.0.128]
name=lo, isUp=true, virtual=false, loopback=true, mac=null, interfaceAddresses=[/0:0:0:0:0:0:0:1%lo/128 [null], /127.0.0.1/8 [null]], inetAddresses=[/0:0:0:0:0:0:0:1%lo, /127.0.0.1]
 */
public class NetworkMain {


  public static void main(String[] args) throws SocketException {
    NetworkInterface.networkInterfaces()
        .forEach(it -> {
          System.out.println();
          try {
            System.out.printf(
                "name=%5s, loopback=%s, pointToPoint=%s, virtual=%s%n",
                it.getName(),
                it.isLoopback(),
                it.isPointToPoint(),
                it.isVirtual()
            );
          } catch (SocketException e) {
            e.printStackTrace();
          }

          it.inetAddresses().forEach(it2 -> {
            System.out.printf(
                "  address=%s%n    anylocal=%s, linkLocal=%s, loopback=%s, multiCast=%s, mcgGlobal=%s, mcNodeLocal=%s, "
                    + "mcOrgLocal=%s, mcSiteLocal=%s, siteLocal=%s%n",
                it2.toString(),
                it2.isAnyLocalAddress(),
                it2.isLinkLocalAddress(),
                it2.isLoopbackAddress(),
                it2.isMulticastAddress(),
                it2.isMCGlobal(),
                it2.isMCNodeLocal(),
                it2.isMCOrgLocal(),
                it2.isMCSiteLocal(),
                it2.isSiteLocalAddress()
            );
          });

        });
  }

}
