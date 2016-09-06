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
import com.wikia.webdriver.elements.mercury.components.discussions.common.PostEntity;
import com.wikia.webdriver.elements.mercury.components.discussions.common.PostsCreator;
import com.wikia.webdriver.elements.mercury.components.discussions.desktop.CategoryPill;
import com.wikia.webdriver.elements.mercury.components.discussions.desktop.PostsCreatorDesktop;
import com.wikia.webdriver.elements.mercury.components.discussions.mobile.PostsCreatorMobile;
import com.wikia.webdriver.elements.mercury.pages.discussions.PostsListPage;
import org.testng.annotations.Test;

@Execute(onWikia = MercuryWikis.DISCUSSIONS_AUTO)
@Test(groups = "discussions-creating-posts")
public class CreatingPostTests extends NewTestTemplate {

  private static final String DESKTOP_RESOLUTION = "1920x1080";

  private static final String TEXT = "text";

  /*
   * ANONS ON MOBILE SECTION
   */

  @Test(groups = "discussions-anonUserOnMobileCanNotWriteNewPost")
  @Execute(asUser = User.ANONYMOUS)
  @InBrowser(browser = Browser.CHROME, emulator = Emulator.GOOGLE_NEXUS_5)
  public void anonUserOnMobileCanNotWriteNewPost() {
    userOnMobileMustBeLoggedInToUsePostCreator();
  }

  /*
   * ANONS ON DESKTOP SECTION
   */

  @Test(groups = "discussions-anonUserOnDesktopCanNotWriteNewPost")
  @Execute(asUser = User.ANONYMOUS)
  @InBrowser(browser = Browser.FIREFOX, browserSize = DESKTOP_RESOLUTION)
  public void anonUserOnDesktopCanNotWriteNewPost() {
    userOnDesktopMustBeLoggedInToUsePostCreator();
  }

  /*
   * LOGGED-IN USERS ON MOBILE SECTION
   */

  @Test(groups = "discussions-loggedInUsersMobilePosting")
  @Execute(asUser = User.USER)
  @InBrowser(browser = Browser.CHROME, emulator = Emulator.GOOGLE_NEXUS_5)
  public void userOnMobileCannotSavePostWithoutCategoryAndDescription() {
    PostsListPage postListPage = new PostsListPage().open();
    PostsCreator postsCreator = postListPage.getPostsCreatorMobile();
    assertThatPostWithoutSelectedCategoryAndDescriptionCannotBeAdded(postsCreator);
  }

  @Test(groups = "discussions-loggedInUsersMobilePosting")
  @Execute(asUser = User.USER)
  @InBrowser(browser = Browser.CHROME, emulator = Emulator.GOOGLE_NEXUS_5)
  public void userOnMobileCanAddPostWithoutTitle() {
    final String description = createUniqueDescription();

    PostsListPage postListPage = new PostsListPage().open();
    PostsCreator postsCreator = postListPage.getPostsCreatorMobile();

    final CategoryPill categoryPill = fillPostCategoryWith(postsCreator, description);

    postsCreator.clickSubmitButton();

    PostEntity postEntity = postListPage.getPost()
        .waitForPostToAppearWith(description)
        .getTheNewestPost();

    assertThatPostWasAddedWith(postEntity, description, categoryPill.getName());
  }

  /*
   * LOGGED-IN USERS ON DESKTOP SECTION
   */

  @Test(groups = "discussions-loggedInUsersDesktopPosting")
  @Execute(asUser = User.USER)
  @InBrowser(browser = Browser.FIREFOX, browserSize = DESKTOP_RESOLUTION)
  public void loggedInUserCanExpandPostEditorOnDesktop() {
    PostsCreatorDesktop postsCreator = new PostsListPage().open().getPostsCreatorDesktop();

    postsCreator.click();

    Assertion.assertTrue(postsCreator.isExpanded());
    Assertion.assertFalse(postsCreator.isPostButtonActive());
  }

  @Test(groups = "discussions-loggedInUsersDesktopPosting")
  @Execute(asUser = User.USER)
  @InBrowser(browser = Browser.FIREFOX, browserSize = DESKTOP_RESOLUTION)
  public void userOnDesktopCannotSavePostWithoutCategoryAndDescription() {
    PostsListPage postListPage = new PostsListPage().open();
    PostsCreator postsCreator = postListPage.getPostsCreatorDesktop();
    assertThatPostWithoutSelectedCategoryAndDescriptionCannotBeAdded(postsCreator);
  }

