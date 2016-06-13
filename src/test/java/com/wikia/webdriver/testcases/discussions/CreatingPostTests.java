package com.wikia.webdriver.testcases.discussions;

import com.wikia.webdriver.common.contentpatterns.MercurySubpages;
import com.wikia.webdriver.common.contentpatterns.MercuryWikis;
import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.core.annotations.Execute;
import com.wikia.webdriver.common.core.annotations.InBrowser;
import com.wikia.webdriver.common.core.drivers.Browser;
import com.wikia.webdriver.common.core.helpers.Emulator;
import com.wikia.webdriver.common.core.helpers.User;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.elements.mercury.components.discussions.common.Post;
import com.wikia.webdriver.elements.mercury.components.discussions.desktop.PostsCreatorDesktop;
import com.wikia.webdriver.elements.mercury.components.discussions.mobile.PostsCreatorMobile;
import com.wikia.webdriver.elements.mercury.pages.discussions.PostsListPage;

import org.testng.annotations.Test;

@Execute(onWikia = MercuryWikis.DISCUSSIONS_AUTO)
public class CreatingPostTests extends NewTestTemplate {

  private static final String DESKTOP_RESOLUTION = "1920x1080";

  /**
   * ANONS ON MOBILE SECTION
   */

  @Test(groups = "discussions-anonUserOnMobileCanNotWriteNewPost")
  @Execute(asUser = User.ANONYMOUS)
  @InBrowser(emulator = Emulator.GOOGLE_NEXUS_5)

  public void anonUserOnMobileCanNotWriteNewPost() {
    userOnMobileMustBeLoggedInToUsePostCreator();
  }

  /**
   * ANONS ON DESKTOP SECTION
   */

  @Test(groups = "discussions-anonUserOnDesktopCanNotWriteNewPost")
  @Execute(asUser = User.ANONYMOUS)
  @InBrowser(browser = Browser.FIREFOX, browserSize = DESKTOP_RESOLUTION)

  public void anonUserOnDesktopCanNotWriteNewPost() {
    userOnDesktopMustBeLoggedInToUsePostCreator();
  }

  /**
   * LOGGED IN USER ON DESKTOP SECTION
   */

  @Test(groups = "discussions-loggedInUserOnDesktopCanAddNewPost")
  @Execute(asUser = User.USER_3)
  @InBrowser(browserSize = DESKTOP_RESOLUTION)

  public void loggedInUserOnDesktopCanAddNewPost() {
    userOnDesktopAddsNewPost();
  }

  /**
   * TESTING METHODS SECTION
   */


  private void userOnMobileMustBeLoggedInToUsePostCreator() {
    PostsCreatorMobile postsCreator = new PostsListPage().open().getPostsCreatorMobile();

    Assertion.assertTrue(postsCreator.clickPostCreator().isModalDialogVisible());

    postsCreator.clickOkButtonInSignInDialog();

    Assertion.assertTrue(postsCreator.clickPostCreator().isModalDialogVisible());

    postsCreator.clickSignInButtonInSignInDialog();

    Assertion.assertTrue(driver.getCurrentUrl().contains(MercurySubpages.JOIN_PAGE));
  }

  private void userOnDesktopMustBeLoggedInToUsePostCreator() {
    PostsCreatorDesktop postsCreator = new PostsListPage().open().getPostCreatorDesktop();

    Assertion.assertTrue(postsCreator.clickPostCreatorWrapped().isModalDialogVisible());

    postsCreator.clickOkButtonInSignInDialog();

    Assertion.assertTrue(postsCreator.clickPostCreatorWrapped().isModalDialogVisible());

    postsCreator.clickSignInButtonInSignInDialog();

    Assertion.assertTrue(driver.getCurrentUrl().contains(MercurySubpages.REGISTER_PAGE));
  }

  private void userOnDesktopAddsNewPost() {
    PostsListPage postsListPage = new PostsListPage().open();
    PostsCreatorDesktop postsCreator = postsListPage.getPostCreatorDesktop();
    postsCreator.clickPostCreatorWrapped();

    Assertion.assertFalse(postsCreator.isPostButtonActive());

    postsCreator.clickPostCreatorUnwrapped().typePlainText(PostsCreatorDesktop.getTimeStamp());
    Assertion.assertTrue(postsCreator.isPostButtonActive());

    String postText = PostsCreatorDesktop.getTimeStamp();

    Assertion.assertFalse(postsCreator.deleteText().isPostButtonActive());

    postsCreator.typePlainText(postText).clickPostButton();

    Post post = postsListPage.getPost();
    Assertion.assertEquals(post.getTextFromFirstPost(), postText);
  }

}
