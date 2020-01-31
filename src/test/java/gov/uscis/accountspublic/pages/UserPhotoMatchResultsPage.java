package gov.uscis.accountspublic.pages;

import gov.uscis.accountspublic.cucumber.BasePageObject;
import org.openqa.selenium.By;

public class UserPhotoMatchResultsPage extends BasePageObject {
	
	private static final By UPLOAD_ANOTHER_PHOTO = By.xpath("//button[contains(text(), 'Upload Another Photo')]");
	private static final By VALIDATION_ERROR = By.xpath("//h1[contains(text(), 'Validation: Failure')]");
	
	@Override
	public void checkForPageLoadComplete() {
		checkForPresenceOfElement(UPLOAD_ANOTHER_PHOTO);
		testPageFor508();
	}
	
	public boolean isValidationFailureDisplayed() {
		return checkForPresenceOfElement(VALIDATION_ERROR);
	}

	@Override
	public String getPageName() {
		return "User Photo Search";
	}
	
}
