package com.example.hotmail.tests;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import com.example.hotmail.model.HomePage;
import com.example.hotmail.model.LoginPage;
import com.example.hotmail.model.ResetPasswordPage;
import junit.framework.TestCase;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * The {@code Sample} class represents sample tests for
 * hotmail mail.
 *
 * @author Waldemar Sojka
 *
 */
public class HotmailTestCases extends TestCase {

	private static final String USERNAME = "username@hotmail.com";

	private static final String PASSWORD = "password";

	private static final String SELENIUM_RC_URL = "http://localhost:4444/wd/hub";

	private static WebDriver driver = null;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Exception {
		DesiredCapabilities capabillities = DesiredCapabilities.chrome();
		driver = new RemoteWebDriver(new URL(SELENIUM_RC_URL), capabillities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	/**
	 * Test for signing in on hotmail.com with proper
	 * username and password
	 *
	 * @throws Exception
	 */
	public static void testLogin() throws Exception {
		LoginPage lp = new LoginPage(driver);
		HomePage hp = new HomePage(driver);

		lp.open();
		lp.login(USERNAME, PASSWORD, false);

		hp.waitForPage();
		// verify that home page is visible
		Assert.assertTrue(hp.isPresent());

		hp.logout();
		// verify that we are not on home page anymore
		Assert.assertFalse(hp.isPresent());
	}

	/**
	 * Test for signing in on hotmail.com with
	 * incorrect username or password.
	 *
	 * @throws Exception
	 */
	public static void testLoginError() throws Exception {
		LoginPage lp = new LoginPage(driver);

		// open hotmail login page
		lp.open();

		// signin with incorrect password
		lp.login(USERNAME + "XYZ", PASSWORD, false);

		// verify that error is visible on login page
		Assert.assertTrue(lp.isErrorVisible());

		// signin with incorrect password
		lp.login(USERNAME, PASSWORD + "XYZ", false);

		// verify that error is visible on login page
		Assert.assertTrue(lp.isErrorVisible());
	}

	public static void testLoginRememberMe() throws Exception {
		LoginPage lp = new LoginPage(driver);
		HomePage hp = new HomePage(driver);

		lp.open();
		lp.login(USERNAME, PASSWORD, true);

		hp.waitForPage();
		// verify that home page is visible
		Assert.assertTrue(hp.isPresent());

		// open login page second time
		lp.open();

		// verify that we are not on home page anymore
		hp.waitForPage();
		Assert.assertTrue(hp.isPresent());
	}

	/**
	 * Test for reseting user password with sending
	 * an email.
	 *
	 * @throws Exception
	 */
	public static void testResetPasswordSendEmail() throws Exception {
		LoginPage lp = new LoginPage(driver);
		ResetPasswordPage rpp = new ResetPasswordPage(driver);

		lp.open();
		lp.clickCantAccessYourAccountLink();
		rpp.waitForPage();
		rpp.setAccountName(USERNAME);
		rpp.setCaptcha();
		rpp.clickNext();
		boolean res = rpp.selectEmailMeResetLink();
		Assert.assertTrue(res);
	}

	/**
	 * Test for reseting user password with security
	 * question.
	 *
	 * @throws Exception
	 */
	public static void testResetPasswordNewPassword() throws Exception {
		LoginPage lp = new LoginPage(driver);
		ResetPasswordPage rpp = new ResetPasswordPage(driver);

		lp.open();
		lp.clickCantAccessYourAccountLink();
		rpp.waitForPage();
		rpp.setAccountName(USERNAME);
		rpp.setCaptcha();
		rpp.clickNext();
		boolean res = rpp.selectSecurityQuestion("answer", "new_password");
		Assert.assertTrue(res);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	public void tearDown() throws Exception {
		driver.quit();
	}

}
