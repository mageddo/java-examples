package com.mageddo.jms.queue;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Created by elvis on 17/06/17.
 */

public class SpringELTest {

	@Test
	public void staticParse() throws NoSuchMethodException {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();

		context.registerFunction(
			"reverseString",
			SpringELTest.class.getDeclaredMethod("reverseString", new Class[] { String.class })
		);

		Assert.assertEquals(
			"olleh", parser.parseExpression("#reverseString('hello')").getValue(context, String.class)
		);
	}

	public static String reverseString(String input) {
		StringBuilder backwards = new StringBuilder();
		for (int i = 0; i < input.length(); i++)
			backwards.append(input.charAt(input.length() - 1 - i));
		return backwards.toString();
	}

}
