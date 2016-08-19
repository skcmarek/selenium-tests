package com.wikia.webdriver.testcases.adstests;

import com.google.common.collect.ImmutableMap;
import com.wikia.webdriver.common.contentpatterns.AdsContent;
import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.core.url.Page;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.pageobjectsfactory.pageobject.adsbase.AdsRecoveryObject;
import org.openqa.selenium.*;
import org.testng.annotations.Test;

import java.util.Map;

public class TestAdsRecoveryMetricsOasis extends NewTestTemplate {

  private static Dimension DESKTOP_SIZE = new Dimension(1920, 1080);

  @Test(
          groups = "AdsRecoveryMetricsOasis"
  )
  public void adsRecoveryMetricsOasis() {
    Page page = new Page("arecovery", "SyntheticTests/Static_image");
    String url = urlBuilder.getUrlForPage(page);
    AdsRecoveryObject adsBaseObject = new AdsRecoveryObject(driver, url, DESKTOP_SIZE);
    driver.manage().window().setSize(DESKTOP_SIZE);

    //TOP_LEADERBOARD
    Map<String, Object> leaderBoardSlot = ImmutableMap.<String, Object>builder()
            .put("adUnitId", "wikia_gpt/5441/wka.life/_arecovery//article/gpt/TOP_LEADERBOARD")
            .put("slotName", AdsContent.TOP_LB)
            .put("lineItemId", 277592292)
            .put("src", "gpt")
            .build();
    String adUnitIdLeaderboard = leaderBoardSlot.get("adUnitId").toString();
    String slotNameLeaderboard = leaderBoardSlot.get("slotName").toString();

    String recoveredAdUnitIdSelectorLeaderboard = "#" + adsBaseObject.getRecoveredAdUnitId(adUnitIdLeaderboard);
    WebElement recoveredSlotLeaderboard = driver.findElement(By.cssSelector(recoveredAdUnitIdSelectorLeaderboard));
    boolean resultLeaderboard = true;
    boolean resultMedrec = true;

    try {
      adsBaseObject.triggerAdSlot(slotNameLeaderboard)
              .verifyLineItemId(slotNameLeaderboard, Integer.valueOf(leaderBoardSlot.get("lineItemId").toString()))
              .verifyExpandedAdVisibleInSlot(recoveredAdUnitIdSelectorLeaderboard, recoveredSlotLeaderboard);
    } catch (WebDriverException e) {
      resultLeaderboard = false;
    }

    //MEDREC
    Map<String, Object> slotInfo = ImmutableMap.<String, Object>builder()
            .put("adUnitId", "wikia_gpt/5441/wka.life/_arecovery//article/gpt/TOP_RIGHT_BOXAD")
            .put("slotName", AdsContent.MEDREC)
            .put("lineItemId", 277592292)
            .put("src", "gpt")
            .build();
    String adUnitId = slotInfo.get("adUnitId").toString();
    String slotName = slotInfo.get("slotName").toString();

    String recoveredAdUnitIdSelector = "#" + adsBaseObject.getRecoveredAdUnitId(adUnitId);
    WebElement recoveredSlot = driver.findElement(By.cssSelector(recoveredAdUnitIdSelector));

    try {
      adsBaseObject.triggerAdSlot(slotName)
              .verifyLineItemId(slotName, Integer.valueOf(slotInfo.get("lineItemId").toString()))
              .verifyExpandedAdVisibleInSlot(recoveredAdUnitIdSelector, recoveredSlot);
    } catch (WebDriverException e) {
      resultMedrec = false;
    }

    Assertion.assertEquals(resultLeaderboard || resultMedrec, true, "Both Slots were not visible");
  }
}
