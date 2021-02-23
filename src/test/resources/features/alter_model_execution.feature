Feature: AlterModelPlugin execution


  Background:
    Given the table name is properly configured
    And the interfaces are properly configured
    And the validate method has been called


  Scenario: Should not modify the model base record class if the table does not match
    Given the introspected table is a different table
    When the modelBaseRecordClassGenerated method is called
    Then the execution result will be true
    And the addImportedType method of topLevelClass will have been called 0 times
    And the addSuperInterface method of topLevelClass will have been called 0 times

  
  Scenario: Should add interfaces to the model base record class when the table name matches
    Given the introspected table is the right table
    When the modelBaseRecordClassGenerated method is called
    Then the execution result will be true
    And the addImportedType method of topLevelClass will have been called 1 times
    And the addSuperInterface method of topLevelClass will have been called 1 times
    And the imported type is not null
    And the interface type is not null
    And the imported and interface types are the same
    And the imported type matches the configured interface

