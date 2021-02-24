Feature: AddClassAnnotationsPlugin

  Background:
    Given an instance of AddClassAnnotationsPlugin

  Scenario: Should be invalid without any property configured
    When the validate method is called
    Then validate result is false
    And validate should have produced 2 warnings

  
  Scenario: Should be invalid with only the class configured
    Given the AddClassAnnotationsPlugin class is set to "org.junit.test"
    When the validate method is called
    Then validate result is false
    And validate should have produced 1 warnings


  Scenario: Should be invalid with only the annotation configured
    Given the AddClassAnnotationsPlugin annotation is set to @Test
    When the validate method is called
    Then validate result is false
    And validate should have produced 1 warnings


  Scenario: Should be valid when both properties are configured
    Given the AddClassAnnotationsPlugin class is set to "org.junit.test"
    And the AddClassAnnotationsPlugin annotation is set to @Test
    When the validate method is called
    Then validate result is true
    And validate should have produced 0 warnings


  Scenario: Should add the annotation
    Given the AddClassAnnotationsPlugin class is set to "org.junit.test"
    And the AddClassAnnotationsPlugin annotation is set to @Test
    When the validate method is called
    And the modelBaseRecordClassGenerated method is called
    Then modelBaseRecordClassGenerated result is true
    And the annotation class "org.junit.test" has been imported
    And the annotation @Test has been added


