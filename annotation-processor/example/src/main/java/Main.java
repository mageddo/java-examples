import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Main {
	public static void main(String[] args) throws NoSuchFieldException {
		final MutableClass mutableClass = new MutableClass("Orange");
		final Field nameField = mutableClass.getClass().getDeclaredField("name");
		System.out.printf("is final %b", Modifier.isFinal(nameField.getModifiers()));;
	}
}
