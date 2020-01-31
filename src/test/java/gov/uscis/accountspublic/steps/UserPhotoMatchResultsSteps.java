package gov.uscis.accountspublic.steps;

import org.junit.Assert;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import gov.uscis.accountspublic.pages.UserPhotoMatchResultsPage;
import net.thucydides.core.steps.ScenarioSteps;


public class UserPhotoMatchResultsSteps extends ScenarioSteps {

	private UserPhotoMatchResultsPage photoResultsPage;

	@Then("the system displays match results page with Match Success")
	public void displaysSuccessMessage()  {
		photoResultsPage.checkForPageLoadComplete();
		Assert.assertFalse(photoResultsPage.isValidationFailureDisplayed());
	}
	
	@Then("the system displays match results page with Match Failure")
	public void displaysValidationMessage()  {
		Assert.assertTrue(photoResultsPage.isValidationFailureDisplayed());
	}
	
	
}
