package com.github.dcendents.mybatis.generator.plugin.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Steps for the class AlterModelPlugin.
 */
public class AlterModelPluginSteps {

	private AlterModelPlugin plugin;

	private IntrospectedTable introspectedTable;
	private TopLevelClass topLevelClass;
	private List<String> warnings;
	private boolean validateResult;
	private boolean executionResult;
	private ArgumentCaptor<FullyQualifiedJavaType> typeCaptor;
	private FullyQualifiedJavaType importedType;
	private FullyQualifiedJavaType interfaceType;

	private static final String TABLE_NAME = "table_name";
	private static final String INTERFACES = Serializable.class.getName();

	public AlterModelPluginSteps(AlterModelPlugin plugin) {
		this.plugin = plugin;
	}

	@Before
	public void init() throws Exception {
		warnings = new ArrayList<>();
		typeCaptor = ArgumentCaptor.forClass(FullyQualifiedJavaType.class);
		introspectedTable = Mockito.mock(IntrospectedTable.class);
		topLevelClass = Mockito.mock(TopLevelClass.class);
	}

	@Given("the table name is properly configured")
	public void configureThePluginTableNameProperty() throws Exception {
		plugin.getProperties().put(AlterModelPlugin.TABLE_NAME, TABLE_NAME);
	}

	@Given("the interfaces are properly configured")
	public void configureThePluginInterfacesProperty() throws Exception {
		plugin.getProperties().put(AlterModelPlugin.ADD_INTERFACES, INTERFACES);
	}

	@Given("the introspected table is a different table")
	public void mockIntrospectedTableWithWrongName() throws Exception {
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn("wrong_name");
	}

	@Given("the introspected table is the right table")
	public void mockIntrospectedTableWithRightName() throws Exception {
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
	}

	@Given("the validate method has been called")
	@When("the validate method is called")
	public void validateThePlugin() throws Exception {
		validateResult = plugin.validate(warnings);
	}

	@When("the modelBaseRecordClassGenerated method is called")
	public void invokeRenameResultMapElementAttribute() throws Exception {
		executionResult = plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}

	@Then("validate should return {}")
	public void verifyValidateReturn(boolean validate) throws Exception {
		assertThat(validateResult).isEqualTo(validate);
	}

	@Then("validate should have produced {int} warnings")
	public void verifyValidateWarnings(int noWarnings) throws Exception {
		assertThat(warnings).hasSize(noWarnings);
	}

	@Then("the execution result will be true")
	public void verifyExecutionResult() throws Exception {
		assertThat(executionResult).isTrue();
	}

	@Then("the addImportedType method of topLevelClass will have been called {int} times")
	public void verifyImportedTypeMethodWasCalled(int timesCalled) throws Exception {
		verify(topLevelClass, times(timesCalled)).addImportedType(typeCaptor.capture());
		if (timesCalled > 0) {
			importedType = typeCaptor.getValue();
		}
	}

	@Then("the addSuperInterface method of topLevelClass will have been called {int} times")
	public void verifyAddSuperInterfaceMethodWasCalled(int timesCalled) throws Exception {
		verify(topLevelClass, times(timesCalled)).addSuperInterface(typeCaptor.capture());
		if (timesCalled > 0) {
			interfaceType = typeCaptor.getValue();
		}
	}

	@Then("the imported type is not null")
	public void verifyImportedTypeIsNotNull() throws Exception {
		assertThat(importedType).isNotNull();
	}

	@Then("the interface type is not null")
	public void verifyInterfaceTypeIsNotNull() throws Exception {
		assertThat(interfaceType).isNotNull();
	}

	@Then("the imported and interface types are the same")
	public void verifyImportedAndInterfaceTypesAreTheSame() throws Exception {
		assertThat(importedType).isSameAs(interfaceType);
	}

	@Then("the imported type matches the configured interface")
	public void verifyTypeMatchesConfiguration() throws Exception {
		assertThat(importedType.getFullyQualifiedName()).isEqualTo(INTERFACES);
	}

}
