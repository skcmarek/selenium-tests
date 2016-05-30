package com.wikia.webdriver.pageobjectsfactory.componentobject.visualeditordialogs;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wikia.webdriver.common.logging.PageObjectLogging;
import com.wikia.webdriver.pageobjectsfactory.pageobject.BasePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.visualeditor.VisualEditorPageObject;

public abstract class VisualEditorDialog<T> extends BasePageObject {

  @FindBy(css = ".oo-ui-window-ready")
  protected WebElement dialog;
  @FindBy(css = ".oo-ui-window-ready .oo-ui-window-frame")
  private WebElement frame;
  @FindBy(css = ".oo-ui-window-ready .oo-ui-icon-close")
  private WebElement closeButton;

  public T waitForDialogVisible() {
    wait.forElementVisible(dialog);

    return (T) this;
  }

  public void waitForDialogNotVisible() {
    waitForElementNotVisibleByElement(dialog);
  }

  public VisualEditorPageObject closeDialog() {
    waitForDialogVisible();
    wait.forElementClickable(closeButton);
    closeButton.click();
    waitForDialogNotVisible();
    PageObjectLogging.log("closeDialog", "Dialog is closed", true);
    return new VisualEditorPageObject(driver);
  }
}
