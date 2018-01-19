package com.github.dcendents.mybatis.generator.plugin.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * Tests for the class AlterModelPlugin.
 */
@RunWith(MockitoJUnitRunner.class)
public class AlterModelPluginTest {

	private AlterModelPlugin plugin;

	@Mock
	private IntrospectedTable introspectedTable;
	@Mock
	private TopLevelClass topLevelClass;

	private static final String TABLE_NAME = "table_name";

	@Before
	public void init() throws Exception {
		plugin = new AlterModelPlugin();
		plugin.getProperties().put(AlterModelPlugin.TABLE_NAME, TABLE_NAME);
		plugin.getProperties().put(AlterModelPlugin.ADD_INTERFACES, Serializable.class.getName());
		plugin.validate(new ArrayList<String>());
	}

	@Test
	public void shouldBeInvalidWithoutAnyPropertyConfigured() {
		// Given
		AlterModelPlugin instance = new AlterModelPlugin();

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(2);
	}

	@Test
	public void shouldBeInvalidWithOnlyTheTableNameConfigured() {
		// Given
		AlterModelPlugin instance = new AlterModelPlugin();
		instance.getProperties().put(AlterModelPlugin.TABLE_NAME, TABLE_NAME);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(1);
	}

	@Test
	public void shouldBeInvalidWithOnlyTheInterfacesConfigured() {
		// Given
		AlterModelPlugin instance = new AlterModelPlugin();
		instance.getProperties().put(AlterModelPlugin.ADD_INTERFACES, Serializable.class.getName());

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(1);
	}

	@Test
	public void shouldBeValidWhenBothPropertiesAreConfigured() {
		// Given
		AlterModelPlugin instance = new AlterModelPlugin();
		instance.getProperties().put(AlterModelPlugin.TABLE_NAME, TABLE_NAME);
		instance.getProperties().put(AlterModelPlugin.ADD_INTERFACES, Serializable.class.getName());

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isTrue();
		assertThat(warnings).isEmpty();
	}

	@Test
	public void shouldNotModifyModelBaseRecordClassIfTableDoesNotMatch() throws Exception {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn("wrong_name");

		// When
		boolean ok = plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(topLevelClass, times(0)).addImportedType(any(FullyQualifiedJavaType.class));
		verify(topLevelClass, times(0)).addSuperInterface(any(FullyQualifiedJavaType.class));
	}

	@Test
	public void shouldAddInterfacesToModelBaseRecordClass() throws Exception {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);

		// When
		boolean ok = plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);

		// Then
		assertThat(ok).isTrue();

		ArgumentCaptor<FullyQualifiedJavaType> typeCaptor = ArgumentCaptor.forClass(FullyQualifiedJavaType.class);

		verify(topLevelClass).addImportedType(typeCaptor.capture());
		FullyQualifiedJavaType importedType = typeCaptor.getValue();
		verify(topLevelClass).addSuperInterface(typeCaptor.capture());
		FullyQualifiedJavaType interfaceType = typeCaptor.getValue();

		assertThat(importedType).isNotNull();
		assertThat(interfaceType).isNotNull();

		assertThat(importedType).isSameAs(interfaceType);
		assertThat(importedType.getFullyQualifiedName()).isEqualTo(Serializable.class.getName());
	}

	@Test
	public void shouldAcceptRegexValueForTableName() throws Exception {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		plugin.getProperties().put(AlterModelPlugin.TABLE_NAME, TABLE_NAME.substring(0,  3) + ".*");
		plugin.validate(new ArrayList<String>());

		// When
		boolean ok = plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(topLevelClass).addImportedType(any(FullyQualifiedJavaType.class));
		verify(topLevelClass).addSuperInterface(any(FullyQualifiedJavaType.class));
	}

}
