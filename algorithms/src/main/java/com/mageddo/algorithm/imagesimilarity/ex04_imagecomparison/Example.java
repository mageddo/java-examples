package com.mageddo.algorithm.imagesimilarity.ex04_imagecomparison;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;

public class Example {
  public static void main(String[] args) {

    final var aFile = Paths.get("/home/typer/Downloads/rick-and-morty/rick-and-morty.jpg");
//    final var bFile = Paths.get("/home/typer/Downloads/rick-and-morty/screeshot.jpg");
//    final var bFile = Paths.get("/home/typer/Downloads/rick-and-morty/screenshot-2.jpg");
//    final var bFile = Paths.get("/home/typer/Downloads/rick-and-morty/screenshot-3.jpg");
//    final var bFile = Paths.get("/home/typer/Downloads/rick-and-morty/2021-02-14T01:55:56.jpg");
//    final var bFile = Paths.get("/home/typer/Downloads/rick-and-morty/2021-02-14T01:55:08.jpg");
//    final var bFile = Paths.get("/home/typer/Downloads/rick-and-morty/elvis-presley.jpg");
    final var bFile = Paths.get("/home/typer/Pictures/2021-02-14T02:35:17.jpg");

    // load the images to be compared
//    BufferedImage expectedImage = ImageComparisonUtil.readImageFromResources("/home/typer/Downloads/rick-and-morty/rick-and-morty.jpg");
//    BufferedImage actualImage = ImageComparisonUtil.readImageFromResources("/home/typer/Downloads/rick-and-morty/screeshot.jpg");

    // where to save the result (leave null if you want to see the result in the UI)
    File resultDestination = new File("/home/typer/Downloads/rick-and-morty/result.png");

    //Create ImageComparison object for it.
//    ImageComparison imageComparison = new ImageComparison(expectedImage, actualImage,
//        resultDestination);

    //Can be used another constructor for it, without destination.
    final var imageComparison = new ImageComparison(aFile.toString(), bFile.toString());
//    or
//    new ImageComparison(expectedImage, actualImage);

    //Also can be configured BEFORE comparing next properties:

    //Threshold - it's the max distance between non-equal pixels. By default it's 5.
    imageComparison.setThreshold(10);
    imageComparison.getThreshold();

    //RectangleListWidth - Width of the line that is drawn in the rectangle. By default it's 1.
    imageComparison.setRectangleLineWidth(5);
    imageComparison.getRectangleLineWidth();

    //DifferenceRectangleFilling - Fill the inside the difference rectangles with a transparent
    //fill. By default it's false and 20.0% opacity.
    imageComparison.setDifferenceRectangleFilling(true, 30.0);
    imageComparison.isFillDifferenceRectangles();
    imageComparison.getPercentOpacityDifferenceRectangles();

    //ExcludedRectangleFilling - Fill the inside the excluded rectangles with a transparent fill.
    //By default it's false and 20.0% opacity.
    imageComparison.setExcludedRectangleFilling(true, 30.0);
    imageComparison.isFillExcludedRectangles();
    imageComparison.getPercentOpacityExcludedRectangles();

    //Destination. Before comparing also can be added destination file for result image.
    imageComparison.setDestination(resultDestination);
    imageComparison.getDestination();

    //MaximalRectangleCount - It means that would get first x biggest rectangles for drawing.
    // by default all the rectangles would be drawn.
    imageComparison.setMaximalRectangleCount(10);
    imageComparison.getMaximalRectangleCount();

    //MinimalRectangleSize - The number of the minimal rectangle size. Count as (width x height).
    // by default it's 1.
    imageComparison.setMinimalRectangleSize(100);
    imageComparison.getMinimalRectangleSize();

    //Change the level of the pixel tolerance:
    imageComparison.setPixelToleranceLevel(0.2);
    imageComparison.getPixelToleranceLevel();

    //After configuring the ImageComparison object, can be executed compare() method:
    ImageComparisonResult imageComparisonResult = imageComparison.compareImages();

    //Can be found ComparisonState.
    ImageComparisonState imageComparisonState = imageComparisonResult.getImageComparisonState();

    //And Result Image
    BufferedImage resultImage = imageComparisonResult.getResult();

    //Image can be saved after comparison, using ImageComparisonUtil.
    ImageComparisonUtil.saveImage(resultDestination, resultImage);

    System.out.println(imageComparisonResult.getDifferencePercent());
  }
}
