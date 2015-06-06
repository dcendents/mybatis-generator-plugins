package com.github.dcendents.mybatis.generator.plugin.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * Tests for the class AlterResultMapPlugin.
 */
@RunWith(CdiRunner.class)
public class AlterResultMapPluginTest {

	private AlterResultMapPlugin plugin;

	@Mock
	private XmlElement element;
	@Mock
	private Method method;
	@Mock
	private IntrospectedTable introspectedTable;
	@Mock
	private TopLevelClass topLevelClass;
	@Mock
	private Interface interfaze;

	private static final String TABLE_NAME = "table_name";
	private static final String RESULT_MAP_ID = "FullResultMap";

	@Before
	public void init() throws Exception {
		plugin = new AlterResultMapPlugin();
		plugin.getProperties().put(AlterResultMapPlugin.TABLE_NAME, TABLE_NAME);
		plugin.getProperties().put(AlterResultMapPlugin.RESULT_MAP_ID, RESULT_MAP_ID);
		plugin.validate(new ArrayList<String>());
	}

	@Test
	public void shouldBeInvalidWithoutAnyPropertyConfigured() {
		// Given
		AlterResultMapPlugin instance = new AlterResultMapPlugin();

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
		AlterResultMapPlugin instance = new AlterResultMapPlugin();
		instance.getProperties().put(AlterResultMapPlugin.TABLE_NAME, TABLE_NAME);

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
		AlterResultMapPlugin instance = new AlterResultMapPlugin();
		instance.getProperties().put(AlterResultMapPlugin.RESULT_MAP_ID, RESULT_MAP_ID);

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
		AlterResultMapPlugin instance = new AlterResultMapPlugin();
		instance.getProperties().put(AlterResultMapPlugin.TABLE_NAME, TABLE_NAME);
		instance.getProperties().put(AlterResultMapPlugin.RESULT_MAP_ID, RESULT_MAP_ID);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isTrue();
		assertThat(warnings).isEmpty();
	}

	@Test
	public void shouldNotModifyResultMapAttributeIfTableDoesNotMatch() {
		String resultMapId = "someId";
		List<Attribute> attributes = new ArrayList<>();
		attributes.add(new Attribute(AlterResultMapPlugin.RESULT_MAP_ATTRIBUTE, resultMapId));

		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn("wrong_name");
		given(element.getAttributes()).willReturn(attributes);

		// When
		plugin.renameResultMapAttribute(element, introspectedTable);

		// Then
		assertThat(attributes).hasSize(1);
		assertThat(attributes.get(0).getName()).isEqualTo(AlterResultMapPlugin.RESULT_MAP_ATTRIBUTE);
		assertThat(attributes.get(0).getValue()).isEqualTo(resultMapId);
	}

	@Test
	public void shouldModifyResultMapAttributeWhenTableMatches() {
		String resultMapId = "someId";
		List<Attribute> attributes = new ArrayList<>();
		attributes.add(new Attribute("someName", "someValue"));
		attributes.add(new Attribute(AlterResultMapPlugin.RESULT_MAP_ATTRIBUTE, resultMapId));

		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(element.getAttributes()).willReturn(attributes);

		// When
		plugin.renameResultMapAttribute(element, introspectedTable);

		// Then
		assertThat(attributes).hasSize(2);
		assertThat(attributes.get(1).getName()).isEqualTo(AlterResultMapPlugin.RESULT_MAP_ATTRIBUTE);
		assertThat(attributes.get(1).getValue()).isEqualTo(RESULT_MAP_ID);
	}

	@Test
	public void shouldHandleEmptyAttributeList() {
		List<Attribute> attributes = new ArrayList<>();

		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(element.getAttributes()).willReturn(attributes);

		// When
		plugin.renameResultMapAttribute(element, introspectedTable);

		// Then
		assertThat(attributes).isEmpty();
	}

