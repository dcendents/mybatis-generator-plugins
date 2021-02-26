package com.github.dcendents.mybatis.generator.plugin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.inject.Inject;

import org.mockito.ArgumentCaptor;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import io.cucumber.java.en.Then;

/**
 * Steps for all TopLevelClass.
 */
public class TopLevelClassSteps {

	@Inject
	private WorldState state;

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
}
