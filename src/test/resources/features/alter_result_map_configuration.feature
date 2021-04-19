Feature: AlterResultMapPlugin configuration

  Background:
    Given an instance of AlterResultMapPlugin

  Scenario: Dummy test for better maven surefire output

  Scenario Outline: Configuration with table_name and result_map_id
    Given the AlterResultMapPlugin table name is set to <table_name>
		And the AlterResultMapPlugin result map id is set to <result_map_id>
    When the validate method is called
    Then validate result is <result>
  	And validate should have produced <warnings> warnings
 
    Examples:
    	| table_name | result_map_id | result | warnings |
    	| null | null | false | 2 |
    	| table_name | null | false | 1 |
    	| null | FullResultMap | false | 1 |
    	| table_name | FullResultMap | true | 0 |
