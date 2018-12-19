package utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.mageddo.jpa.Main;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

// create spring context before run test
//@WebAppConfiguration
@ContextConfiguration(classes = Main.class)
//@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
//	TransactionalTestExecutionListener.class })

// clean database every time before any test run
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:base_data.sql")
public @interface IntegratedTest {
}
