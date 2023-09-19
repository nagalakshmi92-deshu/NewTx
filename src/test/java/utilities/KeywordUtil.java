package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.aventstack.extentreports.Status;
import com.google.common.base.Function;

import stepDefinitions.Hooks;

/**
 * @author TX
 */
public class KeywordUtil extends GlobalUtil {

	public static String cucumberTagName;
	private static final Duration DEFAULT_WAIT_SECONDS = Duration.ofSeconds(10);
	protected static final int FAIL = 0;
	static WebElement webElement;
	protected static String url = "";
	private static String userDir = "user.dir";
	public static final String VALUE = "value";
	public static String lastAction = "";
	static String DummyString;
	public static String renamedExtentReportName;

	static String result_FolderName = System.getProperty("user.dir") + "\\ExecutionReports\\HTMLReports";
	static Runtime rt = Runtime.getRuntime();

	public static String currentDateTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String dt = dtf.format(now).replace("/", "-").replace(":", "-").replace(" ", "_");

		return dt;
	}

	/**
	 * Test fail take screenshot.
	 *
	 * @param imagePath the image path
	 * 
	 * @return the string
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * 
	 * 
	 */

	public static String testFailTakeScreenshot(String imagePath) throws IOException {

		File src = ((TakesScreenshot) GlobalUtil.getDriver()).getScreenshotAs(OutputType.FILE);
		File des = new File(imagePath);
		FileUtils.copyFile(src, des);

		DummyString = des.getAbsolutePath();
		String path = DummyString;
		String base = File.separator + CommonConstants.baseFolderName + File.separator
				+ CommonConstants.screenShotFolderName + File.separator;
		String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();

		return relative;
	}
	
	public static String testPassTakeScreenshot(String imagePath) throws IOException {

		File src = ((TakesScreenshot) GlobalUtil.getDriver()).getScreenshotAs(OutputType.FILE);
		File des = new File(imagePath);
		FileUtils.copyFile(src, des);

		DummyString = des.getAbsolutePath();
		String path = DummyString;
		String base = File.separator + CommonConstants.baseFolderName + File.separator
				+ CommonConstants.screenShotFolderName + File.separator;
		String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();

		return relative;
	}

	public static void onExecutionFinish() {

		LogUtil.infoLog(KeywordUtil.class, "Test process has ended");

		String htmlReportFile = System.getProperty("user.dir") + "\\"
				+ ConfigReaderWriter.getValue("HtmlReportFullPath");
		System.out.println("cucumber path is: " + htmlReportFile);
		File f = new File(htmlReportFile);
		if (f.exists()) {
			try {
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + htmlReportFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		renamedExtentReportName = System.getProperty("user.dir") + "\\"
				+ ConfigReaderWriter.getValue("extentReportPathToRename").replace("%s", Hooks.executingTagName)
						.replace("@", currentDateTime() + "_");
		new File(System.getProperty("user.dir") + "\\" + ConfigReaderWriter.getValue("extentReportPath"))
				.renameTo(new File(renamedExtentReportName));
		String renamedExtentReportPath = renamedExtentReportName;
		System.out.println("Extent Report File path is: " + renamedExtentReportPath);
		File extentReport = new File(renamedExtentReportPath);
		if (extentReport.exists()) {
			try {
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + renamedExtentReportPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static int randomeGenerator(int bound) {
		Random random = new Random();
		int randomNumber = random.nextInt(bound);
		return randomNumber;
	}

	public static void checkPageIsReady() {
		JavascriptExecutor js = (JavascriptExecutor) GlobalUtil.getDriver();
		long wt = 20000;
		for (int i = 0; i < 30; i++) {
			try {
				Thread.sleep(wt);
			} catch (InterruptedException e) {
				LogUtil.errorLog(KeywordUtil.class, e.getMessage());
				e.printStackTrace();
				Assert.fail(e.getMessage());
			}
			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				break;
			}
		}
	}

	public static void waitForAjax() {
		new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS).until(new ExpectedCondition<Boolean>() {

			public Boolean apply(WebDriver driver) {
				driver = getDriver();
				JavascriptExecutor js = (JavascriptExecutor) driver;
				return (Boolean) js.executeScript("return jQuery.active == 0");
			}
		});
	}

	public static byte[] takeScreenshot(String screenshotFilePath) {
		try {
			byte[] screenshot = ((TakesScreenshot) GlobalUtil.getDriver()).getScreenshotAs(OutputType.BYTES);
			FileOutputStream fileOuputStream = new FileOutputStream(screenshotFilePath);
			fileOuputStream.write(screenshot);
			fileOuputStream.close();
			LogUtil.infoLog(KeywordUtil.class, "Screenshot taken.");
			return screenshot;
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return null;
	}

	public static boolean scrollingToElementofAPage(By locator, String logStep) {
		boolean flag = false;
		try {
			WebElement element = waitForPresent(locator);
			((JavascriptExecutor) GlobalUtil.getDriver()).executeScript("arguments[0].scrollIntoView();", element);
			ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			LogUtil.infoLog(KeywordUtil.class, "Scrolling to element: " + locator.toString());
			flag = true;
		} catch (Throwable e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}

		return flag;
	}

	public static String getCurrentDateTime() {

		String strDate = null;
		try {
			SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss");
			Date now = new Date();
			strDate = sdfDate.format(now);
			LogUtil.infoLog(KeywordUtil.class, strDate);
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		return strDate;
	}

	public static String getCurrentDateTimeMinusOneMinutes() {

		String inputValue = null;
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/YYYY HH:mm:ss a");
			LocalDateTime now = LocalDateTime.now().minusMinutes(1);
			inputValue = dtf.format(now).toString();
			LogUtil.infoLog(KeywordUtil.class, "Date time minus one minute: " + inputValue);
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		return inputValue;
	}

	/**
	 * @param locator
	 * 
	 * @return
	 */
	public static void navigateToUrl(String url) {
		try {
			KeywordUtil.lastAction = "Navigate to: " + url;
			LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
			getDriver().get(url);

		} catch (Throwable e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}

	public static String getCurrentUrl() {
		String currentURL = null;
		try {
			currentURL = getDriver().getCurrentUrl();
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return currentURL;
	}

	public static WebElement waitForClickable(By locator) {
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
//			wait.ignoring(ElementNotVisibleException.class);
			wait.ignoring(WebDriverException.class);
			element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			LogUtil.infoLog(KeywordUtil.class, "Element is now clickable.");
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

		return element;
	}

	/**
	 * @param locator
	 * 
	 * @return
	 */
	public static WebElement waitForPresent(By locator) {
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
//			wait.ignoring(ElementNotVisibleException.class);
			element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			LogUtil.infoLog(KeywordUtil.class, "Element is now present.");
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return element;
	}

	/**
	 * @param locator
	 * 
	 * @return
	 */
	public static WebElement waitForVisible(By locator) {
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			LogUtil.infoLog(KeywordUtil.class, "Element is now visible.");
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return element;
	}

	public static boolean waitForInVisibile(By locator) {
		Boolean flag = false;
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
			if (wait.until(ExpectedConditions.invisibilityOfElementLocated(locator))) {
				LogUtil.infoLog(KeywordUtil.class, "Element is not visible.");
				flag = true;
			} else {
				LogUtil.errorLog(KeywordUtil.class, "Element is visible.");
				flag = false;
				Assert.fail(e.getMessage());
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	public static WebElement waitForVisibleIgnoreStaleElement(By locator) {
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
			wait.ignoring(StaleElementReferenceException.class);
//			wait.ignoring(ElementNotVisibleException.class);
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return element;
	}

	/**
	 * @param locator
	 * @param seconds
	 * @param poolingMil
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static WebElement findWithFluintWait(final By locator, int seconds, int poolingMil) throws Exception {
		// Because if implicit wait is set then fluent wait will not work

		getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		WebElement element = null;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver()).withTimeout(Duration.ofSeconds(seconds))
					.pollingEvery(Duration.ofMillis(poolingMil)).ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class).ignoring(WebDriverException.class);
			element = wait.until(new Function<WebDriver, WebElement>() {

				@Override
				public WebElement apply(WebDriver driver) {
					return driver.findElement(locator);
				}
			});
			LogUtil.infoLog(KeywordUtil.class, "Element found using fluent wait.");
		} catch (Exception t) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			throw new Exception("Timeout reached when searching for element! Time: " + seconds + " seconds " + "\n"
					+ t.getMessage());
		} finally {
		}

		return element;
	}// End FindWithWait()

	/**
	 * @param locator
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static WebElement findWithFluintWait(final By locator) throws Exception {
		getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		// Because if implicit wait is set then fluent wait will not work
		KeywordUtil.lastAction = "Find Element: " + locator.toString();
		WebElement element = null;

		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver()).withTimeout(Duration.ofSeconds(10))
					.pollingEvery(Duration.ofMillis(200)).ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class).ignoring(WebDriverException.class);

			element = wait.until(new Function<WebDriver, WebElement>() {

				@Override
				public WebElement apply(WebDriver driver) {
					return driver.findElement(locator);
				}
			});
			LogUtil.infoLog(KeywordUtil.class, "Element found using fluent wait.");
		} catch (Exception t) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			throw new Exception("Timeout reached when searching for element! Time: " + DEFAULT_WAIT_SECONDS
					+ " seconds " + "\n" + t.getMessage());
		} finally {
		}

		return element;
	}

	public static WebElement getWebElement(By locator) throws Exception {
		KeywordUtil.lastAction = "Find Element: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		return findWithFluintWait(locator);
	}
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/*
	 * Web driver common functions
	 * ===========================================================
	 */

	/**
	 * @param locator
	 * 
	 * @return
	 */
	//
	public void highLighterMethod(WebDriver driver, WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
	}

	public static boolean click(By locator, String logStep) {
		Boolean flag = false;
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
			wait.until(ExpectedConditions.elementToBeClickable(locator)).isDisplayed();

			KeywordUtil.lastAction = "Click: " + locator.toString();
			LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
			WebElement elm = waitForClickable(locator);
			if (elm == null) {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class, KeywordUtil.lastAction);
				flag = false;
				Assert.fail(e.getMessage());
			} else {
				JavascriptExecutor js = (JavascriptExecutor) getDriver();
				js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');",
						elm);
				elm.click();
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
				flag = true;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	// ............
	public static boolean clickMultipleTimes(By locator, int times, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Click: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
			wait.until(ExpectedConditions.elementToBeClickable(locator)).isDisplayed();

			KeywordUtil.lastAction = "Click: " + locator.toString();
			LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
			WebElement elm = waitForClickable(locator);
			if (elm == null) {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class, KeywordUtil.lastAction);
				flag = false;
				Assert.fail(e.getMessage());
			} else {
				for (int i = 0; i <= times; i++) {
					elm.click();
				}
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
				flag = true;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	public static void selectEligibilityRadioButton(String optionValue) {
		try {
			String elementXpath = "//*[text()='" + optionValue + "']//preceding-sibling::input";
			By radioButtonElement = By.xpath(elementXpath);
			click(radioButtonElement, "Clicking eligibility radio button for option: " + optionValue);
		} catch (Throwable e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	// ....
	public static boolean acceptAlert() {
		Boolean flag = false;
		try {
			Alert alert = GlobalUtil.getDriver().switchTo().alert();
			alert.accept();
			LogUtil.infoLog(KeywordUtil.class, "Alert accepted.");
			flag = true;
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;

	}

	public static boolean checkAlertPresence() {
		Boolean flag = false;
		try {
			WebDriverWait wait = new WebDriverWait(GlobalUtil.getDriver(), DEFAULT_WAIT_SECONDS);
			if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				LogUtil.infoLog(KeywordUtil.class, "Alert was present");
				flag = true;
			} else {
				LogUtil.errorLog(KeywordUtil.class, "Alert was not present");
				flag = false;
				Assert.fail(e.getMessage());
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;

	}

	// ......
	public static boolean switchToWindow() {
		Boolean flag = false;
		try {
			ArrayList<String> tabs2 = new ArrayList<String>(GlobalUtil.getDriver().getWindowHandles());
			GlobalUtil.getDriver().switchTo().window(tabs2.get(1));
			LogUtil.infoLog(KeywordUtil.class, "Alert accepted.");
			flag = true;
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;

	}
	// ....

	/**
	 * @param linkText
	 * 
	 * @return
	 */
	public static boolean clickLink(String linkText, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Click Link: " + linkText;
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		try {
			WebElement elm = waitForClickable(By.linkText(linkText));
			if (elm == null) {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
				Assert.fail(e.getMessage());
			} else {
				elm.click();
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				flag = true;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	/**
	 * @param locator
	 * 
	 * @return
	 */
	public static String getElementText(By locator) {
		String elementText = null;
		KeywordUtil.lastAction = "Get Element text: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		WebElement elm;
		try {
			elm = waitForClickable(locator);
			elementText = elm.getText().trim();
			LogUtil.infoLog(KeywordUtil.class, "Element text is: " + elementText);
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return elementText;
	}

	public static String getPolicyStatus(By locator) {
		String elementText = null;
		KeywordUtil.lastAction = "Get Element text: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		WebElement elm;
		try {
			elm = waitForClickable(locator);
			elementText = elm.getText().split("-")[0].split(":")[1].trim();
			LogUtil.infoLog(KeywordUtil.class, "Element text is: " + elementText);
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return elementText;
	}

	public static String getVehicleHeader(By locator) {
		String elementText = null;
		KeywordUtil.lastAction = "Get Element text: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		WebElement elm;
		try {
			elm = waitForClickable(locator);
			elementText = elm.getText().replaceAll("\\[.*?\\]", "").replaceAll("[\r\n]+", " ").replaceAll("( )+", " ")
					.trim();
			LogUtil.infoLog(KeywordUtil.class, "Element text is: " + elementText);
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return elementText;
	}

	public static String getElementTextByVisibility(By locator) {
		String elementText = null;
		KeywordUtil.lastAction = "Get Element text: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		WebElement elm;
		try {
			elm = waitForVisible(locator);
			elementText = elm.getText().trim();
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return elementText;
	}

	public static String getImageTitle(By locator) {
		String imageTitle = null;
		WebElement elm;
		try {
			elm = waitForVisible(locator);
			imageTitle = elm.getAttribute("title");
			LogUtil.infoLog(KeywordUtil.class, "Image title is: " + imageTitle);
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return imageTitle;

	}

	/**
	 * @param locator
	 * 
	 * @return
	 */
	public static boolean isWebElementVisible(By locator, String logStep) {
		Boolean flag = false;
		try {
			KeywordUtil.lastAction = "Check Element visible: " + locator.toString();
			LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
			WebElement elm = waitForVisible(locator);

			if (elm.isDisplayed()) {
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
				Assert.fail(e.getMessage());
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;

	}

	public static boolean isWebElementEnable(By locator, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Check Element visible: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		WebElement elm;
		try {
			elm = waitForVisible(locator);
			if (elm.isEnabled()) {
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
				Assert.fail(e.getMessage());
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	/**
	 * @param locator
	 * 
	 * @return
	 */
	public static List<WebElement> getListElements(By locator, String logStep) {
		List<WebElement> listWebElements = null;
		KeywordUtil.lastAction = "Get List of Elements: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);

		try {
			findWithFluintWait(locator, 60, 300);
			listWebElements = getDriver().findElements(locator);
			ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
		} catch (Exception e) {
			ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return listWebElements;

	}

	public static boolean areWebElementsPresent(By locator, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Check Element present: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		try {
			List<WebElement> elements = getDriver().findElements(locator);
			if (elements.isEmpty()) {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
				Assert.fail(e.getMessage());
			} else {
				flag = true;
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	public static boolean isWebElementNotPresent(By locator, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Check Element present: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		try {
			WebElement elements = getDriver().findElement(locator);
			if (elements.isDisplayed()) {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
			} else {
				flag = true;
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			flag = false;
		}
		return flag;
	}

	public static boolean hoverOnElement(By by) throws InterruptedException {
		Boolean flag = false;
		try {
			WebElement element = getDriver().findElement(by);
			Actions act = new Actions(getDriver());
			act.moveToElement(element).build().perform();
			Thread.sleep(3000);
			flag = true;
		} catch (InterruptedException e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}

		return flag;

	}

	/**
	 * @param locator
	 * 
	 * @return
	 */
	public static boolean areWebElementsNotPresent(By locator) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Check Element not present: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		try {
			List<WebElement> elements = (new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS))
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
			if (elements.isEmpty()) {
				flag = true;
			} else {
				flag = false;
				Assert.fail(e.getMessage());
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	/**
	 * @param locator
	 * @param data
	 * 
	 * @return
	 */
	public static boolean inputText(By locator, String data, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Input Text: " + data + " - " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		try {
			WebElement elm = waitForVisible(locator);
			if (elm == null) {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class, KeywordUtil.lastAction);
				flag = false;
				Assert.fail(e.getMessage());
			} else {
				elm.clear();
				elm.sendKeys(data);
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
				flag = true;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	public static void pressTabKey(By locator) {
		WebElement elm = waitForVisible(locator);
		try {
			elm.sendKeys(Keys.TAB);
			LogUtil.infoLog(KeywordUtil.class, "TAB key pressed.");
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static void pressEnter(By locator) {
		WebElement elm = waitForVisible(locator);
		try {
			elm.sendKeys(Keys.ENTER);
			LogUtil.infoLog(KeywordUtil.class, "Enter key pressed.");
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * @param locator
	 * @param data
	 * 
	 * @return
	 */
	public static boolean inputTextJS(By locator, String data, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Input Text: " + data + " - " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		try {
			WebElement element = waitForVisible(locator);
			((JavascriptExecutor) getDriver()).executeScript("arguments[0].value = arguments[1]", element, data);
			if (element.getText().equalsIgnoreCase(data)) {
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(logStep);
		}
		return flag;
	}

	/**
	 * @param locator
	 * 
	 * @return
	 */
	public static boolean isRadioSelected(By locator, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Is Radio Selected: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		WebElement element;
		try {
			element = waitForVisible(locator);
			if (element.isSelected()) {
				flag = true;
				LogUtil.infoLog(KeywordUtil.class, logStep);
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			} else {
				LogUtil.errorLog(KeywordUtil.class, logStep);
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(logStep);
		}

		return flag;
	}

	/**
	 * @param locator
	 * 
	 * @return
	 */
	public static boolean isRadioNotSelected(By locator, String logStep) {
		Boolean flag = false;
		boolean check;
		try {
			KeywordUtil.lastAction = "Is Radio Not Selected: " + locator.toString();
			LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
			check = isRadioSelected(locator, logStep);
			if (!check) {
				flag = true;
				LogUtil.infoLog(KeywordUtil.class, logStep);
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			} else {
				LogUtil.errorLog(KeywordUtil.class, logStep);
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(logStep);
		}

		return flag;
	}

	/**
	 * @param locator
	 * 
	 * @return
	 */
	public static boolean clearInput(By locator) {
		Boolean flag = false;
		WebElement element;
		try {
			element = waitForVisible(locator);
			element.clear();
			element = waitForVisible(locator);
			if (element.getAttribute(VALUE).isEmpty()) {
				flag = true;
				LogUtil.infoLog(KeywordUtil.class, "Input cleared.");
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor("Input cleared."));
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor("Failed to clear input."));
				LogUtil.errorLog(KeywordUtil.class, "Failed to clear input.");
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	public static String getValueFromTextValue(By locator, String logstep) {
		String textValueFromField = null;
		WebElement element;
		try {
			element = waitForVisible(locator);
			if (element.getAttribute(VALUE).isEmpty()) {
				textValueFromField = null;
				LogUtil.errorLog(KeywordUtil.class, logstep + " " + textValueFromField);
				ExtentUtil.logger.get().log(Status.FAIL,
						HTMLReportUtil.failStringRedColor(logstep + " " + textValueFromField));
				Assert.fail(e.getMessage());
			} else {
				textValueFromField = element.getAttribute(VALUE).toString();
				ExtentUtil.logger.get().log(Status.PASS,
						HTMLReportUtil.passStringGreenColor(logstep + " " + textValueFromField));
				LogUtil.infoLog(KeywordUtil.class, logstep + " " + textValueFromField);
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			textValueFromField = null;
			Assert.fail(e.getMessage());
		}
		return textValueFromField;
	}

	/**
	 * @param locator
	 * @param data
	 * 
	 * @return
	 */
	public static boolean verifyCssProperty(By locator, String data, String logStep) {
		boolean flag = false;
		KeywordUtil.lastAction = "Verify CSS : " + data + " - " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);

		try {
			String[] property = data.split(":", 2);
			String expProp = property[0];
			String expValue = property[1];
			String prop = (waitForPresent(locator)).getCssValue(expProp);
			if (prop.trim().equals(expValue.trim())) {
				flag = true;
				LogUtil.infoLog(KeywordUtil.class, logStep);
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class, logStep);
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(logStep);
		}
		return flag;
	}

	/**
	 * @param locator
	 * @param data
	 * 
	 * @return
	 */
	public static boolean verifyInputText(By locator, String data, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Verify Input Expected Text: " + data + " - " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		String actual;
		try {
			WebElement element = waitForVisible(locator);
			actual = element.getAttribute(VALUE);
			if (actual.equalsIgnoreCase(data)) {
				LogUtil.infoLog(KeywordUtil.class, "Actual:" + actual);
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class, logStep);
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(logStep);
		}

		return flag;

	}

	/**
	 * @param locator
	 * @param data
	 * 
	 * @return
	 */
	public static boolean verifyInputTextJS(By locator, String data, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Verify Input Expected Text: " + data + " - " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		WebElement element;
		try {
			element = waitForVisible(locator);
			if (data.equalsIgnoreCase(element.getText())) {
				String message = String.format("Verified text expected \"%s\" actual \"%s\" ", data, element.getText());
				LogUtil.infoLog(KeywordUtil.class, message);
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class, logStep);
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(logStep);
		}

		return flag;
	}

	/**
	 * <h1>Log results</h1>
	 * <p>
	 * This function will write results to the log file.
	 * </p>
	 * 
	 * @param locator
	 * @param data
	 * 
	 * @return
	 */
	/**
	 * @param locator
	 * @param data
	 * 
	 * @return
	 */
	public static boolean verifyText(By locator, String data, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Verify Expected Text: " + data + " - " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		WebElement element;
		try {
			element = waitForVisible(locator);
			String message = String.format("Verified text expected \"%s\" actual \"%s\" ", data, element.getText());
			LogUtil.infoLog(KeywordUtil.class, message);
			if (element.getText().equalsIgnoreCase(data)) {
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				LogUtil.infoLog(KeywordUtil.class, logStep);
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class, logStep);
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(logStep);
		}

		return flag;
	}

	public static boolean verifyTextContains(By locator, String data, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Verify Text Contains: " + data + " - " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		WebElement element;
		try {
			element = waitForVisible(locator);
			String message = new String(
					String.format("Verified text expected \"%s\" actual \"%s\" ", data, element.getText()));
			LogUtil.infoLog(KeywordUtil.class, message);
			if (element.getText().toUpperCase().contains(data.toUpperCase())) {
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				LogUtil.infoLog(KeywordUtil.class, logStep);
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class, logStep);
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(logStep);
		}
		return flag;
	}

	/**
	 * @param locator
	 * 
	 * @return
	 */
	public static boolean verifyDisplayAndEnable(By locator, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Is Element Displayed and Enable : " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		WebElement element;
		try {
			element = waitForVisible(locator);
			if (element.isDisplayed() && element.isEnabled()) {
				LogUtil.infoLog(KeywordUtil.class, logStep);
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				flag = true;
			} else {
				LogUtil.errorLog(KeywordUtil.class, logStep);
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(logStep);
		}
		return flag;
	}

	/**
	 * @param locator
	 * @param data
	 * 
	 * @return
	 */
	public static boolean clickJS(By locator, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Click : " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		Object obj;
		try {
			WebElement element = waitForVisible(locator);
			obj = ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
			if (obj == null) {
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				LogUtil.infoLog(KeywordUtil.class, logStep);
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class, logStep);
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(logStep);
		}
		return flag;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/*
	 * Handling selects ===========================================================
	 */

	/**
	 * @param locator
	 * @param index
	 * 
	 * @return
	 */
	public static boolean selectByIndex(By locator, int index, String logStep) {
		Boolean flag = false;
		try {
			KeywordUtil.lastAction = "Select dropdown by index : " + index + " - " + locator.toString();
			LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
			Select sel = new Select(getDriver().findElement(locator));
			sel.selectByIndex(index);

			// Check whether element is selected or not
			sel = new Select(getDriver().findElement(locator));
			if (sel.getFirstSelectedOption().isDisplayed()) {
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				LogUtil.infoLog(KeywordUtil.class, logStep);
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class, logStep);
				flag = false;
				Assert.fail(e.getMessage());
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	/**
	 * @param locator
	 * @param value
	 * 
	 * @return
	 */
	public static boolean selectByValue(By locator, String value, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Select dropdown by value : " + value + " - " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		try {
			Select sel = new Select(getDriver().findElement(locator));
			sel.selectByValue(value);

			// Check whether element is selected or not
			sel = new Select(getDriver().findElement(locator));
			if (sel.getFirstSelectedOption().isDisplayed()) {
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				LogUtil.infoLog(KeywordUtil.class, logStep);
				flag = true;
			} else {
				LogUtil.errorLog(KeywordUtil.class, logStep);
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
				Assert.fail(e.getMessage());
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	/**
	 * @param locator
	 * @param value
	 * 
	 * @return
	 */
	public static boolean selectByVisibleText(By locator, String value, String logStep) {
		try {
			KeywordUtil.lastAction = "Select dropdown by text : " + value + " - " + locator.toString();
			LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
			WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			Select sel = new Select(element);
			sel.selectByVisibleText(value);
			ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			return true;
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
			return false;
		}
	}

	public static boolean selectedOptionInSelectDropDown(By locator, String value, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Select dropdown by text : " + value + " - " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		try {
			Select sel = new Select(getDriver().findElement(locator));
			String expected = value;
			String actual = sel.getFirstSelectedOption().getText();
			if (actual.contentEquals(expected)) {
				LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
				LogUtil.infoLog(KeywordUtil.class, "Actual: " + actual);
				LogUtil.infoLog(KeywordUtil.class, "Expected: " + expected);
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				flag = true;
			} else {
				LogUtil.errorLog(KeywordUtil.class, KeywordUtil.lastAction);
				LogUtil.errorLog(KeywordUtil.class, "Actual: " + actual);
				LogUtil.errorLog(KeywordUtil.class, "Expected: " + expected);
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
			}
		} catch (Exception e) {
			ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	public static String getFullQuoteNumber(By locator, String logstep) {
		String fullQuoteNumber = null;
		KeywordUtil.lastAction = "Get full quote number";
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		WebElement element;
		String fullText = null;
		try {
			checkPageIsReady();
			element = waitForVisible(locator);
			fullText = element.getText();
			if (fullText.length() > 0) {
				fullQuoteNumber = fullText.split(" ")[2];
				LogUtil.infoLog(KeywordUtil.class, "Full Quote Number is : " + fullQuoteNumber);
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logstep));
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logstep));
				LogUtil.errorLog(KeywordUtil.class, e.getMessage());
				Assert.fail(KeywordUtil.lastAction);
			}
		} catch (Throwable e) {
			ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logstep));
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return fullQuoteNumber;
	}

	public static String getFullQuoteStatus(By locator, String logstep) {
		String fullQuoteStatus = null;
		KeywordUtil.lastAction = "Get full quote status";
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		WebElement element;
		String fullText = null;
		try {
			checkPageIsReady();
			element = waitForVisible(locator);
			fullText = element.getText();
			if (fullText.length() > 0) {
				fullQuoteStatus = fullText.split(" ")[3].replace("(", "").replace(")", "");
				LogUtil.infoLog(KeywordUtil.class, "Full Quote status is : " + fullQuoteStatus);
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logstep));
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logstep));
				LogUtil.errorLog(KeywordUtil.class, e.getMessage());
				Assert.fail(KeywordUtil.lastAction);
			}
		} catch (Throwable e) {
			ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logstep));
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return fullQuoteStatus;
	}

	/**
	 * @param locator
	 * @param data
	 * 
	 * @return
	 * 
	 * @throws Throwable
	 */
	public static boolean verifyAllValuesOfDropDown(By locator, String data, String logStep) throws Throwable {
		KeywordUtil.lastAction = "Verify Dropdown all values: " + data + " - " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		boolean flag = false;
		try {
			WebElement element = findWithFluintWait(locator);
			List<WebElement> options = element.findElements(By.tagName("option"));
			String[] allElements = data.split(",");
			String actual;
			for (int i = 0; i < allElements.length; i++) {
				LogUtil.infoLog(KeywordUtil.class, options.get(i).getText());
				LogUtil.infoLog(KeywordUtil.class, allElements[i].trim());

				actual = options.get(i).getText().trim();
				if (actual.equalsIgnoreCase(allElements[i].trim())) {
					ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));

					flag = true;
				} else {
					ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
					flag = false;
					break;
				}
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	public static void clickOnlistOptionBasedOnValue(By locator, String optionToSelect, String logStep)
			throws Throwable {
		KeywordUtil.lastAction = "Searching for : " + optionToSelect + " in list - " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
			List<WebElement> element = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
			for (int i = 0; i < element.size(); i++) {
				LogUtil.infoLog(KeywordUtil.class, element.get(i).getText());
				if (element.get(i).getText().contentEquals(optionToSelect)) {
					element.get(i).click();
					LogUtil.infoLog(KeywordUtil.class, optionToSelect + " Clicked");
					ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
					break;
				}
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			LogUtil.errorLog(KeywordUtil.class, optionToSelect + " Not Clicked");
			ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static void clickOnlistOptionBasedOnValueAttribute(By locator, String optionToSelect, String logStep)
			throws Throwable {
		KeywordUtil.lastAction = "Searching for : " + optionToSelect + " in list - " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		try {
			WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
			List<WebElement> element = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
			for (int i = 0; i < element.size(); i++) {
				LogUtil.infoLog(KeywordUtil.class, element.get(i).getAttribute(VALUE));
				if (element.get(i).getAttribute(VALUE).contentEquals(optionToSelect)) {
					element.get(i).click();
					LogUtil.infoLog(KeywordUtil.class, optionToSelect + " Clicked");
					ExtentUtil.logger.get().log(Status.PASS,
							HTMLReportUtil.passStringGreenColor(logStep + optionToSelect));
					break;
				}
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			LogUtil.errorLog(KeywordUtil.class, optionToSelect + " Not Clicked");
			ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep + optionToSelect));
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * @param locator
	 * @param data
	 * 
	 * @return
	 */
	public static boolean verifyDropdownSelectedValue(By locator, String data, String logStep) {
		Boolean flag = false;
		String defSelectedVal;
		try {
			KeywordUtil.lastAction = "Verify Dropdown selected option: " + data + " - " + locator.toString();
			LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
			Select sel = new Select(waitForVisible(locator));
			defSelectedVal = sel.getFirstSelectedOption().getText();
			if (defSelectedVal.trim().equals(data.trim())) {
				flag = true;
				LogUtil.infoLog(KeywordUtil.class, logStep);
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			} else {
				LogUtil.errorLog(KeywordUtil.class, logStep);
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	/**
	 * @param locator
	 * @param size
	 * 
	 * @return
	 */
	public static boolean verifyElementSize(By locator, int size, String logStep) {
		Boolean flag = false;
		try {
			KeywordUtil.lastAction = "Verify Element size: " + size + " - " + locator.toString();
			LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
			List<WebElement> elements = getDriver().findElements(locator);
			if (elements.size() == size) {
				LogUtil.infoLog(KeywordUtil.class, "Element is Present " + size + "times");
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class, "Element is not Present with required size");
				LogUtil.errorLog(KeywordUtil.class, "Expected size:" + size + " but actual size: " + elements.size());
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	/**
	 * @param locator
	 * @param data
	 * 
	 * @return
	 * 
	 * @throws InterruptedException
	 */
	public static boolean writeInInputCharByChar(By locator, String data, String logStep) throws InterruptedException {
		Boolean flag = false;
		try {
			WebElement element = waitForVisible(locator);
			element.clear();
			String[] b = data.split("");
			for (int i = 0; i < b.length; i++) {
				element.sendKeys(b[i]);
				Thread.sleep(250);
			}
			ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			flag = true;
		} catch (InterruptedException e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	// Get Tag name and locator value of Element
	public static String getElementInfo(By locator) throws Exception {
		String elementInfo = null;
		try {
			elementInfo = " Locator: " + locator.toString();
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return elementInfo;
	}

	public static String getElementInfo(WebElement element) throws Exception {
		String webElementInfo = "";
		try {
			webElementInfo = webElementInfo + "Tag Name: " + element.getTagName() + ", Locator: ["
					+ element.toString().substring(element.toString().indexOf("->") + 2);
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return webElementInfo;
	}

	/**
	 * @param time
	 * 
	 * @throws InterruptedException
	 */
	public static void delay(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @param locator
	 * 
	 * @return
	 */
	public boolean verifyCurrentDateInput(By locator, String logStep) {
		boolean flag = false;
		try {
			WebElement element = waitForVisible(locator);
			String actual = element.getAttribute(VALUE).trim();
			DateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date date = new Date();
			dtFormat.setTimeZone(TimeZone.getTimeZone("US/Central"));
			String expected = dtFormat.format(date).trim();
			if (actual.trim().contains(expected)) {
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
				Assert.fail(e.getMessage());
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	/**
	 * @param locator
	 * @param data
	 * 
	 * @return
	 * 
	 * @throws InterruptedException
	 */
	public static boolean uploadFilesUsingSendKeys(By locator, String data, String logStep)
			throws InterruptedException {
		Boolean flag = false;
		try {
			WebElement element = waitForVisible(locator);
			element.clear();
			element.sendKeys(System.getProperty(userDir) + "\\src\\test\\resources\\uploadFiles\\" + data);
			ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			LogUtil.infoLog(KeywordUtil.class, logStep);
			flag = true;
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	/**
	 * @return
	 */
	public boolean delDirectory() {
		Boolean flag = false;
		File delDestination = null;
		try {
			delDestination = new File(System.getProperty(userDir) + "\\src\\test\\resources\\downloadFile");
			if (delDestination.exists()) {
				File[] files = delDestination.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						delDirectory();
					} else {
						files[i].delete();
					}
				}
			}
			if (delDestination.delete()) {
				flag = true;
			} else {
				flag = false;
				Assert.fail(e.getMessage());
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());
		}
		return flag;
	}

	public static boolean doubleClick(By locator, String logStep) {
		boolean result = false;
		try {
			KeywordUtil.lastAction = "Double click: " + locator.toString();
			LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
			WebElement element = getDriver().findElement(locator);
			Actions action = new Actions(getDriver()).doubleClick(element);
			action.build().perform();
			ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			result = true;
		} catch (StaleElementReferenceException e) {
			LogUtil.errorLog(KeywordUtil.class,
					locator.toString() + " - Element is not attached to the page document " + e.getStackTrace());
			result = false;
			Assert.fail(e.getMessage());
		} catch (NoSuchElementException e) {
			LogUtil.errorLog(KeywordUtil.class,
					locator.toString() + " - Element is not attached to the page document " + e.getStackTrace());
			result = false;
			Assert.fail(e.getMessage());
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class,
					locator.toString() + " - Element is not attached to the page document " + e.getStackTrace());
			result = false;
			Assert.fail(e.getMessage());
		}
		return result;
	}

	public static boolean switchToFrame(String frameName) {

		try {
			getDriver().switchTo().frame(frameName);
			return true;

		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, frameName + " TO FRAME FAILED" + e.getStackTrace());
			Assert.fail(e.getMessage());
			return false;
		}
	}

	public static String createZipFile() throws IOException {
		String outputFile = null;
		try {
			result_FolderName = result_FolderName.replace("\\", "/");
			outputFile = result_FolderName + ".zip";
			FileOutputStream fos = new FileOutputStream(outputFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			packCurrentDirectoryContents(result_FolderName, zos);
			zos.closeEntry();
			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} catch (IOException e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return outputFile;
	}

	public static void packCurrentDirectoryContents(String directoryPath, ZipOutputStream zos) throws IOException {
		try {
			for (String dirElement : new File(directoryPath).list()) {
				String dirElementPath = directoryPath + "/" + dirElement;
				if (new File(dirElementPath).isDirectory()) {
					packCurrentDirectoryContents(dirElementPath, zos);
				} else {
					ZipEntry ze = new ZipEntry(dirElementPath.replaceAll(result_FolderName + "/", ""));
					zos.putNextEntry(ze);
					FileInputStream fis = new FileInputStream(dirElementPath);
					byte[] bytesRead = new byte[512];
					int bytesNum;
					while ((bytesNum = fis.read(bytesRead)) > 0) {
						zos.write(bytesRead, 0, bytesNum);
					}
					fis.close();
				}
			}
		} catch (FileNotFoundException e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} catch (IOException e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static boolean isWebElementPresent(By locator, String logStep) {
		Boolean flag = false;
		KeywordUtil.lastAction = "Check Element present: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		try {
			List<WebElement> elements = getDriver().findElements(locator);
			if (elements.isEmpty()) {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				flag = false;
			} else {
				flag = true;
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			Assert.fail(logStep);
		}
		return flag;
	}

	public static boolean clickOnlastElement(By locator, String logStep) {
		Boolean flag = false;
		try {
			List<WebElement> elements = getDriver().findElements(locator);
			int size = elements.size();
			if (size >= 1) {
				elements.get(size - 1).click();
				LogUtil.infoLog(KeywordUtil.class, "Element is Present " + size + "times");
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class, "Element is not clickable");
				flag = false;
			}
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(logStep);
		}
		return flag;
	}

	public static boolean clickCart(By locator, String logStep) {

		KeywordUtil.lastAction = "Click: " + locator.toString();
		LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
		WebElement elm = waitForClickable(locator);
		if (elm == null) {
			return false;
		} else {

			((JavascriptExecutor) GlobalUtil.getDriver()).executeScript("arguments[0].scrollIntoView();", elm);
			elm.click();
			ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));

			return true;
		}
	}

	public static boolean verifyBordercolor(By locator, String color, String logStep) {
		boolean flag = false;
		try {
			WebElement element = GlobalUtil.getDriver().findElement(locator);
			String actualColor = element.getCssValue("border-color");
			String hexcolor = Color.fromString(actualColor).asHex();
			if (hexcolor.equals(color)) {
				LogUtil.infoLog(KeywordUtil.class, "Actual border color is " + hexcolor);
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class,
						"Expected border color is" + color + " But Actual border color is " + hexcolor);
				flag = false;
			}

		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(logStep);
		}
		return flag;
	}

	public static boolean verifyTextcolor(By locator, String color, String logStep) {
		boolean flag = false;
		try {
			WebElement element = GlobalUtil.getDriver().findElement(locator);
			String actualColor = element.getCssValue("color");
			String hexcolor = Color.fromString(actualColor).asHex();
			if (hexcolor.equals(color)) {
				LogUtil.infoLog(KeywordUtil.class, "Actual border color is " + hexcolor);
				ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(logStep));
				flag = true;
			} else {
				ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(logStep));
				LogUtil.errorLog(KeywordUtil.class,
						"Expected border color is" + color + " But Actual border color is " + hexcolor);
				flag = false;
			}

		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			flag = false;
			Assert.fail(e.getMessage());

		}
		return flag;
	}

	public static void scrollRight(By by, int xOffset, int yOffset) {
		try {
			WebElement slider = GlobalUtil.getDriver().findElement(by);
			Actions move = new Actions(GlobalUtil.getDriver());
			move.dragAndDropBy(slider, xOffset, yOffset).build().perform();
			LogUtil.infoLog(KeywordUtil.class, "Scrolled to element.");
		} catch (Exception e) {
			LogUtil.errorLog(KeywordUtil.class, e.getMessage());
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}

	}// End class

	@SuppressWarnings("serial")
	class TestStepFailedException extends Exception {

		TestStepFailedException(String s) {
			super(s);
		}

		public void scrolldown(WebElement Element) {
			try {
				JavascriptExecutor js = (JavascriptExecutor) GlobalUtil.getDriver();
				js.executeScript("window.scrollBy(0,600);", Element);
				LogUtil.infoLog(KeywordUtil.class, "Scrolled to element.");
			} catch (Exception e) {
				LogUtil.errorLog(KeywordUtil.class, e.getMessage());
				e.printStackTrace();
				Assert.fail(e.getMessage());
			}
		}
	}

	/*
	 * Normal wait is like Thread.sleep
	 */
	public static void normalWait(int sec) {
		try {
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
