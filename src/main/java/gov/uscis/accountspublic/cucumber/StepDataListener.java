package gov.uscis.accountspublic.cucumber;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.serenitybdd.core.Serenity;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestResult;
import net.thucydides.core.model.TestStep;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.uscis.accountspublic.helpers.ExcelUtils;

public class StepDataListener implements StepListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(StepDataListener.class.getName()) ;
	private static SortedSet<BDDTestResult> testResults = new TreeSet<>();
	private static Set<BDDStepExecutionResult> stepExecutionResults = new LinkedHashSet<>();
	private static Map<String, BDDResultsSummary> resultSummary = new HashMap<>();

	public void testSuiteStarted(Class<?> storyClass) {}

	public void testSuiteStarted(Story story) {}

	public void testSuiteFinished() {
		ExcelUtils.createResultsFile();
		if (!testResults.isEmpty()) {
			LOGGER.info("------ Creating test results file");
        	for(BDDTestResult result: testResults) {
        		String resultSummaryId = result.getFeatureDescription();
        		if (ExecutionContext.getJsonData().get("Opportunity Type") != null) {
        			resultSummaryId = ExecutionContext.getJsonData().get("Opportunity Type").getAsString() + " Opportunity Type - " + result.getFeatureDescription() ;
        		}
        		
        		BDDResultsSummary summaryResult = resultSummary.get(resultSummaryId);
        		if (summaryResult == null) {
        			summaryResult = new BDDResultsSummary(resultSummaryId);
        			resultSummary.put(resultSummaryId, summaryResult);
        		}
        		
        		summaryResult.addTotalScenarioCount();
        		if (TestResult.SUCCESS.toString().equals(result.getTestResult())) {
        			summaryResult.addSuccessScenarioCount();
        		} else if (TestResult.ERROR.toString().equals(result.getTestResult())) {
        			summaryResult.addErrorScenarioCount();
        		} else {
        			summaryResult.addFailedScenarioCount();
        		}
        	}
        	ExcelUtils.addScenarioResults(testResults);
		}
		
		if (!stepExecutionResults.isEmpty()) {
			LOGGER.info("------ Creating Step Execution Results file");
			ExcelUtils.addStepExecutionResults(stepExecutionResults);
		}
		
		if (!resultSummary.isEmpty()) {
			LOGGER.info("------ Creating Result Summary file");
			ExcelUtils.addSummaryResults(resultSummary);
		}
	}

	public void testStarted(String description) {
	}

	public void testStarted(String description, String id) {
		String updatedId = id;
		String updatedDescription = description;
		if (ExecutionContext.getJsonData().get("Opportunity Type") != null) {
			updatedId = ExecutionContext.getJsonData().get("Opportunity Type").getAsString() + " Opportunity Type - " + id;
			updatedDescription = ExecutionContext.getJsonData().get("Opportunity Type").getAsString() + " Opportunity Type - " + description;
		}
		LOGGER.info("--- Setting updated test id and description");
		StepEventBus.getEventBus().getBaseStepListener().cancelPreviousTest();
		StepEventBus.getEventBus().getBaseStepListener().testStarted(updatedDescription, updatedId);
	}

	public void testFinished(TestOutcome result) {
		LOGGER.info("--- Setting Test finished Results {}", result.getTitle());
		BDDTestResult testResult = new BDDTestResult();
		testResult.setId(result.getId());
		LOGGER.info("--- Test Case Name {}", result.getUserStory().getName());
		testResult.setScenarioName(result.getTitle());
		testResult.setFeatureFile(result.getPath());
		testResult.setFeatureDescription(result.getStoryTitle());
		testResult.setStepCount(result.getStepCount());
		testResult.setTimeTaken(String.valueOf(result.getDurationInSeconds()));
		testResult.setHtmlReport(result.getHtmlReport());
		testResult.setTestResult(result.getResult().toString());
		if (result.getSuccessCount() != result.getTestStepCount()) {
			testResult.setFailedStep(result.getFailingStep().get().getDescription());
			testResult.setErrorMessage(result.getErrorMessage());
		}
		for(TestStep resultStep: result.getTestSteps()) {
			BDDStepExecutionResult step = new BDDStepExecutionResult(result.getId(), result.getTitle(), result.getPath(), result.getStoryTitle(), resultStep.getDescription(),
					resultStep.getResult() != null? resultStep.getResult().toString() : "", 
					String.valueOf(resultStep.getDurationInSeconds()), 
					resultStep.getErrorMessage(), 
					resultStep.getFirstScreenshot() != null? resultStep.getFirstScreenshot().getScreenshotName() : "");
			stepExecutionResults.add(step);
		}
		testResults.add(testResult);
	}

	public void testRetried() {}

	public void stepStarted ( ExecutedStepDescription description ) { ExecutionContext.resetStepData(); }

	public void skippedStepStarted(ExecutedStepDescription description) {}

	public void stepFailed(StepFailure failure) {
		addAdditionalStepData();
	}

	public void lastStepFailed(StepFailure failure) {}

	public void stepIgnored() {}

	public void stepPending() {}

	public void stepPending(String message) {}

	public void stepFinished() {
		addAdditionalStepData();
	}
	
	private void addAdditionalStepData() {
		if ( ExecutionContext.getDataProvidedForStep().length() > 0 && ExecutionContext.getDataAssertedForStep().length() > 0 )
			Serenity.recordReportData().withTitle("Data Provided & Assertions")
				.andContents("Data Provided : \n\n" + ExecutionContext.getDataProvidedForStep() + "\n\n" +
							 "Data Assertions : \n\n" + ExecutionContext.getDataAssertedForStep());

		else if (ExecutionContext.getDataProvidedForStep().length() > 0)
	    	Serenity.recordReportData().withTitle("Data Provided")
				.andContents(ExecutionContext.getDataProvidedForStep());

    	else if (ExecutionContext.getDataAssertedForStep().length() > 0)
	    	Serenity.recordReportData().withTitle("Data Asssertions")
				.andContents(ExecutionContext.getDataAssertedForStep());
	}

	public void testFailed ( TestOutcome testOutcome, Throwable cause ) {}

	public void testIgnored() {}

	public void testSkipped() {}

	public void testPending() {}

	public void testIsManual() {}

	public void notifyScreenChange() {}

	public void useExamplesFrom(DataTable table) {}

	public void addNewExamplesFrom(DataTable table) {}

	public void exampleStarted(Map<String, String> data) {}

	public void exampleFinished() {}

	public void assumptionViolated(String message) {}

	public void testRunFinished() {}
}
