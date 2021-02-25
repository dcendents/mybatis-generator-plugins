package com.github.dcendents.mybatis.generator.plugin.model;

import javax.inject.Inject;

import com.github.dcendents.mybatis.generator.plugin.WorldState;

import io.cucumber.java.en.Given;

/**
 * Steps for the class AlterModelPlugin.
 */
public class AlterModelPluginSteps {

	@Inject
	private WorldState state;

	@Given("an instance of AlterModelPlugin")
	public void newPlugin() {
		state.setPlugin(new AlterModelPlugin());
	}

	@Given("the AlterModelPlugin table name is set to {word}")
	public void configureThePluginTableNameProperty(String tableName) {
		state.getPlugin().getProperties().put(AlterModelPlugin.TABLE_NAME, tableName);
	}

	@Given("the AlterModelPlugin interfaces are set to {string}")
	public void configureThePluginInterfacesProperty(String interfaces) throws Exception {
		state.getPlugin().getProperties().put(AlterModelPlugin.ADD_INTERFACES, interfaces);
	}

}
