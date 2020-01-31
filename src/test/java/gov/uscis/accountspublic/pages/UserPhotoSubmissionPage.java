package gov.uscis.accountspublic.pages;

import org.openqa.selenium.By;

import com.google.gson.JsonObject;

import gov.uscis.accountspublic.cucumber.BasePageObject;
import gov.uscis.accountspublic.helpers.Utilities;

public class UserPhotoSubmissionPage extends BasePageObject {
	
	private static final By ANUMBER_FIELD = By.id("aNumber");
	private static final By FILE_UPLOAD = By.xpath("//button[contains(text(), 'Select File')]");
	private static final By SUBMIT_BUTTON = By.xpath("//button[contains(text(), 'Submit')]");
	
	@Override
	public void checkForPageLoadComplete() {
		checkForPresenceOfElement(ANUMBER_FIELD);
		testPageFor508();
	}
	
	public boolean isANumberFieldPresent() {
		return checkForPresenceOfElement(ANUMBER_FIELD);
	}
	
	public boolean isFileUploadPresent() {
		return checkForPresenceOfElement(FILE_UPLOAD);
	}
	
	public void provideANumber(String photoSubmissionData) {
		if (photoSubmissionData != null) {
			checkForPresenceOfElement(ANUMBER_FIELD);
			JsonObject photoSubmissionDataJson = Utilities.getJsonObjectFromJsonObject(getJsonData(),photoSubmissionData);
			enterValue(ANUMBER_FIELD, photoSubmissionDataJson.get("aNumber").getAsString());
		}
	}
	
	public void uploadFile(String photoSubmissionData) {
		if (photoSubmissionData != null) {
			JsonObject photoSubmissionDataJson = Utilities.getJsonObjectFromJsonObject(getJsonData(),photoSubmissionData);
			upload(photoSubmissionDataJson.get("Photo file").getAsString()).to(findElement(FILE_UPLOAD));
		}
	}
	
	public void clickSubmitButton() {
		findElement(SUBMIT_BUTTON).click();
	}
	
	@Override
	public String getPageName() {
		return "User Photo Search";
	}
	
}
