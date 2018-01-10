package com.github.dcendents.mybatis.generator.plugin.wrap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * Tests for the class WrapObjectPlugin.
 */
@RunWith(CdiRunner.class)
public class WrapObjectPluginTest {

	private WrapObjectPlugin plugin;

	@Mock
	private IntrospectedTable introspectedTable;
	@Mock
	private TopLevelClass topLevelClass;
	@Mock
	private Field field;
	@Mock
	private IntrospectedColumn introspectedColumn;
	@Mock
	private Method method;

	private static final String TABLE_NAME = "table_name";
	private static final String OBJECT_CLASS = "com.github.dcendents.mybatis.generator.plugin.wrap.ClassDTO";
	private static final String OBJECT_FIELD_NAME = "wrapObj";
	private static final String INCLUDES = "name ,, address , homeAddress ,workAddress,street, ";
	private static final String EXCLUDES = " city ,, postCode , ";

	private List<String> includeList;
	private List<String> excludeList;

	private List<String> methodLines;

	@Before
	public void init() throws Exception {
		plugin = new WrapObjectPlugin();
		plugin.getProperties().put(WrapObjectPlugin.TABLE_NAME, TABLE_NAME);
		plugin.getProperties().put(WrapObjectPlugin.OBJECT_CLASS, OBJECT_CLASS);
		plugin.getProperties().put(WrapObjectPlugin.OBJECT_FIELD_NAME, OBJECT_FIELD_NAME);
		plugin.getProperties().put(WrapObjectPlugin.INCLUDES, INCLUDES);
		plugin.getProperties().put(WrapObjectPlugin.EXCLUDES, EXCLUDES);
		plugin.validate(new ArrayList<String>());

		includeList = new ArrayList<>();
		for (String include : INCLUDES.split(",")) {
			includeList.add(include.trim());
		}
		excludeList = new ArrayList<>();
		for (String exclude : EXCLUDES.split(",")) {
			excludeList.add(exclude.trim());
		}

		methodLines = new ArrayList<>();
		methodLines.add("line1");
		methodLines.add("line2");
		methodLines.add("line3");

		given(method.getBodyLines()).willReturn(methodLines);
		willAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				methodLines.add(invocation.getArgumentAt(0, String.class));
				return null;
			}
		}).given(method).addBodyLine(anyString());
	}

	@Test
	public void shouldHaveDefaultConstructor() throws Exception {
		// Given

		// When
		WrapObjectPlugin instance = new WrapObjectPlugin();

		// Then
		assertThat(instance).isNotNull();
	}

	@Test
	public void shouldBeUnvalidIfTableNameIsNotSet() throws Exception {
		// Given
		WrapObjectPlugin instance = new WrapObjectPlugin();
		instance.getProperties().put(WrapObjectPlugin.OBJECT_CLASS, OBJECT_CLASS);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(1);
	}

	@Test
	public void shouldBeUnvalidIfObjectClassIsNotSet() throws Exception {
		// Given
		WrapObjectPlugin instance = new WrapObjectPlugin();
		instance.getProperties().put(WrapObjectPlugin.TABLE_NAME, TABLE_NAME);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(1);
	}

	@Test
	public void shouldBeUnvalidIfObjectClassCannotBeLoaded() throws Exception {
		// Given
		WrapObjectPlugin instance = new WrapObjectPlugin();
		instance.getProperties().put(WrapObjectPlugin.TABLE_NAME, TABLE_NAME);
		instance.getProperties().put(WrapObjectPlugin.OBJECT_CLASS, "class.does.not.Exists");

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(1);
	}

	@Test
	public void shouldBeValidIfTableNameAndObjectClassAreSet() throws Exception {
		// Given
		WrapObjectPlugin instance = new WrapObjectPlugin();
		instance.getProperties().put(WrapObjectPlugin.TABLE_NAME, TABLE_NAME);
		instance.getProperties().put(WrapObjectPlugin.OBJECT_CLASS, OBJECT_CLASS);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isTrue();
		assertThat(warnings).isEmpty();
	}

	@Test
	public void shouldAcceptComaDelimitedIncludeList() throws Exception {
		// Given
		WrapObjectPlugin instance = new WrapObjectPlugin();
		instance.getProperties().put(WrapObjectPlugin.TABLE_NAME, TABLE_NAME);
		instance.getProperties().put(WrapObjectPlugin.OBJECT_CLASS, OBJECT_CLASS);
		instance.getProperties().put(WrapObjectPlugin.INCLUDES, INCLUDES);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isTrue();
		assertThat(warnings).isEmpty();
		assertThat(instance.getIncludes()).containsOnlyElementsOf(includeList);
	}

	@Test
	public void shouldAcceptComaDelimitedExcludeList() throws Exception {
		// Given
		WrapObjectPlugin instance = new WrapObjectPlugin();
		instance.getProperties().put(WrapObjectPlugin.TABLE_NAME, TABLE_NAME);
		instance.getProperties().put(WrapObjectPlugin.OBJECT_CLASS, OBJECT_CLASS);
		instance.getProperties().put(WrapObjectPlugin.EXCLUDES, EXCLUDES);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isTrue();
		assertThat(warnings).isEmpty();
		assertThat(instance.getExcludes()).containsOnlyElementsOf(excludeList);
	}

	@Test
	public void shouldAcceptValueForFieldName() throws Exception {
		// Given
		String objectFieldName = "fieldName";
		WrapObjectPlugin instance = new WrapObjectPlugin();
		instance.getProperties().put(WrapObjectPlugin.TABLE_NAME, TABLE_NAME);
		instance.getProperties().put(WrapObjectPlugin.OBJECT_CLASS, OBJECT_CLASS);
		instance.getProperties().put(WrapObjectPlugin.OBJECT_FIELD_NAME, objectFieldName);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isTrue();
		assertThat(warnings).isEmpty();
		assertThat(instance.getObjectFieldName()).isEqualTo(objectFieldName);
	}

	@Test
	public void shouldNotModifyModelBaseRecordClassIfTableDoesNotMatch() throws Exception {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn("wrong_name");

		// When
		boolean ok = plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(topLevelClass, times(0)).addField(any(Field.class));
	}

	@Test
	public void shouldAddWrappedObjectFieldToModelBaseRecordClass() throws Exception {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);

		// When
		boolean ok = plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);

		// Then
		assertThat(ok).isTrue();

		ArgumentCaptor<Field> fieldCaptor = ArgumentCaptor.forClass(Field.class);
		ArgumentCaptor<FullyQualifiedJavaType> typeCaptor = ArgumentCaptor.forClass(FullyQualifiedJavaType.class);

		verify(topLevelClass).addField(fieldCaptor.capture());
		verify(topLevelClass).addImportedType(typeCaptor.capture());

		Field field = fieldCaptor.getValue();
		FullyQualifiedJavaType type = typeCaptor.getValue();

		assertThat(field).isNotNull();
		assertThat(type).isNotNull();

		assertThat(field.getName()).isEqualTo(OBJECT_FIELD_NAME);
		assertThat(field.getType()).isEqualTo(type);
		assertThat(field.getType().getFullyQualifiedName()).isEqualTo(OBJECT_CLASS);
		assertThat(field.getVisibility()).isEqualTo(JavaVisibility.PROTECTED);
		assertThat(field.getInitializationString()).isNotNull();
		assertThat(field.getJavaDocLines()).contains(" * @mbggenerated");
	}

	@Test
	public void shouldKeepFieldIfTableDoesNotMatch() throws Exception {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn("wrong_name");

		// When
		boolean ok = plugin.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isTrue();
	}

	@Test
	public void shouldKeepFieldIfNotInIncludeList() throws Exception {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(field.getName()).willReturn("notInList");

		// When
		boolean ok = plugin.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isTrue();
	}

	@Test
	public void shouldKeepFieldIfInExcludeList() throws Exception {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(field.getName()).willReturn(excludeList.get(0));

		// When
		boolean ok = plugin.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isTrue();
	}

	@Test
	public void shouldKeepFieldIfItHasNoGetter() throws Exception {
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(String.class.getName());

		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(field.getName()).willReturn("address");
		given(field.getType()).willReturn(type);

		// When
		boolean ok = plugin.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isTrue();
	}

	@Test
	public void shouldNotGeneratedFieldIfItIsWrapped() throws Exception {
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(String.class.getName());

		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(field.getName()).willReturn("name");
		given(field.getType()).willReturn(type);

		// When
		boolean ok = plugin.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isFalse();
		verify(topLevelClass).addImportedType(eq(type));
	}

	@Test
	public void shouldHandleBooleanPrimitiveFieldType() throws Exception {
		FullyQualifiedJavaType type = FullyQualifiedJavaType.getBooleanPrimitiveInstance();

		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(field.getName()).willReturn("homeAddress");
		given(field.getType()).willReturn(type);

		// When
		boolean ok = plugin.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isFalse();
		verify(topLevelClass).addImportedType(eq(type));
	}

	@Test
	public void shouldHandleBooleanObjectType() throws Exception {
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(Boolean.class.getName());

		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(field.getName()).willReturn("workAddress");
		given(field.getType()).willReturn(type);

		// When
		boolean ok = plugin.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isFalse();
		verify(topLevelClass).addImportedType(eq(type));
	}

	@Test
	public void shouldHandleMixedBooleanPrimitiveObjectType() throws Exception {
		FullyQualifiedJavaType type = new FullyQualifiedJavaType(Boolean.class.getName());

		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(field.getName()).willReturn("homeAddress");
		given(field.getType()).willReturn(type);

		// When
		boolean ok = plugin.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isFalse();
		verify(topLevelClass).addImportedType(eq(type));
	}

	@Test
	public void shouldHandleMixedBooleanObjectPrimitiveType() throws Exception {
		FullyQualifiedJavaType type = FullyQualifiedJavaType.getBooleanPrimitiveInstance();

		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(field.getName()).willReturn("workAddress");
		given(field.getType()).willReturn(type);

		// When
		boolean ok = plugin.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isFalse();
		verify(topLevelClass).addImportedType(eq(type));
	}

	@Test
	public void shouldNotModifyGetterWhenTableDoesNotMatch() throws Exception {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn("wrong_name");

		// When
		boolean ok = plugin.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isTrue();
		assertThat(methodLines).hasSize(3);
		verify(method, times(0)).getBodyLines();
		verify(method, times(0)).addBodyLine(anyString());
	}

	@Test
	public void shouldNotModifyGetterWhenPropertyIsNotWrapped() throws Exception {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		plugin.getGettersToWrap().clear();
		plugin.getWrappedGetters().clear();

		// When
		boolean ok = plugin.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isTrue();
		assertThat(methodLines).hasSize(3);
		verify(method, times(0)).getBodyLines();
		verify(method, times(0)).addBodyLine(anyString());
	}

	@Test
	public void shouldReplaceGetterToCallWrappedProperty() throws Exception {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(method.getName()).willReturn("getName");
		plugin.getGettersToWrap().add("getName");
		plugin.getWrappedGetters().put("getName", "getName");

		// When
		boolean ok = plugin.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isTrue();
		assertThat(methodLines).hasSize(1);
		verify(method).getBodyLines();
		verify(method).addBodyLine(anyString());
	}

	@Test
	public void shouldNotModifySetterWhenTableDoesNotMatch() throws Exception {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn("wrong_name");

		// When
		boolean ok = plugin.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isTrue();
		assertThat(methodLines).hasSize(3);
		verify(method, times(0)).getBodyLines();
		verify(method, times(0)).addBodyLine(anyString());
	}

	@Test
	public void shouldNotModifySetterWhenPropertyIsNotWrapped() throws Exception {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		plugin.getSettersToWrap().clear();

		// When
		boolean ok = plugin.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isTrue();
		assertThat(methodLines).hasSize(3);
		verify(method, times(0)).getBodyLines();
		verify(method, times(0)).addBodyLine(anyString());
	}

	@Test
	public void shouldReplaceSetterToCallWrappedProperty() throws Exception {
		Parameter parameter = new Parameter(FullyQualifiedJavaType.getStringInstance(), "name");
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(parameter);

		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(method.getName()).willReturn("setName");
		given(method.getParameters()).willReturn(parameters);
		plugin.getSettersToWrap().add("setName");

		// When
		boolean ok = plugin.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, null);

		// Then
		assertThat(ok).isTrue();
		assertThat(methodLines).hasSize(1);
		verify(method).getBodyLines();
		verify(method).addBodyLine(anyString());
	}

}
