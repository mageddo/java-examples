package com.mageddo.jvmti.classdelegate.scanning;

import com.mageddo.jvmti.classdelegate.ObjectReference;

import javax.inject.Singleton;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;

@Singleton
public class ReferenceFilterFactory {
  /**
   * @return how many were removed
   */
  public int filter(Collection<WeakReference<ObjectReference>> references, InstanceFilter filter){
    final Iterator<WeakReference<ObjectReference>> it = references.iterator();
    int removed = 0;
    while (it.hasNext()){
      final ObjectReference reference = it.next().get();
      if(!RuleMatcher.apply(it, reference, filter)){
        removed++;
      }
    }
    return removed;
  }
}
