package com.wikia.webdriver.common.eventlisteners;

import java.lang.reflect.Method;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;

import com.wikia.webdriver.common.core.AlertHandler;
import com.wikia.webdriver.common.core.SelectorStack;
import com.wikia.webdriver.common.core.TestContext;
import com.wikia.webdriver.common.core.XMLReader;
import com.wikia.webdriver.common.core.annotations.DontRun;
import com.wikia.webdriver.common.core.annotations.Execute;
import com.wikia.webdriver.common.core.configuration.Configuration;
import com.wikia.webdriver.common.core.elemnt.JavascriptActions;
import com.wikia.webdriver.common.core.helpers.User;
import com.wikia.webdriver.common.driverprovider.DriverProvider;
import com.wikia.webdriver.common.logging.PageObjectLogging;
import com.wikia.webdriver.common.logging.TestReport;
import com.wikia.webdriver.pageobjectsfactory.pageobject.WikiBasePageObject;

public class BrowserEventListener extends AbstractWebDriverEventListener implements ITestListener {

  private By lastFindBy;

  @Override
  public void onTestStart(ITestResult result) {
    String testName = result.getName().toString();
    String className = result.getTestClass().getName().toString();
    System.out.println(className + " " + testName);

    TestReport.startTest();
    TestReport.setTestName(result.getName().toString());
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    PageObjectLogging.stopLogging();
  }

  @Override
  public void onTestFailure(ITestResult result) {
    PageObjectLogging.logError("Test Failed", result.getMethod().toString());
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    if (!PageObjectLogging.isTestStarted()) {
      PageObjectLogging.start(result.getMethod().getConstructorOrMethod().getMethod());
    }
    if (result.getMethod().getConstructorOrMethod().getMethod()
        .isAnnotationPresent(DontRun.class)) {
      PageObjectLogging.log("Test SKIPPED", "this test is not supported in this environment", true);
      result.setStatus(ITestResult.SUCCESS);
      onTestSuccess(result);
    } else {
      result.setStatus(ITestResult.FAILURE);
      result.setThrowable(new SkipException("TEST SKIPPED"));
      onTestFailure(result);
    }
    if (PageObjectLogging.isTestStarted()) {
      PageObjectLogging.stopLogging();
    }
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

  }

  @Override
  public void onStart(ITestContext context) {
    PageObjectLogging.startReport();
  }

  @Override
  public void onFinish(ITestContext context) {
    PageObjectLogging.FinishReport();
  }

  @Override
  public void beforeNavigateBack(WebDriver driver) {
    PageObjectLogging.log("Navigate Back", "attempting to navigate back", true);
  }

  @Override
  public void afterNavigateBack(WebDriver driver) {
    PageObjectLogging.log("Navigate Back", "previous page loaded", true);
  }

  @Override
  public void beforeNavigateForward(WebDriver driver) {
    PageObjectLogging.log("Navigate Froward", "attempting to navigate forward", true);
  }

  @Override
  public void afterNavigateForward(WebDriver driver) {
    PageObjectLogging.log("Navigate Froward", "forward page loaded", true);
  }

  @Override
  public void beforeNavigateTo(String url, WebDriver driver) {
    new JavascriptActions(driver).execute("window.stop()");
    PageObjectLogging.log("Navigate to", url, true);
  }

  @Override
  public void afterNavigateTo(String url, WebDriver driver) {
    String currentUrl = driver.getCurrentUrl();
    if (!AlertHandler.isAlertPresent(driver)) {
      if (url.equals(driver.getCurrentUrl())) {
        PageObjectLogging.log("Url after navigation", currentUrl, true);
      } else {
        if (driver.getCurrentUrl().contains("data:text/html,chromewebdata ")) {
          driver.get(url);
        }
        PageObjectLogging.logWarning("Url after navigation", driver.getCurrentUrl());
      }
    } else {
      PageObjectLogging.logWarning("Url after navigation",
          "Unable to check URL after navigation - alert present");
    }

    if (driver.getCurrentUrl().contains(Configuration.getWikiaDomain())) {
      // HACK FOR DISABLING NOTIFICATIONS
      try {
        new JavascriptActions(driver).execute("$(\".sprite.close-notification\")[0].click()");
      } catch (WebDriverException e) {

      }

      if (TestContext.isIsFirstLoad() && "true".equals(Configuration.getMockAds())) {
        driver.manage().addCookie(new Cookie("mock-ads", XMLReader.getValue("mock.ads_token"),
            Configuration.getWikiaDomain(), null, null));
      }
    }

    Method method = TestContext.getCurrentTestMethod();
    Class<?> declaringClass = method.getDeclaringClass();

    if (TestContext.isIsFirstLoad()) {
      User user = null;
      TestContext.setFirstLoad(false);

      if (declaringClass.isAnnotationPresent(Execute.class)) {
        user = declaringClass.getAnnotation(Execute.class).asUser();
      }

      if (method.isAnnotationPresent(Execute.class)) {
        user = method.getAnnotation(Execute.class).asUser();
      }

      if (user != null && user != User.ANONYMOUS) {
        // log in, make sure user is logged in and flow is on the requested url
        new WikiBasePageObject().loginAs(user);
      }
    }
  }

  @Override
  public void beforeFindBy(By by, WebElement element, WebDriver driver) {
    lastFindBy = by;
    SelectorStack.write(by);
    if (element != null) {
      SelectorStack.contextWrite();
    }
  }

  @Override
  public void beforeClickOn(WebElement element, WebDriver driver) {}

  @Override
  public void afterClickOn(WebElement element, WebDriver driver) {
    PageObjectLogging.logInfo("click", lastFindBy.toString());
  }

  @Override
  public void afterChangeValueOf(WebElement element, WebDriver driver) {
    PageObjectLogging.logInfo("ChangeValueOfField", lastFindBy.toString());
  }
}
