package vanillajavaexamples.resources;

import java.text.NumberFormat;
import java.time.LocalDateTime;

import org.apache.commons.lang3.ThreadUtils;

public class JVMStatsV1Utils {

  static final NumberFormat format = NumberFormat.getInstance();

  private JVMStatsV1Utils() {
  }

  public static String dumpMemory() {

    final Runtime runtime = Runtime.getRuntime();
    final long freeMemory = runtime.freeMemory();
    final long allocatedMemory = runtime.totalMemory();
    final long maxMemory = runtime.maxMemory();
    final long totalFreeMemory = freeMemory + (maxMemory - allocatedMemory);

    final StringBuilder sb = new StringBuilder();

    sb.append("localTime=");
    sb.append(LocalDateTime.now());
    sb.append(' ');

    sb.append("freeMemory=").append(toSummary(freeMemory));
    sb.append(' ');

    sb.append("allocatedMemory=").append(toSummary(allocatedMemory));
    sb.append(' ');

    sb.append("maxMemory=").append(toSummary(maxMemory));
    sb.append(' ');

    sb.append("totalFreeMemory=").append(toSummary(totalFreeMemory));
    sb.append(' ');

    sb.append("threads=").append(ThreadUtils.getAllThreads().size());
    sb.append(' ');

    sb.append('\n');

    return sb.toString();
  }

  private static String toSummary(long bytes) {
    return format.format(bytes / 1024);
  }
}
