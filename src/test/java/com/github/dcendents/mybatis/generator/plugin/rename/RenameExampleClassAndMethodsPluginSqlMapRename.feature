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
      And a primary key column with name:actual_name, property:someProperty, type:DOUBLE
      And the table configuration alias is alias
      And an id column with alias:alias, name:actual_name, property:someProperty, type:DOUBLE
      And the text element content contains the id column at position 0
      And the elements list contains the text element
      And the element has an attribute matching the class search


  Scenario: Should rename element of sqlMapCountByExample
    When the sqlMapCountByExampleElementGenerated method is called
    Then the method will return true
     And the element attributes size will be 1
     And the attribute at position 0 will not be the same
     And the attribute name at position 0 will be the same
     And the attribute value at position 0 will be renamed


  Scenario: Should rename element of sqlMapDeleteByExample
    When the sqlMapDeleteByExampleElementGenerated method is called
    Then the method will return true
     And the element attributes size will be 1
     And the attribute at position 0 will not be the same
     And the attribute name at position 0 will be the same
     And the attribute value at position 0 will be renamed


  Scenario: Should rename element of sqlMapSelectByExampleWithoutBLOBs
    When the sqlMapSelectByExampleWithoutBLOBsElementGenerated method is called
    Then the method will return true
     And the element attributes size will be 1
     And the attribute at position 0 will not be the same
     And the attribute name at position 0 will be the same
     And the attribute value at position 0 will be renamed


  Scenario: Should rename element of sqlMapSelectByExampleWithBLOBs
    When the sqlMapSelectByExampleWithBLOBsElementGenerated method is called
    Then the method will return true
     And the element attributes size will be 1
     And the attribute at position 0 will not be the same
     And the attribute name at position 0 will be the same
     And the attribute value at position 0 will be renamed


  Scenario: Should rename element of sqlMapUpdateByExampleSelective
    When the sqlMapUpdateByExampleSelectiveElementGenerated method is called
    Then the method will return true
     And the element attributes size will be 1
     And the attribute at position 0 will not be the same
     And the attribute name at position 0 will be the same
     And the attribute value at position 0 will be renamed
     And the element elements size will be 1
     And the element at position 0 is not the same as the text element
     And the element at position 0 is a text element
     And the content of element at position 0 does not contain the id column


  Scenario: Should rename element of sqlMapUpdateByExampleWithBLOBs
    When the sqlMapUpdateByExampleWithBLOBsElementGenerated method is called
    Then the method will return true
     And the element attributes size will be 1
     And the attribute at position 0 will not be the same
     And the attribute name at position 0 will be the same
     And the attribute value at position 0 will be renamed
     And the element elements size will be 1
     And the element at position 0 is not the same as the text element
     And the element at position 0 is a text element
     And the content of element at position 0 does not contain the id column


  Scenario: Should rename element of sqlMapUpdateByExampleWithoutBLOBs
    When the sqlMapUpdateByExampleWithoutBLOBsElementGenerated method is called
    Then the method will return true
     And the element attributes size will be 1
     And the attribute at position 0 will not be the same
     And the attribute name at position 0 will be the same
     And the attribute value at position 0 will be renamed
     And the element elements size will be 1
     And the element at position 0 is not the same as the text element
     And the element at position 0 is a text element
     And the content of element at position 0 does not contain the id column


