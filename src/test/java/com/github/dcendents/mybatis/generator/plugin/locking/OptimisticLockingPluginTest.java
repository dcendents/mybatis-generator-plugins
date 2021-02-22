package com.github.dcendents.mybatis.generator.plugin.locking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
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
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * Tests for the class OptimisticLockingPlugin.
 */
@RunWith(MockitoJUnitRunner.class)
public class OptimisticLockingPluginTest {

	private OptimisticLockingPlugin plugin;

	@Mock
	private XmlElement element;
	@Mock
	private Method method;
	@Mock
	private Method withLock;
	@Mock
	private IntrospectedTable introspectedTable;
	@Mock
	private TopLevelClass topLevelClass;
	@Mock
	private Interface interfaze;

	@Mock
	private IntrospectedColumn id;
	@Mock
	private IntrospectedColumn other;
	@Mock
	private IntrospectedColumn modificationDate;


	private static final String TABLE_NAME = "table_name";
	private static final String LOCK_COLUMN = "modification_date";
	private static final String LOCK_COLUMN_FUNCTION = "date_trunc('milliseconds', modification_date)";

	@Before
	public void init() throws Exception {
		given(id.getActualColumnName()).willReturn("id");
		given(other.getActualColumnName()).willReturn("other_column");
		given(modificationDate.getActualColumnName()).willReturn("modification_date");

		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn(TABLE_NAME);
		given(introspectedTable.getAllColumns()).willReturn(Arrays.asList(id, other, modificationDate));

		plugin = new OptimisticLockingPlugin();
		plugin.getProperties().put(OptimisticLockingPlugin.TABLE_NAME, TABLE_NAME);
		plugin.getProperties().put(OptimisticLockingPlugin.LOCK_COLUMN, LOCK_COLUMN);
		plugin.getProperties().put(OptimisticLockingPlugin.LOCK_COLUMN_FUNCTION, LOCK_COLUMN_FUNCTION);
		plugin.validate(new ArrayList<String>());
	}

	@Test
	public void shouldBeInvalidWithoutAnyPropertyConfigured() {
		// Given
		OptimisticLockingPlugin instance = new OptimisticLockingPlugin();

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
		OptimisticLockingPlugin instance = new OptimisticLockingPlugin();
		instance.getProperties().put(OptimisticLockingPlugin.TABLE_NAME, TABLE_NAME);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isFalse();
		assertThat(warnings).hasSize(1);
	}

	@Test
	public void shouldBeInvalidWithOnlyTheLockColumnConfigured() {
		// Given
		OptimisticLockingPlugin instance = new OptimisticLockingPlugin();
		instance.getProperties().put(OptimisticLockingPlugin.LOCK_COLUMN, LOCK_COLUMN);

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
		OptimisticLockingPlugin instance = new OptimisticLockingPlugin();
		instance.getProperties().put(OptimisticLockingPlugin.TABLE_NAME, TABLE_NAME);
		instance.getProperties().put(OptimisticLockingPlugin.LOCK_COLUMN, LOCK_COLUMN);

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = instance.validate(warnings);

		// Then
		assertThat(ok).isTrue();
		assertThat(warnings).isEmpty();
	}

	@Test
	public void shouldSupportRegex() {
		// Given
		OptimisticLockingPlugin instance = new OptimisticLockingPlugin();
		instance.getProperties().put(OptimisticLockingPlugin.TABLE_NAME, "tab.*_n\\S+");
		instance.getProperties().put(OptimisticLockingPlugin.LOCK_COLUMN, LOCK_COLUMN);
		instance.validate(new ArrayList<String>());

		// When
		boolean ok = instance.tableMatches(introspectedTable);

		// Then
		assertThat(ok).isTrue();
	}

