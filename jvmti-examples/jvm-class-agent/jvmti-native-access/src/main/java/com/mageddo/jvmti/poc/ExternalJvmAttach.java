package com.mageddo.jvmti.poc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExternalJvmAttach {

  public static void main(String[] args) {
    log.debug("starting....");
    com.mageddo.jvmti.ExternalJvmAttach.attach(args[0]);
  }

}