package vanillajavaexamples.hexfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HexMain {
  public static void main(String[] args) throws IOException {

    if (args.length != 3) {
      System.err.println("Examples:");
      System.err.println("java HexMain -e tmp.jar tmp.hex");
      System.err.println("java HexMain -d tmp.hex tmp.jar");
      System.exit(-1);
    }

    final Path from = Paths.get(args[1]);
    final Path to = Paths.get(args[2]);
    switch (args[0]) {
      case "-e":
        encode(from, to);
        break;
      case "-d":
        decode(from, to);
        break;
      default:
        System.err.println("Invalid option: " + args[0]);
        System.exit(-2);
    }
  }

  private static void decode(Path from, Path to) throws IOException {
    try (
        BufferedReader br = Files.newBufferedReader(from);
        OutputStream out = Files.newOutputStream(to)
    ) {
      String line;
      while ((line = br.readLine()) != null) {
        final byte[] bytes = decodeHex(line);
        out.write(bytes);
      }
    }
  }

  private static void encode(Path from, Path to) throws IOException {
    try (
        InputStream in = Files.newInputStream(from);
        BufferedWriter out = Files.newBufferedWriter(to)
    ) {
      final byte[] buff = new byte[64];
      int read;
      while ((read = in.read(buff, 0, buff.length)) != -1) {
        final char[] encoded = encodeHex(buff, DIGITS_UPPER, read);
        out.write(encoded);
        out.write('\n');
      }
    }
  }

  public static byte[] decodeHex(final String data) {
    return decodeHex(data.toCharArray());
  }

  public static byte[] decodeHex(final char[] data) {

    final int len = data.length;

    if ((len & 0x01) != 0) {
      throw new RuntimeException("Odd number of characters.");
    }

    final byte[] out = new byte[len >> 1];

    // two characters form the hex value.
    for (int i = 0, j = 0; j < len; i++) {
      int f = toDigit(data[j], j) << 4;
      j++;
      f = f | toDigit(data[j], j);
      j++;
      out[i] = (byte) (f & 0xFF);
    }

    return out;
  }

  protected static int toDigit(final char ch, final int index) {
    final int digit = Character.digit(ch, 16);
    if (digit == -1) {
      throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
    }
    return digit;
  }

  private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
      'E', 'F' };

  protected static char[] encodeHex(final byte[] data, final char[] toDigits, int length) {
    final int l = length;
    final char[] out = new char[l << 1];
    // two characters form the hex value.
    for (int i = 0, j = 0; i < l; i++) {
      out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
      out[j++] = toDigits[0x0F & data[i]];
    }
    return out;
  }
}
