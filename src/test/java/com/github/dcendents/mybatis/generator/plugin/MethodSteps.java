package com.github.dcendents.mybatis.generator.plugin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import javax.inject.Inject;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Parameter;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

/**
 * Steps for all Methods.
 */
public class MethodSteps {

	@Inject
	private WorldState state;

	@Given("the method has the annotation {word}")
	public void addMethodAnnotation(String value) {
		state.getMethod().getAnnotations().add(value);
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

	@Then("the method name is set to {word}")
	public void verifyMethodName(String name) throws Exception {
		verify(state.getMethod()).setName(eq(name));
	}
}
