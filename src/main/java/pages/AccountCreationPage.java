package pages;

import org.openqa.selenium.By;

public class AccountCreationPage {

	public static By username = By.xpath("//input[@id='ap_customer_name']");
	public static By emailID = By.xpath("//input[@id='ap_email']");
	public static By password = By.xpath("//input[@id='ap_password']");
	public static By passwordCheck = By.xpath("//input[@id='ap_password_check']");
	public static By createButton = By.xpath("//span[@id='a-autoid-0']");
}
