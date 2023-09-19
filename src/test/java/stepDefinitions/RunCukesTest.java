package stepDefinitions;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import jirautil.JiraUtil;
import mantisutil.ConnectMantis;
import utilities.ConfigReaderWriter;
import utilities.ExcelDataUtil;
import utilities.ExtentUtil;
import utilities.GlobalUtil;
import utilities.KeywordUtil;
import utilities.LogUtil;

@CucumberOptions(features = "src/test/resources/features", glue = { "stepDefinitions","Hooks" }, plugin = { "pretty", "html:target/cucumber-html-report.html",
		"json:target/cucumber.json" }, tags = "@GreenKart", monochrome = true

)
public class RunCukesTest extends AbstractTestNGCucumberTests {

	public static String tagName = null;

	@BeforeSuite
	public void directoryCleanUp() {
		try {

			String filePath = System.getProperty("user.dir") + File.separator + ConfigReaderWriter.getValue("screenshotPath");
			if (!new File(filePath).exists())
				FileUtils.forceMkdir(new File(filePath));

			filePath = System.getProperty("user.dir") + File.separator + "Jmeter" + File.separator + "Results";
			if (new File(filePath).exists())
				FileUtils.cleanDirectory(new File(filePath));

			filePath = System.getProperty("user.dir") + File.separator + "ExecutionReports" + File.separator + "HTMLReports";
			if (!new File(filePath).exists())
				FileUtils.forceMkdir(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeTest
	public void onStart(ITestContext context) {
		try {
			ExtentUtil.extentInit(System.getProperty("user.dir") + ConfigReaderWriter.getValue("extentReportPath"));

			// Get all the common setting from excel file that are required for
			GlobalUtil.setCommonSettings(ExcelDataUtil.getCommonSettings());

			String browser = "";
			browser = GlobalUtil.getCommonSettings().getBrowser();
			LogUtil.infoLog(RunCukesTest.class, browser);

			String executionEnv = "";
			executionEnv = GlobalUtil.getCommonSettings().getExecutionEnv();

			String url = "";
			url = GlobalUtil.getCommonSettings().getUrl();

			if (browser == null)
				browser = ConfigReaderWriter.getValue("defaultBrowser");

			if (executionEnv == null)
				executionEnv = ConfigReaderWriter.getValue("defaultExecutionEnvironment");

			if (GlobalUtil.getCommonSettings().getManageToolName().equalsIgnoreCase("Jira")) {
				// Jira Test management config
				JiraUtil.JIRA_CYCLE_ID = GlobalUtil.getCommonSettings().getJiraCycleID();
				JiraUtil.JIRA_PROJECT_ID = GlobalUtil.getCommonSettings().getJiraProjectID();
				JiraUtil.ZEPHYR_URL = ConfigReaderWriter.getValue("zephyr_url");
				JiraUtil.ZAPI_ACCESS_KEY = ConfigReaderWriter.getValue("zapi_access_key");
				JiraUtil.ZAPI_SECRET_KEY = ConfigReaderWriter.getValue("zapi_secret_key");

				// remaing details will instailized when Jira is selected a bug
				// tracking tool
			} else
				GlobalUtil.getCommonSettings().setTestlinkTool("NO");

			// setting up of Bug tracking "MANTIS" tool configuration
			if (GlobalUtil.getCommonSettings().getBugToolName().equalsIgnoreCase("Mantis")) {
				ConnectMantis.MANTIS_URL = "http://" + GlobalUtil.getCommonSettings().getbugToolHostName() + "/bugTool/api/soap/bugToolconnect.php";
				ConnectMantis.MANTIS_USER = GlobalUtil.getCommonSettings().getbugToolUserName();
				ConnectMantis.MANTIS_PWD = GlobalUtil.getCommonSettings().getbugToolPassword();
				ConnectMantis.MANTIS_PROJET = GlobalUtil.getCommonSettings().getbugToolProjectName();
			}

			// setting up of Bug tracking "Jira" tool configuration
			if (GlobalUtil.getCommonSettings().getBugToolName().equalsIgnoreCase("Jira")) {
				JiraUtil.JIRA_URL = GlobalUtil.getCommonSettings().getbugToolHostName();
				JiraUtil.USERNAME = GlobalUtil.getCommonSettings().getbugToolUserName();
				JiraUtil.PASSWORD = GlobalUtil.getCommonSettings().getbugToolPassword();
				JiraUtil.JIRA_PROJECT = GlobalUtil.getCommonSettings().getbugToolProjectName();
				GlobalUtil.jiraapi = new JiraUtil();
			} else
				GlobalUtil.getCommonSettings().setbugTool("NO");

			if (url == null) {
				url = ConfigReaderWriter.getValue("BASE_URL");
				GlobalUtil.getCommonSettings().setUrl(url);
			}

			LogUtil.infoLog(getClass(), "\n\n+===========================================================================================================+");
			LogUtil.infoLog(getClass(), " Suite started" + " at " + new Date());
			LogUtil.infoLog(getClass(), "Suite Execution For Web application on environment : " + executionEnv);
			LogUtil.infoLog(getClass(), "\n\n+===========================================================================================================+");

		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.errorLog(getClass(), "Common Settings not properly set may not run the scripts properly");
		}
	}

	@AfterTest
	public void onFinish() {
		LogUtil.infoLog(getClass(), " suite finished" + " at " + new Date());
		LogUtil.infoLog(getClass(), "\n\n+===========================================================================================================+");
		ExtentUtil.extent.flush();
		KeywordUtil.onExecutionFinish();
	}

}