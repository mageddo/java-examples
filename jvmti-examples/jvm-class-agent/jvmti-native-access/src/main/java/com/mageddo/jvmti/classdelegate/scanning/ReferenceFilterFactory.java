package com.mageddo.jvmti.classdelegate.scanning;

import com.mageddo.jvmti.classdelegate.ObjectReference;

import java.util.Iterator;
import java.util.List;

public class ReferenceFilterFactory {
  public void filter(List<ObjectReference> references, InstanceFilter filter){
    final Iterator<ObjectReference> it = references.iterator();
    while (it.hasNext()){
      final ObjectReference reference = it.next();
      RuleMatcher.apply(it, reference, filter);
    }
  }
}
