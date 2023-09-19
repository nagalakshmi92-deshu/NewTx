package stepDefinitions;

import org.openqa.selenium.JavascriptExecutor;

import bufferUtils.BufferUtilMethodLevel;
import bufferUtils.BufferUtilSuiteLevel;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import mantisutil.MantisReport;
import utilities.DriverUtil;
import utilities.ExtentUtil;
import utilities.GlobalUtil;
import utilities.LogUtil;

public class Hooks {

	String imagePath;
	String pathForLogger;
	public static String testCaseDescription;
	public static String executingTagName;

	public static String imagePath1;
	public static String concatt = ".";
	public static Scenario scenario1;

	@Before
	public void beforeMethodAmazon(Scenario scenario) {
		System.out.println("scenario----------"+scenario.getName());
		this.scenario1=scenario;
		System.out.println("scenario----------"+scenario1.getName());
		if (scenario.getName().contains("_"))
			testCaseDescription = scenario.getName().split("_")[1];
		else
			testCaseDescription = scenario.getName();

		BufferUtilMethodLevel.scenarioName = testCaseDescription;
		ExtentUtil.startTestInit(BufferUtilMethodLevel.scenarioName);
		RunCukesTest.tagName = scenario.getSourceTagNames().toString().replace("[@", "").replace("]", "").trim();

		LogUtil.infoLog(getClass(), "\n+----------------------------------------------------------------------------------------------------------------------------+");
		LogUtil.infoLog(getClass(), "Test Started: " + scenario.getName());
		LogUtil.infoLog(getClass(), "Test Started with tag : " + scenario.getSourceTagNames());
		executingTagName = scenario.getSourceTagNames().toArray()[0].toString();
		LogUtil.infoLog(getClass(), "Test Started with tag : " + executingTagName);
		LogUtil.infoLog(Hooks.class, "Test is executed in Environment: " + GlobalUtil.getCommonSettings().getExecutionEnv());
		LogUtil.infoLog(Hooks.class, "Test is started with browser: " + GlobalUtil.getCommonSettings().getBrowser());

		GlobalUtil.setDriver(DriverUtil.getBrowser(GlobalUtil.getCommonSettings().getExecutionEnv(), GlobalUtil.getCommonSettings().getBrowser()));
	}

	@After
	public void afterMethodSmoke(Scenario scenario) {
		String testName = scenario.getName().split("_")[0].trim();
		JavascriptExecutor jse = (JavascriptExecutor) GlobalUtil.getDriver();
		if (scenario.isFailed()) {
			try {
				if (GlobalUtil.getCommonSettings().getExecutionEnv().equalsIgnoreCase("Remote")
						&& GlobalUtil.getCommonSettings().getCloudProvider().equalsIgnoreCase("Browserstack")) {
					jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"failed\"}}");
				}
				ExtentUtil.attachScreenshotToReportOnFailure(scenario);

				// report the bug
				String bugID = "Please check the Bug tool Configuration";
				if (GlobalUtil.getCommonSettings().getBugToolName().equalsIgnoreCase("Mantis")) {
					bugID = MantisReport.reporIssue(scenario.getName(), GlobalUtil.errorMsg, "General",
							"Automated on Browser: " + GlobalUtil.getCommonSettings().getBrowser() + " and Build Name: " + GlobalUtil.getCommonSettings().getBuildNumber(),
							BufferUtilSuiteLevel.screenshotFilePath);
				}

				if (GlobalUtil.getCommonSettings().getBugToolName().equalsIgnoreCase("Jira")) {
					// getting the os name to report the bug
					String osName = System.getProperty("os.name");
					if (GlobalUtil.getCommonSettings().getExecutionEnv().equalsIgnoreCase("Remote")) {
						osName = GlobalUtil.getCommonSettings().getRemoteOS();
					}
					bugID = GlobalUtil.jiraapi
							.reporIssue(
									scenario.getName(), "Automated on OS: " + osName + ",\n Automated on Browser: " + GlobalUtil.getCommonSettings().getBrowser()
											+ ",\n Build Name: " + GlobalUtil.getCommonSettings().getBuildNumber() + ". \n\n\n\n" + GlobalUtil.errorMsg,
									BufferUtilSuiteLevel.screenshotFilePath);
				}

				// updating the results in Testmangement tool
				if (GlobalUtil.getCommonSettings().getManageToolName().equalsIgnoreCase("Jira")) {
					GlobalUtil.jiraapi.updateJiraTestResults(testName, "Please find the BUGID in " + GlobalUtil.getCommonSettings().getBugToolName() + " : " + bugID, "Fail");
				}

			} catch (Exception e) {
				LogUtil.errorLog(Hooks.class, e.getMessage());
			}
		} else {
			if (GlobalUtil.getCommonSettings().getExecutionEnv().equalsIgnoreCase("Remote") && GlobalUtil.getCommonSettings().getCloudProvider().equalsIgnoreCase("Browserstack")) {
				jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\"}}");
			}
			LogUtil.infoLog(Hooks.class, "Test has ended closing browser: " + GlobalUtil.getCommonSettings().getBrowser());
			// updating the results in Test management tool
			if (GlobalUtil.getCommonSettings().getManageToolName().equalsIgnoreCase("Jira")) {
				GlobalUtil.jiraapi.updateJiraTestResults(testName, "This test is passed", "Pass");
			}
		}
		ExtentUtil.attachScreenshotToReportOnPass(scenario);

		// close the browsers
		DriverUtil.closeAllDriver();
//		RunCukesTest.extent.endTest(RunCukesTest.logger);
	}

}