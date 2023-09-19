Feature: Check the Search option in Udemy application
 

  @Udemy
  Scenario Outline: 123_Search for a course and add it to cart
    Given Read the testdata "<TestCase>" from the excel file
    When Navigate to the udemy website with url
    And Login in to application
    And Search for a course
    And Add the course into cart
    Then I verify the required course is added into a cart
    And Logout from the application

    Examples: 
      | TestCase  |
      | TestCase1 |
      
     
