package com.wikia.webdriver.elements.oasis.components.visualeditor;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wikia.webdriver.pageobjectsfactory.componentobject.visualeditordialogs.VisualEditorDialog;
import com.wikia.webdriver.pageobjectsfactory.pageobject.visualeditor.VisualEditorPageObject;

public class VideoDialog extends VisualEditorDialog<VideoDialog> {

  @FindBy(css = ".oo-ui-textInputWidget input")
  private WebElement searchInput;
  @FindBy(css = ".oo-ui-window-foot .oo-ui-labelElement-label")
  private WebElement addMediaButton;

  public VisualEditorPageObject addVideoByURL(String url) {
    searchInput.sendKeys(url);
    wait.forElementVisible(By.cssSelector(".video-thumbnail"));

    clickAddMediaButton();

    return new VisualEditorPageObject(driver);
  }

  private void clickAddMediaButton() {
    wait.forElementVisible(addMediaButton);
    addMediaButton.click();
    waitForDialogNotVisible();
  }
}
