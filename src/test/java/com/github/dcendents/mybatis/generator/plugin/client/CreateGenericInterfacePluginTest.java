package com.github.dcendents.mybatis.generator.plugin.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;

/**
 * Tests for the class CreateGenericInterfacePlugin.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateGenericInterfacePluginTest {

	private CreateGenericInterfacePlugin plugin;

	@Mock
	private Context context;
	@Mock
	private JavaClientGeneratorConfiguration javaClientGeneratorConfiguration;

	@Mock
	private Method method;
	@Mock
	private IntrospectedTable introspectedTable;
	@Mock
	private TopLevelClass topLevelClass;
	@Mock
	private Interface interfaze;

	@Mock
	private Parameter parameter1;
	@Mock
	private Parameter parameter2;

	private static final String INTERFACE = "some.Interface";

	@Before
	public void init() throws Exception {
		given(context.getJavaClientGeneratorConfiguration()).willReturn(javaClientGeneratorConfiguration);

		given(javaClientGeneratorConfiguration.getTargetProject()).willReturn("src/main/java");

		given(method.getParameters()).willReturn(Arrays.asList(parameter1, parameter2));

		plugin = new CreateGenericInterfacePlugin();
		plugin.setContext(context);

		plugin.getProperties().put(CreateGenericInterfacePlugin.INTERFACE, INTERFACE);
//		plugin.getProperties().put(AlterResultMapPlugin.RESULT_MAP_ID, RESULT_MAP_ID);
		plugin.validate(new ArrayList<String>());
	}

	@Test
	public void shouldBeInvalidWithoutAnyPropertyConfigured() {
		// Given
		CreateGenericInterfacePlugin instance = new CreateGenericInterfacePlugin();

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(1);
	}

	@Test
	public void shouldBeValidWhenPropertiesAreConfigured() {
		// Given
		CreateGenericInterfacePlugin instance = new CreateGenericInterfacePlugin();
		instance.getProperties().put(CreateGenericInterfacePlugin.INTERFACE, INTERFACE);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isTrue();
		assertThat(warnings).isEmpty();
	}

	@Test
	public void shouldGenerateAdditionalFile() throws Exception {
		// Given

		// When
		List<GeneratedJavaFile> files = plugin.contextGenerateAdditionalJavaFiles();

		// Then
		then(files).hasSize(1);
	}

	@Test
	public void shouldAddInterfaceToMappers() {
		// Given

		// When
		boolean ok = plugin.clientGenerated(interfaze, topLevelClass, introspectedTable);

		// Then
		then(ok).isTrue();

		ArgumentCaptor<FullyQualifiedJavaType> fullyQualifiedJavaTypeCaptor = ArgumentCaptor.forClass(FullyQualifiedJavaType.class);
		verify(interfaze).addSuperInterface(fullyQualifiedJavaTypeCaptor.capture());

		then(fullyQualifiedJavaTypeCaptor.getValue()).isNotNull();
		then(fullyQualifiedJavaTypeCaptor.getValue().getFullyQualifiedNameWithoutTypeParameters()).isEqualTo(INTERFACE);
	}

	@Test
	public void shouldAddClientCountByExampleMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientCountByExampleMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientCountByExampleMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientDeleteByExampleMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientDeleteByExampleMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientDeleteByExampleMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientDeleteByPrimaryKeyMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientDeleteByPrimaryKeyMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientInsertMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientInsertMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientInsertMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientSelectByExampleWithBLOBsMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientSelectByExampleWithoutBLOBsMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientSelectByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientSelectByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientSelectByPrimaryKeyMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientSelectByPrimaryKeyMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientUpdateByExampleSelectiveMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientUpdateByExampleSelectiveMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByExampleSelectiveMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientUpdateByExampleWithBLOBsMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientUpdateByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByExampleWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientUpdateByExampleWithoutBLOBsMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientUpdateByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByExampleWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientUpdateByPrimaryKeySelectiveMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientUpdateByPrimaryKeySelectiveMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByPrimaryKeySelectiveMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientUpdateByPrimaryKeyWithBLOBsMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientUpdateByPrimaryKeyWithoutBLOBsMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientInsertSelectiveMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientInsertSelectiveMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientInsertSelectiveMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

	@Test
	public void shouldAddClientSelectAllMethod() {
		// Given
		CreateGenericInterfacePlugin plugin = spy(this.plugin);

		// When
		boolean ok1 = plugin.clientSelectAllMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientSelectAllMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addGenericMethod(eq(method), any(FullyQualifiedJavaType.class), Matchers.<FullyQualifiedJavaType>anyVararg());
	}

}
