package stepDefinitions;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import modules.AmazonRegistration;
import pages.UdemyLoginpage;
import utilities.ExcelDataUtil;
import utilities.GlobalUtil;
import utilities.KeywordUtil;

public class UdemySearchCourse extends KeywordUtil{
	
	static Class thisClass = UdemySearchCourse.class;
	static String testCaseID = thisClass.getSimpleName();

	static String logStep;
	public WebDriver driver;
	public static HashMap<String, String> dataMap = new HashMap<String, String>();

	@Given("Read the testdata {string} from the excel file")
	public void read_the_testdata_from_the_excel_file(String args) {
		try {
			KeywordUtil.cucumberTagName = "Web";
			dataMap = ExcelDataUtil.getTestDataWithTestCaseID(args, "Udemy");

		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}

	@When("Navigate to the udemy website with url")
	public void navigate_to_the_udemy_website_with_url() {
		try {
			navigateToUrl(dataMap.get("URL"));

		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}
	
	@And("Login in to application")
	public void login_in_to_application() {
		try {
			//Login into the application
			KeywordUtil.click(UdemyLoginpage.loginIntoApp, "Login button");
			KeywordUtil.inputText(UdemyLoginpage.email,dataMap.get("UserID") , "UserID");	
			KeywordUtil.inputText(UdemyLoginpage.passWord, dataMap.get("Password"), "Password");
			KeywordUtil.click(UdemyLoginpage.loginButton, "Login Button");
			
		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	    
	} 


	@And("Search for a course")
	public void search_for_a_course() {
	   
		try {
			//search for a course
			KeywordUtil.inputText(UdemyLoginpage.search,dataMap.get("Course") , "Course");
			By courseLocator=UdemyLoginpage.searchResult(dataMap.get("Course"));
			KeywordUtil.pressEnter(UdemyLoginpage.search);
			KeywordUtil.waitForVisible(courseLocator);
			By CourseList=UdemyLoginpage.allCourses(dataMap.get("Course"));
			List<WebElement> list=KeywordUtil.getListElements(CourseList, "CourseList");
			list.get(1).click();
//			for(int i=0;i<list.size();i++)
//			{
//				list.get(i).
//			}
//			
		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}
	

	@When("^Add the course into cart$")
	public void add_the_course_into_cart() {
	}
	

	@Then("I verify the required course is added into a cart")
	public void i_verify_the_required_course_is_added_into_a_cart() {
	    
	}

	@And("Logout from the application")
	public void logout_from_the_application() {
	    
	}

}
