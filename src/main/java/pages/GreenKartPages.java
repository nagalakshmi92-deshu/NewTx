package pages;

import org.openqa.selenium.By;

public class GreenKartPages {
	public static By searchBox = By.cssSelector("input.search-keyword");
	public static By searchButton = By.cssSelector("button.search-button");
	public static By addToCartButton = By.xpath("//button[text()='ADD TO CART']");
	public static By cartIcon = By.cssSelector("a.cart-icon");
	public static By proceedToCart = By.xpath("//button[text()='PROCEED TO CHECKOUT']");
	public static By productsInCart = By.xpath("//p[@class='product-name']/font/font");
	public static By discountAmount = By.cssSelector("span.discountAmt");
	public static By placeOrderbutton = By.xpath("//button[text()='Place Order']");
	public static By selectCountry = By.tagName("select");
	public static By checkAgreeCheckBox = By.cssSelector("input.chkAgree");
	public static By proceedButton = By.xpath("//button[text()='Proceed']");
	public static By message = By.xpath("//div[@class='products']/div/span");
	public static By successMessage = By.xpath("//div[@class='products']/div/span[contains(text(),'success')]");
	public static By productsInCartTable = By.cssSelector("table tbody tr td:nth-child(2) p");
	public static By amountInCartTable = By.cssSelector("table tbody tr td:nth-child(5) p");
	public static By totalAmount=By.cssSelector("div.products>div span.totAmt");
	
	//GreenKart2 
	public static By topDeals=By.linkText("Top Deals");
	public static By productsInDeals=By.cssSelector("table.table.table-bordered tbody tr td:nth-child(1)");
	public static By productsActualPrice=By.cssSelector("table.table.table-bordered tbody tr td:nth-child(2)");
	public static By productsDealPrice=By.cssSelector("table.table.table-bordered tbody tr td:nth-child(3)");
	public static By increaseProductQuantity=By.cssSelector("a.increment");
	public static By quantity=By.cssSelector("p.quantity");
	public static By amount=By.xpath("(//p[@class='amount'])[1]");
	public static By total=By.xpath("(//p[@class='amount'])[2]");
	
	
}
