package com.wikia.webdriver.pageobjectsfactory.componentobject.visualeditordialogs;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wikia.webdriver.common.core.interactions.Elements;
import com.wikia.webdriver.common.logging.PageObjectLogging;
import com.wikia.webdriver.pageobjectsfactory.pageobject.article.ArticlePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.visualeditor.VisualEditorPageObject;

public class VisualEditorEditTemplateDialog
    extends VisualEditorDialog<VisualEditorEditTemplateDialog> {

  private static final By PARAM_LABEL_BY = By.cssSelector(".ve-ui-mwParameterPage-label");
  private static final By PARAM_INPUT_BY = By.cssSelector(".ve-ui-mwParameterPage-field textarea");
  private static final By TEMPLATE_PARAMS_BY = By.cssSelector(".ve-ui-mwParameterPage");
  // outside of iframe
  @FindBy(css = ".ve-ui-wikiaTemplateGetInfoWidget-templateInfoButton a")
  private WebElement getInfoLink;
  @FindBy(css = ".ve-ui-mwParameterPage")
  private List<WebElement> templateParams;
  @FindBy(css = ".oo-ui-processDialog-actions-primary a")
  private WebElement doneButton;
  @FindBy(css = ".ve-ui-wikiaTransclusionDialog-cancelButton a")
  private WebElement cancelButton;
  @FindBy(css = ".ve-ui-wikiaFocusWidget-node")
  private WebElement templateFocusedMode;

  @Override
  public VisualEditorEditTemplateDialog waitForDialogVisible() {
    super.waitForDialogVisible();
    wait.forElementVisible(templateFocusedMode);

    return this;
  }

  @Override
  public void waitForDialogNotVisible() {
    super.waitForDialogNotVisible();
    waitForElementNotVisibleByElement(templateFocusedMode);
  }

  public ArticlePageObject clickGetInfoLink() {
    waitForDialogVisible();
    try {
      wait.forElementVisible(getInfoLink);
      // Opens new tab to Template namespace
      getInfoLink.click();
      return new ArticlePageObject();
    } finally {
      waitForDialogNotVisible();
    }
  }

  public void typeInParam(String paramName, String text) {
    waitForDialogVisible();
    if (isElementOnPage(TEMPLATE_PARAMS_BY)) {
      WebElement targetParam =
          Elements.getElementByChildText(templateParams, PARAM_LABEL_BY, paramName);
      WebElement targetParamInput = targetParam.findElement(PARAM_INPUT_BY);
      targetParamInput.sendKeys(text);
      waitForValueToBePresentInElementsAttributeByElement(targetParamInput, "value", text);
    } else {
      throw new NoSuchElementException("This template has no param.");
    }
    PageObjectLogging.log("typeInParam", "Type " + text + " in the " + paramName + " field.", true,
        driver);
  }

  public VisualEditorPageObject clickDone() {
    waitForDialogVisible();
    try {
      if (isElementOnPage(TEMPLATE_PARAMS_BY)) {
        wait.forElementClickable(doneButton);
        doneButton.click();
      } else {
        throw new NoSuchElementException("This template has no param.");
      }
      return new VisualEditorPageObject(driver);
    } finally {
      waitForDialogNotVisible();
    }
  }

  public VisualEditorPageObject clickCancel() {
    waitForDialogVisible();
    try {
      if (isElementOnPage(TEMPLATE_PARAMS_BY)) {
        wait.forElementClickable(cancelButton);
        cancelButton.click();
      } else {
        throw new NoSuchElementException("This template has no param.");
      }
      return new VisualEditorPageObject(driver);
    } finally {
      waitForDialogNotVisible();
    }
  }
}
