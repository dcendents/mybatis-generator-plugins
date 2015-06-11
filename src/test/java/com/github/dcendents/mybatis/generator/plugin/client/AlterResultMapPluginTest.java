package com.github.dcendents.mybatis.generator.plugin.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.github.bmsantos.core.cola.story.annotations.Features;
import com.github.bmsantos.core.cola.story.annotations.Given;
import com.github.bmsantos.core.cola.story.annotations.Group;
import com.github.bmsantos.core.cola.story.annotations.Then;
import com.github.bmsantos.core.cola.story.annotations.When;
import com.github.dcendents.mybatis.generator.plugin.BaseColaTest;

/**
 * Tests for the class AlterResultMapPlugin.
 */
@RunWith(CdiRunner.class)
@Features({ "AlterResultMapPluginConfiguration", "AlterResultMapPluginExecution"})
public class AlterResultMapPluginTest extends BaseColaTest {

	@Inject
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
	private List<String> warnings;
	private List<Attribute> attributes;
	private List<String> annotations;
	private boolean validateResult;
	private boolean generatedResult;

	private static final String TABLE_NAME = "table_name";
	private static final String RESULT_MAP_ID = "FullResultMap";
	private static final String ELEMENT_RESULT_MAP_ID = "BaseResultMap";
	
	@Before
	public void init() throws Exception {
		warnings = new ArrayList<>();
		attributes = new ArrayList<>();
		annotations = new ArrayList<>();
	}
	
	@Given("the element will return a list of attributes")
	public void elementWillReturnListOfAttributes() throws Exception {
		given(element.getAttributes()).willReturn(attributes);
	}
	
	@Given("the method will return a list of annotations")
	public void methodWillReturnListOfAnnotations() throws Exception {
		given(method.getAnnotations()).willReturn(annotations);
	}
	
	@Given("the table name is properly configured")
	public void configureThePluginTableNameProperty() throws Exception {
		plugin.getProperties().put(AlterResultMapPlugin.TABLE_NAME, TABLE_NAME);
	}
	
	@Given("the result map id is properly configured")
	public void configureThePluginResultMapIdProperty() throws Exception {
		plugin.getProperties().put(AlterResultMapPlugin.RESULT_MAP_ID, RESULT_MAP_ID);
	}
	
	@Given("the introspected table is a different table")
	public void mockIntrospectedTableWithWrongName() throws Exception {
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn("wrong_name");
	}
	
	@Given("the introspected table is the right table")
	public void mockIntrospectedTableWithRightName() throws Exception {
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
	}
	
	@Given("the element has a result map attribute")
	public void addAResultMapAttribute() throws Exception {
		attributes.add(new Attribute(AlterResultMapPlugin.RESULT_MAP_ATTRIBUTE, ELEMENT_RESULT_MAP_ID));
	}
	
	@Given("the element has a random attribute")
	public void addARandomAttribute() throws Exception {
		attributes.add(new Attribute("someName", "someValue"));
	}
	
	@Given("the method has a result map annotation")
	public void addAResultMapAnnotation() throws Exception {
		annotations.add(String.format(AlterResultMapPlugin.ANNOTATION_FORMAT, ELEMENT_RESULT_MAP_ID));
	}
	
	@Given("the method has a random annotation")
	public void addARandomAnnotation() throws Exception {
		annotations.add("@otherAnnotation");
	}
	
	@Given("the validate method has been called")
	@When("the validate method is called")
	public void validateThePlugin() throws Exception {
		validateResult = plugin.validate(warnings);
	}
	
	@When("the renameResultMapAttribute for element is called")
	public void invokeRenameResultMapElementAttribute() throws Exception {
		plugin.renameResultMapAttribute(element, introspectedTable);
	}
	
	@When("the renameResultMapAttribute for method is called")
	public void invokeRenameResultMapMethodAnnotation() throws Exception {
		plugin.renameResultMapAttribute(method, introspectedTable);
	}
	
	@When("the SelectByExampleWithoutBLOBs method for element is called")
	public void invokeSelectByExampleWithoutBLOBsForElement() throws Exception {
		generatedResult = plugin.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
	}
	
