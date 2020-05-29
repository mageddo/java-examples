package testing;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {
  @SneakyThrows
  public static String getResourceAsString(String path) {
    return IOUtils.toString(TestUtils.class.getResourceAsStream(path), StandardCharsets.UTF_8);
  }
}
