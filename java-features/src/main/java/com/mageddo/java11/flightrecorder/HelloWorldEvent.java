package com.mageddo.java11.flightrecorder;

import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("Hello World")
@Description("Helps the programmer getting started")
public
class HelloWorldEvent extends Event {
  @Label("Message")
  public String message;

}
