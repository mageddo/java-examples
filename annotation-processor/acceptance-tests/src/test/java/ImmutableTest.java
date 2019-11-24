import org.junit.jupiter.api.Test;
import pojo.Fruit;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImmutableTest {

	@Test
	void clazzShouldBeImmutable() throws Exception {

		// arrange
		final Class<Fruit> clazz = Fruit.class;

		// act
		final int fieldModifiers = clazz.getDeclaredField("name").getModifiers();

		// assert
		assertTrue(Modifier.isFinal(fieldModifiers));
	}

	@Test
	void mustCreateImmutableClass() throws Exception {

		// act
		final Class<?> clazz = Class.forName("pojo.FruitImmutable");

		// assert
		assertNotNull(clazz.newInstance());
	}

}
