import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

/**
 * https://stackoverflow.com/a/17802477/2979435
 */
public class CreateImageFromText {
  public static void main(String arg[]) throws IOException {

    final var imagePath = Files.createTempFile("image", ".jpg");
    String key = "Sample";

    BufferedImage bufferedImage = new BufferedImage(170, 30, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = bufferedImage.getGraphics();
    graphics.setColor(Color.LIGHT_GRAY);
    graphics.fillRect(0, 0, 200, 50);
    graphics.setColor(Color.BLACK);
    graphics.setFont(new Font("Arial", Font.BOLD, 20));
    graphics.drawString(key, 10, 25);

    ImageIO.write(bufferedImage, "jpg", imagePath.toFile());
    System.out.println("Image Created: " + imagePath);
  }
}
