package vanillajavaexamples.resources;

import java.text.NumberFormat;

public class MemoryUtils {

  private MemoryUtils() {
  }

  public static String dumpMemory(){

    final Runtime runtime = Runtime.getRuntime();
    final NumberFormat format = NumberFormat.getInstance();

    final long freeMemory = runtime.freeMemory();
    final long allocatedMemory = runtime.totalMemory();
    final long maxMemory = runtime.maxMemory();
    final var totalFreeMemory = freeMemory + (maxMemory - allocatedMemory);

    final StringBuilder sb = new StringBuilder();

    sb.append("stats (mb)\n");
    sb.append("free memory: ").append(format.format(freeMemory / 1024 / 1024));
    sb.append('\n');

    sb.append("allocated memory: ").append(format.format(allocatedMemory / 1024 / 1024));
    sb.append('\n');

    sb.append("max memory: ").append(format.format(maxMemory / 1024 / 1024));
    sb.append('\n');

    sb.append("total free memory: ").append(format.format(totalFreeMemory / 1024 / 1024));
    sb.append('\n');
    sb.append("-------------------------------------\n");

    return sb.toString();
  }
}
