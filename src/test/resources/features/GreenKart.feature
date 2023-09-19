Feature: Add products into the Cart

  @GreenKart
  Scenario Outline: Verify the selected products price
    Given Read the testdata "<TestCase>" "<SheetName>" from the excel data file
    When Navigate to url
    And Search the product, add it to cart and click on ProceedToCheckout
    Then Verify sum of indivial and total sum of products

    Examples: 
      | TestCase  | SheetName |
      | TestCase1 | GreenKart |

  @GreenKar
  Scenario Outline: Verify the selected products
    Given Read the testdata "<TestCase>" "<SheetName>" from the excel data file
    When Navigate to url
    And Search the product, add it to cart and click on ProceedToCheckout
    Then Verify selected products are present in Cart

    Examples: 
      | TestCase  | SheetName |
      | TestCase2 | GreenKart |

  @GreenKar
  Scenario Outline: Verify success message for the products
    Given Read the testdata "<TestCase>" "<SheetName>" from the excel data file
    When Navigate to url
    And Search the product, add it to cart and click on ProceedToCheckout
    And Select the country and click on proceed button
    Then Verify the success message

    Examples: 
      | TestCase  | SheetName |
      | TestCase3 | GreenKart |

    
  