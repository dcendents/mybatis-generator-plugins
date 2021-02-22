package com.github.dcendents.mybatis.generator.plugin.rename;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.TableConfiguration;

/**
 * Tests for the class RenameExampleClassAndMethodsPlugin.
 */
@RunWith(MockitoJUnitRunner.class)
public class RenameExampleClassAndMethodsPluginTest {

	private RenameExampleClassAndMethodsPlugin plugin;

	@Mock
	private IntrospectedTable introspectedTable;
	@Mock
	private TableConfiguration tableConfiguration;
	@Mock
	private Method method;
	@Mock
	private XmlElement element;
	@Mock
	private TextElement textElement;
	@Mock
	private Interface interfaze;
	@Mock
	private TopLevelClass topLevelClass;

	private static final String CLASS_SEARCH = "Example";
	private static final String CLASS_REPLACE = "Filter";
	private static final String PARAM_SEARCH = "example";
	private static final String PARAM_REPLACE = "filter";

	@Before
	public void init() throws Exception {
		plugin = new RenameExampleClassAndMethodsPlugin();
		plugin.getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_SEARCH_PROPERTY, CLASS_SEARCH);
		plugin.getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_REPLACE_PROPERTY, CLASS_REPLACE);
		plugin.getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_SEARCH_PROPERTY, PARAM_SEARCH);
		plugin.getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_REPLACE_PROPERTY, PARAM_REPLACE);
		plugin.validate(new ArrayList<String>());

		given(introspectedTable.getTableConfiguration()).willReturn(tableConfiguration);
	}

	@Test
	public void shouldHaveDefaultConstructor() throws Exception {
		// Given

		// When
		RenameExampleClassAndMethodsPlugin instance = new RenameExampleClassAndMethodsPlugin();

		// Then
		assertThat(instance).isNotNull();
	}

	@Test
	public void shouldBeValidWhenAllParametersAreSet() throws Exception {
		// Given
		RenameExampleClassAndMethodsPlugin instance = new RenameExampleClassAndMethodsPlugin();
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_SEARCH_PROPERTY, CLASS_SEARCH);
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_REPLACE_PROPERTY, CLASS_REPLACE);
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_SEARCH_PROPERTY, PARAM_SEARCH);
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_REPLACE_PROPERTY, PARAM_REPLACE);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isTrue();
		assertThat(warnings).isEmpty();
	}

	@Test
	public void shouldBeInvalidWhenClassSearchIsNotSet() throws Exception {
		// Given
		RenameExampleClassAndMethodsPlugin instance = new RenameExampleClassAndMethodsPlugin();
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_REPLACE_PROPERTY, CLASS_REPLACE);
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_SEARCH_PROPERTY, PARAM_SEARCH);
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_REPLACE_PROPERTY, PARAM_REPLACE);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(1);
	}

	@Test
	public void shouldBeInvalidWhenClassReplaceIsNotSet() throws Exception {
		// Given
		RenameExampleClassAndMethodsPlugin instance = new RenameExampleClassAndMethodsPlugin();
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_SEARCH_PROPERTY, CLASS_SEARCH);
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_SEARCH_PROPERTY, PARAM_SEARCH);
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_REPLACE_PROPERTY, PARAM_REPLACE);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(1);
	}

	@Test
	public void shouldBeInvalidWhenPAramSearchIsNotSet() throws Exception {
		// Given
		RenameExampleClassAndMethodsPlugin instance = new RenameExampleClassAndMethodsPlugin();
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_SEARCH_PROPERTY, CLASS_SEARCH);
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_REPLACE_PROPERTY, CLASS_REPLACE);
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_REPLACE_PROPERTY, PARAM_REPLACE);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(1);
	}

	@Test
	public void shouldBeInvalidWhenParamReplaceIsNotSet() throws Exception {
		// Given
		RenameExampleClassAndMethodsPlugin instance = new RenameExampleClassAndMethodsPlugin();
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_SEARCH_PROPERTY, CLASS_SEARCH);
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_REPLACE_PROPERTY, CLASS_REPLACE);
		instance.getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_SEARCH_PROPERTY, PARAM_SEARCH);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(1);
	}

	@Test
	public void shouldRenameTheExampleType() throws Exception {
		// Given
		given(introspectedTable.getExampleType()).willReturn(String.format("Some%1$sWith%1$sInName", CLASS_SEARCH));

		// When
		plugin.initialized(introspectedTable);

		// Then
		verify(introspectedTable).setExampleType(eq(String.format("Some%1$sWith%1$sInName", CLASS_REPLACE)));
	}

	@Test
	public void shouldRenameTheMethodName() throws Exception {
		// Given
		given(method.getName()).willReturn(String.format("Some%1$sWith%1$sInName", CLASS_SEARCH));

		// When
		boolean ok = plugin.renameMethod(method);

		// Then
		assertThat(ok).isTrue();
		verify(method).setName(eq(String.format("Some%1$sWith%1$sInName", CLASS_REPLACE)));
	}

	@Test
	public void shouldRenameMethodParameters() throws Exception {
		FullyQualifiedJavaType type = new FullyQualifiedJavaType("type");

		Parameter parameter = new Parameter(type, PARAM_SEARCH, "annotation");
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(parameter);

		// Given
		given(method.getName()).willReturn(String.format("Some%1$sWith%1$sInName", CLASS_SEARCH));
		given(method.getParameters()).willReturn(parameters);

		// When
		boolean ok = plugin.renameMethod(method);

		// Then
		assertThat(ok).isTrue();
		assertThat(parameters).hasSize(1);

		Parameter newParameter = parameters.get(0);
		assertThat(newParameter).isNotSameAs(parameter);
		assertThat(newParameter.getType()).isEqualTo(type);
		assertThat(newParameter.getName()).isEqualTo(PARAM_REPLACE);
		assertThat(newParameter.getAnnotations()).containsExactlyElementsOf(parameter.getAnnotations());
	}

	@Test
	public void shouldIgnoreMethodParametersThatDontMatch() throws Exception {
		FullyQualifiedJavaType type = new FullyQualifiedJavaType("type");

		Parameter parameter = new Parameter(type, "someName", "annotation");
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(parameter);

		// Given
		given(method.getName()).willReturn(String.format("Some%1$sWith%1$sInName", CLASS_SEARCH));
		given(method.getParameters()).willReturn(parameters);

		// When
		boolean ok = plugin.renameMethod(method);

		// Then
		assertThat(ok).isTrue();
		assertThat(parameters).hasSize(1);

		Parameter newParameter = parameters.get(0);
		assertThat(newParameter).isSameAs(parameter);
	}

	@Test
	public void shouldHandleElementWithoutAttribute() throws Exception {
		// Given

		// When
		boolean ok = plugin.renameElement(element);

		// Then
		assertThat(ok).isTrue();
	}

	@Test
	public void shouldIgnoreOtherElementAttributes() throws Exception {
		Attribute attribute = new Attribute("name", "value");
		List<Attribute> attributes = new ArrayList<>();
		attributes.add(attribute);

		// Given
		given(element.getAttributes()).willReturn(attributes);

		// When
		boolean ok = plugin.renameElement(element);

		// Then
		assertThat(ok).isTrue();
		assertThat(attributes).hasSize(1);

		Attribute newAttribute = attributes.get(0);
		assertThat(newAttribute).isSameAs(attribute);
	}

	@Test
	public void shouldRenameElementId() throws Exception {
		Attribute attribute = new Attribute(RenameExampleClassAndMethodsPlugin.XML_ID_ATTRIBUTE,
				String.format("Some%1$sWith%1$sInName", CLASS_SEARCH));
		List<Attribute> attributes = new ArrayList<>();
		attributes.add(attribute);

		// Given
		given(element.getAttributes()).willReturn(attributes);

		// When
		boolean ok = plugin.renameElement(element);

		// Then
		assertThat(ok).isTrue();
		assertThat(attributes).hasSize(1);

		Attribute newAttribute = attributes.get(0);
		assertThat(newAttribute).isNotSameAs(attribute);
		assertThat(newAttribute).isNotEqualTo(attribute);
		assertThat(newAttribute.getName()).isEqualTo(RenameExampleClassAndMethodsPlugin.XML_ID_ATTRIBUTE);
		assertThat(newAttribute.getValue()).isEqualTo(String.format("Some%1$sWith%1$sInName", CLASS_REPLACE));
	}

	@Test
	public void shouldIgnoreTablesWithoutIdColumns() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		given(introspectedTable.getPrimaryKeyColumns()).willReturn(new ArrayList<IntrospectedColumn>());

		// When
		plugin.removeIdColumns(introspectedTable, element);

		// Then
		verify(plugin, times(0)).removeIdColumns(anyList(), any(Element.class), any(XmlElement.class), anyInt());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void shouldRemoveIdColumnsFromUpdateStatements() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		IntrospectedColumn column = new IntrospectedColumn();
		column.setActualColumnName("actual_name");
		column.setJavaProperty("someProperty");
		column.setJdbcTypeName("DOUBLE");

		List<IntrospectedColumn> columns = new ArrayList<>();
		columns.add(column);

		// Given
		given(introspectedTable.getPrimaryKeyColumns()).willReturn(columns);
		given(tableConfiguration.getAlias()).willReturn("alias");

		// When
		plugin.removeIdColumns(introspectedTable, element);

		// Then
		ArgumentCaptor<List> updatesCaptor = ArgumentCaptor.forClass(List.class);

		verify(plugin, times(1)).removeIdColumns(updatesCaptor.capture(), eq(element), isNull(XmlElement.class),
				eq(-1));

		List<String> updates = updatesCaptor.getValue();
		assertThat(updates).hasSameSizeAs(columns);
		String update = updates.get(0);
		assertThat(update).isEqualTo("alias.actual_name = #{record.someProperty,jdbcType=DOUBLE},");
	}

	@Test
	public void shouldIgnoreOtherElementSubTypes() throws Exception {
		// Given
		Element theElement = mock(Element.class);

		// When
		plugin.removeIdColumns(null, theElement, null, -1);

		// Then
	}

	@Test
	public void shouldHandleEmptyUpdateList() throws Exception {
		// Given

		// When
		plugin.removeIdColumns(new ArrayList<String>(), textElement, element, 1);

		// Then
		verify(element, times(0)).getElements();
	}

	@Test
	public void shouldIgnoreTextElementWithoutMatch() throws Exception {
		List<String> updates = new ArrayList<>();
		updates.add("alias.actual_name = #{record.someProperty,jdbcType=DOUBLE},");

		// Given
		given(textElement.getContent()).willReturn("some content");

		// When
		plugin.removeIdColumns(updates, textElement, element, 1);

		// Then
		verify(element, times(0)).getElements();
	}

	@Test
	public void shouldRemoveIdUpdatesFromTextElements() throws Exception {
		String update = "alias.actual_name = #{record.someProperty,jdbcType=DOUBLE},";
		List<String> updates = new ArrayList<>();
		updates.add(update);

		List<Element> elements = new ArrayList<>();
		elements.add(textElement);

		String textContent = "some content with " + update + " and some more";

		// Given
		given(textElement.getContent()).willReturn(textContent);
		given(element.getElements()).willReturn(elements);

		// When
		plugin.removeIdColumns(updates, textElement, element, 0);

		// Then
		verify(element, times(1)).getElements();
		assertThat(elements).hasSize(1);

		TextElement newTextElement = (TextElement) elements.get(0);
		assertThat(newTextElement).isNotSameAs(textElement);
		assertThat(newTextElement.getContent()).isEqualTo("some content with  and some more");
	}

	@Test
	public void shouldHandleElementWithoutChildren() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given

		// When
		plugin.removeIdColumns(null, element, null, -1);

		// Then
		verify(plugin, times(0)).removeIdColumns(anyList(), any(Element.class), eq(element), anyInt());
	}

	@Test
	public void shouldProcessAllChildrenOfAnXmlElement() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		List<Element> elements = new ArrayList<>();
		elements.add(textElement);

		// Given
		willDoNothing().given(plugin).removeIdColumns(isNull(), any(Element.class), eq(element), anyInt());
		given(element.getElements()).willReturn(elements);

		// When
		plugin.removeIdColumns(null, element, null, -1);

		// Then
		verify(plugin, times(1)).removeIdColumns(isNull(), eq(textElement), eq(element), eq(0));
	}

	@Test
	public void shouldRenameMethodOfClientCountByExample() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientCountByExampleMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientCountByExampleMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameMethodOfClientDeleteByExample() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientDeleteByExampleMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientDeleteByExampleMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameMethodOfClientDeleteByPrimaryKey() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientDeleteByPrimaryKeyMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameMethodOfClientSelectByExampleWithBLOBs() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameMethodOfClientSelectByExampleWithoutBLOBs() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientSelectByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameMethodOfClientUpdateByExampleSelective() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientUpdateByExampleSelectiveMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByExampleSelectiveMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameMethodOfClientUpdateByExampleWithBLOBs() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientUpdateByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameMethodOfClientUpdateByExampleWithoutBLOBs() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientUpdateByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameElementOfSqlMapCountByExample() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameElement(element);

		// When
		boolean ok = plugin.sqlMapCountByExampleElementGenerated(element, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).renameElement(eq(element));
	}

	@Test
	public void shouldRenameElementOfSqlMapDeleteByExample() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameElement(element);

		// When
		boolean ok = plugin.sqlMapDeleteByExampleElementGenerated(element, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).renameElement(eq(element));
	}

	@Test
	public void shouldRenameElementOfSqlMapSelectByExampleWithoutBLOBs() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameElement(element);

		// When
		boolean ok = plugin.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).renameElement(eq(element));
	}

	@Test
	public void shouldRenameElementOfSqlMapSelectByExampleWithBLOBs() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameElement(element);

		// When
		boolean ok = plugin.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).renameElement(eq(element));
	}

	@Test
	public void shouldRenameElementOfSqlMapUpdateByExampleSelective() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameElement(element);
		willDoNothing().given(plugin).removeIdColumns(any(IntrospectedTable.class), any(XmlElement.class));

		// When
		boolean ok = plugin.sqlMapUpdateByExampleSelectiveElementGenerated(element, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).removeIdColumns(eq(introspectedTable), eq(element));
		verify(plugin).renameElement(eq(element));
	}

	@Test
	public void shouldRenameElementOfSqlMapUpdateByExampleWithBLOBs() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameElement(element);
		willDoNothing().given(plugin).removeIdColumns(any(IntrospectedTable.class), any(XmlElement.class));

		// When
		boolean ok = plugin.sqlMapUpdateByExampleWithBLOBsElementGenerated(element, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).removeIdColumns(eq(introspectedTable), eq(element));
		verify(plugin).renameElement(eq(element));
	}

	@Test
	public void shouldRenameElementOfSqlMapUpdateByExampleWithoutBLOBs() throws Exception {
		RenameExampleClassAndMethodsPlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameElement(element);
		willDoNothing().given(plugin).removeIdColumns(any(IntrospectedTable.class), any(XmlElement.class));

		// When
		boolean ok = plugin.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).removeIdColumns(eq(introspectedTable), eq(element));
		verify(plugin).renameElement(eq(element));
	}
}
