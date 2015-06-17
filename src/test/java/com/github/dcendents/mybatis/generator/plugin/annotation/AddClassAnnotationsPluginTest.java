package com.github.dcendents.mybatis.generator.plugin.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import com.github.bmsantos.core.cola.story.annotations.Features;
import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Group;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;
import com.github.dcendents.mybatis.generator.plugin.BaseColaTest;

/**
 * Tests for the class AddClassAnnotationsPlugin.
 */
@RunWith(CdiRunner.class)
@Features("AddClassAnnotationsPlugin")
public class AddClassAnnotationsPluginTest extends BaseColaTest {

	@Inject
	private AddClassAnnotationsPlugin plugin;
	@Mock
	private TopLevelClass topLevelClass;
	private List<String> warnings;
	private boolean validateResult;
	private boolean modelBaseRecordClassGenerated;

	@Before
	public void init() throws Exception {
		warnings = new ArrayList<>();
	}
	
	@Given("the plugin class is properly configured")
	public void configureThePluginClassProperty() throws Exception {
		plugin.getProperties().put(AddClassAnnotationsPlugin.ANNOTATION_CLASS, Test.class.getName());
	}
	
	@Given("the plugin annotation is properly configured")
	public void configureThePluginAnnotationProperty() throws Exception {
		plugin.getProperties().put(AddClassAnnotationsPlugin.ANNOTATION_STRING, "@Test");
	}
	
	@When("the validate method is called")
	public void validateThePlugin() throws Exception {
		validateResult = plugin.validate(warnings);
	}
	
	@When("the model base record class is generated")
	public void executeModelBaseRecordClassGenerated() throws Exception {
		modelBaseRecordClassGenerated = plugin.modelBaseRecordClassGenerated(topLevelClass, null);
	}
	
	@Then("validate should return (true|false)")
	public void verifyValidateReturn(@Group(1) final Boolean validate) throws Exception {
		assertThat(validateResult).isEqualTo(validate);
	}
	
	@Then("validate should have produced (\\d+) warnings")
	public void verifyValidateWarnings(@Group(1) final Integer noWarnings) throws Exception {
		assertThat(warnings).hasSize(noWarnings);
	}
	
	@Then("modelBaseRecordClassGenerated should return true")
	public void verifyModelBaseRecordClassGeneratedReturn() throws Exception {
		assertThat(modelBaseRecordClassGenerated).isTrue();
	}
	
	@Then("the annotation class should have been imported")
	public void verifyTheAnnotationClassHasBeenImported() throws Exception {
		verify(topLevelClass).addImportedType(eq(plugin.getProperties().get(AddClassAnnotationsPlugin.ANNOTATION_CLASS).toString()));
	}
	
	@Then("the annotation string should have been added")
	public void verifyTheAnnotationStringHasBeenAdded() throws Exception {
		verify(topLevelClass).addAnnotation(eq(plugin.getProperties().get(AddClassAnnotationsPlugin.ANNOTATION_STRING).toString()));
	}
	
}
