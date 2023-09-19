package pages;

import org.openqa.selenium.By;

public class ProductListPage {

	public static By searchText = By.xpath("//*[@id=\"twotabsearchtextbox\"]");
	public static By bookName = By.xpath("//div[contains(@class,'s-result-list')]//a[contains(@class,'a-link-normal')]/span[text()='The Alchemist']");
	public static By downPage = By.xpath("//span[@id='pagnPrevString']");
	public static By nextPage = By.xpath("//span[@id='pagnNextString']");

}
