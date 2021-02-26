package com.github.dcendents.mybatis.generator.plugin;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.mybatis.generator.api.dom.xml.Attribute;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

/**
 * Steps for all XmlElements.
 */
public class XmlElementSteps {

	@Inject
	private WorldState state;

	@Given("the xml element attribute {word} is {word}")
	public void addElementAttribute(String name, String value) {
		state.getXmlElement().getAttributes().add(new Attribute(name, value));
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
}
