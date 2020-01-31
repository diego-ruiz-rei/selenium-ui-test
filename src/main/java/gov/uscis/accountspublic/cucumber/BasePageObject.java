package gov.uscis.accountspublic.cucumber;

import java.io.File;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.deque.axe.AXE;
import com.google.gson.JsonObject;

import gov.uscis.accountspublic.helpers.RandomTestDataProvider;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.WhenPageOpens;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePageObject extends PageObject {
	
	private static final int TIMEOUT = 30;
	private static final Logger LOGGER = LoggerFactory.getLogger(BasePageObject.class) ;

	public abstract String getPageName ();

	@WhenPageOpens
	public abstract void checkForPageLoadComplete();

	public void clickElement ( By by ) {
		WebElement element = findElement(by);
		try{
			element.click();
		} catch ( WebDriverException e ) {
			JavascriptExecutor jse = (JavascriptExecutor) getDriver();
			jse.executeScript("arguments[0].click()",element);
		}
	}

	public void clickElement ( WebElement element ){
		try{
			element.click();
		} catch ( WebDriverException e ) {
			JavascriptExecutor jse = (JavascriptExecutor) getDriver();
			jse.executeScript("arguments[0].click()",element);
		}
	}

	public void clickButtonWithText ( String buttonText ) {
		clickElement(By.xpath("//button[contains(.,'" + buttonText + "')]"));
	}

	public void clickLinkWithText ( String linkText ) {
		clickElement(By.partialLinkText(linkText));
	}

	public JsonObject getJsonData () {
		return ExecutionContext.getJsonData();
	}

	public void testPageFor508() {
		String pageName = getPageName();
		JSONObject responseJSON = new AXE.Builder(getDriver(), getClass().getResource("/axe.min.js")).options("{runOnly: {type: \"tag\",values: [\"section508\"]}}").analyze();
        JSONArray violations = responseJSON.getJSONArray("violations");
        String violationsStr;
        if (violations.length() != 0) {
        	LOGGER.info("Count of violations found for page {} is {}", pageName, violations.length());
        	violationsStr = AXE.report(violations);
        } else {
        	LOGGER.info("No violations found for page {}", pageName);
        	violationsStr =  "No violations found for page " + pageName;
        }
        
        File file = new File("target/AccessibilityReport/508-violations_for_" + pageName + ".txt");
        try {
			FileUtils.writeStringToFile(file, violationsStr, "UTF-16");
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}
	
	public List<WebElementFacade> findAllElements(By by) {
		withTimeoutOf(TIMEOUT,  ChronoUnit.SECONDS).waitForPresenceOf(by);
		return findAll(by);
	}
	
	public WebElement findElement(By by) {
		withTimeoutOf(TIMEOUT, ChronoUnit.SECONDS).waitForPresenceOf(by);
		return find(by);
	}
	
	public WebElement findElement(By by, int timeoutSeconds) {
		withTimeoutOf(timeoutSeconds, ChronoUnit.SECONDS).waitForPresenceOf(by);
		return find(by);
	}

    public WebElement elementToBeClickable(By by) {
        withTimeoutOf(TIMEOUT, ChronoUnit.SECONDS).waitFor(ExpectedConditions.elementToBeClickable(by));
        return find(by);
    }

	@Override
	public FieldEntry enter(CharSequence... keysToSend) {
		return super.enter(keysToSend);
	}

	public void enterValue(By by, String value) {
		element(by).clear();
		int numberOfClear = 0;
		String textBoxValue = element(by).getAttribute("value");
		while ( !textBoxValue.isEmpty() && numberOfClear < 5 ) {
			element(by).clear();
			textBoxValue = element(by).getAttribute("value");
			numberOfClear++;
		}
		if (value != null && !value.isEmpty()) {
			element(by).sendKeys(value);
			ExecutionContext.appendToStepData("TextField", by.toString(), value);
		} else element(by).sendKeys("\t");
	}

	public void enterValue ( By by, int index, String value ) {
		WebElement webElement = findAll(by).get(index - 1);
		webElement.clear();
		if ( value != null && !value.isEmpty() ) {
			webElement.sendKeys(value);
			ExecutionContext.appendToStepData("TextField", by.toString(), value);
		} else webElement.sendKeys("\t");
	}
	
	public String getValueOfElement(By by) {
		return findElement(by).getText();
	}

	public void clearValueOfElement(By by) {
		findElement(by).clear();
	}
	
	public void selectDropdownValue(By by, String value) {
		if ( value != null && !value.isEmpty() ) {
			selectFromDropdown(findElement(by), value);
			ExecutionContext.appendToStepData("Dropdown", by.toString(), value);
		} 
	}
	
	public String getSelectedValueOfDropdown(By by) {
		return getSelectedValueFrom(findElement(by));
	}
	
	public boolean checkForPresenceOfElement(By by) {
		return findElement(by) != null;
	}
	
	public boolean checkForPresenceOfElement(By by,  int timeoutSeconds) {
		return findElement(by, timeoutSeconds) != null;
	}
	
	public boolean checkForPresenceOfElementWithoutWaiting(By by) {
		boolean isisDisplayed = false;
		WebElementFacade element = find(by);
		try {
			isisDisplayed = element.isDisplayed();
		} catch (NoSuchElementException ne) {
			LOGGER.warn(ne.getMessage());
		}
		return isisDisplayed;
	}

	//Methods related to getting rows/column data from Table
	public WebElement findTableByCaption(String caption) {
	    return element(By.xpath("//table[caption='" + caption  + "']"));
	}
	
	public WebElement findTableById(String id) {
	    List<WebElement> tables = find(By.tagName("table"));
	    for (WebElement table : tables) {
	    	if (id.equalsIgnoreCase(table.getAttribute("id"))) {
	    		return table;
	    	}
	    }
	    return null;
	}
	
	public int getTableRowCount(WebElement table) {
		List<WebElement> allRows = table.findElements(By.tagName("tr"));
	    return allRows.size() - 1;
	}
	
	public WebElement getNthTableRow(WebElement table, int n) {
	    List<WebElement> allRows = table.findElements(By.tagName("tr"));
	    return allRows.get(n);
	}
	
	public WebElement getRowWithCellValue(WebElement table, int colNo, String cellValue) {
		WebElement rowElement = null;
		List<WebElement> allRows = table.findElements(By.tagName("tr"));
		for ( WebElement row: allRows ) {
			WebElement cell = getCellAtIndex(row, colNo);
			if ( cell != null && cell.getText().equals(cellValue) ) rowElement = row;
		}
		return rowElement;
	}
	
	public WebElement getCellAtIndex(WebElement row, int colNo) {
		WebElement cellElement = null;
		List<WebElement> cells =  row.findElements(By.tagName("td"));
		if (cells != null && cells.size() >=colNo) cellElement = cells.get(colNo - 1);
		return cellElement;
	}

	public void scrollToBottom() { ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0, document.body.scrollHeight)"); }
		
	public void scrollToTop() { ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0, -document.body.scrollHeight)"); }
	
	public void scrollUntilElementVisible(WebElementFacade element) { ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element); }
		
	public List<String> getValidationErrorMessages(By by) {
		List<String> errorMessages = new ArrayList<>();
		List<WebElementFacade> errorMessageElements = findAllElements(by);
		for (WebElement element: errorMessageElements) errorMessages.add(element.getText());
		return errorMessages;
	}

	public void navigateToURL(String url){ getDriver().get(url); }

	public static String getSystemBaseURL() {
		EnvironmentVariables systemVariables = SystemEnvironmentVariables.createEnvironmentVariables();
		return systemVariables.getProperty(ThucydidesSystemProperty.WEBDRIVER_BASE_URL);
	}

	protected String selectDropdown(By byDropdown, String value) {
		Select select = new Select(find(byDropdown));
		select.selectByVisibleText(value);
		return select.getFirstSelectedOption().getText();
	}

	protected String randomSelectDropdown(By byDropdown) {
		Select select = new Select(find(byDropdown));
		int maxSize = select.getOptions().size();
		select.selectByIndex(RandomTestDataProvider.getRandomInt(1, maxSize - 1));
		return select.getFirstSelectedOption().getText();
	}

	protected void clickRadioButton ( String radioButtonValue ) {
		clickElement(By.xpath("//div[@class='usa-radio'][contains(., '" + radioButtonValue + "')]"));
	}
}
