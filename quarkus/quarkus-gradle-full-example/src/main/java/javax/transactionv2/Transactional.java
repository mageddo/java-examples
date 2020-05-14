package javax.transactionv2;
/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

/**
 * Describes a transaction attribute on an individual method or on a class.
 *
 * <p>At the class level, this annotation applies as a default to all methods of
 * the declaring class and its subclasses. Note that it does not apply to ancestor
 * classes up the class hierarchy; methods need to be locally redeclared in order
 * to participate in a subclass-level annotation.
 *
 * <p>This annotation type is generally directly comparable to Spring's
 * class, and in fact  will directly
 * convert the data to the latter class, so that Spring's transaction support code
 * does not have to know about annotations. If no rules are relevant to the exception,
 * it will be treated like
 * (rolling back on {@link RuntimeException} and {@link Error} but not on checked
 * exceptions).
 *
 * <p>For specific information about the semantics of this annotation's attributes,
 *
 * @author Colin Sampaleanu
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 1.2
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@InterceptorBinding
public @interface Transactional {

  /**
   * The transaction propagation type.
   * <p>Defaults to {@link Propagation#REQUIRED}.
   *
   */
  @Nonbinding
  Propagation propagation() default Propagation.REQUIRED;

  /**
   * The transaction isolation level.
   * <p>Defaults to {@link Isolation#DEFAULT}.
   * <p>Exclusively designed for use with {@link Propagation#REQUIRED} or
   * {@link Propagation#REQUIRES_NEW} since it only applies to newly started
   * transactions. Consider switching the "validateExistingTransactions" flag to
   * "true" on your transaction manager if you'd like isolation level declarations
   * to get rejected when participating in an existing transaction with a different
   * isolation level.
   *
   */
  @Nonbinding
  Isolation isolation() default Isolation.DEFAULT;

  /**
   * The timeout for this transaction (in seconds).
   * <p>Defaults to the default timeout of the underlying transaction system.
   * <p>Exclusively designed for use with {@link Propagation#REQUIRED} or
   * {@link Propagation#REQUIRES_NEW} since it only applies to newly started
   * transactions.
   *
   */
  @Nonbinding
  int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

  /**
   * A boolean flag that can be set to {@code true} if the transaction is
   * effectively read-only, allowing for corresponding optimizations at runtime.
   * <p>Defaults to {@code false}.
   * <p>This just serves as a hint for the actual transaction subsystem;
   * it will <i>not necessarily</i> cause failure of write access attempts.
   * A transaction manager which cannot interpret the read-only hint will
   * <i>not</i> throw an exception when asked for a read-only transaction
   * but rather silently ignore the hint.
   *
   */
  @Nonbinding
  boolean readOnly() default false;

  /**
   * Defines zero (0) or more exception {@link Class classes}, which must be
   * subclasses of {@link Throwable}, indicating which exception types must cause
   * a transaction rollback.
   * <p>By default, a transaction will be rolling back on {@link RuntimeException}
   * and {@link Error} but not on checked exceptions (business exceptions). See
   * for a detailed explanation.
   * <p>This is the preferred way to construct a rollback rule (in contrast to
   * {@link #rollbackForClassName}), matching the exception class and its subclasses.
   * <p>Similar to
   *
   * @see #rollbackForClassName
   */
  @Nonbinding
  Class<? extends Throwable>[] rollbackFor() default {};

  /**
   * Defines zero (0) or more exception names (for exceptions which must be a
   * subclass of {@link Throwable}), indicating which exception types must cause
   * a transaction rollback.
   * <p>This can be a substring of a fully qualified class name, with no wildcard
   * support at present. For example, a value of {@code "ServletException"} would
   * match {@code javax.servlet.ServletException} and its subclasses.
   * <p><b>NB:</b> Consider carefully how specific the pattern is and whether
   * to include package information (which isn't mandatory). For example,
   * {@code "Exception"} will match nearly anything and will probably hide other
   * rules. {@code "java.lang.Exception"} would be correct if {@code "Exception"}
   * were meant to define a rule for all checked exceptions. With more unusual
   * {@link Exception} names such as {@code "BaseBusinessException"} there is no
   * need to use a FQN.
   * <p>Similar to
   *
   * @see #rollbackFor
   */
  @Nonbinding
  String[] rollbackForClassName() default {};

  /**
   * Defines zero (0) or more exception {@link Class Classes}, which must be
   * subclasses of {@link Throwable}, indicating which exception types must
   * <b>not</b> cause a transaction rollback.
   * <p>This is the preferred way to construct a rollback rule (in contrast
   * to {@link #noRollbackForClassName}), matching the exception class and
   * its subclasses.
   * <p>Similar to
   *
   * @see #noRollbackForClassName
   */
  @Nonbinding
  Class<? extends Throwable>[] noRollbackFor() default {};

  /**
   * Defines zero (0) or more exception names (for exceptions which must be a
   * subclass of {@link Throwable}) indicating which exception types must <b>not</b>
   * cause a transaction rollback.
   * <p>See the description of {@link #rollbackForClassName} for further
   * information on how the specified names are treated.
   * <p>Similar to
   *
   * @see #noRollbackFor
   */
  @Nonbinding
  String[] noRollbackForClassName() default {};

}
