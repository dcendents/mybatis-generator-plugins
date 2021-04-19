Feature: RenameExampleClassAndMethodsPlugin execution

  Background:
    Given an instance of RenameExampleClassAndMethodsPlugin
    And the RenameExampleClassAndMethodsPlugin class search is set to Example
    And the RenameExampleClassAndMethodsPlugin class replace is set to Filter
    And the RenameExampleClassAndMethodsPlugin param search is set to example
    And the RenameExampleClassAndMethodsPlugin param replace is set to filter
    And the validate method has been called

  Scenario: Dummy test for better maven surefire output

  Scenario Outline: Should rename the example type
    Given the introspected table example type is <old_name>
    When the initialized method is called
    Then the introspected table example type is set to <new_name>

		Examples:
			| old_name | new_name |
			| SomeType | SomeType |
			| SomeExampleWithExampleInName | SomeFilterWithFilterInName |

  Scenario Outline: Should rename the method name
    Given the method name is <old_name>
    When the RenameExampleClassAndMethodsPlugin renameMethod method is called
    Then renameMethod result is true
    And the method name is set to <new_name>

		Examples:
			| old_name | new_name |
			| SomeName | SomeName |
			| SomeExampleWithExampleInName | SomeFilterWithFilterInName |

  Scenario: Should rename the method parameters and keep annotations
    Given the method name is SomeExampleWithExampleInName
    And the method has a parameter [type], [example], [true], [annotation1,annotation2]
    When the RenameExampleClassAndMethodsPlugin renameMethod method is called
    Then renameMethod result is true
    And the method name is set to SomeFilterWithFilterInName
    And the method has 1 parameter
    And the method parameter 0 is [type], [filter], [true], [annotation1,annotation2]

  Scenario: Should keep other method parameters as is
    Given the method name is SomeExampleWithExampleInName
    And the method has a parameter [type], [other], [true], [annotation1,annotation2]
    When the RenameExampleClassAndMethodsPlugin renameMethod method is called
    Then renameMethod result is true
    And the method name is set to SomeFilterWithFilterInName
    And the method has 1 parameter
    And the method parameter 0 is [type], [other], [true], [annotation1,annotation2]

  Scenario: Should handle methods without parameters
    Given the method name is SomeExampleWithExampleInName
    When the RenameExampleClassAndMethodsPlugin renameMethod method is called
    Then renameMethod result is true
    And the method name is set to SomeFilterWithFilterInName
    And the method has 0 parameters

