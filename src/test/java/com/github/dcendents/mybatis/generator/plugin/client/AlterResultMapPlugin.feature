Feature: AlterResultMapPlugin

  Scenario Outline: Should be invalid without any property configured
    When the validate method is called
    Then validate should return <validate>
     And validate should have produced <warnings> warnings
  
    Examples:
      | validate | warnings |
      | false    | 2        |

  
  Scenario Outline: Should be invalid with only the table name configured
    Given the table name is properly configured
    When the validate method is called
    Then validate should return <validate>
     And validate should have produced <warnings> warnings
  
    Examples:
      | validate | warnings |
      | false    | 1        |

  
  Scenario Outline: Should be invalid with only the interfaces configured
    Given the result map id is properly configured
    When the validate method is called
    Then validate should return <validate>
     And validate should have produced <warnings> warnings
  
    Examples:
      | validate | warnings |
      | false    | 1        |


  Scenario Outline: Should be valid when both properties are configured
    Given the table name is properly configured
      And the result map id is properly configured
    When the validate method is called
    Then validate should return <validate>
     And validate should have produced <warnings> warnings
  
    Examples:
      | validate | warnings |
      | true     | 0        |

  
  Scenario Outline: Should not modify the result map attribute if the table does not match
    Given the table name is properly configured
      And the result map id is properly configured
      And the element has a result map attribute
      But the introspected table is a different table
    When the validate method is called
     And the renameResultMapAttribute for element is called
    Then the element attributes size will be <size>
     And the result map attribute's name at position <position> won't have changed
     And the result map attribute's value at position <position> won't have changed
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  Scenario Outline: Should modify the result map attribute when the table matches
    Given the table name is properly configured
      And the result map id is properly configured
      And the element has a random attribute
      And the element has a result map attribute
      And the introspected table is the right table
    When the validate method is called
     And the renameResultMapAttribute for element is called
    Then the element attributes size will be <size>
     And the result map attribute's name at position <position> won't have changed
     And the result map attribute's value at position <position> will have been modified
  
    Examples:
      | size | position |
      | 2    | 1        |

  
  Scenario Outline: Should handle an element with an empty attribute list
    Given the table name is properly configured
      And the result map id is properly configured
      And the introspected table is the right table
    When the validate method is called
     And the renameResultMapAttribute for element is called
    Then the element attributes size will be <size>
  
    Examples:
      | size |
      | 0    |

  
  Scenario Outline: Should not modify the result map annotation if the table does not match
    Given the table name is properly configured
      And the result map id is properly configured
      And the method has a result map annotation
      But the introspected table is a different table
    When the validate method is called
     And the renameResultMapAttribute for method is called
    Then the method annotations size will be <size>
     And the annotation at position <position> won't have changed
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  Scenario Outline: Should modify the result map annotation when the table matches
    Given the table name is properly configured
      And the result map id is properly configured
      And the method has a random annotation
      And the method has a result map annotation
      And the introspected table is the right table
    When the validate method is called
     And the renameResultMapAttribute for method is called
    Then the method annotations size will be <size>
     And the annotation at position <position> will have been modified
  
    Examples:
      | size | position |
      | 2    | 1        |

  
  Scenario Outline: Should handle a method with an empty annotation list
    Given the table name is properly configured
      And the result map id is properly configured
      And the introspected table is the right table
    When the validate method is called
     And the renameResultMapAttribute for method is called
    Then the method annotations size will be <size>
  
    Examples:
      | size |
      | 0    |

  
  Scenario Outline: Should modify the result map attribute of method SelectByExampleWithoutBLOBs for element
    Given the table name is properly configured
      And the result map id is properly configured
      And the element has a result map attribute
      And the introspected table is the right table
    When the validate method is called
     And the SelectByExampleWithoutBLOBs method for element is called
    Then the generated method return value will be true
     And the element attributes size will be <size>
     And the result map attribute's name at position <position> won't have changed
     And the result map attribute's value at position <position> will have been modified
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  Scenario Outline: Should modify the result map attribute of method SelectByExampleWithBLOBs for element
    Given the table name is properly configured
      And the result map id is properly configured
      And the element has a result map attribute
      And the introspected table is the right table
    When the validate method is called
     And the SelectByExampleWithBLOBs method for element is called
    Then the generated method return value will be true
     And the element attributes size will be <size>
     And the result map attribute's name at position <position> won't have changed
     And the result map attribute's value at position <position> will have been modified
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  Scenario Outline: Should modify the result map attribute of method SelectByPrimaryKey for element
    Given the table name is properly configured
      And the result map id is properly configured
      And the element has a result map attribute
      And the introspected table is the right table
    When the validate method is called
     And the SelectByPrimaryKey method for element is called
    Then the generated method return value will be true
     And the element attributes size will be <size>
     And the result map attribute's name at position <position> won't have changed
     And the result map attribute's value at position <position> will have been modified
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  Scenario Outline: Should modify the result map attribute of method SelectAll for element
    Given the table name is properly configured
      And the result map id is properly configured
      And the element has a result map attribute
      And the introspected table is the right table
    When the validate method is called
     And the SelectAll method for element is called
    Then the generated method return value will be true
     And the element attributes size will be <size>
     And the result map attribute's name at position <position> won't have changed
     And the result map attribute's value at position <position> will have been modified
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  Scenario Outline: Should modify the result map annotation of method SelectByExampleWithBLOBs for interface
    Given the table name is properly configured
      And the result map id is properly configured
      And the method has a result map annotation
      And the introspected table is the right table
    When the validate method is called
     And the SelectByExampleWithBLOBs method for interface is called
    Then the generated method return value will be true
     And the method annotations size will be <size>
     And the annotation at position <position> will have been modified
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  Scenario Outline: Should modify the result map annotation of method SelectByExampleWithoutBLOBs for interface
    Given the table name is properly configured
      And the result map id is properly configured
      And the method has a result map annotation
      And the introspected table is the right table
    When the validate method is called
     And the SelectByExampleWithoutBLOBs method for interface is called
    Then the generated method return value will be true
     And the method annotations size will be <size>
     And the annotation at position <position> will have been modified
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  Scenario Outline: Should modify the result map annotation of method SelectByPrimaryKey for interface
    Given the table name is properly configured
      And the result map id is properly configured
      And the method has a result map annotation
      And the introspected table is the right table
    When the validate method is called
     And the SelectByPrimaryKey method for interface is called
    Then the generated method return value will be true
     And the method annotations size will be <size>
     And the annotation at position <position> will have been modified
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  Scenario Outline: Should modify the result map annotation of method SelectAll for interface
    Given the table name is properly configured
      And the result map id is properly configured
      And the method has a result map annotation
      And the introspected table is the right table
    When the validate method is called
     And the SelectAll method for interface is called
    Then the generated method return value will be true
     And the method annotations size will be <size>
     And the annotation at position <position> will have been modified
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  Scenario Outline: Should modify the result map annotation of method SelectByExampleWithBLOBs for class
    Given the table name is properly configured
      And the result map id is properly configured
      And the method has a result map annotation
      And the introspected table is the right table
    When the validate method is called
     And the SelectByExampleWithBLOBs method for class is called
    Then the generated method return value will be true
     And the method annotations size will be <size>
     And the annotation at position <position> will have been modified
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  Scenario Outline: Should modify the result map annotation of method SelectByExampleWithoutBLOBs for class
    Given the table name is properly configured
      And the result map id is properly configured
      And the method has a result map annotation
      And the introspected table is the right table
    When the validate method is called
     And the SelectByExampleWithoutBLOBs method for class is called
    Then the generated method return value will be true
     And the method annotations size will be <size>
     And the annotation at position <position> will have been modified
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  Scenario Outline: Should modify the result map annotation of method SelectByPrimaryKey for class
    Given the table name is properly configured
      And the result map id is properly configured
      And the method has a result map annotation
      And the introspected table is the right table
    When the validate method is called
     And the SelectByPrimaryKey method for class is called
    Then the generated method return value will be true
     And the method annotations size will be <size>
     And the annotation at position <position> will have been modified
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  Scenario Outline: Should modify the result map annotation of method SelectAll for class
    Given the table name is properly configured
      And the result map id is properly configured
      And the method has a result map annotation
      And the introspected table is the right table
    When the validate method is called
     And the SelectAll method for class is called
    Then the generated method return value will be true
     And the method annotations size will be <size>
     And the annotation at position <position> will have been modified
  
    Examples:
      | size | position |
      | 1    | 0        |

  
  