package com.mageddo.jvmti.classdelegate.scanning;

import com.mageddo.jvmti.classdelegate.ObjectReference;

import java.util.Iterator;
import java.util.List;

public class RuleMatcher {

  public static boolean apply(Iterator it, ObjectReference reference, InstanceFilter instanceFilter){
    if(!apply(reference, instanceFilter)){
      it.remove();
      return false;
    }
    return true;
  }
  public static boolean apply(ObjectReference reference, InstanceFilter instanceFilter){
    if(!applyForFields(reference, instanceFilter.getFieldFilters())){
      return false;
    }
    return applyForMethods(reference, instanceFilter.getMethodFilters());
  }

  static boolean applyForMethods(ObjectReference reference, List<MethodFilter> methodFilters) {
    for (MethodFilter filter : methodFilters) {
      final ObjectReference methodReturnReference = reference.invoke(filter.getMethodName(), filter.getArguments());
      if(!apply(methodReturnReference, filter.getRules())){
        return false;
      }
    }
    return true;
  }

  public static boolean applyForFields(ObjectReference reference, List<FieldFilter> fieldFilters) {
    for (FieldFilter fieldFilter : fieldFilters) {
      final ObjectReference fieldReference = reference.getFieldValue(fieldFilter.getFieldName());
      if(!apply(fieldReference, fieldFilter.getRules())){
        return false;
      }
    }
    return true;
  }

  private static boolean apply(ObjectReference reference, List<Rule> rules) {
    for (Rule rule : rules) {
      if(!rule.matches(reference)){
        return false;
      }
    }
    return true;
  }

}
