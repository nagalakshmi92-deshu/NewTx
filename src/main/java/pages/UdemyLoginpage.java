package pages;

import org.openqa.selenium.By;

public class UdemyLoginpage {

	
	public static By loginIntoApp = By.xpath("//a[span[text()=\"Log in\"]]");
	public static By email = By.name("email");
	public static By passWord = By.name("password");
	public static By loginButton = By.xpath("//button[span[text()='Log in']]");
	
	public static By search = By.cssSelector("input[name='q']");
	
	public static By searchResult(String searchCpurse)
	{
		return By.xpath("//h1[contains(text(),'"+searchCpurse+"')]");
	}
	
	public static By allCourses(String searchCpurse)
	{
		return By.xpath("//div[@query='"+searchCpurse+"']");
	}
}
