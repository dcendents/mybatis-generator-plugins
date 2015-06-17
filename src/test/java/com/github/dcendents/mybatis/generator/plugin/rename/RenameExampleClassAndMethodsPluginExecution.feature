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


  Scenario: Should rename the example type
    Given the example type of the introspected table matches the class search parameter
    When the initialized method is called
    Then the example type of the introspected table will be renamed


  Scenario: Should rename the method name
    Given the method name matches the class search parameter
    When the renameMethod method is called
    Then the method will return true
     And the method name will be renamed


  Scenario: Should rename the method parameters and keep annotations
    Given the method name matches the class search parameter
      And the method has a parameter matching the param search
    When the renameMethod method is called
    Then the method will return true
     And the method name will be renamed
     And the method parameters size will be 1
     And the parameter at position 0 will not be the same
     And the type of the parameter at position 0 will be the same
     And the name of the parameter at position 0 will be renamed
     And the annotations of the parameter at position 0 will be identical


  Scenario: Should keep other method parameters as is
    Given the method name matches the class search parameter
      And the method has a parameter not matching the param search
    When the renameMethod method is called
    Then the method will return true
     And the method name will be renamed
     And the method parameters size will be 1
     And the parameter at position 0 will be the same


  Scenario: Should handle elements without attributes
    When the renameElement method is called
    Then the method will return true
     And the element attributes size will be 0


  Scenario: Should ignore other element attributes
    Given the element has some other attribute
    When the renameElement method is called
    Then the method will return true
     And the element attributes size will be 1
     And the attribute at position 0 will be the same


  Scenario: Should rename element id
    Given the element has an attribute matching the class search
    When the renameElement method is called
    Then the method will return true
     And the element attributes size will be 1
     And the attribute at position 0 will not be the same
     And the attribute name at position 0 will be the same
     And the attribute value at position 0 will be renamed


  Scenario: should handle empty id column list on removeIdColumns
    Given the removeIdColumns element is textElement
    When the removeIdColumnsForList method is called
    Then the method getElements of element will have been called 0 times


  Scenario: should handle a text element that does not match any id column with type
    Given the removeIdColumns element is textElement
      And an id column with alias:alias, name:actual_name, property:someProperty, type:DOUBLE
      And the text element content does not contain the id column
    When the removeIdColumnsForList method is called
    Then the method getElements of element will have been called 0 times


  Scenario: should remove id column from a text element
    Given the removeIdColumns element is textElement
      And an id column with alias:alias, name:actual_name, property:someProperty, type:DOUBLE
      And the text element content contains the id column at position 0
      And the elements list contains the text element
      And the removeIdColumnsIndex is 0
    When the removeIdColumnsForList method is called
    Then the method getElements of element will have been called 1 times
     And the element elements size will be 1
     And the element at position 0 is not the same as the text element
     And the element at position 0 is a text element
     And the content of element at position 0 does not contain the id column


  Scenario: should handle element without children in removeIdColumns
    Given the removeIdColumns element is element
    When the removeIdColumnsForList method is called
    Then the method getElements of element will have been called 1 times
     And the element elements size will be 0


  Scenario: should process all children of an xml element
    Given the removeIdColumns element is element
      And an id column with alias:alias, name:actual_name, property:someProperty, type:DOUBLE
      And the text element content contains the id column at position 0
      And the elements list contains the text element
      And the removeIdColumnsIndex is 0
    When the removeIdColumnsForList method is called
    Then the element elements size will be 1
     And the element at position 0 is not the same as the text element
     And the element at position 0 is a text element
     And the content of element at position 0 does not contain the id column


  Scenario: should ignore other element types in removeIdColumns
    Given the removeIdColumns element is a mock
    When the removeIdColumnsForList method is called
    Then the method getElements of element will have been called 0 times


  Scenario: should ignore tables without id columns on removeIdColumns
    When the removeIdColumnsForTable method is called
    Then the method getElements of element will have been called 0 times


  Scenario: should remove table id columns from update statements
    Given a primary key column with name:actual_name, property:someProperty, type:DOUBLE
      And the table configuration alias is alias
      And an id column with alias:alias, name:actual_name, property:someProperty, type:DOUBLE
      And the text element content contains the id column at position 0
      And the elements list contains the text element
    When the removeIdColumnsForTable method is called
    Then the element elements size will be 1
     And the element at position 0 is not the same as the text element
     And the element at position 0 is a text element
     And the content of element at position 0 does not contain the id column

  
