package com.wikia.webdriver.testcases.followingtests;

import org.testng.annotations.Test;

import com.wikia.webdriver.common.core.annotations.Execute;
import com.wikia.webdriver.common.core.helpers.User;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.SpecialFollowPageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.SpecialVideosPage;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.filepage.FilePagePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.watch.WatchPageObject;

public class FollowVideosTests extends NewTestTemplate {

  String videoName;

  @Test(groups = "FollowVideo")
  @Execute(asUser = User.USER)
  public void FollowVideo_001_setup() {
    SpecialVideosPage special = new SpecialVideosPage().open();
    WatchPageObject watch = special.unfollowVideo(wikiURL, special.getRandomVideo());
    watch.confirmWatchUnwatch();
    special.verifyPageUnfollowed();
    videoName = special.getHeaderText();
  }

  @Test(groups = "FollowVideo", dependsOnMethods = {"FollowVideo_001_setup"})
  @Execute(asUser = User.USER)
  public void FollowVideo_002_follow() {
    new FilePagePageObject(driver).open(videoName).follow();
  }

  @Test(groups = {"FollowVideo", "Follow"}, dependsOnMethods = {"FollowVideo_002_follow"})
  @Execute(asUser = User.USER)
  public void FollowVideo_003_verify() {
    new SpecialFollowPageObject(driver).open().verifyFollowedImageVideo(videoName);
  }
}
