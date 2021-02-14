/*
 * Part of the Java Image Processing Cookbook, please see
 * http://www.lac.inpe.br/~rafael.santos/JIPCookbook.jsp
 * for information on usage and distribution.
 * Rafael Santos (rafael.santos@lac.inpe.br)
 */
package com.mageddo.algorithm.imagesimilarity.ex01_jai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.iterator.RandomIter;
import javax.media.jai.iterator.RandomIterFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.sun.media.jai.widget.DisplayJAI;

/**
 * This class uses a very simple, naive similarity algorithm to compare an image
 * with all others in the same directory.
 */
public class NaiveSimilarityFinder extends JFrame {
  // The reference image "signature" (25 representative pixels, each in R,G,B).
  // We use instances of Color to make things simpler.
  private Color[][] signature;
  // The base size of the images.
  private static final int baseSize = 300;
  // Where are all the files?
  private static final String basePath = System.getProperty("user.home");

  /*
   * The constructor, which creates the GUI and start the image processing task.
   */
  public NaiveSimilarityFinder(File reference) throws IOException {
    // Create the GUI
    super("Naive Similarity Finder");
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    // Put the reference, scaled, in the left part of the UI.
    RenderedImage ref = rescale(ImageIO.read(reference));
    cp.add(new DisplayJAI(ref), BorderLayout.WEST);
    // Calculate the signature vector for the reference.
    signature = calcSignature(ref);
    // Now we need a component to store X images in a stack, where X is the
    // number of images in the same directory as the original one.
    File[] others = getOtherImageFiles(reference);
    JPanel otherPanel = new JPanel(new GridLayout(others.length, 2));
    cp.add(new JScrollPane(otherPanel), BorderLayout.CENTER);
    // For each image, calculate its signature and its distance from the
    // reference signature.
    RenderedImage[] rothers = new RenderedImage[others.length];
    double[] distances = new double[others.length];
    for (int o = 0; o < others.length; o++) {
      rothers[o] = rescale(ImageIO.read(others[o]));
      distances[o] = calcDistance(rothers[o]);
    }
    // Sort those vectors *together*.
    for (int p1 = 0; p1 < others.length - 1; p1++)
      for (int p2 = p1 + 1; p2 < others.length; p2++) {
        if (distances[p1] > distances[p2]) {
          double tempDist = distances[p1];
          distances[p1] = distances[p2];
          distances[p2] = tempDist;
          RenderedImage tempR = rothers[p1];
          rothers[p1] = rothers[p2];
          rothers[p2] = tempR;
          File tempF = others[p1];
          others[p1] = others[p2];
          others[p2] = tempF;
        }
      }
    // Add them to the UI.
    for (int o = 0; o < others.length; o++) {
      otherPanel.add(new DisplayJAI(rothers[o]));
      JLabel ldist = new JLabel("<html>" + others[o].getName() + "<br>"
          + String.format("% 13.3f", distances[o]) + "</html>");
      ldist.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 36));
      System.out.printf("<td class=\"simpletable legend\"> " +
          "<img src=\"MiscResources/ImageSimilarity/icons/miniicon_%s\" " +
          "alt=\"Similarity result\"><br>% 13.3f</td>\n", others[o].getName(), distances[o]);
      otherPanel.add(ldist);
    }
    // More GUI details.
    pack();
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /*
   * This method rescales an image to 300,300 pixels using the JAI scale
   * operator.
   */
  private RenderedImage rescale(RenderedImage i) {
    float scaleW = ((float) baseSize) / i.getWidth();
    float scaleH = ((float) baseSize) / i.getHeight();
    // Scales the original image
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(i);
    pb.add(scaleW);
    pb.add(scaleH);
    pb.add(0.0F);
    pb.add(0.0F);
    pb.add(new InterpolationNearest());
    // Creates a new, scaled image and uses it on the DisplayJAI component
    return JAI.create("scale", pb);
  }

  /*
   * This method calculates and returns signature vectors for the input image.
   */
  private Color[][] calcSignature(RenderedImage i) {
    // Get memory for the signature.
    Color[][] sig = new Color[5][5];
    // For each of the 25 signature values average the pixels around it.
    // Note that the coordinate of the central pixel is in proportions.
    float[] prop = new float[]
        {1f / 10f, 3f / 10f, 5f / 10f, 7f / 10f, 9f / 10f};
    for (int x = 0; x < 5; x++)
      for (int y = 0; y < 5; y++)
        sig[x][y] = averageAround(i, prop[x], prop[y]);
    return sig;
  }

  /*
   * This method averages the pixel values around a central point and return the
   * average as an instance of Color. The point coordinates are proportional to
   * the image.
   */
  private Color averageAround(RenderedImage i, double px, double py) {
    // Get an iterator for the image.
    RandomIter iterator = RandomIterFactory.create(i, null);
    // Get memory for a pixel and for the accumulator.
    double[] pixel = new double[3];
    double[] accum = new double[3];
    // The size of the sampling area.
    int sampleSize = 15;
    int numPixels = 0;
    // Sample the pixels.
    for (double x = px * baseSize - sampleSize; x < px * baseSize + sampleSize; x++) {
      for (double y = py * baseSize - sampleSize; y < py * baseSize + sampleSize; y++) {
        iterator.getPixel((int) x, (int) y, pixel);
        accum[0] += pixel[0];
        accum[1] += pixel[1];
        accum[2] += pixel[2];
        numPixels++;
      }
    }
    // Average the accumulated values.
    accum[0] /= numPixels;
    accum[1] /= numPixels;
    accum[2] /= numPixels;
    return new Color((int) accum[0], (int) accum[1], (int) accum[2]);
  }

  /*
   * This method calculates the distance between the signatures of an image and
   * the reference one. The signatures for the image passed as the parameter are
   * calculated inside the method.
   */
  private double calcDistance(RenderedImage other) {
    // Calculate the signature for that image.
    Color[][] sigOther = calcSignature(other);
    // There are several ways to calculate distances between two vectors,
    // we will calculate the sum of the distances between the RGB values of
    // pixels in the same positions.
    double dist = 0;
    for (int x = 0; x < 5; x++)
      for (int y = 0; y < 5; y++) {
        int r1 = signature[x][y].getRed();
        int g1 = signature[x][y].getGreen();
        int b1 = signature[x][y].getBlue();
        int r2 = sigOther[x][y].getRed();
        int g2 = sigOther[x][y].getGreen();
        int b2 = sigOther[x][y].getBlue();
        double tempDist = Math.sqrt((r1 - r2) * (r1 - r2) + (g1 - g2)
            * (g1 - g2) + (b1 - b2) * (b1 - b2));
        dist += tempDist;
      }
    return dist;
  }

  /*
   * This method get all image files in the same directory as the reference.
   * Just for kicks include also the reference image.
   */
  private File[] getOtherImageFiles(File reference) {
    File dir = new File(reference.getParent());
    // List all the image files in that directory.
    File[] others = dir.listFiles(new JPEGImageFileFilter());
    return others;
  }

  /*
   * The entry point for the application, which opens a file with an image that
   * will be used as reference and starts the application.
   * https://web.archive.org/web/20121108205937/http://www.lac.inpe.br/JIPCookbook/6050-howto-compareimages.jsp
   */
  public static void main(String[] args) throws IOException {
//    Image.na
    final var referenceFile = Paths.get("/home/typer/Downloads/rick-and-morty/screeshot.jpg").toFile();
    new NaiveSimilarityFinder(referenceFile);
  }

}
