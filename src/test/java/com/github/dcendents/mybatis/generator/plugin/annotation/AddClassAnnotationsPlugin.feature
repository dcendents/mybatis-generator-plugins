Feature: AddClassAnnotationsPlugin

  Scenario Outline: Should be invalid without any property configured
    When the validate method is called
    Then validate should return <validate>
      And validate should have produced <warnings> warnings
  
    Examples:
      | validate | warnings |
      | false    | 2        |

  
  Scenario Outline: Should be invalid with only the class configured
    Given the plugin class is properly configured
    When the validate method is called
    Then validate should return <validate>
      And validate should have produced <warnings> warnings
  
    Examples:
      | validate | warnings |
      | false    | 1        |


  Scenario Outline: Should be invalid with only the annotation configured
    Given the plugin annotation is properly configured
    When the validate method is called
    Then validate should return <validate>
      And validate should have produced <warnings> warnings
  
    Examples:
      | validate | warnings |
      | false    | 1        |


  Scenario Outline: Should be valid when both properties are configured
    Given the plugin class is properly configured
      And the plugin annotation is properly configured
    When the validate method is called
    Then validate should return <validate>
      And validate should have produced <warnings> warnings
  
    Examples:
      | validate | warnings |
      | true     | 0        |


  Scenario: Should add the annotation
    Given the plugin class is properly configured
      And the plugin annotation is properly configured
    When the validate method is called
      And the model base record class is generated
    Then modelBaseRecordClassGenerated should return true
      And the annotation class should have been imported
      And the annotation string should have been added


