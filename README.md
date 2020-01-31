# CIS-ACCOUNTS-PUBLIC-TEST
CIS Accounts public BDD Testing with Cucumber

## Prerequisites:
- Java SDK 1.8
- Maven
- driver for [chrome](https://sites.google.com/a/chromium.org/chromedriver/)
- [Cucumber plugin for Eclipse](http://cucumber.github.com/cucumber-eclipse/update-site) or [Cucumber plugin for IntelliJ](https://plugins.jetbrains.com/plugin/download?updateId=39976)

### Writing Cucumber tests
Follow the below steps to implement cucumber test
1. Create feature file with scenario(s) under src/test/resources/features folder in Given, When, And, Then format. Refer to [Cucumber documentation](https://cucumber.io/docs/reference) on how to write scenarios
2. Create a step implementation class. Extend net.thucydides.core.steps.ScenarioSteps. The Steps in the scenario from the feature file are linked to the method using annotations @Given, @When, @Then and @And
3. Implement a Page object class. Extend gov.cis.cucumber.PageObject to get access to WebDriver and ExecutionContext that provides the data from the json files. Implement the getPageName and checkForPageLoadComplete methods in the PageObject. Calling the checkForPageLoadComplete in a StepDefinition or another method within the Page Object class will run the 508 check on the page.
4. Create json file for data under src/test/resources/data folder
5. Create Runner file and provide the json file as @TestDataFile to use in the runner
 				
### Running Tests in IDE
1. Update webdriver.base.url in serenity.properties to set the url to use for running tests
2. Update the tags to select only certain scenarios to run if needed
3. Run the appropriate runner class as Junit test

### Running Tests with Maven
1. Run `mvn verify serenity:aggregate -Dwebdriver.base.url={environment_url} -Dwebdriver.driver=chrome`

### Accessing Serenity reports
1. Run serenity:aggregate target
2. Navigate to `target/site/serenity/index.html`
3. Open `index.html` in your desired browser
4. Navigate to `target/AccessiblityReport` to view any 508 issues for each page

