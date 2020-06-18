package com.mageddo.jvmti.agents.entrypoint.vo;

import java.util.List;

public class MethodFilterReq {
  String name;
  List<ArgsReq> args;
  List<RuleReq> rules;
}
