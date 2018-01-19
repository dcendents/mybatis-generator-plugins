package com.github.dcendents.mybatis.generator.plugin.dynamic.sql;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;

/**
 * Tests for the class DynamicSqlPlugin.
 */
@RunWith(MockitoJUnitRunner.class)
public class DynamicSqlPluginTest {

	private DynamicSqlPlugin plugin;

	@Mock
	private IntrospectedTable table;
	@Mock
	private FullyQualifiedTable tableName;

	@Mock
	private IntrospectedColumn column1;
	@Mock
	private IntrospectedColumn column2;

	@Mock
	private Context context;
	@Mock
	private CommentGenerator commentGenerator;
	@Mock
	private JavaClientGeneratorConfiguration javaClientGeneratorConfiguration;

	@Before
	public void init() {
		given(context.getCommentGenerator()).willReturn(commentGenerator);
		given(context.getJavaClientGeneratorConfiguration()).willReturn(javaClientGeneratorConfiguration);

		given(javaClientGeneratorConfiguration.getTargetProject()).willReturn("src/main/java");

		given(table.getFullyQualifiedTable()).willReturn(tableName);
		given(table.getFullyQualifiedTableNameAtRuntime()).willReturn("table_name");
		given(table.getMyBatis3JavaMapperType()).willReturn("some.package.JavaMapperType");
		given(table.getBaseRecordType()).willReturn("some.package.BaseRecordType");
		given(table.getAllColumns()).willReturn(Arrays.asList(column1, column2));

		given(column1.getFullyQualifiedJavaType()).willReturn(new FullyQualifiedJavaType("int"));
		given(column1.getTableAlias()).willReturn("a");

		given(column2.getFullyQualifiedJavaType()).willReturn(new FullyQualifiedJavaType("java.util.Calendar"));
		given(column2.getTypeHandler()).willReturn("type.Handler");

		plugin = new DynamicSqlPlugin();
		plugin.setContext(context);

		Properties properties = new Properties();
		properties.put(DynamicSqlPlugin.TABLE_CLASS_SUFFIX, "Table");
		properties.put(DynamicSqlPlugin.ADD_ALIASED_COLUMNS, "true");
		plugin.setProperties(properties);
		plugin.validate(new ArrayList<String>());
	}

	@Test
	public void shouldBeValidWithoutAnyPropertyConfigured() {
		// Given
		plugin = new DynamicSqlPlugin();

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = plugin.validate(warnings);

		// Then
		then(ok).isTrue();
		then(warnings).isEmpty();
	}

	@Test
	public void shouldBeValidWithPropertiesConfigured() {
		// Given

		// When
		List<String> warnings = new ArrayList<>();
		boolean ok = plugin.validate(warnings);

		// Then
		then(ok).isTrue();
		then(warnings).isEmpty();
	}

	@Test
	public void shouldGenerateAdditionalFile() throws Exception {
		// Given

		// When
		List<GeneratedJavaFile> files = plugin.contextGenerateAdditionalJavaFiles(table);

		// Then
		then(files).hasSize(1);
	}

	@Test
	public void shouldGenerateAdditionalFileEvenWhenTableHasNoColumns() throws Exception {
		// Given
		given(table.getAllColumns()).willReturn(new ArrayList<IntrospectedColumn>());

		// When
		List<GeneratedJavaFile> files = plugin.contextGenerateAdditionalJavaFiles(table);

		// Then
		then(files).hasSize(1);
	}

}
