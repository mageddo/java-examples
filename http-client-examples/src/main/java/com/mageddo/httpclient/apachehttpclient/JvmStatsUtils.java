package com.mageddo.httpclient.apachehttpclient;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.sun.management.OperatingSystemMXBean;

import org.apache.commons.lang3.ThreadUtils;
import org.apache.commons.lang3.tuple.Pair;

public final class JvmStatsUtils {

  public static final String ID = UUID.randomUUID()
      .toString()
      .substring(0, 8);

  private JvmStatsUtils() {
  }

  public static String dumpStats() {

    final var jmx = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    final Runtime runtime = Runtime.getRuntime();
    final long freeMemory = runtime.freeMemory();
    final long allocatedMemory = runtime.totalMemory();
    final long maxMemory = runtime.maxMemory();
    final long totalFreeMemory = freeMemory + (maxMemory - allocatedMemory);

    final StringBuilder sb = new StringBuilder();

    sb.append("jvmId=")
        .append(ID)
        .append(' ');

    sb.append("localTime=");
    sb.append(LocalDateTime.now());
    sb.append(' ');

    sb.append("heapAllocatedFreeMemory=")
        .append(toSummary(freeMemory));
    sb.append(' ');

    sb.append("heapAllocatedMemory=")
        .append(toSummary(allocatedMemory));
    sb.append(' ');

    sb.append("heapMaxMemory=")
        .append(toSummary(maxMemory));
    sb.append(' ');

    sb.append("heapFreeMemory=")
        .append(toSummary(totalFreeMemory));
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

    final int threadsCount = ThreadUtils
        .getAllThreads()
        .size();
    sb.append("threads=")
        .append(threadsCount)
        .append(' ');

    sb.append("threadsByGroup ")
        .append(findThreadsByGroup().toString().replaceAll("[\\[\\]]+", ""))
        .append(' ');

    return sb.toString();
  }

  private static String toSummary(long bytes) {
    return String.format("%d", bytes / 1024);
  }

  public static List<String> findThreadNames() {
    return ThreadUtils
        .getAllThreads()
        .stream()
        .map(Thread::getName)
        .collect(Collectors.toList())
        ;
  }

  public static List<String> findThreadsByGroup() {
    final var threads = findThreadNames()
        .stream()
        .map(it -> it.replaceAll("\\d+", "_"))
        .collect(
            Collectors.groupingBy(
                Function.identity(),
                Collectors.summingInt((it) -> 1)
            )
        );
    final Comparator<Pair<String, Integer>> comparing = Comparator.comparing(Pair::getValue);
    return threads.keySet()
        .stream()
        .map(key -> Pair.of(key, threads.get(key)))
        .sorted(comparing.reversed())
        .map(it -> String.format("%s=%s", it.getKey(), it.getValue()))
        .collect(Collectors.toList())
        ;
  }
}
