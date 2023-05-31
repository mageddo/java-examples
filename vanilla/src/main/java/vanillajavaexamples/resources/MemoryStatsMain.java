package vanillajavaexamples.resources;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/***
 * To check what JVM memory stats methods returns while data is being allocated.
 *
 * Ex:
 *
 * free memory: 511,506
 * allocated memory: 514,048
 * max memory: 8,202,240
 * total free memory: 8,199,698
 * -------------------------------------
 *
 * How much memory to allocate in MB?
 * 300
 * free memory: 202,957
 * allocated memory: 514,048
 * max memory: 8,202,240
 * total free memory: 7,891,149
 * -------------------------------------
 *
 */
public class MemoryStatsMain {
  public static void main(String[] args) throws Exception {
    final List<byte[]> data = new LinkedList<>();
    final Scanner in = new Scanner(System.in);
    System.out.println(MemoryUtils.dumpMemory());
    while (true) {
      System.out.println("How much memory to allocate in MB?");
      data.add(new byte[Integer.parseInt(in.nextLine()) * 1024 * 1024]);
      System.out.println(MemoryUtils.dumpMemory());
    }
  }
}
