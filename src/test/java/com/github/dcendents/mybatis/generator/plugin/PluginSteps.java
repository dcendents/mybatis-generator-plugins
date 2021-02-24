package com.github.dcendents.mybatis.generator.plugin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.ArgumentCaptor;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Steps for the class AlterModelPlugin.
 */
public class PluginSteps {

	private WorldState state;

	public PluginSteps(WorldState state) {
		this.state = state;
	}

	@Given("the introspected table is for {word}")
	public void mockIntrospectedTableWithWrongName(String tableName) {
		given(state.getIntrospectedTable().getFullyQualifiedTableNameAtRuntime()).willReturn(tableName);
	}

	@Given("the validate method has been called")
	@When("the validate method is called")
	public void validateThePlugin() throws Exception {
		boolean result = state.getPlugin().validate(state.getWarnings());
		state.getResults().put("validate", result);
	}

	@When("the modelBaseRecordClassGenerated method is called")
	public void invokeModelBaseRecordClassGenerated() {
		boolean result = state.getPlugin().modelBaseRecordClassGenerated(state.getTopLevelClass(), state.getIntrospectedTable());
		state.getResults().put("modelBaseRecordClassGenerated", result);
	}

	@Then("{word} result is {}")
	public void verifyReturn(String result, boolean value) {
		assertThat(state.getResult(result)).isEqualTo(value);
	}

	@Then("validate should have produced {int} warnings")
	public void verifyValidateWarnings(int noWarnings) {
		assertThat(state.getWarnings()).hasSize(noWarnings);
	}

	@Then("the topLevelClass addImportedType will have been called {int} times")
	public void verifyImportedTypeMethodWasCalled(int timesCalled) {
		verify(state.getTopLevelClass(), times(timesCalled)).addImportedType(any(FullyQualifiedJavaType.class));
	}

	@Then("the topLevelClass addImportedType is {string}")
	public void verifyImportedType(String importedTypeName) {
		ArgumentCaptor<FullyQualifiedJavaType> typeCaptor = ArgumentCaptor.forClass(FullyQualifiedJavaType.class);
		verify(state.getTopLevelClass(), atLeastOnce()).addImportedType(typeCaptor.capture());
		assertThat(typeCaptor.getValue().getFullyQualifiedName()).isEqualTo(importedTypeName);
	}

	@Then("the topLevelClass addSuperInterface will have been called {int} times")
	public void verifyAddSuperInterfaceMethodWasCalled(int timesCalled) {
		verify(state.getTopLevelClass(), times(timesCalled)).addSuperInterface(any(FullyQualifiedJavaType.class));
	}

	@Then("the topLevelClass addSuperInterface is {string}")
	public void verifySuperInterfaceType(String importedTypeName) {
		ArgumentCaptor<FullyQualifiedJavaType> typeCaptor = ArgumentCaptor.forClass(FullyQualifiedJavaType.class);
		verify(state.getTopLevelClass(), atLeastOnce()).addSuperInterface(typeCaptor.capture());
		assertThat(typeCaptor.getValue().getFullyQualifiedName()).isEqualTo(importedTypeName);
	}
	
	@Then("the annotation class {string} has been imported")
	public void verifyTheAnnotationClassHasBeenImported(String className) throws Exception {
		verify(state.getTopLevelClass()).addImportedType(eq(className));
	}
	
	@Then("the annotation {word} has been added")
	public void verifyTheAnnotationStringHasBeenAdded(String annotation) throws Exception {
		verify(state.getTopLevelClass()).addAnnotation(eq(annotation));
	}
}
