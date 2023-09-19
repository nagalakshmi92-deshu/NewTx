package utilities;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import bufferUtils.BufferUtilMethodLevel;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * This DriverUtil class refer to browsers, os details, browser versions and will close all browsers
 */

public class DriverUtil {

	public static final String IE = "IE";
	public static final String REMOTE = "BrowserStack";
	public static final String EDGE = "edge";
	public static final String CHROME = "Chrome";
	public static final String FIREFOX = "Firefox";
	public static final String SAFARI = "Safari";
	public static final String WINDOWS = "Windows";
	public static final String MACOS = "OS X";
	private static Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();
	public static DesiredCapabilities capabilities = new DesiredCapabilities();

	public static XSSFWorkbook wb;
	public static XSSFSheet sheet1;

	/**
	 * @param browserName
	 * 
	 * @return
	 */
	private DriverUtil() {

	}

	/**
	 * @param browserName
	 * 
	 * @return
	 */
	public static WebDriver getBrowser(String exeEnv, String browserName) {
		WebDriver browser = null;
		try {
//			DesiredCapabilities caps;

			MutableCapabilities capabilities = new MutableCapabilities();
			String URL = null;

			if (exeEnv.equals("REMOTE")) {

				HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
				browserstackOptions.put("projectName", GlobalUtil.getCommonSettings().getProjectName());
				browserstackOptions.put("buildName", GlobalUtil.getCommonSettings().getBuildNumber());
				browserstackOptions.put("sessionName", BufferUtilMethodLevel.scenarioName);

				if (browserName.equalsIgnoreCase(CHROME)) {
//					caps = DesiredCapabilities.chrome();
//					caps = capabilities.caps.setCapability("browser_version", "latest");
					capabilities.setCapability("browserName", CHROME);
					browserstackOptions.put("browserVersion", "latest");
				} else if (browserName.equalsIgnoreCase(IE)) {
//					caps = DesiredCapabilities.internetExplorer();
//					caps.setCapability("browser_version", "11.0");
					capabilities.setCapability("browserName", IE);
					browserstackOptions.put("browserVersion", "latest");
				} else if (browserName.equalsIgnoreCase(EDGE)) {
//					caps = DesiredCapabilities.edge();
//					caps.setCapability("browser_version", "latest");
					capabilities.setCapability("browserName", EDGE);
					browserstackOptions.put("browserVersion", "latest");
				} else if (browserName.equalsIgnoreCase(SAFARI)) {
//					caps = DesiredCapabilities.safari();
//					caps.setCapability("browser_version", "15.0");
					capabilities.setCapability("browserName", SAFARI);
					browserstackOptions.put("browserVersion", "latest");
				} else {
//					caps = DesiredCapabilities.firefox();
//					caps.setCapability("browser_version", "latest");
					capabilities.setCapability("browserName", CHROME);
					browserstackOptions.put("browserVersion", "latest");
				}

				if (GlobalUtil.getCommonSettings().getRemoteOS().split("_")[0].equalsIgnoreCase(WINDOWS)) {
					browserstackOptions.put("os", WINDOWS);
					browserstackOptions.put("osVersion", GlobalUtil.getCommonSettings().getRemoteOS().split("_")[1]);
//					caps.setCapability("os", WINDOWS);
//					caps.setCapability("os_version", GlobalUtil.getCommonSettings().getRemoteOS().split("_")[1]);
				}
				if (GlobalUtil.getCommonSettings().getRemoteOS().split("_")[0].equalsIgnoreCase(MACOS)) {
					browserstackOptions.put("os", MACOS);
					browserstackOptions.put("osVersion", GlobalUtil.getCommonSettings().getRemoteOS().split("_")[1]);
//					caps.setCapability("os", MACOS);
//					caps.setCapability("os_version", GlobalUtil.getCommonSettings().getRemoteOS().split("_")[1]);
				}

				if (GlobalUtil.getCommonSettings().getCloudProvider().equalsIgnoreCase("BrowserStack")) {
					browserstackOptions.put("debug", "true");
//					caps.setCapability("browserstack.debug", "true");
//					caps.setCapability("browserstack.geoLocation", "US");
//					caps.setCapability("build", GlobalUtil.getCommonSettings().getBuildNumber());
					browserstackOptions.put("buildName", GlobalUtil.getCommonSettings().getBuildNumber());
					browserstackOptions.put("local", "false");
					browserstackOptions.put("seleniumVersion", "4.8.0");
					URL = "https://" + GlobalUtil.getCommonSettings().getHostName() + ":" + GlobalUtil.getCommonSettings().getKey() + "@hub-cloud.browserstack.com/wd/hub";
				}
				try {
					capabilities.setCapability("bstack:options", browserstackOptions);
					browser = new RemoteWebDriver(new URL(URL), capabilities);
				} catch (Exception e) {
					e.printStackTrace();
				}
				drivers.put(browserName, browser);

			} else {

				if (browserName.equalsIgnoreCase(CHROME)) {
					browser = drivers.get(CHROME);
					if (browser == null) {
						//WebDriverManager.chromedriver().setup();
						browser = new ChromeDriver();
						drivers.put(CHROME, browser);
					}
				}

				else if (browserName.equalsIgnoreCase(IE)) {
					browser = drivers.get(IE);
					if (browser == null) {
						WebDriverManager.iedriver().arch64().setup();
						browser = new InternetExplorerDriver();
						drivers.put(IE, browser);
					}
				}

				else if (browserName.equalsIgnoreCase(FIREFOX)) {
					browser = drivers.get(FIREFOX);
					if (browser == null) {
						WebDriverManager.firefoxdriver().setup();
						browser = new FirefoxDriver();
						drivers.put(FIREFOX, browser);
					}
				}

				else if (browserName.equalsIgnoreCase(EDGE)) {
					browser = drivers.get(EDGE);
					if (browser == null) {
						WebDriverManager.edgedriver().setup();
						browser = new EdgeDriver();
						drivers.put(EDGE, browser);
					}
				}
			}
			browser.manage().window().maximize();
			LogUtil.infoLog(DriverUtil.class, GlobalUtil.getCommonSettings().getBrowser() + " : Browser Launched and Maximized.");
		} catch (Exception e) {
			LogUtil.errorLog(DriverUtil.class, "Browser not launched. Please check the configuration ", e);
			e.printStackTrace();
		}
		return browser;
	}

	/**
	 * close all browsers
	 * 
	 * @return
	 */
	public static void closeAllDriver() {

		drivers.entrySet().forEach(key -> {
			key.getValue().quit();
			key.setValue(null);
		});

		LogUtil.infoLog(DriverUtil.class, "Closing Browsers");
	}

	public static String getImgRef(String imgFile) {
		return new DriverUtil().getRefImage(imgFile);
	}

	private String getRefImage(String imgFile) {
		String openCVImgsFolder = "OpenCVImages/";
		URL refImgUrl = getClass().getClassLoader().getResource(openCVImgsFolder + imgFile + ".png");
		File refImgFile;
		try {
			refImgFile = Paths.get(refImgUrl.toURI()).toFile();
			LogUtil.infoLog(DriverUtil.class, "File Found : " + refImgFile.exists());
			return Base64.getEncoder().encodeToString(Files.readAllBytes(refImgFile.toPath()));
		} catch (URISyntaxException | IOException e) {
			LogUtil.errorLog(getClass(), e.getMessage());
			e.printStackTrace();
			return "";
		}
	}
}
