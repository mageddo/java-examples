package com.example.hotmail.model;

import com.example.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * The {@code HomePage} class represents hotmail Home page.
 * 
 * @author Waldemar Sojka
 *
 */
public class HomePage extends Page {

	private final String TITLE = "Home - Windows Live";

	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public HomePage(WebDriver driver) {
		super(driver);
	}

	/* (non-Javadoc)
	 * @see com.example.model.hotmail.Page#getTitle()
	 */
	@Override
	public String getTitle() {
		return TITLE;
	}

	/**
	 * Logouts from hotmail.com
	 */
	public void logout() {
		WebElement logout = driver.findElement(By.xpath("//a[@id='c_signout']"));

		logout.click();

		// waits as long as we are on home page
		(new WebDriverWait(driver, TIMEOUT))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return driver.getTitle().compareTo(TITLE) != 0;
					}
				});
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ignore) {
		}
	}

}
