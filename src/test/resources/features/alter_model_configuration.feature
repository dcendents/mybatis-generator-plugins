Feature: AlterModelPlugin configuration

  Background:
    Given an instance of AlterModelPlugin

  Scenario Outline: Configuration with <table_name>, <interfaces>
    Given the AlterModelPlugin table name is set to <table_name>
		And the AlterModelPlugin interfaces are set to "<interfaces>"
    When the validate method is called
    Then validate result is <result>
  	And validate should have produced <warnings> warnings
 
    Examples:
    	| table_name | interfaces | result | warnings |
    	| null | null | false | 2 |
    	| table_name | null | false | 1 |
    	| null | java.io.Serializable | false | 1 |
    	| table_name | java.io.Serializable | true | 0 |
    	