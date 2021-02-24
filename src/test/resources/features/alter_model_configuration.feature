Feature: AlterModelPlugin configuration


	Background:
		Given an empty warnings list

  Scenario: Should be invalid without any property configured
    When the validate method is called
    Then validate should return false
    And validate should have produced 2 warnings

  
  Scenario: Should be invalid with only the table name configured
    Given the table name is set to table_name
    When the validate method is called
    Then validate should return false
    And validate should have produced 1 warnings

  
  Scenario: Should be invalid with only the interfaces configured
    Given the interfaces are set to "java.io.Serializable"
    When the validate method is called
    Then validate should return false
    And validate should have produced 1 warnings


  Scenario: Should be valid when both properties are configured
    Given the table name is set to table_name
		And the interfaces are set to "java.io.Serializable"
    When the validate method is called
    Then validate should return true
  	And validate should have produced 0 warnings

 