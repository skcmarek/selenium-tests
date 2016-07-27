package com.wikia.webdriver.testcases.visualeditor;

import com.wikia.webdriver.common.contentpatterns.PageContent;
import com.wikia.webdriver.common.contentpatterns.VEContent;
import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.core.annotations.Execute;
import com.wikia.webdriver.common.core.helpers.User;
import com.wikia.webdriver.common.dataprovider.VisualEditorDataProvider.InsertDialog;
import com.wikia.webdriver.common.dataprovider.VisualEditorDataProvider.Transclusion;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.pageobjectsfactory.componentobject.visualeditordialogs.VisualEditorEditTemplateDialog;
import com.wikia.webdriver.pageobjectsfactory.componentobject.visualeditordialogs.VisualEditorInsertTemplateDialog;
import com.wikia.webdriver.pageobjectsfactory.componentobject.visualeditordialogs.VisualEditorReviewChangesDialog;
import com.wikia.webdriver.pageobjectsfactory.componentobject.visualeditordialogs.VisualEditorSaveChangesDialog;
import com.wikia.webdriver.pageobjectsfactory.pageobject.WikiBasePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.article.ArticlePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.visualeditor.VisualEditorPageObject;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class VETemplateTests extends NewTestTemplate {


  @Execute(asUser = User.USER)
  @Test(groups = {"VETemplate", "VETemplateTests_001", "VETemplateSearch"})
  public void VETemplateTests_001_SearchTemplate() {
    WikiBasePageObject base = new WikiBasePageObject();
    String articleName = PageContent.ARTICLE_NAME_PREFIX + base.getTimeStamp();
    VisualEditorPageObject ve = base.openVEOnArticle(wikiURL, articleName);

    ve.verifyVEToolBarPresent();
    ve.verifyEditorSurfacePresent();
    VisualEditorInsertTemplateDialog templateDialog =
        (VisualEditorInsertTemplateDialog) ve.openDialogFromMenu(InsertDialog.TEMPLATE);

    templateDialog.typeInSearchInput(VEContent.TEMPLATE_SEARCH_NOMATCH);
    Assertion.assertEquals(templateDialog.getNumberOfResultTemplates(), 0);

    templateDialog.clearSearchInput();
    templateDialog.typeInSearchInput(VEContent.TEMPLATE_SEARCH_MATCH_ARTICLE);
    Assertion.assertTrue(templateDialog.getNumberOfResultTemplates() > 0);
  }

  @Execute(asUser = User.USER)
  @Test(groups = {"VETemplate", "VETemplateTests_002", "VETemplateSuggestion"})
  public void VETemplateTests_002_SuggestedTemplate() {
    WikiBasePageObject base = new WikiBasePageObject();
    String articleName = PageContent.ARTICLE_NAME_PREFIX + base.getTimeStamp();
    VisualEditorPageObject ve = base.openVEOnArticle(wikiURL, articleName);

    ve.verifyVEToolBarPresent();
    ve.verifyEditorSurfacePresent();

    VisualEditorInsertTemplateDialog templateDialog =
        (VisualEditorInsertTemplateDialog) ve.openDialogFromMenu(InsertDialog.TEMPLATE);
    Assertion.assertEquals(templateDialog.getNumberOfResultTemplates(), 0);

    Assertion.assertTrue(templateDialog.areSuggestedTemplateDisplayed());
  }

  @Execute(asUser = User.USER)
  @Test(groups = {"VETemplate", "VETemplateTests_003", "VEAddTemplate"})
  public void VETemplateTests_003_AddTemplates() {
    WikiBasePageObject base = new WikiBasePageObject();
    String articleName = PageContent.ARTICLE_NAME_PREFIX + base.getTimeStamp();
    VisualEditorPageObject ve = base.openVEOnArticle(wikiURL, articleName);

    ve.verifyVEToolBarPresent();
    ve.verifyEditorSurfacePresent();
    int numBlockTransclusion = ve.getNumberOfBlockTransclusion();
    int numInlineTransclusion = ve.getNumberOfInlineTransclusion();
    VisualEditorInsertTemplateDialog templateDialog =
        (VisualEditorInsertTemplateDialog) ve.openDialogFromMenu(InsertDialog.TEMPLATE);

    VisualEditorEditTemplateDialog editTemplateDialog = templateDialog.selectSuggestedTemplate(0);
    ve = editTemplateDialog.clickDone();

    Assertion.assertEquals(ve.getNumberOfBlockTransclusion(), numBlockTransclusion);
    Assertion.assertEquals(ve.getNumberOfInlineTransclusion(), ++numInlineTransclusion);

    templateDialog =
        (VisualEditorInsertTemplateDialog) ve.openDialogFromMenu(InsertDialog.TEMPLATE);
    editTemplateDialog =
        templateDialog.selectResultTemplate(VEContent.TEMPLATE_SEARCH_PARTIALMATCH, 1);
    ve = editTemplateDialog.closeDialog();
    Assertion.assertEquals(ve.getNumberOfBlockTransclusion(), ++numBlockTransclusion);
    Assertion.assertEquals(ve.getNumberOfInlineTransclusion(), numInlineTransclusion);

    VisualEditorSaveChangesDialog saveDialog = ve.clickPublishButton();
    ArticlePageObject article = saveDialog.savePage();
    article.verifyVEPublishComplete();
  }

  @Execute(asUser = User.USER)
  @Test(groups = {"VETemplate", "VETemplateTests_004", "VEAddTemplate", "VETemplateTests_005",
                "VETemplateTests_006"})
  public void VETemplateTests_004_CheckBlockedTransclusion() {
    WikiBasePageObject base = new WikiBasePageObject();
    String articleName = PageContent.ARTICLE_NAME_PREFIX + base.getTimeStamp();
    VisualEditorPageObject ve = base.openVEOnArticle(wikiURL, articleName);
    ve.verifyVEToolBarPresent();
    ve.verifyEditorSurfacePresent();
    String selectText = PageContent.ARTICLE_TEXT.substring(12, 13);
    int numBlockTransclusion = ve.getNumberOfBlockTransclusion();
    int numInlineTransclusion = ve.getNumberOfInlineTransclusion();
    ve.typeTextArea(PageContent.ARTICLE_TEXT);
    ve.selectText(selectText);
    VisualEditorInsertTemplateDialog templateDialog =
        (VisualEditorInsertTemplateDialog) ve.openDialogFromMenu(InsertDialog.TEMPLATE);
    VisualEditorEditTemplateDialog editTemplateDialog =
        templateDialog.selectResultTemplate(VEContent.TEMPLATE_SEARCH_EXACTMATCH, 0);
    ve = editTemplateDialog.clickDone();

    Assertion.assertEquals(ve.getNumberOfBlockTransclusion(), numBlockTransclusion);
    Assertion.assertEquals(ve.getNumberOfInlineTransclusion(), ++numInlineTransclusion);
    templateDialog =
        (VisualEditorInsertTemplateDialog) ve.openDialogFromMenu(InsertDialog.TEMPLATE);
    editTemplateDialog =
        templateDialog.selectResultTemplate(VEContent.TEMPLATE_SEARCH_EXACTMATCH, 0);
    ve = editTemplateDialog.clickDone();

    Assertion.assertEquals(ve.getNumberOfBlockTransclusion(), numBlockTransclusion);
    Assertion.assertEquals(ve.getNumberOfInlineTransclusion(), ++numInlineTransclusion);
    VisualEditorSaveChangesDialog saveDialog = ve.clickPublishButton();
    ArticlePageObject article = saveDialog.savePage();
    article.verifyVEPublishComplete();
    article.logOut(wikiURL);
  }

  @Execute(asUser = User.USER)
  @Test(groups = {"VETemplate", "VETemplateTests_005", "VEDeleteTemplate"},
      dependsOnGroups = "VETemplateTests_004")
  public void VETemplateTests_005_DeleteTemplates() {
    WikiBasePageObject base = new WikiBasePageObject();
    String articleName = PageContent.ARTICLE_NAME_PREFIX + base.getTimeStamp();
    VisualEditorPageObject ve = base.openVEOnArticle(wikiURL, articleName);
    ve.verifyVEToolBarPresent();
    ve.verifyEditorSurfacePresent();
    int numBlockTransclusion = ve.getNumberOfBlockTransclusion();
    int numInlineTransclusion = ve.getNumberOfInlineTransclusion();
    ve.deleteTransclusion(1, Transclusion.INLINE);

    Assertion.assertEquals(ve.getNumberOfBlockTransclusion(), numBlockTransclusion);
    Assertion.assertEquals(ve.getNumberOfInlineTransclusion(), --numInlineTransclusion);
    VisualEditorSaveChangesDialog saveDialog = ve.clickPublishButton();
    ArticlePageObject article = saveDialog.savePage();
    article.verifyVEPublishComplete();
    article.logOut(wikiURL);
  }

  @Execute(asUser = User.USER)
  @Test(groups = {"VETemplate", "VETemplateTests_006", "VEAddTemplate"},
      dependsOnGroups = "VETemplateTests_004")
  public void VETemplateTests_006_EditTemplate() {
    WikiBasePageObject base = new WikiBasePageObject();
    List<String> templateWikiTexts = new ArrayList<>();
    templateWikiTexts.add(VEContent.TEMPLATE_WIKITEXT);
    String articleName = PageContent.ARTICLE_NAME_PREFIX + base.getTimeStamp();
    VisualEditorPageObject ve = base.openVEOnArticle(wikiURL, articleName);
    ve.verifyVEToolBarPresent();
    ve.verifyEditorSurfacePresent();
    ve.clickTransclusion(0, Transclusion.INLINE);
    VisualEditorEditTemplateDialog editTemplateDialog = ve.openEditTemplateDialog();
    editTemplateDialog
        .typeInParam(VEContent.TEMPLATE_PARAM_LABEL1, VEContent.TEMPLATE_PARAM_VALUE1);
    editTemplateDialog
        .typeInParam(VEContent.TEMPLATE_PARAM_LABEL2, VEContent.TEMPLATE_PARAM_VALUE2);
    ve = editTemplateDialog.clickDone();
    VisualEditorSaveChangesDialog saveDialog = ve.clickPublishButton();
    VisualEditorReviewChangesDialog reviewDialog = saveDialog.clickReviewYourChanges();
    reviewDialog.verifyAddedDiffs(templateWikiTexts);
    saveDialog = reviewDialog.clickReturnToSaveFormButton();
    ArticlePageObject article = saveDialog.savePage();
    article.verifyVEPublishComplete();
    article.logOut(wikiURL);
  }
}
