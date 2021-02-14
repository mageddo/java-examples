/*
 * Part of the Java Image Processing Cookbook, please see
 * http://www.lac.inpe.br/~rafael.santos/JIPCookbook.jsp
 * for information on usage and distribution.
 * Rafael Santos (rafael.santos@lac.inpe.br)
 */
package com.mageddo.algorithm.imagesimilarity.ex01_jai;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/*
 * This class implements a generic file name filter that allows the listing/selection
 * of JPEG files.
 */
public class JPEGImageFileFilter extends FileFilter implements java.io.FileFilter {
  public boolean accept(File f) {
    if (f.getName().toLowerCase().endsWith(".jpeg")) return true;
    if (f.getName().toLowerCase().endsWith(".jpg")) return true;
    return false;
  }

  public String getDescription() {
    return "JPEG files";
  }

}
