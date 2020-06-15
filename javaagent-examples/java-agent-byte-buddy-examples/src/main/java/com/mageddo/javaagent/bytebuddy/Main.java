package com.mageddo.javaagent.bytebuddy;

public class Main {

  public static void main(String[] args) throws InterruptedException {

    new Thread(() -> { while (true){
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }}, "worker").start();

    new Thread(() -> {}, "worker-2");

    System.out.println("pid = " + VmUtils.getCurrentPid());
    for (int id = 1; id < 10_000; id++) {
      final JiraIssue jiraIssue = new JiraIssue(String.format("SUS-%d", id));
      System.out.println("created: " + jiraIssue);
      Thread.sleep(2000);
    }
  }
}
