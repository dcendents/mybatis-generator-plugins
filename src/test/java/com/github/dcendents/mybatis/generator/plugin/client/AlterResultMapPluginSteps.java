package com.github.dcendents.mybatis.generator.plugin.client;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.mybatis.generator.api.dom.xml.Attribute;

import com.github.dcendents.mybatis.generator.plugin.WorldState;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Steps for the class AlterResultMapPlugin.
 */
public class AlterResultMapPluginSteps {

	@Inject
	private WorldState state;

	@Given("an instance of AlterResultMapPlugin")
	public void newPlugin() {
		state.setPlugin(new AlterResultMapPlugin());
	}

	@Given("the AlterResultMapPlugin table name is set to {word}")
	public void configureThePluginTableNameProperty(String tableName) {
		state.getPlugin().getProperties().put(AlterResultMapPlugin.TABLE_NAME, tableName);
	}

	@Given("the AlterResultMapPlugin result map id is set to {word}")
	public void configureThePluginResultMapIdProperty(String resultMapId) {
		state.getPlugin().getProperties().put(AlterResultMapPlugin.RESULT_MAP_ID, resultMapId);
	}

	@Given("the xml element attribute AlterResultMapPlugin result map is {word}")
	public void addAResultMapAttribute(String resultMapId) {
		state.getXmlElement().getAttributes()
				.add(new Attribute(AlterResultMapPlugin.RESULT_MAP_ATTRIBUTE, resultMapId));
	}

	@Given("the method annotation AlterResultMapPlugin result map is {word}")
	public void addAResultMapAnnotation(String resultMapId) {
		state.getMethod().getAnnotations().add(String.format(AlterResultMapPlugin.ANNOTATION_FORMAT, resultMapId));
	}

	@When("the renameResultMapAttribute is called with the xml element")
	public void invokeRenameResultMapElementAttribute() {
		AlterResultMapPlugin plugin = (AlterResultMapPlugin) state.getPlugin();
		plugin.renameResultMapAttribute(state.getXmlElement(), state.getIntrospectedTable());
	}

	@When("the renameResultMapAttribute is called with the method")
	public void invokeRenameResultMapMethodAnnotation() {
		AlterResultMapPlugin plugin = (AlterResultMapPlugin) state.getPlugin();
		plugin.renameResultMapAttribute(state.getMethod(), state.getIntrospectedTable());
	}

	@Then("the xml element attribute {int} name is the AlterResultMapPlugin result map attribute")
	public void verifyElementResultMapAttributeValue(int position) {
		assertThat(state.getXmlElement().getAttributes().get(position).getName())
				.isEqualTo(AlterResultMapPlugin.RESULT_MAP_ATTRIBUTE);
	}

	@Then("the method annotation {int} is the AlterResultMapPlugin result map annotation with value {word}")
	public void verifyMethodResultMapAnnotationValue(int position, String resultMapId) {
		assertThat(state.getMethod().getAnnotations().get(position))
				.isEqualTo(String.format(AlterResultMapPlugin.ANNOTATION_FORMAT, resultMapId));
	}

}
