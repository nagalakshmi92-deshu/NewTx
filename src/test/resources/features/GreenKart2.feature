Feature: Check the GreenKart Application

  @GreenKart
  Scenario Outline: Verify the top deals prices
    Given Read the testdata "<TestCase>" "<SheetName>" from the excel data file
    When Navigate to url
    And Click on the Top Deals
    Then Verify the prices of the products with discount prices
     And "scope" in response body is "APP"

    Examples: 
      | TestCase  | SheetName |
      | TestCase4 | GreenKart |

  @GreenKart
  Scenario Outline: Verify the price of product, when we took multiple kgs
    Given Read the testdata "<TestCase>" "<SheetName>" from the excel data file
    When Navigate to url
    And Select one product, pick atleast two kgs and add it to cart
    Then Verify the total price with mulitple of the product

    Examples: 
      | TestCase  | SheetName |
      | TestCase5 | GreenKart |
