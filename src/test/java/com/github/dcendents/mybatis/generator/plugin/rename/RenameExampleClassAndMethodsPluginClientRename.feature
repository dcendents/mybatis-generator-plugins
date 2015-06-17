Feature: RenameExampleClassAndMethodsPlugin execution

  Background:
    Given the class search is properly configured
      And the class replace is properly configured
      And the param search is properly configured
      And the param replace is properly configured
      And the validate method has been called
      And the introspected table will return the table configuration
      And the introspected table will return a list of primary keys
      And the method will return a list of parameters
      And the element will return a list of attributes
      And the element will return a list of elements
      And the method name matches the class search parameter


  Scenario: Should rename method of ClientCountByExample for interface
    When the clientCountByExampleMethodGenerated method for interface is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientDeleteByExample for interface
    When the clientDeleteByExampleMethodGenerated method for interface is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientDeleteByPrimaryKey for interface
    When the clientDeleteByPrimaryKeyMethodGenerated method for interface is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientSelectByExampleWithBLOBs for interface
    When the clientSelectByExampleWithBLOBsMethodGenerated method for interface is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientSelectByExampleWithoutBLOBs for interface
    When the clientSelectByExampleWithoutBLOBsMethodGenerated method for interface is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientUpdateByExampleSelective for interface
    When the clientUpdateByExampleSelectiveMethodGenerated method for interface is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientUpdateByExampleWithBLOBs for interface
    When the clientUpdateByExampleWithBLOBsMethodGenerated method for interface is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientUpdateByExampleWithoutBLOBs for interface
    When the clientUpdateByExampleWithoutBLOBsMethodGenerated method for interface is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientCountByExample for table
    When the clientCountByExampleMethodGenerated method for table is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientDeleteByExample for table
    When the clientDeleteByExampleMethodGenerated method for table is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientDeleteByPrimaryKey for table
    When the clientDeleteByPrimaryKeyMethodGenerated method for table is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientSelectByExampleWithBLOBs for table
    When the clientSelectByExampleWithBLOBsMethodGenerated method for table is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientSelectByExampleWithoutBLOBs for table
    When the clientSelectByExampleWithoutBLOBsMethodGenerated method for table is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientUpdateByExampleSelective for table
    When the clientUpdateByExampleSelectiveMethodGenerated method for table is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientUpdateByExampleWithBLOBs for table
    When the clientUpdateByExampleWithBLOBsMethodGenerated method for table is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename method of ClientUpdateByExampleWithoutBLOBs for table
    When the clientUpdateByExampleWithoutBLOBsMethodGenerated method for table is called
    Then the method will return true
     And the method name will be renamed