  @Test(groups = "discussions-loggedInUsersDesktopPosting")
  @Execute(asUser = User.USER)
  @InBrowser(browser = Browser.FIREFOX, browserSize = DESKTOP_RESOLUTION)
  public void userOnDesktopCanAddPostWithoutTitle() {
    final String description = createUniqueDescription();

    PostsListPage postListPage = new PostsListPage().open();
    PostsCreator postsCreator = postListPage.getPostsCreatorDesktop();

    final CategoryPill categoryPill = fillPostCategoryWith(postsCreator, description);

    postsCreator.clickSubmitButton();

    PostEntity postEntity = postListPage.getPost()
        .waitForPostToAppearWith(description)
        .getTheNewestPost();

    assertThatPostWasAddedWith(postEntity, description, categoryPill.getName());
  }

  /**
   * TESTING METHODS SECTION
   */

  private void userOnMobileMustBeLoggedInToUsePostCreator() {
    PostsCreatorMobile postsCreator = new PostsListPage().open().getPostsCreatorMobile();

    Assertion.assertTrue(postsCreator.click().isSignInDialogVisible());

    postsCreator.clickOkButtonInSignInDialog();

    Assertion.assertTrue(postsCreator.click().isSignInDialogVisible());

    postsCreator.clickSignInButtonInSignInDialog();

    Assertion.assertTrue(driver.getCurrentUrl().contains(MercurySubpages.JOIN_PAGE));
  }

  private void userOnDesktopMustBeLoggedInToUsePostCreator() {
    PostsCreatorDesktop postsCreator = new PostsListPage().open().getPostsCreatorDesktop();

    Assertion.assertTrue(postsCreator.click().isSignInDialogVisible());

    postsCreator.clickOkButtonInSignInDialog();

    Assertion.assertTrue(postsCreator.click().isSignInDialogVisible());

    postsCreator.clickSignInButtonInSignInDialog();

    Assertion.assertTrue(driver.getCurrentUrl().contains(MercurySubpages.REGISTER_PAGE));
  }

  /**
   * This test covers all situations when post cannot be added (submit button is disabled).
   *
   * | Category | Title | Description |
   * |          |     x |             |
   * |          |     x |           x |
   * |          |       |           x |
   * |        x |       |             |
   * |        x |     x |             |
   *
   * * x - means category was selected or text was added
   */
  private void assertThatPostWithoutSelectedCategoryAndDescriptionCannotBeAdded(PostsCreator postsCreator) {
    postsCreator.click()
        .closeGuidelinesMessage();
    Assertion.assertFalse(postsCreator.isPostButtonActive());

    postsCreator.fillTitleWith(TEXT);
    Assertion.assertFalse(postsCreator.isPostButtonActive(),
        "User should not be able to add post with only title filled.");

    postsCreator.fillDescriptionWith(TEXT);
    Assertion.assertFalse(postsCreator.isPostButtonActive(),
        "User should not be able to add post with title and description filled.");

    postsCreator.clearTitle();
    Assertion.assertFalse(postsCreator.isPostButtonActive(),
        "User should not be able to add post with only description filled.");
    postsCreator.clearDescription();

    postsCreator.clickAddCategoryButton()
        .findCategoryOnPosition(0)
        .click();
    Assertion.assertFalse(postsCreator.isPostButtonActive(),
        "User should not be able to add post with only category selected.");

    postsCreator.fillTitleWith(TEXT);
    Assertion.assertFalse(postsCreator.isPostButtonActive(),
        "User should not be able to add post with category selected and title filled.");
  }

  private String createUniqueDescription() {
    final long timestamp = System.nanoTime();
    return "Automated test, timestamp " + timestamp;
  }

  private CategoryPill fillPostCategoryWith(final PostsCreator postsCreator, final String description) {
    CategoryPill categoryPill = postsCreator.click()
        .closeGuidelinesMessage()
        .fillDescriptionWith(description)
        .clickAddCategoryButton()
        .findCategoryOnPosition(0);

    categoryPill.click();

    return categoryPill;
  }

  private void assertThatPostWasAddedWith(final PostEntity postEntity, final String description,
      final String categoryName) {
    if (null != postEntity) {
      Assertion.assertStringContains(postEntity.findTimestamp(), "now");
      Assertion.assertEquals(postEntity.findDescription(), description);
      Assertion.assertStringContains(postEntity.findCategory(), categoryName);
    } else {
      Assertion.fail("Post with description \"" + description + "\" was not added or not found.");
    }
  }
}
