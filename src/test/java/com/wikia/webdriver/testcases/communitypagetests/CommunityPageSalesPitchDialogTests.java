package com.wikia.webdriver.testcases.communitypagetests;

import com.wikia.webdriver.common.contentpatterns.PageContent;
import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.core.annotations.Execute;
import com.wikia.webdriver.common.core.helpers.User;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.pageobjectsfactory.pageobject.actions.DeletePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.article.ArticlePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.communitypage.SalesPitchDialog;
import com.wikia.webdriver.pageobjectsfactory.pageobject.communitypage.SpecialCommunity;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.SpecialNewFilesPageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.filepage.FilePagePageObject;

import org.testng.annotations.Test;

@Test(groups = "CommunityPageTests")
@Execute(onWikia = "mediawiki119")
public class CommunityPageSalesPitchDialogTests extends NewTestTemplate {

  private static final String DIALOG_IMAGE_NAME = "Community-Page-Modal-Image.jpg";

  @Execute(disableCommunityPageSalesPitchDialog = "false")
  public void verifySalesPitchDialogDisplayedOnFourthPageview() {
    String articleTitle = PageContent.ARTICLE_NAME_PREFIX + ArticlePageObject.getTimeStamp();
    ArticlePageObject article = new ArticlePageObject();

    // 2nd pageview
    article.open(articleTitle);
    // 3rd pageview
    article.open(articleTitle);
    // 4th pageview
    article.open(articleTitle);

    SalesPitchDialog dialog = new SalesPitchDialog();
    Assertion.assertTrue(dialog.isVisible());
  }

  @Execute(disableCommunityPageSalesPitchDialog = "false")
  public void verifyEditPageIsNotCountedAsPageview() {
    String articleTitle = PageContent.ARTICLE_NAME_PREFIX + ArticlePageObject.getTimeStamp();
    ArticlePageObject article = new ArticlePageObject();

    // 2nd pageview
    article.open(articleTitle);
    article.navigateToArticleEditPage();

    // 3rd pageview
    article.open(articleTitle);
    article.navigateToArticleEditPage();

    // 4th pageview
    article.open(articleTitle);

    SalesPitchDialog dialog = new SalesPitchDialog();
    Assertion.assertTrue(dialog.isVisible());
  }

  @Execute(disableCommunityPageSalesPitchDialog = "false")
  public void verifyClickEntryPointButtonRedirectToSpecialCommunity() {
    String articleTitle = PageContent.ARTICLE_NAME_PREFIX + ArticlePageObject.getTimeStamp();
    ArticlePageObject article = new ArticlePageObject();

    // 2nd pageview
    article.open(articleTitle);
    // 3rd pageview
    article.open(articleTitle);
    // 4th pageview
    article.open(articleTitle);

    SalesPitchDialog dialog = new SalesPitchDialog();
    dialog.clickEntryPointButton();

    SpecialCommunity page = new SpecialCommunity();
    page.isCommunityPage();
  }

  public void verifySalesPitchDialogIsNotShownIfCookieIsSet() {
    String articleTitle = PageContent.ARTICLE_NAME_PREFIX + ArticlePageObject.getTimeStamp();
    ArticlePageObject article = new ArticlePageObject();

    // 2nd pageview
    article.open(articleTitle);
    // 3rd pageview
    article.open(articleTitle);
    // 4th pageview
    article.open(articleTitle);

    SalesPitchDialog dialog = new SalesPitchDialog();
    dialog.isNotVisible();
  }

  @Test
  @Execute(disableCommunityPageSalesPitchDialog = "false")
  public void verifyImageIsNotSetByDefault() {
    String articleTitle = PageContent.ARTICLE_NAME_PREFIX + ArticlePageObject.getTimeStamp();
    ArticlePageObject article = new ArticlePageObject();

    // 2nd pageview
    article.open(articleTitle);
    // 3rd pageview
    article.open(articleTitle);
    // 4th pageview
    article.open(articleTitle);

    Assertion.assertTrue(new SalesPitchDialog().getImageSource().isEmpty());
  }

  @Test(dependsOnMethods = {"verifyImageIsNotSetByDefault"})
  @Execute(disableCommunityPageSalesPitchDialog = "false", asUser = User.USER)
  public void verifySettingCustomDialogImage() {
    String articleTitle = PageContent.ARTICLE_NAME_PREFIX + ArticlePageObject.getTimeStamp();
    ArticlePageObject article = new ArticlePageObject();

    SpecialNewFilesPageObject filesPage =
        new SpecialNewFilesPageObject(driver).openSpecialNewFiles(wikiURL);
    filesPage.addPhoto();
    filesPage.selectFileToUpload(PageContent.BIGFILE);
    filesPage.clickOnMoreOptions();
    filesPage.setFileName(DIALOG_IMAGE_NAME);
    filesPage.checkIgnoreAnyWarnings();
    filesPage.clickUploadButton();
    filesPage.logOut();

    // 3rd pageview
    article.open(articleTitle);
    // 4th pageview
    article.open(articleTitle);

    Assertion.assertTrue(new SalesPitchDialog().getImageSource().contains(DIALOG_IMAGE_NAME));
  }

  @Test(dependsOnMethods = {"verifyImageIsNotSetByDefault", "verifySettingCustomDialogImage"})
  @Execute(disableCommunityPageSalesPitchDialog = "false", asUser = User.STAFF)
  public void verifyDeletingCustomDialogImage() {
    String articleTitle = PageContent.ARTICLE_NAME_PREFIX + ArticlePageObject.getTimeStamp();
    ArticlePageObject article = new ArticlePageObject();

    DeletePageObject deletePage =
        new FilePagePageObject(driver).open(DIALOG_IMAGE_NAME, true).deletePage();
    deletePage.submitDeletion();
    deletePage.logOut();

    // 2nd pageview
    article.open(articleTitle);
    // 3rd pageview
    article.open(articleTitle);
    // 4th pageview
    article.open(articleTitle);

    Assertion.assertTrue(new SalesPitchDialog().getImageSource().isEmpty());
  }

}
