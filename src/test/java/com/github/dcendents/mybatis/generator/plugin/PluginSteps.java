package com.github.dcendents.mybatis.generator.plugin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import javax.inject.Inject;

import org.mockito.ArgumentCaptor;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.xml.Attribute;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Steps for all Plugins.
 */
public class PluginSteps {

	@Inject
	private WorldState state;

	@Given("the introspected table {word}")
	public void mockIntrospectedTableWithWrongName(String tableName) {
		given(state.getIntrospectedTable().getFullyQualifiedTableNameAtRuntime()).willReturn(tableName);
	}

	@Given("the validate method has been called")
	@When("the validate method is called")
	public void validateThePlugin() {
		boolean result = state.getPlugin().validate(state.getWarnings());
		state.getResults().put("validate", result);
	}

	@Given("the xml element attribute {word} is {word}")
	public void addElementAttribute(String name, String value) {
		state.getXmlElement().getAttributes().add(new Attribute(name, value));
	}

	@Given("the method has the annotation {word}")
	public void addMethodAnnotation(String value) {
		state.getMethod().getAnnotations().add(value);
	}

	@Given("the introspected table example type is {word}")
	public void configureIntrospectedTableExampleType(String exampleType) {
		given(state.getIntrospectedTable().getExampleType()).willReturn(exampleType);
	}

	@Given("the method name is {word}")
	public void configureMethodName(String methodName) {
		given(state.getMethod().getName()).willReturn(methodName);
	}

	@Given("the method has a parameter [{word}], [{word}], [{}], [{}]")
	public void configureMethodParameter(String type, String name, boolean varargs, String annotations) {
		Parameter parameter = new Parameter(new FullyQualifiedJavaType(type), name, varargs);
		parameter.getAnnotations().addAll(Arrays.asList(annotations.split(",")));
		state.getMethod().getParameters().add(parameter);
	}

	@When("the initialized method is called")
	public void initializedThePlugin() {
		state.getPlugin().initialized(state.getIntrospectedTable());
	}

	@When("the modelBaseRecordClassGenerated method is called")
	public void invokeModelBaseRecordClassGenerated() {
		boolean result = state.getPlugin().modelBaseRecordClassGenerated(state.getTopLevelClass(),
				state.getIntrospectedTable());
		state.getResults().put("modelBaseRecordClassGenerated", result);
	}

	@When("the SelectByExampleWithoutBLOBs method is called with the xml element")
	public void invokeSqlMapSelectByExampleWithoutBLOBsElementGenerated() {
		boolean result = state.getPlugin().sqlMapSelectByExampleWithoutBLOBsElementGenerated(state.getXmlElement(),
				state.getIntrospectedTable());
		state.getResults().put("SelectByExampleWithoutBLOBs", result);
	}

	@When("the SelectByExampleWithBLOBs method is called with the xml element")
	public void invokeSqlMapSelectByExampleWithBLOBsElementGenerated() {
		boolean result = state.getPlugin().sqlMapSelectByExampleWithBLOBsElementGenerated(state.getXmlElement(),
				state.getIntrospectedTable());
		state.getResults().put("SelectByExampleWithBLOBs", result);
	}

	@When("the SelectByPrimaryKey method is called with the xml element")
	public void invokeSqlMapSelectByPrimaryKeyElementGenerated() {
		boolean result = state.getPlugin().sqlMapSelectByPrimaryKeyElementGenerated(state.getXmlElement(),
				state.getIntrospectedTable());
		state.getResults().put("SelectByPrimaryKey", result);
	}

	@When("the SelectAll method is called with the xml element")
	public void invokeSqlMapSelectAllElementGenerated() {
		boolean result = state.getPlugin().sqlMapSelectAllElementGenerated(state.getXmlElement(),
				state.getIntrospectedTable());
		state.getResults().put("SelectAll", result);
	}

	@When("the SelectByExampleWithoutBLOBs method is called with the interface")
	public void invokeClientSelectByExampleWithoutBLOBsMethodGeneratedInterface() {
		boolean result = state.getPlugin().clientSelectByExampleWithoutBLOBsMethodGenerated(state.getMethod(),
				state.getInterfaze(), state.getIntrospectedTable());
		state.getResults().put("SelectByExampleWithoutBLOBs", result);
	}

	@When("the SelectByExampleWithBLOBs method is called with the interface")
	public void invokeClientSelectByExampleWithBLOBsMethodGeneratedInterface() {
		boolean result = state.getPlugin().clientSelectByExampleWithBLOBsMethodGenerated(state.getMethod(),
				state.getInterfaze(), state.getIntrospectedTable());
		state.getResults().put("SelectByExampleWithBLOBs", result);
	}

