package com.wikia.webdriver.TestCases.VisualEditor;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.wikia.webdriver.Common.ContentPatterns.InteractiveMapsContent;
import com.wikia.webdriver.Common.ContentPatterns.PageContent;
import com.wikia.webdriver.Common.ContentPatterns.URLsContent;
import com.wikia.webdriver.Common.DataProvider.VisualEditorDataProvider.InsertDialog;
import com.wikia.webdriver.Common.Properties.Credentials;
import com.wikia.webdriver.Common.Templates.NewTestTemplateBeforeClass;
import com.wikia.webdriver.PageObjectsFactory.ComponentObject.InteractiveMaps.CreateAMapComponentObject;
import com.wikia.webdriver.PageObjectsFactory.ComponentObject.InteractiveMaps.CreatePinTypesComponentObject;
import com.wikia.webdriver.PageObjectsFactory.ComponentObject.InteractiveMaps.CreateRealMapComponentObject;
import com.wikia.webdriver.PageObjectsFactory.ComponentObject.VisualEditorDialogs.VisualEditorAddMapDialog;
import com.wikia.webdriver.PageObjectsFactory.ComponentObject.VisualEditorDialogs.VisualEditorSaveChangesDialog;
import com.wikia.webdriver.PageObjectsFactory.PageObject.WikiBasePageObject;
import com.wikia.webdriver.PageObjectsFactory.PageObject.Article.ArticlePageObject;
import com.wikia.webdriver.PageObjectsFactory.PageObject.Special.InteractiveMaps.InteractiveMapPageObject;
import com.wikia.webdriver.PageObjectsFactory.PageObject.VisualEditor.VisualEditorPageObject;

/**
 * @author Robert 'Rochan' Chan
 * @ownership Contribution
 *
 * VE-1337 - Adding existing map onto article
 * VE-1337 - Checking empty state dialog on wiki with no maps
 * VE-1351 - Adding map in a empty state
 *
 */

public class VEAddMapTests extends NewTestTemplateBeforeClass {

	Credentials credentials = config.getCredentials();
	WikiBasePageObject base;
	String articleName;

	@BeforeMethod(alwaysRun = true)
	public void setup_VEPreferred() {
		base = new WikiBasePageObject(driver);
		wikiURL = urlBuilder.getUrlForWiki(URLsContent.veEnabledTestMainPage);
		base.logInCookie(credentials.userNameVEPreferred, credentials.passwordVEPreferred, wikiURL);
	}

	@Test(
		groups = {"VEAddMap", "VEAddMapTests_001", "VEAddExistingMap"}
	)
	public void VEAddMapTests_001_AddExistingMap() {
		articleName = PageContent.articleNamePrefix + base.getTimeStamp();
		VisualEditorPageObject ve = base.launchVisualEditorWithMainEdit(articleName, wikiURL);
		VisualEditorAddMapDialog mapDialog =
			(VisualEditorAddMapDialog) ve.openDialogFromMenu(InsertDialog.MAP);
		VisualEditorPageObject veNew = mapDialog.addExistingMap(0);
		veNew.verifyMapPresent();
		VisualEditorSaveChangesDialog save = veNew.clickPublishButton();
		ArticlePageObject article = save.savePage();
		article.verifyVEPublishComplete();
		article.logOut(wikiURL);
	}

	@Test(
		groups = {"VEAddMap", "VEAddMapTests_002", "VEEmptyMap"}
	)
	public void VEAddMapTests_002_CheckEmptyMapWiki() {
		wikiURL = urlBuilder.getUrlForWiki(URLsContent.veDisabledTestMainPage);
		articleName = PageContent.articleNamePrefix + base.getTimeStamp();
		VisualEditorPageObject ve = base.launchVisualEditorWithMainEdit(articleName, wikiURL);
		VisualEditorAddMapDialog mapDialog =
			(VisualEditorAddMapDialog) ve.openDialogFromMenu(InsertDialog.MAP);
		mapDialog.checkIsEmptyState();
	}

	@Test(
		groups = {"VEAddMap", "VEAddMapTests_003", "VEEmptyMap"},
		dependsOnGroups = {"VEAddMapTests_002"}
	)
	public void VEAddMapTests_003_InsertMapFromZeroState() {
		wikiURL = urlBuilder.getUrlForWiki(URLsContent.veDisabledTestMainPage);
		int expectedMapNum = 1;

		VisualEditorPageObject ve = base.launchVisualEditorWithMainEdit(articleName, wikiURL);
		VisualEditorAddMapDialog mapDialog =
			(VisualEditorAddMapDialog) ve.openDialogFromMenu(InsertDialog.MAP);
		CreateAMapComponentObject map = mapDialog.clickCreateAMapButton();
		CreateRealMapComponentObject realMap = map.clickRealMap();
		realMap.verifyRealMapPreviewImage();
		realMap.typeMapName(InteractiveMapsContent.mapName);
		CreatePinTypesComponentObject pinDialog = realMap.clickNext();
		pinDialog.typePinTypeTitle(InteractiveMapsContent.pinTypeName, InteractiveMapsContent.pinTypeIndex);
		InteractiveMapPageObject createdMap = pinDialog.clickSave();
		createdMap.verifyMapOpened();
		createdMap.verifyControButtonsAreVisible();
		mapDialog = createdMap.switchBackToVETab();
		//Defect VE-1557 - the next line would fail
		mapDialog.verifyNumOfMaps(expectedMapNum);
	}
}
