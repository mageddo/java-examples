package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "com.example:type=MyBean", description = "Custom MBean")
public class MyJmxBean {

  @PostConstruct
  public void init() {
    System.out.println("MyJmxBean registered!");
  }

  @ManagedOperation
  public String restart() {
    System.out.println("Restart called via JMX");
    return "Restart triggered!";
  }
}
