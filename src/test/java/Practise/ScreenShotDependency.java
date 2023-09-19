package Practise;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenShotDependency {
	
	public static String screenshot(WebDriver driver)
	{
	TakesScreenshot takes=(TakesScreenshot)driver;
	String codeString=takes.getScreenshotAs(OutputType.BASE64);
	System.out.println("Screenshot successfull");
	return codeString;
	}
		
	public static String screenshot(WebDriver driver,String filename)
	{
	TakesScreenshot takes=(TakesScreenshot)driver;
	File sr=takes.getScreenshotAs(OutputType.FILE);
	System.out.println("Screenshot successfull");
	File de=new File("screen/"+filename);
	try{
	FileUtils.copyFile(sr,de);
	}catch(Exception e)
	{
	e.printStackTrace();
	}
	return de.getAbsolutePath();
	}
	
	public static String passStringGreenColor(String stepName) {
		String html = "<span style='color:#008000'><b>" + stepName + " - PASSED" + "</b></span>";
		return html;
	}
	
	public static String passStringRedColor(String stepName) {
		String html = "<span style='color:#800000'><b>" + stepName + " - Failed" + "</b></span>";
		return html;
	}

}
