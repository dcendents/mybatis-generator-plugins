package com.github.dcendents.mybatis.generator.plugin.annotation;

import javax.inject.Inject;

import com.github.dcendents.mybatis.generator.plugin.WorldState;

import io.cucumber.java.en.Given;

/**
 * Steps for the class AddClassAnnotationsPlugin.
 */
public class AddClassAnnotationsPluginSteps {

	@Inject
	private WorldState state;

	@Given("an instance of AddClassAnnotationsPlugin")
	public void newPlugin() {
		state.setPlugin(new AddClassAnnotationsPlugin());
	}

	@Given("the AddClassAnnotationsPlugin class is set to {string}")
	public void configureThePluginClassProperty(String className) {
		if (!"null".equalsIgnoreCase(className)) {
			state.getPlugin().getProperties().put(AddClassAnnotationsPlugin.ANNOTATION_CLASS, className);
		}
	}

	@Given("the AddClassAnnotationsPlugin annotation is set to {word}")
	public void configureThePluginAnnotationProperty(String annotation) {
		if (!"null".equalsIgnoreCase(annotation)) {
			state.getPlugin().getProperties().put(AddClassAnnotationsPlugin.ANNOTATION_STRING, annotation);
		}
	}

}
