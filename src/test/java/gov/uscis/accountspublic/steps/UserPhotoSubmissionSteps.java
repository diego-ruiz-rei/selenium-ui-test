package gov.uscis.accountspublic.steps;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import gov.uscis.accountspublic.pages.UserPhotoSubmissionPage;
import net.thucydides.core.steps.ScenarioSteps;


public class UserPhotoSubmissionSteps extends ScenarioSteps {

	private UserPhotoSubmissionPage photoSubmissionPage;

	@Given("the user is on the home page$")
	public void accessPhotoSubmissionPage() throws Exception {
		photoSubmissionPage.open();
		photoSubmissionPage.checkForPageLoadComplete();
	}
	
	@Then("the system displays content$")
	public void systemDisplaysOptionsToAddANumberAndUploadPhoto() {
		Assert.assertTrue(photoSubmissionPage.isANumberFieldPresent());
	}
	
}
