package com.wikia.webdriver.testcases.adstests;

import com.wikia.webdriver.common.core.annotations.InBrowser;
import com.wikia.webdriver.common.core.drivers.Browser;
import com.wikia.webdriver.common.core.helpers.Emulator;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.pageobjectsfactory.pageobject.adsbase.AdsBaseObject;
import org.testng.annotations.Test;

public class TestMoat extends NewTestTemplate {

  @Test(groups = {"TestMoatMercury", "TestMoat"}, invocationCount = 10)
  @InBrowser(
          emulator = Emulator.GOOGLE_NEXUS_5,
          browser = Browser.CHROME
  )
  public void testMoatMercury() {
    driver.get("http://project43.wikia.com/wiki/SyntheticTests/Mercury/Slots/ConsecutivePageViews/1");
    AdsBaseObject ads = new AdsBaseObject(driver);
    ads.waitForMobileLeaderboard();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ads.scrollToPosition(".mobile-prefooter");
    ads.waitForMobilePrefooter();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test(groups = {"TestMoatOasis", "TestMoat"}, invocationCount = 10)
  public void testMoatOasis() {
    driver.get("http://project43.wikia.com/wiki/SyntheticTests/Mercury/Slots/ConsecutivePageViews/1");
    AdsBaseObject ads = new AdsBaseObject(driver);
    ads.waitForDesktopLeaderboard();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ads.waitForMedrec();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
