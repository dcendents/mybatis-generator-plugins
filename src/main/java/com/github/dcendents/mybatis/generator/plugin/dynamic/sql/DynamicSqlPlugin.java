package com.github.dcendents.mybatis.generator.plugin.dynamic.sql;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.CompilationUnit;

/**
 * Mybatis generator plugin to add dynamic sql table definitions.
 */
@NoArgsConstructor
public class DynamicSqlPlugin extends PluginAdapter {
	public static final String TABLE_CLASS_SUFFIX = "tableClassSuffix";
	public static final String ADD_ALIASED_COLUMNS = "addAliasedColumns";
	public static final String ADD_TABLE_ALIAS = "addTableAlias";
	public static final String TABLE_ALIAS_FIELD_NAME = "tableAliasFieldName";

	public static final String DEFAULT_TABLE_ALIAS_FIELD = "tableAlias";

	private String tableClassSuffix;
	private boolean addAliasedColumns;
	private boolean addTableAlias;
	private String tableAliasFieldName;

	@Override
	public boolean validate(List<String> warnings) {
		tableClassSuffix = properties.getProperty(TABLE_CLASS_SUFFIX);
		String addAliasedColumnsString = properties.getProperty(ADD_ALIASED_COLUMNS);
		String addTableAliasString = properties.getProperty(ADD_TABLE_ALIAS);
		tableAliasFieldName = properties.getProperty(TABLE_ALIAS_FIELD_NAME);

		tableClassSuffix = tableClassSuffix == null ? "" : tableClassSuffix.trim();
		addAliasedColumns = Boolean.parseBoolean(addAliasedColumnsString);
		addTableAlias = Boolean.parseBoolean(addTableAliasString);
		if (StringUtils.isBlank(tableAliasFieldName)) {
			tableAliasFieldName = DEFAULT_TABLE_ALIAS_FIELD;
		}

		return true;
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		List<GeneratedJavaFile> models = new ArrayList<>();

		CompilationUnit unit = DynamicSqlSupportClassGenerator
				.of(introspectedTable, context.getCommentGenerator(), tableClassSuffix, addAliasedColumns, addTableAlias, tableAliasFieldName, properties)
				.generate();

		GeneratedJavaFile dynamicSqlModel =
				new GeneratedJavaFile(unit, context.getJavaClientGeneratorConfiguration().getTargetProject(), new DefaultJavaFormatter());

		models.add(dynamicSqlModel);

		return models;
	}

}
