package com.mageddo.jvmti.agents.entrypoint.vos;

import java.util.List;

public class MethodFilterReq {
  String name;
  List<ArgsReq> args;
  List<RuleReq> rules;
}
