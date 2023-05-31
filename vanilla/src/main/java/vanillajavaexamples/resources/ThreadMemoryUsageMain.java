package vanillajavaexamples.resources;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ThreadMemoryUsageMain {
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
