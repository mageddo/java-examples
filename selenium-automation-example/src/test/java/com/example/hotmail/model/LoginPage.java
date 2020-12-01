package com.example.hotmail.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * The {@code LoginPage} represents hotmail.com login page.
 * 
 * @author Waldemar Sojka
 *
 */
public class LoginPage extends Page {

	private final String TITLE = "Sign In";

	private final String ERROR_XPATH = "//div[contains(@class, 'errorDiv')]";

	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public LoginPage(WebDriver driver) {
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
	 * Opens hotmail login page
	 */
	public void open() {
		driver.get(HOTMAIL_URL);
	}
	
	/**
	 * Sign-in to hotmail.com mailbox using given username and
	 * password. If keep_signed_in is true, then checkbox
	 * on login page with be marked.
	 * 
	 * @param user account username
	 * @param pass account password 
	 * @param keep_signed_in
	 */
	public void login(String user, String pass, boolean keep_signed_in) {
		driver.get(HOTMAIL_URL);

		if (!isPresent())
			return;

		WebElement userField = driver.findElement(By.xpath("//input[@name='login']"));
		WebElement passField = driver.findElement(By.xpath("//input[@name='passwd']"));
		WebElement kmsi = driver.findElement(By.xpath("//input[@name='KMSI']"));
		WebElement submitBtn = driver.findElement(By.xpath("//input[@name='SI']"));

		userField.sendKeys(user);
		passField.sendKeys(pass);

		if (keep_signed_in) {
			if (!kmsi.isSelected())
				kmsi.click();
		} else if (kmsi.isSelected())
			kmsi.click();

		submitBtn.submit();
	}

	/**
	 * Returns true if there is an error visible on login page, which
	 * indicates problems with signing in.
	 * 
	 * @return true if error is visible, false otherwise
	 */
	public boolean isErrorVisible() {
		try {
			(new WebDriverWait(driver, TIMEOUT))
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							return driver.findElements(By.xpath(ERROR_XPATH))
									.size() > 0;
						}
					});
		} catch (Exception ignore) {
		}

		if (driver.findElements(By.xpath(ERROR_XPATH)).size() > 0)
			return true;
		else
			return false;

	}

	public void clickCantAccessYourAccountLink() {
		WebElement link = driver.findElement(By.xpath("//div[@id='idDiv_PWD_ForgotPassword']/a"));
		link.click();
	}
}
