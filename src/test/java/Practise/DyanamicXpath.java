package Practise;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DyanamicXpath {

	public static void main(String[] args) {
		
		WebDriverManager.chromedriver().setup();
		WebDriver driver=new ChromeDriver();
		driver.get("https://www.bbc.com/");
	driver.manage().window().maximize();
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		String web=driver.findElement(By.xpath("//section[@class='module module--header']/h2")).getText();
		System.out.println(web);
		String[] web1=web.split("\n");
		System.out.println(web1[1]);
		driver.close();
	}

}
