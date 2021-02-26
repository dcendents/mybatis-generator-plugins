package com.github.dcendents.mybatis.generator.plugin;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import javax.inject.Inject;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

/**
 * Steps for all IntrospectionTables.
 */
public class IntrospectedTableSteps {

	@Inject
	private WorldState state;

	@Given("the introspected table {word}")
	public void mockIntrospectedTableWithWrongName(String tableName) {
		given(state.getIntrospectedTable().getFullyQualifiedTableNameAtRuntime()).willReturn(tableName);
	}

	@Given("the introspected table example type is {word}")
	public void configureIntrospectedTableExampleType(String exampleType) {
		given(state.getIntrospectedTable().getExampleType()).willReturn(exampleType);
	}

	@Then("the introspected table example type is set to {word}")
	public void verifyExampleTypeOfIntrospectedTable(String exampleType) {
		verify(state.getIntrospectedTable()).setExampleType(eq(exampleType));
	}
}
