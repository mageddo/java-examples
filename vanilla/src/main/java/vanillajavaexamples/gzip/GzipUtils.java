package vanillajavaexamples.gzip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;

import lombok.SneakyThrows;

public class GzipUtils {
  @SneakyThrows
  public static byte[] gzip(byte[] data) {
    final var bout = new ByteArrayOutputStream();
    final var out = new GZIPOutputStream(bout);
    try (out) {
      out.write(data);
    }
    return bout.toByteArray();
  }

  @SneakyThrows
  public static byte[] gUnzip(String data) {
    return gUnzip(Base64.decodeBase64(data));
  }

  @SneakyThrows
  public static byte[] gUnzip(byte[] data) {
    final var buf = new byte[128];
    final var bout = new ByteArrayOutputStream();
    try (var in = new GZIPInputStream(new ByteArrayInputStream(data));) {
      int read;
      while ((read = in.read(buf)) != -1) {
        bout.write(buf, 0, read);
      }
    }
    return bout.toByteArray();
  }

  public static String gzipToBase64(byte[] data) {
    return Base64.encodeBase64String(gzip(data));
  }
}
