package com.mageddo.jvmti.agents.entrypoint.vo;

import lombok.Data;

import java.util.List;

@Data
public class FieldFilterReq {
  String name;
  List<RuleReq> rules;
}
