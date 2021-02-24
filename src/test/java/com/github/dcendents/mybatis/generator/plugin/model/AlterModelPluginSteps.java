package com.github.dcendents.mybatis.generator.plugin.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Steps for the class AlterModelPlugin.
 */
public class AlterModelPluginSteps {

	private AlterModelPlugin plugin;

	private IntrospectedTable introspectedTableMock;
	private TopLevelClass topLevelClassMock;
	private List<String> warnings;
	private boolean validateResult;
	private boolean executionResult;
	private ArgumentCaptor<FullyQualifiedJavaType> typeCaptor;
	private FullyQualifiedJavaType importedType;
	private FullyQualifiedJavaType interfaceType;

	public AlterModelPluginSteps(AlterModelPlugin plugin) {
		this.plugin = plugin;
	}

	@Given("an empty warnings list")
	public void emptyWarningsList() {
		warnings = new ArrayList<>();
	}

	@Given("a mock for IntrospectedTable")
	public void mockIntrospectedTable() {
		introspectedTableMock = Mockito.mock(IntrospectedTable.class);
	}

	@Given("a mock for TopLevelClass")
	public void mockTopLevelClass() {
		topLevelClassMock = Mockito.mock(TopLevelClass.class);
	}

	@Given("a type captor for FullyQualifiedJavaType")
	public void typeCaptorFullyQualifiedJavaType() {
		typeCaptor = ArgumentCaptor.forClass(FullyQualifiedJavaType.class);
	}

	@Given("the table name is set to {word}")
	public void configureThePluginTableNameProperty(String tableName) {
		plugin.getProperties().put(AlterModelPlugin.TABLE_NAME, tableName);
	}

	@Given("the interfaces are set to {string}")
	public void configureThePluginInterfacesProperty(String interfaces) throws Exception {
		plugin.getProperties().put(AlterModelPlugin.ADD_INTERFACES, interfaces);
	}

	@Given("the introspected table is for {word}")
	public void mockIntrospectedTableWithWrongName(String tableName) {
		given(introspectedTableMock.getFullyQualifiedTableNameAtRuntime()).willReturn(tableName);
	}

	@Given("the validate method has been called")
	@When("the validate method is called")
	public void validateThePlugin() throws Exception {
		validateResult = plugin.validate(warnings);
	}

	@When("the modelBaseRecordClassGenerated method is called")
	public void invokeRenameResultMapElementAttribute() {
		executionResult = plugin.modelBaseRecordClassGenerated(topLevelClassMock, introspectedTableMock);
	}

	@Then("validate should return {}")
	public void verifyValidateReturn(boolean validate) {
		assertThat(validateResult).isEqualTo(validate);
	}

	@Then("validate should have produced {int} warnings")
	public void verifyValidateWarnings(int noWarnings) {
		assertThat(warnings).hasSize(noWarnings);
	}

	@Then("the execution result will be true")
	public void verifyExecutionResult() {
		assertThat(executionResult).isTrue();
	}

	@Then("the addImportedType method of topLevelClass will have been called {int} times")
	public void verifyImportedTypeMethodWasCalled(int timesCalled) {
		verify(topLevelClassMock, times(timesCalled)).addImportedType(typeCaptor.capture());
		if (timesCalled > 0) {
			importedType = typeCaptor.getValue();
		}
	}

	@Then("the addSuperInterface method of topLevelClass will have been called {int} times")
	public void verifyAddSuperInterfaceMethodWasCalled(int timesCalled) {
		verify(topLevelClassMock, times(timesCalled)).addSuperInterface(typeCaptor.capture());
		if (timesCalled > 0) {
			interfaceType = typeCaptor.getValue();
		}
	}

	@Then("the imported type is not null")
	public void verifyImportedTypeIsNotNull() {
		assertThat(importedType).isNotNull();
	}

	@Then("the interface type is not null")
	public void verifyInterfaceTypeIsNotNull() {
		assertThat(interfaceType).isNotNull();
	}

	@Then("the imported and interface types are the same")
	public void verifyImportedAndInterfaceTypesAreTheSame() {
		assertThat(importedType).isSameAs(interfaceType);
	}

	@Then("the imported type is {string}")
	public void verifyTypeMatchesConfiguration(String importedTypeName) {
		assertThat(importedType.getFullyQualifiedName()).isEqualTo(importedTypeName);
	}
}