	@Test
	public void shouldNotAddMethodIfTableDoesNotMatch() {
		// Given
		given(introspectedTable.getFullyQualifiedTableNameAtRuntime()).willReturn("wrong_name");

		// When
		boolean ok1 = plugin.clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(interfaze, times(0)).addMethod(any(Method.class));
		verify(topLevelClass, times(0)).addMethod(any(Method.class));
	}

	@Test
	public void shouldAddNewMethod() {
		// Given
		OptimisticLockingPlugin plugin = spy(this.plugin);
		willReturn(withLock).given(plugin).addMethod(eq(method), eq(introspectedTable));

		// When
		boolean ok1 = plugin.clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable);
		boolean ok2 = plugin.clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(method, topLevelClass, introspectedTable);

		// Then
		then(ok1).isTrue();
		then(ok2).isTrue();
		verify(plugin, times(2)).addMethod(eq(method), eq(introspectedTable));
		verify(interfaze).addMethod(eq(withLock));
		verify(topLevelClass).addMethod(eq(withLock));
	}

	@Test
	public void shouldCreateNewMethodUsingBaseName() {
		// Given
		Method realMethod = new Method("methodName");

		// When
		Method newMethod = plugin.addMethod(realMethod, introspectedTable);

		// Then
		then(newMethod).isNotNull();
		then(newMethod.getName()).isEqualTo(realMethod.getName() + OptimisticLockingPlugin.METHOD_SUFFIX);
	}

	@Test
	public void shouldAddConditionToWhereClauseInAnnotation() {
		// Given
		Method realMethod = new Method("methodName");
		realMethod.addAnnotation("@Update({");
		realMethod.addAnnotation("    \"update schema.table_name\",");
		realMethod.addAnnotation("    \"set id = #{id,jdbcType=INT},\",");
		realMethod.addAnnotation("      \"other_column = #{other,jdbcType=INT},\",");
		realMethod.addAnnotation("    \"where id = #{id,jdbcType=BIGINT}\"");
		realMethod.addAnnotation("})");

		// When
		Method newMethod = plugin.addMethod(realMethod, introspectedTable);

		// Then
		then(newMethod).isNotNull();
		then(newMethod.getName()).isEqualTo(realMethod.getName() + OptimisticLockingPlugin.METHOD_SUFFIX);

		then(newMethod.getAnnotations()).hasSize(realMethod.getAnnotations().size() + 1);
		then(newMethod.getAnnotations()).containsAll(realMethod.getAnnotations().subList(0, 4));
		then(newMethod.getAnnotations().get(4)).isEqualTo(realMethod.getAnnotations().get(4) + ",");
		then(newMethod.getAnnotations().get(5)).contains(String.format("and %s = ", LOCK_COLUMN_FUNCTION));
		then(newMethod.getAnnotations().get(6)).isEqualTo(realMethod.getAnnotations().get(5));
	}

	@Test
	public void shouldSetCorrectTypeHandler() {
		// Given
		String typeHandler = "some.type.Handler";
		given(modificationDate.getTypeHandler()).willReturn(typeHandler);

		Method realMethod = new Method("methodName");
		realMethod.addAnnotation("@Update({");
		realMethod.addAnnotation("    \"update schema.table_name\",");
		realMethod.addAnnotation("    \"set id = #{id,jdbcType=INT},\",");
		realMethod.addAnnotation("      \"other_column = #{other,jdbcType=INT},\",");
		realMethod.addAnnotation("    \"where id = #{id,jdbcType=BIGINT}\"");
		realMethod.addAnnotation("})");

		// When
		Method newMethod = plugin.addMethod(realMethod, introspectedTable);

		// Then
		then(newMethod.getAnnotations().get(5)).contains(typeHandler);
	}

	@Test
	public void shouldIgnoreMissingColumn() {
		// Given
		given(introspectedTable.getAllColumns()).willReturn(Arrays.asList(id, other));

		// When
		IntrospectedColumn column = plugin.getColumn(introspectedTable);

		// Then
		then(column).isNull();
	}

}
