Feature: RenameExampleClassAndMethodsPlugin configuration

  Scenario: Should be invalid without any property configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 4 warnings


  Scenario: Should be invalid when only the class search is set
    Given the class search is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 3 warnings


  Scenario: Should be invalid when only the class replace is set
    Given the class replace is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 3 warnings


  Scenario: Should be invalid when only the param search is set
    Given the param search is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 3 warnings


  Scenario: Should be invalid when only the param replace is set
    Given the param replace is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 3 warnings


  Scenario: Should be invalid when only the class search and replace are set
    Given the class search is properly configured
      And the class replace is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 2 warnings


  Scenario: Should be invalid when only the class search and param search are set
    Given the class search is properly configured
      And the param search is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 2 warnings


  Scenario: Should be invalid when only the class search and param replace are set
    Given the class search is properly configured
      And the param replace is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 2 warnings


  Scenario: Should be invalid when only the class replace and param search are set
    Given the class replace is properly configured
      And the param search is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 2 warnings


  Scenario: Should be invalid when only the class replace and param replace are set
    Given the class replace is properly configured
      And the param replace is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 2 warnings


  Scenario: Should be invalid when only the param search and replace are set
    Given the param search is properly configured
      And the param replace is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 2 warnings


  Scenario: Should be invalid when the class search is not set
    Given the class replace is properly configured
      And the param search is properly configured
      And the param replace is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 1 warnings


  Scenario: Should be invalid when the class replace is not set
    Given the class search is properly configured
      And the param search is properly configured
      And the param replace is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 1 warnings


  Scenario: Should be invalid when the param search is not set
    Given the class search is properly configured
      And the class replace is properly configured
      And the param replace is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 1 warnings


  Scenario: Should be invalid when the param replace is not set
    Given the class search is properly configured
      And the class replace is properly configured
      And the param search is properly configured
    When the validate method is called
    Then validate should return false
      And validate should have produced 1 warnings


  Scenario: Should be valid when all four parameters are set
    Given the class search is properly configured
      And the class replace is properly configured
      And the param search is properly configured
      And the param replace is properly configured
    When the validate method is called
    Then validate should return true
      And validate should have produced 0 warnings


