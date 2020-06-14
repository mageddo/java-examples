import de.invesdwin.instrument.DynamicInstrumentationLoader;

public class Main {
	public static void main(String[] args) {
		DynamicInstrumentationLoader.waitForInitialized();
		DynamicInstrumentationLoader.initLoadTimeWeavingContext();
//		DynamicInstrumentationLoader.

	}
}
