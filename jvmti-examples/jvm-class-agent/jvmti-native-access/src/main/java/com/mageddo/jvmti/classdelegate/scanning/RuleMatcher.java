package com.mageddo.jvmti.classdelegate.scanning;

import com.mageddo.jvmti.classdelegate.ObjectReference;

import java.util.Iterator;
import java.util.List;

public class RuleMatcher {

  public static void apply(Iterator it, ObjectReference reference, InstanceFilter instanceFilter){
    applyForFields(it, reference, instanceFilter.getFieldFilters());
    applyForMethods(it, reference, instanceFilter.getMethodFilters());
  }

  private static void applyForMethods(Iterator it, ObjectReference reference, List<MethodFilter> methodFilters) {
    for (MethodFilter filter : methodFilters) {
      final ObjectReference methodReturnReference = reference.invoke(filter.getMethodName(), filter.getArguments());
      apply(it, methodReturnReference, filter.getRules());
    }
  }

  public static void applyForFields(Iterator it, ObjectReference reference, List<FieldFilter> fieldFilters) {
    for (FieldFilter fieldFilter : fieldFilters) {
      final ObjectReference fieldReference = reference.getFieldValue(fieldFilter.getFieldName());
      apply(it, fieldReference, fieldFilter.getRules());
    }
  }

  private static void apply(Iterator it, ObjectReference reference, List<Rule> rules) {
    for (Rule rule : rules) {
      apply(it, reference, rule);
    }
  }

  public static void apply(Iterator it, ObjectReference reference, Rule r){
    if(!r.matches(reference)){
      it.remove();
    }
  }
}
