import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;

/**
 * https://stackoverflow.com/a/6416215/2979435
 */
public class WriteTextOnImage {

  public static final Color SOME_KIND_OF_GREY = Color.decode("0xF2F2F2");

  public static void main(String arg[]) throws IOException {
    final var source = ImageIO.read(WriteTextOnImage.class.getResourceAsStream("/meme.jpg"));
    final var targetImage = Files.createTempFile("image", ".jpg");
    final var bufferedImage = writeText(source);
    final var success = ImageIO.write(bufferedImage, "jpg", targetImage.toFile());
    System.out.printf("success=%s, result=%s", success, targetImage);
  }

  private static BufferedImage writeText(Path imagePath) {
    return writeText(createBufferedImageFromPath(imagePath));
  }

  private static BufferedImage createBufferedImageFromPath(Path imagePath) {
    try {
      return ImageIO.read(imagePath.toFile());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Writes text on bottom center of the image with a foreground
   *
   * @param source
   * @return
   */
  private static BufferedImage writeText(BufferedImage source) {

    final var textColor = Color.BLACK;
    final var foreGroundColor = SOME_KIND_OF_GREY;
    final var text = "MEMEZERODEPLANTAO.COM";
    final var font = new Font("Arial", Font.BOLD, 16);

    final var img = new BufferedImage(
        source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB // JPG
    );
    final var g2d = img.createGraphics();
    g2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
    g2d.drawImage(source, 0, 0, source.getWidth(), source.getHeight(), new JPanel());
    g2d.setFont(font);
    g2d.setPaint(foreGroundColor);

    final var fm = g2d.getFontMetrics();
    final var rect = fm.getStringBounds(text, g2d);

    g2d.fillRect(
        0, img.getHeight() - (int) rect.getHeight() - 5,
        img.getWidth(), (int) rect.getHeight() + fm.getAscent()
    );

    final int x = img.getWidth() / 2 - (int) rect.getWidth() / 2;
    final int y = img.getHeight() - 5;
    g2d.setPaint(textColor);
    g2d.drawString(text, x, y);

    g2d.dispose();
    return img;
  }
}
