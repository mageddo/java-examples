package ex05;

import java.util.Arrays;
import java.util.function.Supplier;

import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.config.JdbiConfig;
import org.jdbi.v3.core.internal.exceptions.Unchecked;
import org.jdbi.v3.core.mapper.reflect.internal.BuilderPojoPropertiesFactory;
import org.jdbi.v3.core.mapper.reflect.internal.PojoPropertiesFactory;
import org.jdbi.v3.core.mapper.reflect.internal.PojoTypes;

public class LombokBuilder implements JdbiConfig<LombokBuilder> {

  private ConfigRegistry registry;

  public LombokBuilder() {
  }

  @Override
  public void setRegistry(ConfigRegistry registry) {
    this.registry = registry;
  }

  @Override
  public LombokBuilder createCopy() {
    return new LombokBuilder();
  }

  /**
   * Register bean arguments and row mapping for a {@code Freebuilder} value class, expecting the
   * default generated class and builder names.
   *
   * @param <S>  the specification class
   * @param spec the specification interface of abstract class
   * @return a plugin that configures type mapping for the given class
   */
  public <S> LombokBuilder registerFreeBuilder(Class<S> spec) {
    final Class<? extends S> impl = valueClass(spec);
    return registerFreeBuilder(spec, impl, builderConstructor(spec));
  }

  /**
   * Convenience method for registering many freebuilder types.
   *
   * @return
   * @see #registerFreeBuilder(Class)
   */
  public LombokBuilder registerFreeBuilder(Class<?>... specs) {
    return registerFreeBuilder(Arrays.asList(specs));
  }

  /**
   * Convenience method for registering many freebuilder types.
   *
   * @return
   * @see #registerFreeBuilder(Class)
   */
  public LombokBuilder registerFreeBuilder(Iterable<Class<?>> specs) {
    specs.forEach(this::registerFreeBuilder);
    return this;
  }

  /**
   * Register bean arguments and row mapping for a {@code FreeBuilder} value class, using a
   * supplied implementation and constructor.
   *
   * @param <S>                the specification class
   * @param <I>                the generated value class
   * @param spec               the specification interface or abstract class
   * @param impl               the generated value class
   * @param builderConstructor a supplier of new Builder instances
   * @return a plugin that configures type mapping for the given class
   */
  public <S, I extends S> LombokBuilder registerFreeBuilder(Class<S> spec, Class<I> impl,
      Supplier<?> builderConstructor) {
    return register(spec, impl, BuilderPojoPropertiesFactory.builder(spec, builderConstructor));
  }

  private <S> LombokBuilder register(Class<S> spec, Class<? extends S> impl,
      PojoPropertiesFactory factory) {
    registry.get(PojoTypes.class).register(spec, factory).register(impl, factory);
    return this;
  }

  private <S> Class<? extends S> valueClass(Class<S> spec) {
    final String valueName;
    if (spec.getEnclosingClass() == null) {
      valueName = spec.getPackage().getName() + "." + spec.getSimpleName();
    } else {
      String enclosingName = spec.getEnclosingClass().getSimpleName();
      valueName =
          spec.getPackage().getName() + "." + enclosingName + "_" + spec.getSimpleName() + "_" +
              "Builder" + "$" + "Value";
    }
    try {
      return (Class<? extends S>) Class.forName(valueName);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Couldn't locate value class " + valueName, e);
    }
  }

  @SuppressWarnings("unchecked")
  private <S> Supplier<?> builderConstructor(Class<S> spec) {
    final Class builderClass = builderClass(spec);
    return Unchecked.supplier(() -> {
      final var c = builderClass.getDeclaredConstructor();
      c.setAccessible(true);
      return c.newInstance();
    });
  }

  private <S> Class builderClass(Class<S> spec) {
    final String builderName;
    if (spec.getEnclosingClass() == null) {
      builderName =
          spec.getPackage().getName() + "." + spec.getSimpleName() + "$" + spec.getSimpleName() + "Builder";
    } else {
      String enclosingName = spec.getEnclosingClass().getSimpleName();
      builderName =
          spec.getPackage().getName() + "." + enclosingName + "$" + spec.getSimpleName() + "$" +
              "Builder";
    }

    try {
      return Class.forName(builderName);
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException("Couldn't locate builder class " + builderName, e);
    }
  }
}
