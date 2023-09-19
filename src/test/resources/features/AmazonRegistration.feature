Feature: Create an account for new user

  @Amazon
  Scenario Outline: 18883_Searching a product and add to cart for Valid Login Credentials
    Given Read the test data  "<TestData>" from Excel file
    When Navigate to the url
    And Search for an item
    And Add the item into cart
    Then Verify item is added to cart
    And Navigate to home Page

    Examples: 
      | TestData  |
      | TestData3 |

  @Amazon
  Scenario Outline: 18884_Searching a product and Add to cart for Invalid Login Credentials
    Given Read the test data  "<TestData>" from Excel file
    When Navigate to the url
    And Click on Account and Lists
    And Enter the login credentials
    And Search for an item
    And Add the item into cart
    Then Verify item is added to cart
    And Navigate to home Page

    Examples: 
      | TestData  |
      | TestData2 |
