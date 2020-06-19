package com.mageddo.jvmti.classdelegate.scanning;

import com.mageddo.jvmti.classdelegate.ObjectReference;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Iterator;

@Singleton
public class ReferenceFilterFactory {
  /**
   * @return how many were removed
   */
  public int filter(Collection<ObjectReference> references, InstanceFilter filter){
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
