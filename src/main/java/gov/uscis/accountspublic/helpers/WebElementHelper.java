package gov.uscis.accountspublic.helpers;

import org.openqa.selenium.support.ui.Select;
import net.serenitybdd.core.pages.WebElementFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebElementHelper {

	private static final Logger logger = LoggerFactory.getLogger(WebElementHelper.class);

	public static void EnterText(WebElementFacade element, boolean clearFirst, String value) {
		element.waitUntilVisible();
		if ( clearFirst ) {
			element.clear();
			element.sendKeys(value);
		} else element.sendKeys(value); }

	public static void ClickOnElement(WebElementFacade element) {
		element.waitUntilClickable();
		if (element.isVisible()) {
			logger.info( "{} is visible", element);
			element.click();
		} else {
			logger.info("{} is not visible", element);
		}
	}

	public static void SelectDropDownByIndex(WebElementFacade element, int indexNumber) {
		Select selectElement = new Select(element);
		selectElement.selectByIndex(indexNumber);
	}

	public static void selectDropDownByVisibleText(WebElementFacade element, String text) {
		Select selectElement = new Select(element);
		selectElement.selectByVisibleText(text);
	}
}
