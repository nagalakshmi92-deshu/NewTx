package stepDefinitions;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.GreenKartPages;
import utilities.ExcelDataUtil;
import utilities.ExtentUtil;
import utilities.GlobalUtil;
import utilities.KeywordUtil;
import utilities.LogUtil;

public class GreenKart extends KeywordUtil {

	static Class thisClass = GreenKart.class;
	static String testCaseID = thisClass.getSimpleName();
	static String logStep;
	public WebDriver driver;
	public static HashMap<String, String> dataMap = new HashMap<String, String>();
	Scenario scenario;

	@Given("Read the testdata {string} {string} from the excel data file")
	public void read_the_testdata_from_the_excel_data_file(String testcaseId, String sheetName) {
		try {
			KeywordUtil.cucumberTagName = "Web";
			dataMap = ExcelDataUtil.getTestDataWithTestCaseID(testcaseId, sheetName);
			System.out.println(dataMap);
			this .scenario=Hooks.scenario1;
			LogUtil.infoLog(thisClass, "Get the datat");

		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}

	@When("Navigate to url")
	public void navigate_to_url() {

		try {
			navigateToUrl(dataMap.get("URL"));
		
			ExtentUtil.attachScreenShot(testCaseID, "Naviagte to Url");
			LogUtil.infoLog(thisClass, "Reach url");
			//KeywordUtil.takeScreenshot("Naviagate to url");

		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}

	@When("Search the product, add it to cart and click on ProceedToCheckout")
	public void search_the_product_add_it_to_cart_and_click_on_proceed_to_checkout() {
		try {
			// Get products from testDataSheet
			String products = dataMap.get("Products");
			System.out.println("All Products : " + products);
			String[] allProducts = products.split(",");
			System.out.println("All Products : " + allProducts);

			// search indivual products and add them into kart
			for (int i = 0; i < allProducts.length; i++) {
				KeywordUtil.inputText(GreenKartPages.searchBox, allProducts[i],
						i + 1 + " " + allProducts[i] + " product");
				KeywordUtil.click(GreenKartPages.searchButton, "SearchButton");
				KeywordUtil.normalWait(1000);
				KeywordUtil.waitForVisible(GreenKartPages.addToCartButton);
				KeywordUtil.click(GreenKartPages.addToCartButton, "Add To Kart button");
				ExtentUtil.attachScreenShot("addTOKArt", "Naviagte to Url");

			}

			// click on Add to Cart icon
			KeywordUtil.click(GreenKartPages.cartIcon, "Cart Icon");
			KeywordUtil.waitForVisible(GreenKartPages.proceedToCart);
			KeywordUtil.click(GreenKartPages.proceedToCart, "Proceed To Cart button");

		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}

	@When("Select the country and click on proceed button")
	public void select_the_country_and_click_on_proceed_button() {
		try {
			// click on Place Order button
			KeywordUtil.waitForPresent(GreenKartPages.placeOrderbutton);
			KeywordUtil.scrollingToElementofAPage(GreenKartPages.placeOrderbutton, "Place Order button");
			KeywordUtil.click(GreenKartPages.placeOrderbutton, "Place Order button");

			// select the country
			KeywordUtil.waitForPresent(GreenKartPages.selectCountry);
			KeywordUtil.selectByVisibleText(GreenKartPages.selectCountry, dataMap.get("Country"),
					dataMap.get("Country"));
			KeywordUtil.click(GreenKartPages.checkAgreeCheckBox, "Agree terms and conditions");
			KeywordUtil.waitForPresent(GreenKartPages.selectCountry);
			KeywordUtil.click(GreenKartPages.proceedButton, "Proceed Button");
		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}

	}

	@Then("Verify the success message")
	public void verify_the_success_message() {
		try {
			// Verify the success message
			KeywordUtil.waitForVisible(GreenKartPages.message);
			Assert.assertTrue(KeywordUtil.getElementText(GreenKartPages.message).contains("success"));

		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}

	@Then("Verify sum of indivial and total sum of products")
	public void verify_sum_of_indivial_and_total_sum_of_products() {
		try {
			List<WebElement> amountInTable = KeywordUtil.getListElements(GreenKartPages.amountInCartTable,
					"Amount of products");
			int sumOfProducts = 0;
			for (int i = 0; i < amountInTable.size(); i++) {
				sumOfProducts = sumOfProducts + Integer.parseInt(amountInTable.get(i).getText());
			}
			int totalPrice = Integer.parseInt(KeywordUtil.getElementText(GreenKartPages.totalAmount));
			Assert.assertTrue(sumOfProducts == totalPrice);
			LogUtil.infoLog(thisClass, "Total Amount is equal to Sum of products in the Cart");

		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}

	@Then("Verify selected products are present in Cart")
	public void verify_selected_products_are_present_in_cart() {

		try {
			// Get products from testDataSheet
			String products = dataMap.get("Products");
			System.out.println("All Products : " + products);
			String[] allProducts = products.split(",");
			System.out.println("All Products : " + allProducts);

			// get table products
			List<WebElement> productsInTsble = KeywordUtil.getListElements(GreenKartPages.productsInCartTable,
					"Total products");
			int count = 0;
			// search indivisual products and add them into kart
			for (int i = 0; i < allProducts.length; i++) {

				for (int j = 0; j < productsInTsble.size(); j++) {
					System.out.println("given  " + allProducts[i]);
					System.out.println("taken  " + productsInTsble.get(i).getText());
					if (productsInTsble.get(j).getText().contains(allProducts[i])) {
						LogUtil.infoLog(thisClass, allProducts[i] + " Product is present");
						count += 1;
						System.out.println("count =" + count);
					}

				}

			}
			if (count == allProducts.length) {
				Assert.assertTrue(true, "Selected products in the Cart Table");
			} else {
				Assert.assertTrue(false, "Selected products are not in the Cart Table");
			}

		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}

	@When("Click on the Top Deals")
	public void click_on_the_top_deals() {
		try {
			KeywordUtil.click(GreenKartPages.topDeals, "Top Deals");
			KeywordUtil.switchToWindow();

		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}

	@Then("Verify the prices of the products with discount prices")
	public void verify_the_prices_of_the_products_with_discount_prices() {
		try {
			List<WebElement> dealProducts = KeywordUtil.getListElements(GreenKartPages.productsInDeals,
					"Deal products");
			List<WebElement> act = KeywordUtil.getListElements(GreenKartPages.productsActualPrice,
					"Actual Price of the product");
			List<WebElement> deal = KeywordUtil.getListElements(GreenKartPages.productsDealPrice,
					"Deal Price of the product");

			for (int i = 0; i < act.size(); i++) {
				LogUtil.infoLog(getClass(), dealProducts.get(i).getText());
				int actInt = Integer.parseInt(act.get(i).getText());
				System.out.println("Actual price =" + actInt);
				int dealInt = Integer.parseInt(deal.get(i).getText());
				System.out.println(" Deal price =" + dealInt);
				if (actInt > dealInt) {
					System.out.println(dealProducts.get(i).getText() + " is in deal");
				} else {
					// System.out.println(dealProducts.get(i).getText()+" Wrong product is displayed
					// in deals");
					Assert.fail(dealProducts.get(i).getText() + " Wrong product is displayed in deals");
				}
			}

		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}

	@When("Select one product, pick atleast two kgs and add it to cart")
	public void select_one_product_pick_atleast_two_kgs_and_add_it_to_cart() {
		try {

			// Get products from testDataSheet
			String products = dataMap.get("Products");
			System.out.println("All Products : " + products);

			int countQuantity = Integer.parseInt(dataMap.get("Count"));
			System.out.println("countQuantity " + countQuantity);

			// search indivual products and add them into kart
			KeywordUtil.inputText(GreenKartPages.searchBox, products, products + " product");
			KeywordUtil.click(GreenKartPages.searchButton, "SearchButton");

			for (int i = 1; i <= countQuantity; i++) {
				KeywordUtil.normalWait(1000);
				KeywordUtil.click(GreenKartPages.increaseProductQuantity, "click on + button");

			}
			// click on Add to cart button
			KeywordUtil.waitForVisible(GreenKartPages.addToCartButton);
			KeywordUtil.click(GreenKartPages.addToCartButton, "Add To Kart button");

			// click on Add to Cart icon
			KeywordUtil.click(GreenKartPages.cartIcon, "Cart Icon");
			KeywordUtil.waitForVisible(GreenKartPages.proceedToCart);
			KeywordUtil.click(GreenKartPages.proceedToCart, "Proceed To Cart button");

		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}

	@Then("Verify the total price with mulitple of the product")
	public void verify_the_total_price_with_mulitple_of_the_product() {
		try {

		} catch (Throwable e) {
			GlobalUtil.e = e;
			GlobalUtil.errorMsg = e.getMessage();
			Assert.fail(e.getMessage());
		}
	}

}
