Feature: RenameExampleClassAndMethodsPlugin configuration

  Background:
    Given an instance of RenameExampleClassAndMethodsPlugin

  Scenario: Dummy test for better maven surefire output

  Scenario Outline: Configuration with <class_search>, <class_replace>, <param_search>, <param_replace>
    Given the RenameExampleClassAndMethodsPlugin class search is set to <class_search>
    And the RenameExampleClassAndMethodsPlugin class replace is set to <class_replace>
    And the RenameExampleClassAndMethodsPlugin param search is set to <param_search>
    And the RenameExampleClassAndMethodsPlugin param replace is set to <param_replace>
    When the validate method is called
    Then validate result is <result>
    And validate should have produced <warnings> warnings
 
    Examples:
    	| class_search | class_replace | param_search | param_replace | result | warnings |
    	| null | null | null | null | false | 4 |
    	| Example | null | null | null | false | 3 |
    	| null | Filter | null | null | false | 3 |
    	| null | null | example | null | false | 3 |
    	| null | null | null | filter | false | 3 |
    	| Example | Filter | null | null | false | 2 |
    	| Example | null | example | null | false | 2 |
    	| Example | null | null | filter | false | 2 |
    	| null | Filter | example | null | false | 2 |
    	| null | Filter | null | filter | false | 2 |
    	| null | null | example | filter | false | 2 |
    	| Example | Filter | example | null | false | 1 |
    	| Example | Filter | null | filter | false | 1 |
    	| Example | null | example | filter | false | 1 |
    	| null | Filter | example | filter | false | 1 |
    	| Example | Filter | example | filter | true | 0 |
