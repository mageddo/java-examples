package vanillajavaexamples.network;

import java.net.NetworkInterface;
import java.net.SocketException;

public class NetworkMain {


  public static void main(String[] args) throws SocketException {
    NetworkInterface.networkInterfaces()
        .forEach(it -> {
          System.out.println();
          try {
            System.out.printf(
                "name=%5s, loopback=%s, pointToPoint=%s, virtual=%s, mac=%s%n",
                it.getName(),
                it.isLoopback(),
                it.isPointToPoint(),
                it.isVirtual(),
                toString(it.getHardwareAddress())
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

  private static String toString(byte[] addr) {
    if(addr == null){
      return null;
    }
    final var sb = new StringBuilder();
    for (byte b : addr) {
      sb.append(String.format("%02x", Byte.toUnsignedInt(b)));
      sb.append(':');
    }
    sb.deleteCharAt(sb.length()-1);
    return sb.toString();
  }
}