	@Test
	public void shouldNotModifyResultMapAnnotationIfTableDoesNotMatch() {
		String resultMapId = "someId";
		List<String> annotations = new ArrayList<>();
		annotations.add(String.format(AlterResultMapPlugin.ANNOTATION_FORMAT, resultMapId));

		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn("wrong_name");
		given(method.getAnnotations()).willReturn(annotations);

		// When
		plugin.renameResultMapAttribute(method, introspectedTable);

		// Then
		assertThat(annotations).hasSize(1);
		assertThat(annotations.get(0)).isEqualTo(String.format(AlterResultMapPlugin.ANNOTATION_FORMAT, resultMapId));
	}

	@Test
	public void shouldModifyResultMapAnnotationWhenTableMatches() {
		String resultMapId = "someId";
		List<String> annotations = new ArrayList<>();
		annotations.add("@otherAnnotation");
		annotations.add(String.format(AlterResultMapPlugin.ANNOTATION_FORMAT, resultMapId));

		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(method.getAnnotations()).willReturn(annotations);

		// When
		plugin.renameResultMapAttribute(method, introspectedTable);

		// Then
		assertThat(annotations).hasSize(2);
		assertThat(annotations.get(1)).isEqualTo(String.format(AlterResultMapPlugin.ANNOTATION_FORMAT, RESULT_MAP_ID));
	}

	@Test
	public void shouldHandleEmptyAnnotationList() {
		List<String> annotations = new ArrayList<>();

		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(method.getAnnotations()).willReturn(annotations);

		// When
		plugin.renameResultMapAttribute(method, introspectedTable);

		// Then
		assertThat(annotations).isEmpty();
	}

	@Test
	public void shouldRenameResultMapOfSqlMapSelectByExampleWithoutBLOBs() throws Exception {
		AlterResultMapPlugin plugin = spy(this.plugin);

		// Given

		// When
		boolean ok = plugin.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).renameResultMapAttribute(eq(element), eq(introspectedTable));
	}

	@Test
	public void shouldRenameResultMapOfSqlMapSelectByExampleWithBLOBs() throws Exception {
		AlterResultMapPlugin plugin = spy(this.plugin);

		// Given

		// When
		boolean ok = plugin.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).renameResultMapAttribute(eq(element), eq(introspectedTable));
	}

	@Test
	public void shouldRenameResultMapOfSqlMapSelectByPrimaryKey() throws Exception {
		AlterResultMapPlugin plugin = spy(this.plugin);

		// Given

		// When
		boolean ok = plugin.sqlMapSelectByPrimaryKeyElementGenerated(element, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).renameResultMapAttribute(eq(element), eq(introspectedTable));
	}

	@Test
	public void shouldRenameResultMapOfSqlMapSelectAll() throws Exception {
		AlterResultMapPlugin plugin = spy(this.plugin);

		// Given

		// When
		boolean ok = plugin.sqlMapSelectAllElementGenerated(element, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).renameResultMapAttribute(eq(element), eq(introspectedTable));
	}

	@Test
	public void shouldRenameResultMapOfClientSelectByExampleWithBLOBs() throws Exception {
		AlterResultMapPlugin plugin = spy(this.plugin);

		// Given

		// When
		boolean ok1 = plugin.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameResultMapAttribute(eq(method), eq(introspectedTable));
	}

	@Test
	public void shouldRenameResultMapOfClientSelectByExampleWithoutBLOBs() throws Exception {
		AlterResultMapPlugin plugin = spy(this.plugin);

		// Given

		// When
		boolean ok1 = plugin.clientSelectByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameResultMapAttribute(eq(method), eq(introspectedTable));
	}

	@Test
	public void shouldRenameResultMapOfClientSelectByPrimaryKey() throws Exception {
		AlterResultMapPlugin plugin = spy(this.plugin);

		// Given

		// When
		boolean ok1 = plugin.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientSelectByPrimaryKeyMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameResultMapAttribute(eq(method), eq(introspectedTable));
	}

	@Test
	public void shouldRenameResultMapOfClientSelectAll() throws Exception {
		AlterResultMapPlugin plugin = spy(this.plugin);

		// Given

		// When
		boolean ok1 = plugin.clientSelectAllMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientSelectAllMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameResultMapAttribute(eq(method), eq(introspectedTable));
	}

}
