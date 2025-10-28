package com.pagbank.logging;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloWorldController {

  @GetMapping("/hello-world")
  Object helloWorld() {
    log.info("action=helloWorld");
    return "Hello World!";
  }
}