	@When("the SelectByPrimaryKey method is called with the interface")
	public void invokeClientSelectByPrimaryKeyMethodGeneratedInterface() {
		boolean result = state.getPlugin().clientSelectByPrimaryKeyMethodGenerated(state.getMethod(),
				state.getInterfaze(), state.getIntrospectedTable());
		state.getResults().put("SelectByPrimaryKey", result);
	}

	@When("the SelectAll method is called with the interface")
	public void invokeClientSelectAllMethodGeneratedInterface() {
		boolean result = state.getPlugin().clientSelectAllMethodGenerated(state.getMethod(), state.getInterfaze(),
				state.getIntrospectedTable());
		state.getResults().put("SelectAll", result);
	}

	@When("the SelectByExampleWithoutBLOBs method is called with the class")
	public void invokeClientSelectByExampleWithoutBLOBsMethodGeneratedClass() {
		boolean result = state.getPlugin().clientSelectByExampleWithoutBLOBsMethodGenerated(state.getMethod(),
				state.getTopLevelClass(), state.getIntrospectedTable());
		state.getResults().put("SelectByExampleWithoutBLOBs", result);
	}

	@When("the SelectByExampleWithBLOBs method is called with the class")
	public void invokeClientSelectByExampleWithBLOBsMethodGeneratedClass() {
		boolean result = state.getPlugin().clientSelectByExampleWithBLOBsMethodGenerated(state.getMethod(),
				state.getTopLevelClass(), state.getIntrospectedTable());
		state.getResults().put("SelectByExampleWithBLOBs", result);
	}

	@When("the SelectByPrimaryKey method is called with the class")
	public void invokeClientSelectByPrimaryKeyMethodGeneratedClass() {
		boolean result = state.getPlugin().clientSelectByPrimaryKeyMethodGenerated(state.getMethod(),
				state.getTopLevelClass(), state.getIntrospectedTable());
		state.getResults().put("SelectByPrimaryKey", result);
	}

	@When("the SelectAll method is called with the class")
	public void invokeClientSelectAllMethodGeneratedClass() {
		boolean result = state.getPlugin().clientSelectAllMethodGenerated(state.getMethod(), state.getTopLevelClass(),
				state.getIntrospectedTable());
		state.getResults().put("SelectAll", result);
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
	public void verifyTheAnnotationClassHasBeenImported(String className) {
		verify(state.getTopLevelClass()).addImportedType(eq(className));
	}

	@Then("the annotation {word} has been added")
	public void verifyTheAnnotationStringHasBeenAdded(String annotation) {
		verify(state.getTopLevelClass()).addAnnotation(eq(annotation));
	}

	@Then("the xml element has {int} attribute(s)")
	public void verifyElementAttributesSize(int size) {
		assertThat(state.getXmlElement().getAttributes()).hasSize(size);
	}

	@Then("the xml element attribute {int} name is {word}")
	public void verifyElementAttributeName(int position, String value) {
		assertThat(state.getXmlElement().getAttributes().get(position).getName()).isEqualTo(value);
	}

	@Then("the xml element attribute {int} value is {word}")
	public void verifyElementAttributeValue(int position, String value) {
		assertThat(state.getXmlElement().getAttributes().get(position).getValue()).isEqualTo(value);
	}

	@Then("the method has {int} annotation(s)")
	public void verifyMethodAnnotationsSize(int size) {
		assertThat(state.getMethod().getAnnotations()).hasSize(size);
	}

	@Then("the method annotation {int} is {word}")
	public void verifyMethodAnnotation(int position, String value) {
		assertThat(state.getMethod().getAnnotations().get(position)).isEqualTo(value);
	}

	@Then("the method has {int} parameter(s)")
	public void verifyMethodParametersSize(int size) {
		assertThat(state.getMethod().getParameters()).hasSize(size);
	}

	@Then("the method parameter {int} is [{word}], [{word}], [{}], [{}]")
	public void verifyMethodParameter(int position, String type, String name, boolean varargs, String annotations) {
		Parameter parameter = state.getMethod().getParameters().get(position);
		assertThat(parameter.getType()).isEqualTo(new FullyQualifiedJavaType(type));
		assertThat(parameter.getName()).isEqualTo(name);
		assertThat(parameter.isVarargs()).isEqualTo(varargs);
		assertThat(parameter.getAnnotations()).containsExactlyElementsOf(Arrays.asList(annotations.split(",")));
	}

	@Then("the introspected table example type is set to {word}")
	public void verifyExampleTypeOfIntrospectedTable(String exampleType) {
		verify(state.getIntrospectedTable()).setExampleType(eq(exampleType));
	}

	@Then("the method name is set to {word}")
	public void verifyMethodName(String name) throws Exception {
		verify(state.getMethod()).setName(eq(name));
	}
}
