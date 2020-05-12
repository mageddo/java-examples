package com.mageddo;

import java.util.Map;

import org.jboss.jandex.DotName;
import org.jboss.jandex.Type;

import io.quarkus.arc.deployment.InjectionPointTransformerBuildItem;
import io.quarkus.arc.processor.InjectionPointsTransformer;
import io.quarkus.deployment.annotations.BuildStep;

public class Interceptorx {

  @BuildStep
  InjectionPointTransformerBuildItem transformer() {
    return new InjectionPointTransformerBuildItem(new InjectionPointsTransformer() {

      public boolean appliesTo(Type requiredType) {
        return requiredType.name().equals(DotName.createSimple(Object.class.getName()));
      }

      public void transform(TransformationContext context) {
        if (context.getQualifiers().stream()
            .anyMatch(a -> a.name().equals(DotName.createSimple(Object.class.getName())))) {
          context.transform()
              .removeAll()
              .add(DotName.createSimple(Map.class.getName()))
              .done();
        }
      }
    });
  }
}
