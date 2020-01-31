package gov.uscis.accountspublic.steps;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import gov.uscis.accountspublic.pages.UserPhotoSubmissionPage;
import net.thucydides.core.steps.ScenarioSteps;


public class UserPhotoSubmissionSteps extends ScenarioSteps {

	private UserPhotoSubmissionPage photoSubmissionPage;

	@Given("the user accesses the Photo Submission page$")
	public void accessPhotoSubmissionPage() throws Exception {
		photoSubmissionPage.open();
		photoSubmissionPage.checkForPageLoadComplete();
	}
	
	@Then("^the system displays options add ANumber and Upload Photo$")
	public void systemDisplaysOptionsToAddANumberAndUploadPhoto() {
		Assert.assertTrue(photoSubmissionPage.isANumberFieldPresent());
		Assert.assertTrue(photoSubmissionPage.isFileUploadPresent());
	}
	
	@Given("the user provides \"([^\"]*)\" data for photo submission$")
	public void provideDetailsToPhotoSubmission(String photoSubmissionData) {
		photoSubmissionPage.provideANumber(photoSubmissionData);
		photoSubmissionPage.uploadFile(photoSubmissionData);
		photoSubmissionPage.clickSubmitButton();
	}
	
	@Then("the system displays error message for \"([^\"]*)\"")
	public void systemDisplaysErrorDetails(String errorJson) {
		
	}
	
}
