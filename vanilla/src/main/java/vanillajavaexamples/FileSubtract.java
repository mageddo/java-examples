package vanillajavaexamples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Usage example :
 *
 *  java FileSubtract.java a.txt b.txt
 */
public class FileSubtract {
  public static void main(String[] args) throws IOException {

    if (getArg(args, 0).equals("--help")) {
      System.out.println("Compara dois arquivos subtraindo o segundo do primeiro, em outras palavras, as linhas do "
          + "primeiro arquivo  que não existirem no segundo, serão impressas");
      System.exit(0);
    }
    if (args.length != 2) {
      System.err.println("Informe dois arquivos a serem comparados");
      System.exit(1);
    }

    final Path a = Paths.get(getArg(args, 0));
    final Path b = Paths.get(getArg(args, 1));

    final List<String> aLines = Files.readAllLines(a);
    final Set<String> bLines = new HashSet<>(Files.readAllLines(b));

    for (final String aLine : aLines) {
      if (!bLines.contains(aLine)) {
        System.out.println(aLine);
      }
    }

  }

  private static String getArg(String[] args, int index) {
    if (args.length <= index) {
      return "";
    }
    return args[index];
  }
}
