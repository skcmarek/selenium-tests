package com.wikia.webdriver.elements.mercury.components.discussions.desktop;

import com.wikia.webdriver.pageobjectsfactory.pageobject.BasePageObject;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class PostsCreatorDesktop extends BasePageObject {

  @FindBy (css = ".editor-container.pinned-top .editor-wrapper")
  private WebElement postCreatorWrapped;

  @FindBy (css = ".editor-container.pinned-top .editor-wrapper .editor-textarea")
  private WebElement postCreatorUnwrapped;

  @FindBy (css = ".modal-dialog-wrapper:not(.discussion-editor-dialog) .modal-dialog")
  private WebElement dialogSignIn;

  @FindBy (css = ".modal-dialog-content + footer > :first-child")
  private WebElement okButtonInSignInDialog;

  @FindBy (css = ".modal-dialog-content + footer > :last-child")
  private WebElement signInButtonInSignInDialog;

  @FindBy (css = ".pinned-top .active-element-theme-color")
  private WebElement postButton;


  public PostsCreatorDesktop clickPostCreatorWrapped() {
    postCreatorWrapped.click();
    return this;
  }

  public static String getTimeStamp() {
    return BasePageObject.getTimeStamp();
  }

  public boolean isModalDialogVisible() {
    return dialogSignIn.isDisplayed();
  }

  public PostsCreatorDesktop clickOkButtonInSignInDialog() {
    okButtonInSignInDialog.click();
    return this;
  }

  public PostsCreatorDesktop clickSignInButtonInSignInDialog() {
    signInButtonInSignInDialog.click();
    return this;
  }

  public PostsCreatorDesktop clickPostCreatorUnwrapped() {
    postCreatorUnwrapped.click();
    return this;
  }

  public PostsCreatorDesktop typePlainText(String text){
    postCreatorUnwrapped.sendKeys(text);
    return this;
  }

  public boolean isPostButtonActive() {
    return postButton.isEnabled();
  }

  public PostsCreatorDesktop deleteText() {
    postCreatorUnwrapped.clear();
    return this;
  }

  public PostsCreatorDesktop clickPostButton() {
    postCreatorUnwrapped.click();
    return this;
  }

}
