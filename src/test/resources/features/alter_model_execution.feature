Feature: AlterModelPlugin execution


  Background:
    Given the table name is set to table_name
    And the interfaces are set to "java.io.Serializable"
    And an empty warnings list
    And the validate method has been called
    And a mock for IntrospectedTable
    And a mock for TopLevelClass
    And a type captor for FullyQualifiedJavaType


  Scenario: Should not modify the model base record class if the table does not match
    Given the introspected table is for wrong_name
    When the modelBaseRecordClassGenerated method is called
    Then the execution result will be true
    And the addImportedType method of topLevelClass will have been called 0 times
    And the addSuperInterface method of topLevelClass will have been called 0 times

  Scenario: Should add interfaces to the model base record class when the table name matches
    Given the introspected table is for table_name
    When the modelBaseRecordClassGenerated method is called
    Then the execution result will be true
    And the addImportedType method of topLevelClass will have been called 1 times
    And the addSuperInterface method of topLevelClass will have been called 1 times
    And the imported type is not null
    And the interface type is not null
    And the imported and interface types are the same
    And the imported type is "java.io.Serializable"

	Scenario: Should accept regex value for table name
		Given the table name is set to reg.*
		And the validate method has been called
		And the introspected table is for regex_table
    When the modelBaseRecordClassGenerated method is called
    Then the execution result will be true
    And the addImportedType method of topLevelClass will have been called 1 times
    And the addSuperInterface method of topLevelClass will have been called 1 times
    And the imported type is not null
    And the interface type is not null
    And the imported and interface types are the same
    And the imported type is "java.io.Serializable"
