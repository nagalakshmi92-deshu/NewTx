package modules;

import java.util.HashMap;

import pages.AccountCreationPage;
import pages.LoginPage;
import pages.ProductListPage;
import utilities.KeywordUtil;

public class AmazonRegistration {

	static boolean flag;

	public static void fillInTheMandatoryFields(HashMap<String, String> dataMap) throws Exception {

		KeywordUtil.inputText(AccountCreationPage.username, dataMap.get("Username"), "enter username");
		KeywordUtil.inputText(AccountCreationPage.emailID, dataMap.get("Email"), "Enter email");
		KeywordUtil.inputText(AccountCreationPage.password, dataMap.get("Password"), "Enter password");
		KeywordUtil.inputText(AccountCreationPage.passwordCheck, dataMap.get("Password"), "Repeat password");

	}

	public static void login(HashMap<String, String> dataMap) throws Exception {

		KeywordUtil.inputText(AccountCreationPage.emailID, dataMap.get("Email"), "Enter the username");
		KeywordUtil.click(LoginPage.continueButton, "Click on continue button");
		KeywordUtil.inputText(AccountCreationPage.password, dataMap.get("Password"), "Enter the password");
		KeywordUtil.click(LoginPage.signInButton, "Click on Sign on Button");
	}

	public static void searchForAnItem(HashMap<String, String> dataMap) throws Exception {
		KeywordUtil.inputText(ProductListPage.searchText, dataMap.get("SearchText"), "Enter the search text");
		KeywordUtil.delay(1000);
		KeywordUtil.pressEnter(ProductListPage.searchText);
		KeywordUtil.click(ProductListPage.bookName, "Select the Book");
		KeywordUtil.delay(2000);

	}

}
