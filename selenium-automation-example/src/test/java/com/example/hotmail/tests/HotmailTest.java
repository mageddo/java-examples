package com.example.hotmail.tests;

import com.exame.RemoteDriverCreator;
import com.example.hotmail.model.HomePage;
import com.example.hotmail.model.LoginPage;
import com.example.hotmail.model.ResetPasswordPage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

/**
 * The {@code Sample} class represents sample tests for
 * hotmail mail.
 *
 * @author Waldemar Sojka
 */
public class HotmailTest {

  private static final String USERNAME = "username@hotmail.com";

  private static final String PASSWORD = "password";

  private static final String SELENIUM_RC_URL = "http://localhost:4444/wd/hub";

  private WebDriver driver = null;

  @Before
  public void setUp() throws Exception {
    driver = RemoteDriverCreator.getInstance();
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
  }

  /**
   * Test for signing in on hotmail.com with proper
   * username and password
   *
   * @throws Exception
   */
  @Test
  public void testLogin() throws Exception {
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
  @Test
  public void testLoginError() throws Exception {
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

  @Test
  public void testLoginRememberMe() throws Exception {
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
  @Test
  public void testResetPasswordSendEmail() throws Exception {
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
  @Test
  public void testResetPasswordNewPassword() throws Exception {
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

}
