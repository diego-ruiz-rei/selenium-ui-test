package gov.uscis.accountspublic.runners;

import cucumber.api.CucumberOptions;
import gov.uscis.accountspublic.cucumber.JsonDataSerenityRunner;
import gov.uscis.accountspublic.cucumber.TestDataFile;

import org.junit.runner.RunWith;

@CucumberOptions(plugin = { "pretty" , "html:target/Runner.html" },
        tags = {"@accountspublic", "~@wip"},
        features = {
			"src/test/resources/features/",
				},
        glue={"gov.uscis.accountspublic"}
)
@TestDataFile(files = {
		"src/test/resources/test-data/photoSubmission.json",
		})
@RunWith(JsonDataSerenityRunner.class)
public class Runner {}