	@When("the SelectByExampleWithBLOBs method for element is called")
	public void invokeSelectByExampleWithBLOBsForElement() throws Exception {
		generatedResult = plugin.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);
	}
	
	@When("the SelectByPrimaryKey method for element is called")
	public void invokeSelectByPrimaryKeyForElement() throws Exception {
		generatedResult = plugin.sqlMapSelectByPrimaryKeyElementGenerated(element, introspectedTable);
	}
	
	@When("the SelectAll method for element is called")
	public void invokeSelectAllForElement() throws Exception {
		generatedResult = plugin.sqlMapSelectAllElementGenerated(element, introspectedTable);
	}
	
	@When("the SelectByExampleWithBLOBs method for interface is called")
	public void invokeSelectByExampleWithBLOBsForInterface() throws Exception {
		generatedResult = plugin.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
	}
	
	@When("the SelectByExampleWithoutBLOBs method for interface is called")
	public void invokeSelectByExampleWithoutBLOBsForInterface() throws Exception {
		generatedResult = plugin.clientSelectByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
	}
	
	@When("the SelectByPrimaryKey method for interface is called")
	public void invokeSelectByPrimaryKeyForInterface() throws Exception {
		generatedResult = plugin.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
	}
	
	@When("the SelectAll method for interface is called")
	public void invokeSelectAllForInterface() throws Exception {
		generatedResult = plugin.clientSelectAllMethodGenerated(method, interfaze, introspectedTable);
	}
	
	@When("the SelectByExampleWithBLOBs method for class is called")
	public void invokeSelectByExampleWithBLOBsForClass() throws Exception {
		generatedResult = plugin.clientSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
	}
	
	@When("the SelectByExampleWithoutBLOBs method for class is called")
	public void invokeSelectByExampleWithoutBLOBsForClass() throws Exception {
		generatedResult = plugin.clientSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);
	}
	
	@When("the SelectByPrimaryKey method for class is called")
	public void invokeSelectByPrimaryKeyForClass() throws Exception {
		generatedResult = plugin.clientSelectByPrimaryKeyMethodGenerated(method, topLevelClass, introspectedTable);
	}
	
	@When("the SelectAll method for class is called")
	public void invokeSelectAllForClass() throws Exception {
		generatedResult = plugin.clientSelectAllMethodGenerated(method, topLevelClass, introspectedTable);
	}
	
	@Then("validate should return (true|false)")
	public void verifyValidateReturnIsFalse(@Group(1) final Boolean validate) throws Exception {
		assertThat(validateResult).isEqualTo(validate);
	}
	
	@Then("validate should have produced (\\d+) warnings")
	public void verifyValidateWarnings(@Group(1) final Integer noWarnings) throws Exception {
		assertThat(warnings).hasSize(noWarnings);
	}
	
	@Then("the element attributes size will be (\\d+)")
	public void verifyElementAttributesSize(@Group(1) final Integer size) throws Exception {
		assertThat(attributes).hasSize(size);
	}
	
	@Then("the result map attribute's name at position (\\d+) won't have changed")
	public void verifyAttributeNameIsSame(@Group(1) final Integer position) throws Exception {
		assertThat(attributes.get(position).getName()).isEqualTo(AlterResultMapPlugin.RESULT_MAP_ATTRIBUTE);
	}
	
	@Then("the result map attribute's value at position (\\d+) won't have changed")
	public void verifyAttributeValueIsSame(@Group(1) final Integer position) throws Exception {
		assertThat(attributes.get(position).getValue()).isEqualTo(ELEMENT_RESULT_MAP_ID);
	}
	
	@Then("the result map attribute's value at position (\\d+) will have been modified")
	public void verifyAttributeValueIsModified(@Group(1) final Integer position) throws Exception {
		assertThat(attributes.get(position).getValue()).isEqualTo(RESULT_MAP_ID);
	}
	
	@Then("the method annotations size will be (\\d+)")
	public void verifyMethodAnnotationsSize(@Group(1) final Integer size) throws Exception {
		assertThat(annotations).hasSize(size);
	}
	
	@Then("the annotation at position (\\d+) won't have changed")
	public void verifyAnnotationIsSame(@Group(1) final Integer position) throws Exception {
		assertThat(annotations.get(position)).isEqualTo(String.format(AlterResultMapPlugin.ANNOTATION_FORMAT, ELEMENT_RESULT_MAP_ID));
	}
	
	@Then("the annotation at position (\\d+) will have been modified")
	public void verifyAnnotationIsModified(@Group(1) final Integer position) throws Exception {
		assertThat(annotations.get(position)).isEqualTo(String.format(AlterResultMapPlugin.ANNOTATION_FORMAT, RESULT_MAP_ID));
	}
	
	@Then("the generated method return value will be true")
	public void verifyGeneratedMethodResult() throws Exception {
		assertThat(generatedResult).isTrue();
	}
	

	
//	@Test
//	public void shouldRenameResultMapOfClientSelectAll() throws Exception {
//		AlterResultMapPlugin plugin = spy(this.plugin);
//
//		// Given
//
//		// When
//		boolean ok1 = plugin.clientSelectAllMethodGenerated(method, interfaze, introspectedTable);
//		boolean ok2 = plugin.clientSelectAllMethodGenerated(method, topLevelClass, introspectedTable);
//
//		// Then
//		assertThat(ok1).isTrue();
//		assertThat(ok2).isTrue();
//		verify(plugin, times(2)).renameResultMapAttribute(eq(method), eq(introspectedTable));
//	}

}
