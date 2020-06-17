package com.mageddo.jvmti.poc;

public class JiraIssue {

  private String id;

  public JiraIssue(String id) {
    this.id = id;
  }

  public JiraIssue setId(String id) {
    this.id = id;
    return this;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "JiraIssue{" +
      "id='" + id + '\'' +
      ", hashCode='" + this.hashCode() + '\'' +
      '}';
  }
}
