package com.wikia.webdriver.testcases.adstests;

import com.wikia.webdriver.common.core.annotations.InBrowser;
import com.wikia.webdriver.common.core.drivers.Browser;
import com.wikia.webdriver.common.core.elemnt.JavascriptActions;
import com.wikia.webdriver.common.core.helpers.Emulator;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.elements.mercury.components.Loading;
import com.wikia.webdriver.pageobjectsfactory.pageobject.adsbase.AdsBaseObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.Test;

public class TestUAPViewability extends NewTestTemplate {

  @Test(groups={"TestUAPViewability"})
  @InBrowser(
          emulator = Emulator.GOOGLE_NEXUS_5,
          browser = Browser.CHROME
  )
  public void testUAPViewability() {
    driver.get("http://project43.wikia.com/wiki/SyntheticTests/UAP/Infobox");
    JavascriptExecutor js = driver;
    AdsBaseObject ads = new AdsBaseObject(driver);
    ads.waitForBFAA();
    js.executeScript("document.querySelector('.mobile-in-content').scrollIntoView()");
    ads.waitForMobileInContent();
    js.executeScript("document.querySelector('.mobile-prefooter').scrollIntoView()");
    ads.waitForMobilePrefooter();
    js.executeScript("document.querySelector('.mobile-bottom-leaderboard').scrollIntoView()");
    ads.waitForBFAB();

    ads.clickLink();
    Loading loading = new Loading(driver);
    loading.handleAsyncPageReload();
    ads.waitForPageLoaded();
    ads.waitForBFAA();
    js.executeScript("document.querySelector('.mobile-in-content').scrollIntoView()");
    ads.waitForMobileInContent();
    js.executeScript("document.querySelector('.mobile-prefooter').scrollIntoView()");
    ads.waitForMobilePrefooter();
    js.executeScript("document.querySelector('.mobile-bottom-leaderboard').scrollIntoView()");
    ads.waitForBFAB();
  }
}
