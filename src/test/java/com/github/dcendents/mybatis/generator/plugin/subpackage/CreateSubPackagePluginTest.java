package com.github.dcendents.mybatis.generator.plugin.subpackage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
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
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * Tests for the class CreateSubPackagePlugin.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateSubPackagePluginTest {

	private CreateSubPackagePlugin plugin;

	@Mock
	private RenameProperties modelProperties;
	@Mock
	private RenameProperties mapperProperties;
	@Mock
	private RenameProperties exampleProperties;

	@Mock
	private IntrospectedTable introspectedTable;
	@Mock
	private Method method;
	@Mock
	private Document document;
	@Mock
	private XmlElement element;
	@Mock
	private Interface interfaze;
	@Mock
	private TopLevelClass topLevelClass;

	private static final String MODEL_PACKAGE = "gen1";
	private static final String MODEL_SUFFIX = "Gen2";
	private static final String MAPPER_PACKAGE = "gen3";
	private static final String MAPPER_SUFFIX = "Gen4";
	private static final String EXAMPLE_PACKAGE = "filter1";
	private static final String EXAMPLE_SUFFIX = "Filter2";

	@Before
	public void init() throws Exception {
		plugin = new CreateSubPackagePlugin(modelProperties, mapperProperties, exampleProperties);

		plugin.getProperties().put(CreateSubPackagePlugin.MODEL_PACKAGE_PROPERTY, MODEL_PACKAGE);
		plugin.getProperties().put(CreateSubPackagePlugin.MODEL_CLASS_SUFFIX_PROPERTY, MODEL_SUFFIX);
		plugin.getProperties().put(CreateSubPackagePlugin.MAPPER_PACKAGE_PROPERTY, MAPPER_PACKAGE);
		plugin.getProperties().put(CreateSubPackagePlugin.MAPPER_CLASS_SUFFIX_PROPERTY, MAPPER_SUFFIX);
		plugin.getProperties().put(CreateSubPackagePlugin.EXAMPLE_PACKAGE_PROPERTY, EXAMPLE_PACKAGE);
		plugin.getProperties().put(CreateSubPackagePlugin.EXAMPLE_CLASS_SUFFIX_PROPERTY, EXAMPLE_SUFFIX);

		given(document.getRootElement()).willReturn(element);
	}

	@Test
	public void shouldHaveDefaultConstructor() throws Exception {
		// Given

		// When
		CreateSubPackagePlugin instance = new CreateSubPackagePlugin();

		// Then
		assertThat(instance).isNotNull();
	}

	@Test
	public void shouldBeValidIfAtLeastOneConfigurationIsSet() throws Exception {
		// Given
		given(modelProperties.isEnabled()).willReturn(true);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = plugin.validate(warnings);

		// Then
		assertThat(ok).isTrue();
		assertThat(warnings).isEmpty();
		verify(modelProperties).validate(eq(MODEL_PACKAGE), eq(MODEL_SUFFIX));
		verify(mapperProperties).validate(eq(MAPPER_PACKAGE), eq(MAPPER_SUFFIX));
		verify(exampleProperties).validate(eq(EXAMPLE_PACKAGE), eq(EXAMPLE_SUFFIX));
	}

	@Test
	public void shouldNotBeValidIfAllPropertiesAreDisabled() throws Exception {
		// Given

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = plugin.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).isEmpty();
		verify(modelProperties).validate(eq(MODEL_PACKAGE), eq(MODEL_SUFFIX));
		verify(mapperProperties).validate(eq(MAPPER_PACKAGE), eq(MAPPER_SUFFIX));
		verify(exampleProperties).validate(eq(EXAMPLE_PACKAGE), eq(EXAMPLE_SUFFIX));
	}

	@Test
	public void shouldInitializeNewTypes() throws Exception {
		String baseRecordType = "baseRecordType";
		String newBaseRecordType = "newBaseRecordType";
		String javaMapperType = "javaMapperType";
		String newJavaMapperType = "newJavaMapperType";
		String exampleType = "exampleType";
		String newExampleType = "newExampleType";

		// Given
		given(introspectedTable.getBaseRecordType()).willReturn(baseRecordType);
		given(introspectedTable.getMyBatis3JavaMapperType()).willReturn(javaMapperType);
		given(introspectedTable.getExampleType()).willReturn(exampleType);

		given(modelProperties.setTypes(eq(baseRecordType))).willReturn(newBaseRecordType);
		given(mapperProperties.setTypes(eq(javaMapperType))).willReturn(newJavaMapperType);
		given(exampleProperties.setTypes(eq(exampleType))).willReturn(newExampleType);

		// When
		plugin.initialized(introspectedTable);

		// Then
		verify(introspectedTable).setBaseRecordType(eq(newBaseRecordType));
		verify(introspectedTable).setMyBatis3JavaMapperType(eq(newJavaMapperType));
		verify(introspectedTable).setExampleType(eq(newExampleType));
	}

	@Test
	public void shouldRenameMethodTypes() throws Exception {
		FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("returnType");
		FullyQualifiedJavaType newReturnType = new FullyQualifiedJavaType("newReturnType");

		// Given
		given(method.getReturnType()).willReturn(returnType);
		given(modelProperties.renameType(eq(returnType))).willReturn(newReturnType);

		// When
		plugin.renameMethod(method);

		// Then
		verify(method).setReturnType(eq(newReturnType));
	}

	@Test
	public void shouldRenameMethodParameters() throws Exception {
		FullyQualifiedJavaType type = new FullyQualifiedJavaType("type");
		FullyQualifiedJavaType renamedType = new FullyQualifiedJavaType("renamedType");

		Parameter parameter = new Parameter(type, "name", "annotation");
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(parameter);

		// Given
		given(method.getParameters()).willReturn(parameters);
		given(modelProperties.renameType(eq(type))).willReturn(renamedType);

		// When
		boolean ok = plugin.renameMethod(method);

		// Then
		assertThat(ok).isTrue();
		assertThat(parameters).hasSize(1);

		Parameter newParameter = parameters.get(0);
		assertThat(newParameter).isNotSameAs(parameter);
		assertThat(newParameter.getType()).isEqualTo(renamedType);
		assertThat(newParameter.getName()).isEqualTo(parameter.getName());
		assertThat(newParameter.getAnnotations()).containsExactlyElementsOf(parameter.getAnnotations());
	}

	@Test
	public void shouldIgnoreMethodParametersOfDifferentType() throws Exception {
		FullyQualifiedJavaType type = new FullyQualifiedJavaType("type");

		Parameter parameter = new Parameter(type, "name", "annotation");
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(parameter);

		// Given
		given(method.getParameters()).willReturn(parameters);
		given(modelProperties.renameType(eq(type))).willReturn(type);

		// When
		boolean ok = plugin.renameMethod(method);

		// Then
		assertThat(ok).isTrue();
		assertThat(parameters).hasSize(1);

		Parameter newParameter = parameters.get(0);
		assertThat(newParameter).isSameAs(parameter);
	}

	@Test
	public void shouldRenameMethodAnnotations() throws Exception {
		List<String> annotations = new ArrayList<>();
		annotations.add("@MultiLine({");
		annotations.add("\"	line1\",");
		annotations.add("\"	line2 type\",");
		annotations.add("\"	line3\",");
		annotations.add("})");
		annotations.add("@SingleLine(\"type\")");
		final int annotationsSize = annotations.size();

		// Given
		given(method.getAnnotations()).willReturn(annotations);

		// When
		boolean ok = plugin.renameMethod(method);

		// Then
		assertThat(ok).isTrue();
		assertThat(annotations).hasSize(annotationsSize);

		verify(modelProperties).renameAnnotations(eq(annotations));
		verify(mapperProperties).renameAnnotations(eq(annotations));
	}

	@Test
	public void shouldHandleElementWithoutAttribute() throws Exception {
		// Given

		// When
		boolean ok = plugin.renameElementAttribute(element, "");

		// Then
		assertThat(ok).isTrue();
	}

	@Test
	public void shouldIgnoreUnspecifiedElementAttribute() throws Exception {
		Attribute attribute = new Attribute("name", "value");
		List<Attribute> attributes = new ArrayList<>();
		attributes.add(attribute);

		// Given
		given(element.getAttributes()).willReturn(attributes);

		// When
		boolean ok = plugin.renameElementAttribute(element, "otherName");

		// Then
		assertThat(ok).isTrue();
		assertThat(attributes).hasSize(1);

		Attribute newAttribute = attributes.get(0);
		assertThat(newAttribute).isSameAs(attribute);
	}

	@Test
	public void shouldRenameModelElementAttribute() throws Exception {
		Attribute attribute = new Attribute("name", "value");
		List<Attribute> attributes = new ArrayList<>();
		attributes.add(attribute);

		Attribute renamedAttribute = new Attribute("name", "newValue");

		// Given
		given(element.getAttributes()).willReturn(attributes);
		given(modelProperties.renameAttribute(eq(attribute))).willReturn(renamedAttribute);
		given(mapperProperties.renameAttribute(eq(renamedAttribute))).willReturn(renamedAttribute);

		// When
		boolean ok = plugin.renameElementAttribute(element, "name");

		// Then
		assertThat(ok).isTrue();
		assertThat(attributes).hasSize(1);

		Attribute newAttribute = attributes.get(0);
		assertThat(newAttribute).isNotSameAs(attribute);
		assertThat(newAttribute).isSameAs(renamedAttribute);
	}

	@Test
	public void shouldRenameMapperElementAttribute() throws Exception {
		Attribute attribute = new Attribute("name", "value");
		List<Attribute> attributes = new ArrayList<>();
		attributes.add(attribute);

		Attribute renamedAttribute = new Attribute("name", "newValue");

		// Given
		given(element.getAttributes()).willReturn(attributes);
		given(modelProperties.renameAttribute(eq(attribute))).willReturn(attribute);
		given(mapperProperties.renameAttribute(eq(attribute))).willReturn(renamedAttribute);

		// When
		boolean ok = plugin.renameElementAttribute(element, "name");

		// Then
		assertThat(ok).isTrue();
		assertThat(attributes).hasSize(1);

		Attribute newAttribute = attributes.get(0);
		assertThat(newAttribute).isNotSameAs(attribute);
		assertThat(newAttribute).isSameAs(renamedAttribute);
	}

	@Test
	public void shouldRenameNamespaceOfSqlMap() throws Exception {
		CreateSubPackagePlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameElementAttribute(eq(element),
				eq(CreateSubPackagePlugin.ATTRIBUTE_NAMESPACE));

		// When
		boolean ok = plugin.sqlMapDocumentGenerated(document, null);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).renameElementAttribute(eq(element), eq(CreateSubPackagePlugin.ATTRIBUTE_NAMESPACE));
	}

	@Test
	public void shouldRenameTypeOfSqlMapWithoutBlobs() throws Exception {
		CreateSubPackagePlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameElementAttribute(eq(element),
				eq(CreateSubPackagePlugin.ATTRIBUTE_TYPE));

		// When
		boolean ok = plugin.sqlMapResultMapWithoutBLOBsElementGenerated(element, null);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).renameElementAttribute(eq(element), eq(CreateSubPackagePlugin.ATTRIBUTE_TYPE));
	}

	@Test
	public void shouldRenameTypeOfSqlMapWithBlobs() throws Exception {
		CreateSubPackagePlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameElementAttribute(eq(element),
				eq(CreateSubPackagePlugin.ATTRIBUTE_TYPE));

		// When
		boolean ok = plugin.sqlMapResultMapWithBLOBsElementGenerated(element, null);

		// Then
		assertThat(ok).isTrue();
		verify(plugin).renameElementAttribute(eq(element), eq(CreateSubPackagePlugin.ATTRIBUTE_TYPE));
	}

	@Test
	public void shouldRenameMethodOfClientSelectByPrimaryKey() throws Exception {
		CreateSubPackagePlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientSelectByPrimaryKeyMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameMethodOfClientCountByExample() throws Exception {
		CreateSubPackagePlugin plugin = spy(this.plugin);

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
		CreateSubPackagePlugin plugin = spy(this.plugin);

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
		CreateSubPackagePlugin plugin = spy(this.plugin);

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
	public void shouldRenameMethodOfClientInsert() throws Exception {
		CreateSubPackagePlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientInsertMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientInsertMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameMethodOfClientSelectByExampleWithBLOBs() throws Exception {
		CreateSubPackagePlugin plugin = spy(this.plugin);

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
		CreateSubPackagePlugin plugin = spy(this.plugin);

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
		CreateSubPackagePlugin plugin = spy(this.plugin);

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
		CreateSubPackagePlugin plugin = spy(this.plugin);

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
		CreateSubPackagePlugin plugin = spy(this.plugin);

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
	public void shouldRenameMethodOfClientUpdateByPrimaryKeySelective() throws Exception {
		CreateSubPackagePlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientUpdateByPrimaryKeySelectiveMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByPrimaryKeySelectiveMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameMethodOfClientUpdateByPrimaryKeyWithBLOBs() throws Exception {
		CreateSubPackagePlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameMethodOfClientUpdateByPrimaryKeyWithoutBLOBs() throws Exception {
		CreateSubPackagePlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameMethodOfClientInsertSelective() throws Exception {
		CreateSubPackagePlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientInsertSelectiveMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientInsertSelectiveMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldRenameMethodOfClientSelectAll() throws Exception {
		CreateSubPackagePlugin plugin = spy(this.plugin);

		// Given
		willReturn(true).given(plugin).renameMethod(method);

		// When
		boolean ok1 = plugin.clientSelectAllMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientSelectAllMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		assertThat(ok1).isTrue();
		assertThat(ok2).isTrue();
		verify(plugin, times(2)).renameMethod(eq(method));
	}

	@Test
	public void shouldAddImportedTypesToClient() throws Exception {
		String type = "someType";

		// Given
		given(modelProperties.getOriginalType()).willReturn(type);

		// When
		boolean ok = plugin.clientGenerated(interfaze, topLevelClass, introspectedTable);

		// Then
		assertThat(ok).isTrue();

		ArgumentCaptor<FullyQualifiedJavaType> typeCaptor = ArgumentCaptor.forClass(FullyQualifiedJavaType.class);

		verify(interfaze).addImportedType(typeCaptor.capture());
		FullyQualifiedJavaType interfaceType = typeCaptor.getValue();
		assertThat(interfaceType).isNotNull();
		assertThat(interfaceType.getFullyQualifiedName()).isEqualTo(type);

		verify(topLevelClass).addImportedType(typeCaptor.capture());
		FullyQualifiedJavaType classType = typeCaptor.getValue();
		assertThat(classType).isNotNull();
		assertThat(classType.getFullyQualifiedName()).isEqualTo(type);
	}

	@Test
	public void shouldHandleClientNullValues() throws Exception {
		String type = "someType";

		// Given
		given(modelProperties.getOriginalType()).willReturn(type);

		// When
		boolean ok = plugin.clientGenerated(null, null, null);

		// Then
		assertThat(ok).isTrue();
	}

	@Test
	public void shouldSetBaseClassAbstract() throws Exception {
		// Given
		given(modelProperties.isEnabled()).willReturn(true);

		// When
		boolean ok = plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(topLevelClass).setAbstract(eq(true));
	}

	@Test
	public void shouldNotSetBaseClassAbstractIfDisabled() throws Exception {
		// Given
		given(modelProperties.isEnabled()).willReturn(false);

		// When
		boolean ok = plugin.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);

		// Then
		assertThat(ok).isTrue();
		verify(topLevelClass, times(0)).setAbstract(anyBoolean());
	}
}
