package com.github.dcendents.mybatis.generator.plugin;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Steps for all Plugins.
 */
public class PluginSteps {

	@Inject
	private WorldState state;

	@Given("the validate method has been called")
	@When("the validate method is called")
	public void validateThePlugin() {
		boolean result = state.getPlugin().validate(state.getWarnings());
		state.getResults().put("validate", result);
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
}
