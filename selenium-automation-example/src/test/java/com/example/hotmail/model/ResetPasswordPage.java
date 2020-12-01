package com.example.hotmail.model;

import com.example.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * The {@code ResetPasswordPage} represents page used to
 * reset user's password.
 * 
 * @author Waldemar Sojka
 *
 */
public class ResetPasswordPage extends Page {

	private final String TITLE = "Reset your password";

	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	public ResetPasswordPage(WebDriver driver) {
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
	 * Sets account name
	 * 
	 * @param name account name
	 */
	public void setAccountName(String name) {
		WebElement accountName = driver.findElement(By.xpath("//input[@id='iMemberName']"));
		accountName.sendKeys(name);
	}

	/**
	 * Sets captcha code. This should be fixed value available
	 * only in testing environment.
	 */
	public void setCaptcha() {
		// This can be hardcoded value working only in test
		// environment. If it would be possible to automate
		// captcha then what is the purpose of captcha at all? :)
		WebElement captcha = driver.findElement(By.xpath("//input[starts-with(@id, 'wlspispSolutionElement')]"));
		captcha.sendKeys("abc123");
	}

	/**
	 * Clicks "Next" button
	 */
	public void clickNext() {
		WebElement nextBtn = driver.findElement(By.xpath("//input[@id='btHipSubmit']"));
		nextBtn.click();
	}

	/**
	 * Waits for reset option to be available
	 */
	private void waitForResetPasswordOptions() {
		(new WebDriverWait(driver, TIMEOUT))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return driver.findElements(
								By.xpath("//input[@id='idProofEmail']")).size() > 0;
					}
				});
	}

	/**
	 * Resets account password by sending email to
	 * another account setup during enrollment. 
	 * 
	 * @return true if email send correctly, false otherwise
	 */
	public boolean selectEmailMeResetLink() {
		waitForResetPasswordOptions();

		WebElement opt = driver.findElement(By.xpath("//input[@id='idProofEmail']"));
		opt.click();
		sleep(1000);
		WebElement next = driver.findElement(By.xpath("//input[@id='idEmailSubmit']"));
		next.click();
		final String email_sent = "//div[contains(., \"We've sent a password reset link to these email addresses.\")]";
		try {
			(new WebDriverWait(driver, TIMEOUT))
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							return driver.findElements(By.xpath(email_sent))
									.size() > 0;
						}
					});
		} catch (Exception ignore) {}

		if (driver.findElements(By.xpath(email_sent)).size() > 0)
			return true;
		else
			return false;

	}

	/**
	 * Resets account password by asking security question.
	 * 
	 * @param answer answer for security question
	 * @param newPassword new password to use
	 * @return true is password changed correctly, false otherwise
	 */
	public boolean selectSecurityQuestion(String answer, String newPassword) {
		waitForResetPasswordOptions();

		WebElement opt = driver.findElement(By.xpath("//input[@id='idProofSQSA']"));
		opt.click();
		sleep(1000);
		WebElement ans = driver.findElement(By.xpath("//input[@id='iSecretAnswer']"));
		ans.sendKeys(answer);
		WebElement next = driver.findElement(By.xpath("//input[@id='btQsaSubmit']"));
		next.click();

		(new WebDriverWait(driver, TIMEOUT))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return driver.findElements(
								By.xpath("//div[@id='NewPasswordSection']"))
								.size() > 0;
					}
				});

		WebElement pass = driver.findElement(By.xpath("//input[@id='iPassword']"));
		WebElement pass2 = driver.findElement(By.xpath("//input[@id='iRetypePassword']"));
		pass.sendKeys(newPassword);
		pass2.sendKeys(newPassword);
		next = driver.findElement(By.xpath("//input[@id='btPwdSubmit']"));
		next.click();
		final String title = "Sign in to your Microsoft account";
		try {
			(new WebDriverWait(driver, TIMEOUT))
					.until(new ExpectedCondition<Boolean>() {
						public Boolean apply(WebDriver d) {
							return driver.getTitle().compareTo(title) == 0;
						}
					});
		} catch (Exception ignore) {}

		return driver.getTitle().compareTo(title) == 0;
	}
}
