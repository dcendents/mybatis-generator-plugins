package com.github.dcendents.mybatis.generator.plugin.rename;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

import com.github.bmsantos.core.cola.story.annotations.Features;
import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Group;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;
import com.github.dcendents.mybatis.generator.plugin.BaseColaTest;

/**
 * Tests for the class RenameExampleClassAndMethodsPlugin.
 */
@RunWith(CdiRunner.class)
@Features({ "RenameExampleClassAndMethodsPluginConfiguration", "RenameExampleClassAndMethodsPluginExecution",
		"RenameExampleClassAndMethodsPluginClientRename", "RenameExampleClassAndMethodsPluginSqlMapRename" })
public class RenameExampleClassAndMethodsPluginTest extends BaseColaTest {

	@Inject
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

	private List<String> warnings;
	private boolean validateResult;
	private boolean methodResult;
	private List<Parameter> methodParameters;
	private Parameter methodParameter;
	private List<Attribute> elementAttributes;
	private Attribute elementAttribute;
	private List<Element> elementElements;
	private List<String> idColumnsWithType;
	private Element removeIdColumnsElement;
	private int removeIdColumnsIndex;
	private List<IntrospectedColumn> primaryKeys;

	private static final String CLASS_SEARCH = "Example";
	private static final String CLASS_REPLACE = "Filter";
	private static final String PARAM_SEARCH = "example";
	private static final String PARAM_REPLACE = "filter";

	private static final String FORMAT = "Some%1$sWith%1$sInName";
	private static final String MATCHING_CLASS = String.format(FORMAT, CLASS_SEARCH);
	private static final String REPLACED_CLASS = String.format(FORMAT, CLASS_REPLACE);

	private static final String TEXT_ELEMENT_FORMAT = "some content with %s and some more";

	@Before
	public void init() throws Exception {
		warnings = new ArrayList<>();
		methodParameters = new ArrayList<>();
		elementAttributes = new ArrayList<>();
		elementElements = new ArrayList<>();
		idColumnsWithType = new ArrayList<>();
		primaryKeys = new ArrayList<>();
		removeIdColumnsIndex = -1;
	}

