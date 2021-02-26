package com.github.dcendents.mybatis.generator.plugin.rename;

import javax.inject.Inject;

import com.github.dcendents.mybatis.generator.plugin.WorldState;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

/**
 * Steps for the class RenameExampleClassAndMethodsPlugin.
 */
public class RenameExampleClassAndMethodsPluginSteps {

	@Inject
	private WorldState state;

	@Given("an instance of RenameExampleClassAndMethodsPlugin")
	public void newPlugin() {
		state.setPlugin(new RenameExampleClassAndMethodsPlugin());
	}

	@Given("the RenameExampleClassAndMethodsPlugin class search is set to {word}")
	public void configureThePluginClassSearchProperty(String search) {
		if (!"null".equalsIgnoreCase(search)) {
			state.getPlugin().getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_SEARCH_PROPERTY, search);
		}
	}

	@Given("the RenameExampleClassAndMethodsPlugin class replace is set to {word}")
	public void configureThePluginClassReplaceProperty(String replace) {
		if (!"null".equalsIgnoreCase(replace)) {
			state.getPlugin().getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_REPLACE_PROPERTY, replace);
		}
	}

	@Given("the RenameExampleClassAndMethodsPlugin param search is set to {word}")
	public void configureThePluginParamSearchProperty(String search) {
		if (!"null".equalsIgnoreCase(search)) {
			state.getPlugin().getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_SEARCH_PROPERTY, search);
		}
	}

	@Given("the RenameExampleClassAndMethodsPlugin param replace is set to {word}")
	public void configureThePluginParamReplaceProperty(String replace) {
		if (!"null".equalsIgnoreCase(replace)) {
			state.getPlugin().getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_REPLACE_PROPERTY, replace);
		}
	}

	@When("the RenameExampleClassAndMethodsPlugin renameMethod method is called")
	public void invokeRenameMethod() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = (RenameExampleClassAndMethodsPlugin) state.getPlugin();
		boolean result = plugin.renameMethod(state.getMethod());
		state.getResults().put("renameMethod", result);
	}

}
