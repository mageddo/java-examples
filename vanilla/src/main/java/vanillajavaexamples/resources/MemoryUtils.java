package vanillajavaexamples.resources;

import java.text.NumberFormat;

public class MemoryUtils {

  private MemoryUtils() {
  }

  public static String dumpMemory() {

    final Runtime runtime = Runtime.getRuntime();
    final long freeMemory = runtime.freeMemory();
    final long allocatedMemory = runtime.totalMemory();
    final long maxMemory = runtime.maxMemory();
    final var totalFreeMemory = freeMemory + (maxMemory - allocatedMemory);
    final var processMemoryUsed = maxMemory - freeMemory;

    final StringBuilder sb = new StringBuilder();

    sb.append("stats (kb)\n");
    sb.append("free memory: ").append(toSummary(freeMemory));
    sb.append('\n');

    sb.append("allocated memory: ").append(toSummary(allocatedMemory));
    sb.append('\n');

    sb.append("max memory: ").append(toSummary(maxMemory));
    sb.append('\n');

    sb.append("total free memory: ").append(toSummary(totalFreeMemory));
    sb.append('\n');

    sb.append("process memory: ").append(toSummary(processMemoryUsed));
    sb.append('\n');

    sb.append("-------------------------------------\n");

    return sb.toString();
  }

  static final NumberFormat format = NumberFormat.getInstance();

  private static String toSummary(long bytes) {
    return format.format(bytes / 1024);
  }
}
