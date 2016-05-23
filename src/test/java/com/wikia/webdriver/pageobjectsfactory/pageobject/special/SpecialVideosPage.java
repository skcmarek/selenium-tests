package com.wikia.webdriver.pageobjectsfactory.pageobject.special;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wikia.webdriver.common.contentpatterns.URLsContent;
import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.core.video.YoutubeVideo;
import com.wikia.webdriver.common.core.video.YoutubeVideoProvider;
import com.wikia.webdriver.common.logging.PageObjectLogging;
import com.wikia.webdriver.pageobjectsfactory.componentobject.lightbox.LightboxComponentObject;
import com.wikia.webdriver.pageobjectsfactory.componentobject.vet.VetAddVideoComponentObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.watch.WatchPageObject;

public class SpecialVideosPage extends SpecialPageObject {

  private static final String SPECIAL_VIDEOS_PATH = "Special:Videos";
  private static final String NEWEST_VIDEO_CSS = ".special-videos-grid li:nth-child(1) .title a";
  @FindBy(css = ".WikiaPageHeader h1")
  private WebElement h1Header;
  @FindBy(css = "a.button.addVideo")
  private WebElement addVideo;
  @FindBy(css = ".special-videos-grid li:nth-child(1)")
  private WebElement newestVideo;
  @FindBy(css = ".special-videos-grid li:nth-child(1) .title")
  private WebElement newestVideoTitle;
  @FindBy(css = ".special-videos-grid li:nth-child(1) .remove")
  private WebElement newestVideoDeleteIcon;
  @FindBy(css = ".image.video > img")
  private List<WebElement> videos;
  @FindBy(css = ".special-videos-grid a.video")
  private List<WebElement> videoItem;
  @FindBy(css = "#WikiaConfirmOk")
  private WebElement deleteConfirmButton;

  public SpecialVideosPage open() {
    getUrl(urlBuilder.getUrlForPath(SPECIAL_VIDEOS_PATH));

    return this;
  }

  public SpecialVideosPage open(String queryString) {
    getUrl(urlBuilder.appendQueryStringToURL(urlBuilder.getUrlForPath(SPECIAL_VIDEOS_PATH),
        queryString));

    return this;
  }

  public SpecialVideosPage openRecent() {
    return open("sort=recent");
  }

  public String getRandomVideo() {
    int rnd = new Random().nextInt(videos.size());
    return videos.get(rnd).getAttribute("data-video-key");
  }

  public WatchPageObject unfollowVideo(String wikiURL, String videoName) {
    getUrl(wikiURL + URLsContent.WIKI_DIR + URLsContent.FILE_NAMESPACE + videoName
        + "?action=unwatch");
    return new WatchPageObject(driver);
  }

  private void verifyH1() {
    wait.forElementVisible(h1Header);
  }

  private void verifyNewestVideo() {
    wait.forElementVisible(newestVideo);
  }

  private void verifyAddVideoButton() {
    wait.forElementClickable(addVideo);
  }

  public VetAddVideoComponentObject clickAddAVideo() {
    verifyAddVideoButton();
    scrollAndClick(addVideo);
    return new VetAddVideoComponentObject(driver);
  }

  public void verifyVideoAdded(String videoTitle) {
    waitForValueToBePresentInElementsAttributeByCss(NEWEST_VIDEO_CSS, "title", videoTitle);
    PageObjectLogging.log("verifyVideoAdded",
        "verify that video with following description was added: " + videoTitle, true);
  }

  public LightboxComponentObject openLightboxForGridVideo(int itemNumber) {
    scrollAndClick(videoItem, itemNumber);
    return new LightboxComponentObject(driver);
  }

  public String getNewestVideoTitle() {
    wait.forElementVisible(newestVideo);
    return newestVideoTitle.getText();
  }

  public void deleteVideo() {
    openRecent();
    jsActions.execute("$('.special-videos-grid .remove').first().show()");
    wait.forElementVisible(newestVideo);
    newestVideoDeleteIcon.click();
    wait.forElementVisible(deleteConfirmButton);
    deleteConfirmButton.click();
  }

  public void verifyDeleteViaGlobalNotifications() {
    YoutubeVideo video = YoutubeVideoProvider.getLatestVideoForQuery("truth");

    addVideoViaAjax(video.getUrl());
    deleteVideo();
    String deletedVideo = "\"File:" + video.getTitle() + "\" has been deleted. (undelete)";
    Assertion.assertEquals(getFlashMessageText(), deletedVideo);
    PageObjectLogging.log("verifyDeleteVideoGlobalNotifications",
        "verify video " + deletedVideo + " was deleted", true);
  }

  public void verifyDeleteViaVideoNotPresent() {
    YoutubeVideo video = YoutubeVideoProvider.getLatestVideoForQuery("truth");

    addVideoViaAjax(video.getUrl());
    deleteVideo();
    verifyNotificationMessage();
    Assertion.assertNotEquals(getNewestVideoTitle(), video.getTitle());
    PageObjectLogging.log("verifyDeleteVideoNotPresent",
        "verify video " + video.getTitle() + " was deleted", true);
  }

  public void verifyElementsOnPage() {
    verifyH1();
    PageObjectLogging.log("verifyElementsOnPage", "verify that H1 is present", true);
    verifyAddVideoButton();
    PageObjectLogging.log("verifyElementsOnPage", "verify that sort dropdown is present", true);
    verifyNewestVideo();
    PageObjectLogging.log("verifyElementsOnPage", "verify that there is at least one video present",
        true);
  }
}
