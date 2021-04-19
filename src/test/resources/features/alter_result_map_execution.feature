Feature: AlterResultMapPlugin execution

  Background:
    Given an instance of AlterResultMapPlugin
    And the AlterResultMapPlugin table name is set to table_name
    And the AlterResultMapPlugin result map id is set to FullResultMap
    And the validate method has been called

  Scenario: Dummy test for better maven surefire output

  Scenario: Should not modify the result map attribute if the table does not match
    Given the xml element attribute AlterResultMapPlugin result map is BaseResultMap
    And the introspected table wrong_table
    When the renameResultMapAttribute is called with the xml element
    Then the xml element has 1 attribute
    And the xml element attribute 0 name is the AlterResultMapPlugin result map attribute
    And the xml element attribute 0 value is BaseResultMap
  
  Scenario: Should modify the result map attribute when the table matches
    Given the xml element attribute random_name is random_value
    And the xml element attribute AlterResultMapPlugin result map is BaseResultMap
    And the introspected table table_name
    When the renameResultMapAttribute is called with the xml element
    Then the xml element has 2 attributes
    And the xml element attribute 0 name is random_name
    And the xml element attribute 0 value is random_value
    And the xml element attribute 1 name is the AlterResultMapPlugin result map attribute
    And the xml element attribute 1 value is FullResultMap
  
  Scenario: Should handle an element with an empty attribute list
  	Given the introspected table table_name
    When the renameResultMapAttribute is called with the xml element
    Then the xml element has 0 attributes
  
  Scenario: Should not modify the result map annotation if the table does not match
    Given the method annotation AlterResultMapPlugin result map is BaseResultMap
    And the introspected table wrong_table
    When the renameResultMapAttribute is called with the method
    Then the method has 1 annotation
    And the method annotation 0 is the AlterResultMapPlugin result map annotation with value BaseResultMap
  
  Scenario: Should modify the result map annotation when the table matches
    Given the method has the annotation @randomAnnotation
    And the method annotation AlterResultMapPlugin result map is BaseResultMap
    And the introspected table table_name
    When the renameResultMapAttribute is called with the method
    Then the method has 2 annotations
    And the method annotation 0 is @randomAnnotation
    And the method annotation 1 is the AlterResultMapPlugin result map annotation with value FullResultMap
  
  Scenario: Should handle a method with an empty annotation list
  	Given the introspected table table_name
    When the renameResultMapAttribute is called with the method
    Then the method has 0 annotations

  Scenario Outline: Should modify the result map attribute of method for xml element
    Given the xml element attribute AlterResultMapPlugin result map is BaseResultMap
    And the introspected table table_name
    When the <method> method is called with the <element>
    Then <method> result is true
    And the xml element has 1 attribute
    And the xml element attribute 0 name is the AlterResultMapPlugin result map attribute
    And the xml element attribute 0 value is FullResultMap

		Examples:
			| method | element |
			| SelectByExampleWithoutBLOBs | xml element |
			| SelectByExampleWithBLOBs | xml element |
			| SelectByPrimaryKey | xml element |
			| SelectAll | xml element |
  
  Scenario Outline: Should modify the result map attribute of method for interface or class
    Given the method annotation AlterResultMapPlugin result map is BaseResultMap
    And the introspected table table_name
    When the <method> method is called with the <element>
    Then <method> result is true
    And the method has 1 annotation
    And the method annotation 0 is the AlterResultMapPlugin result map annotation with value FullResultMap

		Examples:
			| method | element |
			| SelectByExampleWithoutBLOBs | interface |
			| SelectByExampleWithBLOBs | interface |
			| SelectByPrimaryKey | interface |
			| SelectAll | interface |
			| SelectByExampleWithoutBLOBs | class |
			| SelectByExampleWithBLOBs | class |
			| SelectByPrimaryKey | class |
			| SelectAll | class |
  