package vanillajavaexamples.resources;

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;
import java.time.LocalDateTime;

import com.sun.management.OperatingSystemMXBean;

import org.apache.commons.lang3.ThreadUtils;

public class JVMStatsUtils {

  static final NumberFormat format = NumberFormat.getInstance();

  private JVMStatsUtils() {
  }

  public static String dumpStats() {

    final var jmx = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    final Runtime runtime = Runtime.getRuntime();
    final long freeMemory = runtime.freeMemory();
    final long allocatedMemory = runtime.totalMemory();
    final long maxMemory = runtime.maxMemory();
    final long totalFreeMemory = freeMemory + (maxMemory - allocatedMemory);

    final StringBuilder sb = new StringBuilder();

    sb.append("localTime=");
    sb.append(LocalDateTime.now());
    sb.append(' ');

    sb.append("heapAllocatedFreeMemory=").append(toSummary(freeMemory));
    sb.append(' ');

    sb.append("heapAllocatedMemory=").append(toSummary(allocatedMemory));
    sb.append(' ');

    sb.append("heapMaxMemory=").append(toSummary(maxMemory));
    sb.append(' ');

    sb.append("heapFreeMemory=").append(toSummary(totalFreeMemory));
    sb.append(' ');

    sb.append("totalPhysicalMemorySize=");
    sb.append(toSummary(jmx.getTotalPhysicalMemorySize()));
    sb.append(' ');

    sb.append("processCpuLoad=");
    sb.append(String.format("%.2f", jmx.getProcessCpuLoad()));
    sb.append(' ');

    sb.append("systemCpuLoad=");
    sb.append(String.format("%.2f", jmx.getSystemCpuLoad()));
    sb.append(' ');

    sb.append("threads=").append(format.format(ThreadUtils.getAllThreads().size()));
    sb.append(' ');

    sb.append('\n');

    return sb.toString();
  }

  private static String toSummary(long bytes) {
    return format.format(bytes / 1024);
  }
}
