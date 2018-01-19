package com.github.dcendents.mybatis.generator.plugin.dynamic.sql;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;

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

	private String tableClassSuffix;
	private boolean addAliasedColumns;

	@Override
	public boolean validate(List<String> warnings) {
		tableClassSuffix = properties.getProperty(TABLE_CLASS_SUFFIX);
		String addAliasedColumnsString = properties.getProperty(ADD_ALIASED_COLUMNS);

		tableClassSuffix = tableClassSuffix == null ? "" : tableClassSuffix.trim();
		addAliasedColumns = Boolean.parseBoolean(addAliasedColumnsString);

		return true;
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		List<GeneratedJavaFile> models = new ArrayList<>();

		CompilationUnit unit =  DynamicSqlSupportClassGenerator.of(introspectedTable, context.getCommentGenerator(), tableClassSuffix, addAliasedColumns).generate();

		GeneratedJavaFile dynamicSqlModel = new GeneratedJavaFile(unit, context.getJavaClientGeneratorConfiguration().getTargetProject(), new DefaultJavaFormatter());

		models.add(dynamicSqlModel);

		return models;
	}

}
