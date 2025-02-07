package com.example.google.model;

import java.io.FileOutputStream;

import com.exame.RemoteDriverCreator;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by elvis on 27/08/16.
 */
public class GoogleSearchTest {

	private WebDriver driver = null;

	@Before
	public void before() {
		this.driver = RemoteDriverCreator.getInstance();
	}

  @After
  public void after(){
    this.driver.close();
    this.driver.quit();
  }

	@Test
	public void search() throws Exception {
		final int search = new GoogleSearch(driver).search("mageddo.com");
		final byte[] screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

		IOUtils.write(screenshotAs, new FileOutputStream("build/tmp.jpg"));
		assertNotEquals(search, 0);
	}

}
