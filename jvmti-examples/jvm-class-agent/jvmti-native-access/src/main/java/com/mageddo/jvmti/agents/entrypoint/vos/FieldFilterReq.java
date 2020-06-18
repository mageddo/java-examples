package com.mageddo.jvmti.agents.entrypoint.vos;

import lombok.Data;

import java.util.List;

@Data
public class FieldFilterReq {
  String name;
  List<RuleReq> rules;
}
