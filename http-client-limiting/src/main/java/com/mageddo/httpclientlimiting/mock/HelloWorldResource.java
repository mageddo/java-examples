package com.mageddo.httpclientlimiting.mock;

import com.mageddo.common.concurrent.concurrent.Threads;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldResource {
  @GetMapping(path = "/api/v1/hello-world", produces = MediaType.TEXT_PLAIN_VALUE)
  public  @ResponseBody String helloWorld(){
    Threads.sleep(2000);
    return "Hello World!!!";
  }
}
