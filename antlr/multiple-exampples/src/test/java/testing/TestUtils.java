package testing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;

/**
 * Some utilities while testing
 * Version: 1.0
 *
 * @author Elvis Souza
 */
public class TestUtils {

  public static void copy(InputStream source, OutputStream target) {
    try {
      final byte[] buff = new byte[256];
      int read;
      while ((read = source.read(buff, 0, buff.length)) != -1) {
        target.write(buff, 0, read);
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static String getResourceAsString(String path) {
    final InputStream source = getResourceAsStream(path);
    final ByteArrayOutputStream target = new ByteArrayOutputStream();
    copy(source, target);
    return target.toString();
  }

  public static InputStream getResourceAsStream(String path) {
    return TestUtils.class.getResourceAsStream(path);
  }
}