	@Given("the class search is properly configured")
	public void configureThePluginClassSearchProperty() throws Exception {
		plugin.getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_SEARCH_PROPERTY, CLASS_SEARCH);
	}

	@Given("the class replace is properly configured")
	public void configureThePluginClassReplaceProperty() throws Exception {
		plugin.getProperties().put(RenameExampleClassAndMethodsPlugin.CLASS_REPLACE_PROPERTY, CLASS_REPLACE);
	}

	@Given("the param search is properly configured")
	public void configureThePluginParamSearchProperty() throws Exception {
		plugin.getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_SEARCH_PROPERTY, PARAM_SEARCH);
	}

	@Given("the param replace is properly configured")
	public void configureThePluginParamReplaceProperty() throws Exception {
		plugin.getProperties().put(RenameExampleClassAndMethodsPlugin.PARAM_REPLACE_PROPERTY, PARAM_REPLACE);
	}

	@Given("the introspected table will return the table configuration")
	public void configureIntrospectedTable() throws Exception {
		given(introspectedTable.getTableConfiguration()).willReturn(tableConfiguration);
	}

	@Given("the introspected table will return a list of primary keys")
	public void configureIntrospectedTablePrimaryKeys() throws Exception {
		given(introspectedTable.getPrimaryKeyColumns()).willReturn(primaryKeys);
	}

	@Given("the example type of the introspected table matches the class search parameter")
	public void configureIntrospectedTableExampleType() throws Exception {
		given(introspectedTable.getExampleType()).willReturn(MATCHING_CLASS);
	}

	@Given("the method name matches the class search parameter")
	public void configureMethodName() throws Exception {
		given(method.getName()).willReturn(MATCHING_CLASS);
	}

	@Given("the method has a parameter matching the param search")
	public void configureMethodMatchingParameter() throws Exception {
		methodParameter = new Parameter(new FullyQualifiedJavaType("type"), PARAM_SEARCH, "annotation");
		methodParameters.add(methodParameter);
	}

	@Given("the method has a parameter not matching the param search")
	public void configureMethodOtherParameter() throws Exception {
		methodParameter = new Parameter(new FullyQualifiedJavaType("type"), "other", "annotation");
		methodParameters.add(methodParameter);
	}

	@Given("the method will return a list of parameters")
	public void methodWillReturnListOfAnnotations() throws Exception {
		given(method.getParameters()).willReturn(methodParameters);
	}

	@Given("the element will return a list of attributes")
	public void elementWillReturnListOfAttributes() throws Exception {
		given(element.getAttributes()).willReturn(elementAttributes);
	}

	@Given("the element will return a list of elements")
	public void elementWillReturnListOfElements() throws Exception {
		given(element.getElements()).willReturn(elementElements);
	}

	@Given("the element has some other attribute")
	public void configureElementOtherAttribute() throws Exception {
		elementAttribute = new Attribute("name", "value");
		elementAttributes.add(elementAttribute);
	}

	@Given("the element has an attribute matching the class search")
	public void configureElementMatchingAttribute() throws Exception {
		elementAttribute = new Attribute(RenameExampleClassAndMethodsPlugin.XML_ID_ATTRIBUTE, MATCHING_CLASS);
		elementAttributes.add(elementAttribute);
	}

	@Given("the removeIdColumns element is textElement")
	public void setRemoveIdColumnsElementToTextElement() throws Exception {
		removeIdColumnsElement = textElement;
	}

	@Given("the removeIdColumns element is element")
	public void setRemoveIdColumnsElementToXmlElement() throws Exception {
		removeIdColumnsElement = element;
	}

	@Given("the removeIdColumns element is a mock")
	public void setRemoveIdColumnsElementToMock() throws Exception {
		removeIdColumnsElement = mock(Element.class);
	}

	@Given("an id column with alias:(\\w+), name:(\\w+), property:(\\w+), type:(\\w+)")
	public void addIdColumnsWithType(@Group(1) final String alias, @Group(2) final String name,
			@Group(3) final String property, @Group(4) final String type) throws Exception {
		String aliasWithDot = alias + ".";
		idColumnsWithType.add(String.format("%4$s%1$s = #{record.%2$s,jdbcType=%3$s},", name, property, type,
				aliasWithDot));
	}

	@Given("a primary key column with name:(\\w+), property:(\\w+), type:(\\w+)")
	public void addPrimaryKeyColumn(@Group(1) final String name, @Group(2) final String property,
			@Group(3) final String type) throws Exception {
		IntrospectedColumn column = new IntrospectedColumn();
		column.setActualColumnName(name);
		column.setJavaProperty(property);
		column.setJdbcTypeName(type);
		primaryKeys.add(column);
	}

	@Given("the text element content does not contain the id column")
	public void textElementContentDoesNotContainIdColumn() throws Exception {
		given(textElement.getContent()).willReturn(String.format(TEXT_ELEMENT_FORMAT, StringUtils.EMPTY));
	}

	@Given("the text element content contains the id column at position (\\d+)")
	public void textElementContentContainsIdColumn(@Group(1) final Integer position) throws Exception {
		given(textElement.getContent()).willReturn(String.format(TEXT_ELEMENT_FORMAT, idColumnsWithType.get(position)));
	}

	@Given("the elements list contains the text element")
	public void addTextElementToElementElements() throws Exception {
		elementElements.add(textElement);
	}

	@Given("the removeIdColumnsIndex is (\\d+)")
	public void addTextElementToElementElements(@Group(1) final Integer value) throws Exception {
		removeIdColumnsIndex = value;
	}

	@Given("the table configuration alias is (\\w+)")
	public void configureTableAlias(@Group(1) final String alias) throws Exception {
		given(tableConfiguration.getAlias()).willReturn(alias);
	}

	@Given("the validate method has been called")
	@When("the validate method is called")
	public void validateThePlugin() throws Exception {
		validateResult = plugin.validate(warnings);
	}

	@When("the initialized method is called")
	public void invokeInitialized() throws Exception {
		plugin.initialized(introspectedTable);
	}

	@When("the renameMethod method is called")
	public void invokeRenameMethod() throws Exception {
		methodResult = plugin.renameMethod(method);
	}

	@When("the renameElement method is called")
	public void invokeRenameElement() throws Exception {
		methodResult = plugin.renameElement(element);
	}

	@When("the removeIdColumnsForList method is called")
	public void invokeRemoveIdColumnsForList() throws Exception {
		plugin.removeIdColumns(idColumnsWithType, removeIdColumnsElement, element, removeIdColumnsIndex);
	}

	@When("the removeIdColumnsForTable method is called")
	public void invokeRemoveIdColumnsForTable() throws Exception {
		plugin.removeIdColumns(introspectedTable, element);
	}
	
	@When("the clientCountByExampleMethodGenerated method for interface is called")
	public void invokeClientCountByExampleMethodGeneratedForInterface() throws Exception {
		methodResult = plugin.clientCountByExampleMethodGenerated(method, interfaze, introspectedTable);
	}
	
	@When("the clientDeleteByExampleMethodGenerated method for interface is called")
	public void invokeClientDeleteByExampleMethodGeneratedForInterface() throws Exception {
		methodResult = plugin.clientDeleteByExampleMethodGenerated(method, interfaze, introspectedTable);
	}
	
	@When("the clientDeleteByPrimaryKeyMethodGenerated method for interface is called")
	public void invokeClientDeleteByPrimaryKeyMethodGeneratedForInterface() throws Exception {
		methodResult = plugin.clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
	}
	
	@When("the clientSelectByExampleWithBLOBsMethodGenerated method for interface is called")
	public void invokeClienSelectByExampleWithBLOBsMethodGeneratedForInterface() throws Exception {
		methodResult = plugin.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
	}
	
	@When("the clientSelectByExampleWithoutBLOBsMethodGenerated method for interface is called")
	public void invokeClientSelectByExampleWithoutBLOBsMethodGeneratedForInterface() throws Exception {
		methodResult = plugin.clientSelectByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
	}
	
	@When("the clientUpdateByExampleSelectiveMethodGenerated method for interface is called")
	public void invokeClientUpdateByExampleSelectiveMethodGeneratedForInterface() throws Exception {
		methodResult = plugin.clientUpdateByExampleSelectiveMethodGenerated(method, interfaze, introspectedTable);
	}
	
	@When("the clientUpdateByExampleWithBLOBsMethodGenerated method for interface is called")
	public void invokeClientUpdateByExampleWithBLOBsMethodGeneratedForInterface() throws Exception {
		methodResult = plugin.clientUpdateByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
	}
	
	@When("the clientUpdateByExampleWithoutBLOBsMethodGenerated method for interface is called")
	public void invokeClientUpdateByExampleWithoutBLOBsMethodGeneratedForInterface() throws Exception {
		methodResult = plugin.clientUpdateByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
	}
	
	@When("the clientCountByExampleMethodGenerated method for table is called")
	public void invokeClientCountByExampleMethodGeneratedForTable() throws Exception {
		methodResult = plugin.clientCountByExampleMethodGenerated(method, topLevelClass, introspectedTable);
	}
	
	@When("the clientDeleteByExampleMethodGenerated method for table is called")
	public void invokeClientDeleteByExampleMethodGeneratedForTable() throws Exception {
		methodResult = plugin.clientDeleteByExampleMethodGenerated(method, topLevelClass, introspectedTable);
	}
	
	@When("the clientDeleteByPrimaryKeyMethodGenerated method for table is called")
	public void invokeClientDeleteByPrimaryKeyMethodGeneratedForTable() throws Exception {
		methodResult = plugin.clientDeleteByPrimaryKeyMethodGenerated(method, topLevelClass, introspectedTable);
	}
	
	@When("the clientSelectByExampleWithBLOBsMethodGenerated method for table is called")
	public void invokeClienSelectByExampleWithBLOBsMethodGeneratedForTable() throws Exception {
		methodResult = plugin.clientSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
	}
	
	@When("the clientSelectByExampleWithoutBLOBsMethodGenerated method for table is called")
	public void invokeClientSelectByExampleWithoutBLOBsMethodGeneratedForTable() throws Exception {
		methodResult = plugin.clientSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
	}
	
	@When("the clientUpdateByExampleSelectiveMethodGenerated method for table is called")
	public void invokeClientUpdateByExampleSelectiveMethodGeneratedForTable() throws Exception {
		methodResult = plugin.clientUpdateByExampleSelectiveMethodGenerated(method, topLevelClass, introspectedTable);
	}
	
	@When("the clientUpdateByExampleWithBLOBsMethodGenerated method for table is called")
	public void invokeClientUpdateByExampleWithBLOBsMethodGeneratedForTable() throws Exception {
		methodResult = plugin.clientUpdateByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
	}
	
	@When("the clientUpdateByExampleWithoutBLOBsMethodGenerated method for table is called")
	public void invokeClientUpdateByExampleWithoutBLOBsMethodGeneratedForTable() throws Exception {
		methodResult = plugin.clientUpdateByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
	}
	
	@When("the sqlMapCountByExampleElementGenerated method is called")
	public void invokeSqlMapCountByExampleElementGenerated() throws Exception {
		methodResult = plugin.sqlMapCountByExampleElementGenerated(element, introspectedTable);
	}
	
	@When("the sqlMapDeleteByExampleElementGenerated method is called")
	public void invokeSqlMapDeleteByExampleElementGenerated() throws Exception {
		methodResult = plugin.sqlMapDeleteByExampleElementGenerated(element, introspectedTable);
	}
	
	@When("the sqlMapSelectByExampleWithoutBLOBsElementGenerated method is called")
	public void invokeSqlMapSelectByExampleWithoutBLOBsElementGenerated() throws Exception {
		methodResult = plugin.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
	}
	
	@When("the sqlMapSelectByExampleWithBLOBsElementGenerated method is called")
	public void invokeSqlMapSelectByExampleWithBLOBsElementGenerated() throws Exception {
		methodResult = plugin.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);
	}
	
	@When("the sqlMapUpdateByExampleSelectiveElementGenerated method is called")
	public void invokeSqlMapUpdateByExampleSelectiveElementGenerated() throws Exception {
		methodResult = plugin.sqlMapUpdateByExampleSelectiveElementGenerated(element, introspectedTable);
	}
	
	@When("the sqlMapUpdateByExampleWithBLOBsElementGenerated method is called")
	public void invokeSqlMapUpdateByExampleWithBLOBsElementGenerated() throws Exception {
		methodResult = plugin.sqlMapUpdateByExampleWithBLOBsElementGenerated(element, introspectedTable);
	}
	
	@When("the sqlMapUpdateByExampleWithoutBLOBsElementGenerated method is called")
	public void invokeSqlMapUpdateByExampleWithoutBLOBsElementGenerated() throws Exception {
		methodResult = plugin.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
	}
	
	@Then("validate should return (true|false)")
	public void verifyValidateReturn(@Group(1) final Boolean validate) throws Exception {
		assertThat(validateResult).isEqualTo(validate);
	}

	@Then("validate should have produced (\\d+) warnings")
	public void verifyValidateWarnings(@Group(1) final Integer noWarnings) throws Exception {
		assertThat(warnings).hasSize(noWarnings);
	}

	@Then("the example type of the introspected table will be renamed")
	public void verifyExampleTypeOfIntrospectedTable() throws Exception {
		verify(introspectedTable).setExampleType(eq(REPLACED_CLASS));
	}

	@Then("the method will return (true|false)")
	public void verifyMethodReturn(@Group(1) final Boolean validate) throws Exception {
		assertThat(methodResult).isEqualTo(validate);
	}

	@Then("the method name will be renamed")
	public void verifyMethodName() throws Exception {
		verify(method).setName(eq(REPLACED_CLASS));
	}

	@Then("the method parameters size will be (\\d+)")
	public void verifyMethodParametersSize(@Group(1) final Integer size) throws Exception {
		assertThat(methodParameters).hasSize(size);
	}

	@Then("the parameter at position (\\d+) will not be the same")
	public void verifyMethodParameterIsNotSame(@Group(1) final Integer position) throws Exception {
		assertThat(methodParameters.get(position)).isNotSameAs(methodParameter);
	}

	@Then("the parameter at position (\\d+) will be the same")
	public void verifyMethodParameterIsTheSame(@Group(1) final Integer position) throws Exception {
		assertThat(methodParameters.get(position)).isSameAs(methodParameter);
	}

	@Then("the type of the parameter at position (\\d+) will be the same")
	public void verifyMethodParameterTypeIsSame(@Group(1) final Integer position) throws Exception {
		assertThat(methodParameters.get(position).getType()).isEqualTo(methodParameter.getType());
	}

	@Then("the name of the parameter at position (\\d+) will be renamed")
	public void verifyMethodParameterName(@Group(1) final Integer position) throws Exception {
		assertThat(methodParameters.get(position).getName()).isEqualTo(PARAM_REPLACE);
	}

	@Then("the annotations of the parameter at position (\\d+) will be identical")
	public void verifyMethodParameterAnnotations(@Group(1) final Integer position) throws Exception {
		assertThat(methodParameters.get(position).getAnnotations()).containsExactlyElementsOf(
				methodParameter.getAnnotations());
	}

	@Then("the element attributes size will be (\\d+)")
	public void verifyElementAttributesSize(@Group(1) final Integer size) throws Exception {
		assertThat(elementAttributes).hasSize(size);
	}

	@Then("the attribute at position (\\d+) will be the same")
	public void verifyElementAttributeIsTheSame(@Group(1) final Integer position) throws Exception {
		assertThat(elementAttributes.get(position)).isSameAs(elementAttribute);
	}

	@Then("the attribute at position (\\d+) will not be the same")
	public void verifyElementAttributeIsNotTheSame(@Group(1) final Integer position) throws Exception {
		assertThat(elementAttributes.get(position)).isNotSameAs(elementAttribute);
	}

	@Then("the attribute name at position (\\d+) will be the same")
	public void verifyElementAttributeNameIsSame(@Group(1) final Integer position) throws Exception {
		assertThat(elementAttributes.get(position).getName()).isEqualTo(elementAttribute.getName());
	}

	@Then("the attribute value at position (\\d+) will be renamed")
	public void verifyElementAttributeValue(@Group(1) final Integer position) throws Exception {
		assertThat(elementAttributes.get(position).getValue()).isEqualTo(REPLACED_CLASS);
	}

	@Then("the method getElements of element will have been called (\\d+) times")
	public void verifyTimesGetElementsOfElementCalled(@Group(1) final Integer noTimes) throws Exception {
		verify(element, times(noTimes)).getElements();
	}

	@Then("the element elements size will be (\\d+)")
	public void verifyElementElementsSize(@Group(1) final Integer size) throws Exception {
		assertThat(elementElements).hasSize(size);
	}

	@Then("the element at position (\\d+) is not the same as the text element")
	public void verifyElementIsNotTextElement(@Group(1) final Integer position) throws Exception {
		assertThat(elementElements.get(position)).isNotSameAs(textElement);
	}

	@Then("the element at position (\\d+) is a text element")
	public void verifyElementIsATextElement(@Group(1) final Integer position) throws Exception {
		assertThat(elementElements.get(position)).isInstanceOf(TextElement.class);
	}

	@Then("the content of element at position (\\d+) does not contain the id column")
	public void verifyTextElementDoesNotContainIdColumn(@Group(1) final Integer position) throws Exception {
		TextElement textElem = (TextElement) elementElements.get(position);
		assertThat(textElem.getContent()).isEqualTo(String.format(TEXT_ELEMENT_FORMAT, StringUtils.EMPTY));
	}

}
