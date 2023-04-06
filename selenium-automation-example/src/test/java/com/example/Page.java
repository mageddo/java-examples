package com.example;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * This is basic abstract class that every other class representing
 * hotmail's page should inherit from.
 *
 * @author Waldemar Sojka
 *
 */
public abstract class Page {

	protected WebDriver driver;

	// hotmail's login page url
	protected final String HOTMAIL_URL = "http://hotmail.com";

	// default timeout for waitForPage() method
	protected Duration TIMEOUT = Duration.ofMillis(120);

	// abstract method returning title for current page
	public abstract String getTitle();

	/**
	 * Constructor
	 *
	 * @param driver
	 */
	public Page(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Checks whether given page is actually present
	 *
	 * @return true if page is present, false otherwise
	 */
	public boolean isPresent() {
		return getTitle().compareTo(driver.getTitle()) == 0;
	}

	/**
	 * Waits for page with given title
	 */
	public void waitForPage() {
		(new WebDriverWait(driver, TIMEOUT))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return driver.getTitle().compareTo(getTitle()) == 0;
					}
				});
	}

	/**
	 * Sleeps specified amount of milliseconds
	 *
	 * @param ms time to wait in milliseconds
	 */
	public void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ignore) {}
	}
}
