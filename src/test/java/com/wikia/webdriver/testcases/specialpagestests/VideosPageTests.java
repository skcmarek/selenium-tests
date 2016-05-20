package com.wikia.webdriver.testcases.specialpagestests;

import org.testng.annotations.Test;

import com.wikia.webdriver.common.core.annotations.Execute;
import com.wikia.webdriver.common.core.helpers.User;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.SpecialVideosPage;

public class VideosPageTests extends NewTestTemplate {

  /**
   * Verify UI elements on the Special:Videos page Logged-Out
   */
  @Test(groups = {"VideosPage", "VideosPageTest_001", "Media"})
  public void VideosPageTest_001() {
    SpecialVideosPage specialVideos = new SpecialVideosPage().openRecent();
    specialVideos.verifyElementsOnPage();
  }

  /**
   * Checks if a video can successfully be deleted from the Special:Videos page. Specifically, this
   * test checks if, after the video has been deleted, its title shows up in the delete confirmation
   * presented by Global Notifications. (Note: This test also adds a video beforehand to make sure
   * running this test is sustainable).
   */
  @Test(groups = {"VideosPage", "VideosPageTest_002", "Media"})
  @Execute(asUser = User.STAFF)
  public void VideosPageTest_002() {
    SpecialVideosPage specialVideos = new SpecialVideosPage().open();
    specialVideos.verifyDeleteViaGlobalNotifications();
  }

  /**
   * Checks if a video can successfully be deleted from the Special:Videos page. Specifically, this
   * test checks if, after the video has been deleted, it is no longer present in the list of most
   * recent videos on Special:Videos. (Note: in order to accomplish this the test also adds a video
   * before hand to ensure that 1.) the test is sustainable, and 2.) it knows what the most recent
   * video is).
   */
  @Test(groups = {"VideosPage", "VideosPageTest_003", "Media"})
  @Execute(asUser = User.STAFF)
  public void VideosPageTest_003() {
    SpecialVideosPage specialVideos = new SpecialVideosPage().open();
    specialVideos.verifyDeleteViaVideoNotPresent();
  }
}
