package com.github.dcendents.mybatis.generator.plugin.locking;

import java.util.List;
import java.util.regex.Pattern;

import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * Mybatis generator plugin to add update statements with optimistic locking.
 */
@NoArgsConstructor
public class OptimisticsLockingPlugin extends PluginAdapter {
	public static final String TABLE_NAME = "fullyQualifiedTableName";
	public static final String LOCK_COLUMN = "lockColumn";
	public static final String LOCK_COLUMN_FUNCTION = "lockColumnFunction";

	private String tableName;
	private String lockColumn;
	private String lockColumnFunction;

	static final String RESULT_MAP_ATTRIBUTE = "resultMap";
	static final Pattern ANNOTATION_PATTERN = Pattern.compile("@ResultMap\\(\".*\"\\)");
	static final String ANNOTATION_FORMAT = "@ResultMap(\"%s\")";

	@Override
	public boolean validate(List<String> warnings) {
		tableName = properties.getProperty(TABLE_NAME);
		lockColumn = properties.getProperty(LOCK_COLUMN);
		lockColumnFunction = properties.getProperty(LOCK_COLUMN_FUNCTION);

		String warning = "Property %s not set for plugin %s";
		if (!stringHasValue(tableName)) {
			warnings.add(String.format(warning, TABLE_NAME, this.getClass().getSimpleName()));
		}
		if (!stringHasValue(lockColumn)) {
			warnings.add(String.format(warning, LOCK_COLUMN, this.getClass().getSimpleName()));
		}

		if (StringUtils.isBlank(lockColumnFunction)) {
			lockColumnFunction = lockColumn;
		}

		return stringHasValue(tableName) && stringHasValue(lockColumn);
	}

	private boolean tableMatches(IntrospectedTable introspectedTable) {
		return tableName.equals(introspectedTable.getFullyQualifiedTableNameAtRuntime())
				|| Pattern.matches(tableName, introspectedTable.getFullyQualifiedTableNameAtRuntime());
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			Method withLock = addMethod(method, introspectedTable);
			interfaze.addMethod(withLock);
		}

		return true;
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			Method withLock = addMethod(method, introspectedTable);
			topLevelClass.addMethod(withLock);
		}

		return true;
	}

	Method addMethod(Method method, IntrospectedTable introspectedTable) {
		IntrospectedColumn column = getColumn(introspectedTable);

		Method withLock = new Method(method);
		withLock.setName(method.getName() + "WithOptimisticLocking");

		withLock.getAnnotations().clear();

		for (String line : method.getAnnotations()) {
			if (line.matches("\\s*\".*\"\\s*")) {
				withLock.getAnnotations().add(line + ",");

				String typeHandler = column.getTypeHandler() != null ? String.format(",typeHandler=%s", column.getTypeHandler()) : "";
				withLock.getAnnotations().add(String.format("    \"and %1$s = #{%2$s,jdbcType=%3$s%4$s}\"", lockColumnFunction,
						column.getJavaProperty(), column.getJdbcTypeName(), typeHandler));
			} else {
				withLock.getAnnotations().add(line);
			}
		}

		return withLock;
	}

	IntrospectedColumn getColumn(IntrospectedTable introspectedTable) {
		for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
			if (lockColumn.equals(column.getActualColumnName())) {
				return column;
			}
		}

		return null;
	}

}
