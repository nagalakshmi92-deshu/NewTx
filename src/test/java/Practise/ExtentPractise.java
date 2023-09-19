package Practise;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ExtentPractise {
	public ExtentReports extentReports;
	public ExtentSparkReporter extentSparkReporter;
	public ExtentTest extentTest;
	public WebDriver driver;
	public Logger logger;
	public String url="https://rahulshettyacademy.com/seleniumPractise/#/";
	public String extentReportFileName="./NewExtentReport/t1.html";
	
	@BeforeClass
	public void setExtent()
	{
		//logger
		logger=LogManager.getLogger(this.getClass());
		
		//Extent reports
		extentReports=new ExtentReports();
		extentSparkReporter=new ExtentSparkReporter("t1.html");
		extentReports.attachReporter(extentSparkReporter);
		extentReports.setSystemInfo("QA", "Nagalakshmi");
		extentReports.setSystemInfo("Environment", "QA");
		
		
	}
	
	@BeforeMethod
	public void setUp()
	{
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(20,TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		
		driver.get(url);		
	}
	
	@Test
	public void testMethod1()
	{
		extentTest=extentReports.createTest("Method1");
		String title=driver.getTitle();
		logger.info("Title of the page : "+title);
		String pathOfScreenshot=ScreenShotDependency.screenshot(driver, title+".png" );
		//extentTest.addScreenCaptureFromPath(pathOfScreenshot, title);
		extentTest.info(MediaEntityBuilder.createScreenCaptureFromPath(pathOfScreenshot, "Google.jpg").build());
		
	}
	

	@Test
	public void testMethod2()
	{
		extentTest=extentReports.createTest("Method2");
		String currentUrl=driver.getCurrentUrl();
		logger.info("Title of the page : "+currentUrl);
		Assert.assertEquals(currentUrl, url);
		//String pathOfScreenshot=ScreenShotDependency.screenshot(driver, currentUrl+".png" );
		//extentTest.addScreenCaptureFromPath(pathOfScreenshot, currentUrl);
		
	}
	@AfterMethod
	public void resultListener(ITestResult result) {
		if (result.getStatus() == ITestResult.SUCCESS) {
			extentTest.log(Status.PASS,"The cases Sucess is "+ScreenShotDependency.passStringGreenColor(result.getName()));
			extentTest.log(Status.PASS,"The cases Success is "+result.getThrowable());
			
			String pathOfScreenshot=ScreenShotDependency.screenshot(driver, result.getName()+".png" );
			extentTest.addScreenCaptureFromPath(pathOfScreenshot, result.getName());
		}
		else if (result.getStatus() == ITestResult.FAILURE) {
			extentTest.log(Status.FAIL,"The cases Failed is "+ScreenShotDependency.passStringRedColor(result.getName()));
			extentTest.log(Status.FAIL,"The cases Failed is "+result.getThrowable());

			String pathOfScreenshot=ScreenShotDependency.screenshot(driver, result.getName()+".png" );
			extentTest.addScreenCaptureFromPath(pathOfScreenshot, result.getName());
		}
		else if (result.getStatus() == ITestResult.SKIP) {
			extentTest.log(Status.SKIP,"The cases skipped is "+ScreenShotDependency.passStringGreenColor(result.getName()));
			extentTest.log(Status.SKIP,"The cases skipped is "+result.getThrowable());

			String pathOfScreenshot=ScreenShotDependency.screenshot(driver, result.getName()+".png" );
			extentTest.addScreenCaptureFromPath(pathOfScreenshot, result.getName());
		}
		
		else
		{
			extentTest.info("No perticular status");
			String pathOfScreenshot=ScreenShotDependency.screenshot(driver, result.getName()+".png" );
			extentTest.addScreenCaptureFromPath(pathOfScreenshot, result.getName());
		}
		
		driver.close();
	}

	@AfterTest
	public void tearDown() throws IOException
	{
		
		extentReports.flush();
		Desktop.getDesktop().browse(new File("t1.html").toURI());
	}
	

}
