package com.mageddo.jvmti.poc;

import com.mageddo.jvmti.ProcessUtils;

import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    System.load("/home/typer/dev/projects/ram-spider/jattach/build/libjattach.so");
    final int pid = ProcessUtils.getCurrentPid();
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