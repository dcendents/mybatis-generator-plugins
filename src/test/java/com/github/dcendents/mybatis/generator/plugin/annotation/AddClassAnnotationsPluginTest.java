package com.github.dcendents.mybatis.generator.plugin.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * Tests for the class AddClassAnnotationsPlugin.
 */
@RunWith(CdiRunner.class)
public class AddClassAnnotationsPluginTest {

	@Inject
	private AddClassAnnotationsPlugin plugin;

	@Mock
	private TopLevelClass topLevelClass;

	@Test
	public void shouldBeInvalidWithoutAnyPropertyConfigured() {
		// Given

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = plugin.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(2);
	}

	@Test
	public void shouldBeInvalidWithOnlyTheClassConfigured() {
		// Given
		plugin.getProperties().put(AddClassAnnotationsPlugin.ANNOTATION_CLASS, Test.class.getName());

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = plugin.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(1);
	}

	@Test
	public void shouldBeInvalidWithOnlyTheAnnotationConfigured() {
		// Given
		plugin.getProperties().put(AddClassAnnotationsPlugin.ANNOTATION_STRING, "@Test");

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = plugin.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(1);
	}

	@Test
	public void shouldBeValidWhenBothPropertiesAreConfigured() {
		// Given
		plugin.getProperties().put(AddClassAnnotationsPlugin.ANNOTATION_CLASS, Test.class.getName());
		plugin.getProperties().put(AddClassAnnotationsPlugin.ANNOTATION_STRING, "@Test");

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = plugin.validate(warnings);

		// Then
		assertThat(ok).isTrue();
		assertThat(warnings).isEmpty();
	}

	@Test
	public void shouldAddTheAnnotation() {
		// Given
		plugin.getProperties().put(AddClassAnnotationsPlugin.ANNOTATION_CLASS, Test.class.getName());
		plugin.getProperties().put(AddClassAnnotationsPlugin.ANNOTATION_STRING, "@Test");

		List<String> warnings = new ArrayList<>();
		plugin.validate(warnings);

		// When
		boolean ok = plugin.modelBaseRecordClassGenerated(topLevelClass, null);

		// Then
		assertThat(ok).isTrue();
		verify(topLevelClass, times(1)).addImportedType(eq(plugin.getProperties().get(AddClassAnnotationsPlugin.ANNOTATION_CLASS).toString()));
		verify(topLevelClass, times(1)).addAnnotation(eq(plugin.getProperties().get(AddClassAnnotationsPlugin.ANNOTATION_STRING).toString()));
	}
}
