package com.mageddo.javaagent.bytebuddy;

public class JiraIssue {

  public String id;

  public JiraIssue(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public JiraIssue setId(String id) {
    this.id = id;
    return this;
  }

  @Override
  public String toString() {
    return "JiraIssue(" +
        "id='" + id + '\'' +
        ')';
  }
}
