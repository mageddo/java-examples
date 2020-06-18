package com.mageddo.jvmti.entrypoint.vo;

import com.mageddo.jvmti.classdelegate.ObjectReference;
import com.mageddo.jvmti.classdelegate.scanning.FieldFilter;
import com.mageddo.jvmti.classdelegate.scanning.InstanceFilter;
import com.mageddo.jvmti.classdelegate.scanning.MethodFilter;
import com.mageddo.jvmti.classdelegate.scanning.rules.EqualToRule;
import com.mageddo.jvmti.classdelegate.scanning.rules.Rule;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class InstanceFilterReqTest {
  @Test
  void mustParseToInstanceFilter(){
    // arrange
    final InstanceFilterReq instanceFilterReq = new InstanceFilterReq()
      .setFieldFilters(Collections.singletonList(
        new FieldFilterReq()
        .setName("id")
        .setRules(Collections.singletonList(
          new RuleReq()
          .setRule(EqualToRule.class.getName())
            .setValue("ZRQ-50")
        ))
      ))
      .setMethodFilters(Collections.singletonList(
        new MethodFilterReq()
        .setName("getId")
        .setArgs(Collections.singletonList(
          new ArgsReq()
          .setClazz(String.class.getName())
          .setValue("ZRQ-50")
        ))
        .setRules(Collections.singletonList(
          new RuleReq()
            .setValue("ZRQ-50")
            .setRule(EqualToRule.class.getName())
        ))
      ));

    // act
    final InstanceFilter instanceFilter = instanceFilterReq.toInstanceFilter();

    // assert
    assertEquals(1, instanceFilter.getFieldFilters().size());
    assertEquals(1, instanceFilter.getMethodFilters().size());

    final MethodFilter firstMethodFilter = instanceFilter.getMethodFilters().get(0);
    assertEquals("getId", firstMethodFilter.getMethodName());
    assertEquals(Collections.singletonList("ZRQ-50"), firstMethodFilter.getArguments());
    assertEquals(1, firstMethodFilter.getRules().size());
    final Rule firstMethodRule = firstMethodFilter.getRules().get(0);
    assertTrue(firstMethodRule.matches(ObjectReference.of("ZRQ-50")));


    final FieldFilter firstFieldFilter = instanceFilter.getFieldFilters().get(0);
    assertEquals("id", firstFieldFilter.getFieldName());
    assertEquals(1, firstFieldFilter.getRules().size());
    final Rule firstFieldRule = firstFieldFilter.getRules().get(0);
    assertTrue(firstFieldRule.matches(ObjectReference.of("ZRQ-50")));
  }
}