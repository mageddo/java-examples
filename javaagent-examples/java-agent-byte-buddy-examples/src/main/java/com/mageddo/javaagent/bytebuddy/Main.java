package com.mageddo.javaagent.bytebuddy;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    final int pid = VmUtils.getCurrentPid();
    System.out.println("pid = " + pid);
    List instances = new ArrayList();
    for (int id = 1; id < 10_000; id++) {
      final JiraIssue jiraIssue = new JiraIssue(String.format("%d-SUS-%d", pid, id));
      System.out.println("created: " + jiraIssue);
      instances.add(jiraIssue);
      Thread.sleep(2000);
    }
  }
}
