package com.mageddo.jvmti.classdelegate.scanning;

import com.mageddo.jvmti.classdelegate.ObjectReference;

import java.util.Iterator;
import java.util.List;

public class ReferenceFilterFactory {
  /**
   * @return how many were removed
   */
  public int filter(List<ObjectReference> references, InstanceFilter filter){
    final Iterator<ObjectReference> it = references.iterator();
    int removed = 0;
    while (it.hasNext()){
      final ObjectReference reference = it.next();
      if(!RuleMatcher.apply(it, reference, filter)){
        removed++;
      }
    }
    return removed;
  }
}
