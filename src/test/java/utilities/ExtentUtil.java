package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.apache.commons.compress.utils.IOUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import bufferUtils.BufferUtilSuiteLevel;
import io.cucumber.java.Scenario;
import stepDefinitions.Hooks;

/**
 * The Class ExtentUtil.
 */
public class ExtentUtil {

	public static ExtentReports extent;
	public static ExtentSparkReporter spark;
	public static ExtentTest loggerTest;
	public static ExtentTest loggerTestStep;
	public static ThreadLocal<ExtentTest> logger = new ThreadLocal<ExtentTest>();

	/**
	 * Extent initialization.
	 *
	 * @param filePath
	 *        the file path
	 * 
	 * @throws IOException
	 */
	public static void extentInit(String filePath) throws IOException {
		extent = new ExtentReports();
		spark = new ExtentSparkReporter(filePath);
		spark.config().setTheme(Theme.DARK);
		spark.config().setTimelineEnabled(true);
		spark.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");
		spark.config().thumbnailForBase64(true);
		extent.attachReporter(spark);
		extent.setSystemInfo("Executed by", System.getProperty("user.name"));
		extent.setSystemInfo("Operating System and Version", System.getProperty("os.name"));
		extent.setSystemInfo("JAVA Version", "JDK " + Runtime.version().toString());
	}

	/**
	 * Start test initialization.
	 *
	 * @param testCaseName
	 *        the test case name
	 */
	public static void startTestInit(String testCaseName) {
		loggerTest = extent.createTest(testCaseName);
		logger.set(loggerTest);
	}

	/**
	 * Attach screenshot to report on failure.
	 * 
	 */
	
	public static void attachScreenShot(String filepath,String mesaage)
	{
		try
		{
			
			String path=KeywordUtil.testPassTakeScreenshot(filepath);
			logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(mesaage), MediaEntityBuilder.createScreenCaptureFromPath(path, mesaage).build());
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void attachScreenshotToReportOnFailure(Scenario scenario) {
		try {
			String scFileName = "ScreenShot_" + Hooks.executingTagName.replace("@", "") + "_" + KeywordUtil.currentDateTime() + ".png";
			BufferUtilSuiteLevel.screenshotFilePath = CommonConstants.generalFolderPath + CommonConstants.screenShotFolderName + File.separator + scFileName;
			String imagePath = KeywordUtil.testFailTakeScreenshot(BufferUtilSuiteLevel.screenshotFilePath);
			InputStream is = new FileInputStream(imagePath);
			byte[] imageBytes = IOUtils.toByteArray(is);
			Thread.sleep(2000);
			String base64 = Base64.getEncoder().encodeToString(imageBytes);

			logger.get().fail(MarkupHelper.createLabel("Failed at Point: ", ExtentColor.RED));

			if (GlobalUtil.errorMsg != null) {
				logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(GlobalUtil.errorMsg), MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
			} else {
				logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(GlobalUtil.e.toString()), MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
			}
			scenario.attach(imageBytes, "image/png", "Failed Screenshot");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void attachScreenshotToReportOnPass(Scenario scenario) {
		try {
			String scFileName = "ScreenShot_" + Hooks.executingTagName.replace("@", "") + "_" + KeywordUtil.currentDateTime() + ".png";
			BufferUtilSuiteLevel.screenshotFilePath = CommonConstants.generalFolderPath + CommonConstants.screenShotFolderName + File.separator + scFileName;
			String imagePath = KeywordUtil.testFailTakeScreenshot(BufferUtilSuiteLevel.screenshotFilePath);
			InputStream is = new FileInputStream(imagePath);
			byte[] imageBytes = IOUtils.toByteArray(is);
			Thread.sleep(2000);
			String base64 = Base64.getEncoder().encodeToString(imageBytes);

			logger.get().pass(MarkupHelper.createLabel("pass at Point: ", ExtentColor.GREEN));

			if (GlobalUtil.errorMsg != null) {
				logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(GlobalUtil.e.toString()), MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
			} else {
				logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(GlobalUtil.e.toString()), MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
			}
			scenario.attach(imageBytes, "image/png", "pass Screenshot");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		/**
	 * Open report on suite complete.
	 */
	public static void openReportOnSuiteComplete(String generalPath, String backUpFolderName, String backUpReportName) {
		BufferUtilSuiteLevel.extentHtmlreportWithNameAndPath = generalPath + backUpFolderName + File.separator + backUpReportName;
		File extentReport = new File(BufferUtilSuiteLevel.extentHtmlreportWithNameAndPath);
		if (extentReport.exists()) {
			try {
				Runtime rt = Runtime.getRuntime();
				rt.exec("rundll32 url.dll,FileProtocolHandler " + BufferUtilSuiteLevel.extentHtmlreportWithNameAndPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
