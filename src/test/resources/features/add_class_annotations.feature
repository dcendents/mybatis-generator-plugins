Feature: AddClassAnnotationsPlugin

  Background:
    Given an instance of AddClassAnnotationsPlugin

  Scenario: Dummy test for better maven surefire output

  Scenario Outline: Configuration with <class>, <annotation>
    Given the AddClassAnnotationsPlugin class is set to "<class>"
		And the AddClassAnnotationsPlugin annotation is set to <annotation>
    When the validate method is called
    Then validate result is <result>
  	And validate should have produced <warnings> warnings
 
    Examples:
    	| class | annotation | result | warnings |
    	| null | null | false | 2 |
    	| org.junit.test | null | false | 1 |
    	| null | @Test | false | 1 |
    	| org.junit.test | @Test | true | 0 |

  Scenario: Should add the annotation
    Given the AddClassAnnotationsPlugin class is set to "org.junit.test"
    And the AddClassAnnotationsPlugin annotation is set to @Test
    When the validate method is called
    And the modelBaseRecordClassGenerated method is called
    Then modelBaseRecordClassGenerated result is true
    And the annotation class "org.junit.test" has been imported
    And the annotation @Test has been added


