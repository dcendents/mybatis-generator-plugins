package com.github.dcendents.mybatis.generator.plugin.model;

import java.util.List;
import java.util.regex.Pattern;

import lombok.NoArgsConstructor;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * Mybatis generator plugin to modify the generated model.
 */
@NoArgsConstructor
public class AlterModelPlugin extends PluginAdapter {
	public static final String TABLE_NAME = "fullyQualifiedTableName";
	public static final String ADD_INTERFACES = "addInterfaces";

	private String tableName;
	private String[] addInterfaces;

	@Override
	public boolean validate(List<String> warnings) {
		tableName = properties.getProperty(TABLE_NAME);
		String interfacesString = properties.getProperty(ADD_INTERFACES);

		String warning = "Property %s not set for plugin %s";
		if (!stringHasValue(tableName)) {
			warnings.add(String.format(warning, TABLE_NAME, this.getClass().getSimpleName()));
		}
		if (!stringHasValue(interfacesString)) {
			warnings.add(String.format(warning, ADD_INTERFACES, this.getClass().getSimpleName()));
		} else {
			addInterfaces = interfacesString.split(",");
		}

		return stringHasValue(tableName) && addInterfaces != null;
	}

	private boolean tableMatches(IntrospectedTable introspectedTable) {
		return tableName.equals(introspectedTable.getFullyQualifiedTableNameAtRuntime())
				|| Pattern.matches(tableName, introspectedTable.getFullyQualifiedTableNameAtRuntime());
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			for (String theInterface : addInterfaces) {
				FullyQualifiedJavaType type = new FullyQualifiedJavaType(theInterface);
				topLevelClass.addImportedType(type);
				topLevelClass.addSuperInterface(type);
			}
		}

		return true;
	}

}
