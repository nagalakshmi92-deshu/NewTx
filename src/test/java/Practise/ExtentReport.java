package Practise;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReport {

	
	public static ExtentReports extent;
	public static ExtentSparkReporter spark;
	public static ExtentTest loggerTest;
	public static ExtentTest loggerTestStep;
	public static ThreadLocal<ExtentTest> logger = new ThreadLocal<ExtentTest>();
	
}
