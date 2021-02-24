Feature: AlterModelPlugin execution

  Background:
    Given an instance of AlterModelPlugin
    And the AlterModelPlugin table name is set to table_name
    And the AlterModelPlugin interfaces are set to "java.io.Serializable"
    And the validate method has been called

  Scenario: Should not modify the model base record class if the table does not match
    Given the introspected table is for wrong_name
    When the modelBaseRecordClassGenerated method is called
    Then modelBaseRecordClassGenerated result is true
    And the topLevelClass addImportedType will have been called 0 times
    And the topLevelClass addSuperInterface will have been called 0 times

  Scenario: Should add interfaces to the model base record class when the table name matches
    Given the introspected table is for table_name
    When the modelBaseRecordClassGenerated method is called
    Then modelBaseRecordClassGenerated result is true
    And the topLevelClass addImportedType will have been called 1 times
    And the topLevelClass addSuperInterface will have been called 1 times
    And the topLevelClass addImportedType is "java.io.Serializable"
    And the topLevelClass addSuperInterface is "java.io.Serializable"

	Scenario: Should accept regex value for table name
		Given the AlterModelPlugin table name is set to reg.*
		And the validate method has been called
		And the introspected table is for regex_table
    When the modelBaseRecordClassGenerated method is called
    Then modelBaseRecordClassGenerated result is true
    And the topLevelClass addImportedType will have been called 1 times
    And the topLevelClass addSuperInterface will have been called 1 times
    And the topLevelClass addImportedType is "java.io.Serializable"
    And the topLevelClass addSuperInterface is "java.io.Serializable"
