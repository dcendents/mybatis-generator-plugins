Feature: AlterResultMapPlugin  configuration

  Scenario: Should be invalid without any property configured
    When the validate method is called
    Then validate should return false
     And validate should have produced 2 warnings
  
  
  Scenario: Should be invalid with only the table name configured
    Given the table name is properly configured
    When the validate method is called
    Then validate should return false
     And validate should have produced 1 warnings

  
  Scenario: Should be invalid with only the interfaces configured
    Given the result map id is properly configured
    When the validate method is called
    Then validate should return false
     And validate should have produced 1 warnings


  Scenario: Should be valid when both properties are configured
    Given the table name is properly configured
      And the result map id is properly configured
    When the validate method is called
    Then validate should return true
     And validate should have produced 0 warnings

  
