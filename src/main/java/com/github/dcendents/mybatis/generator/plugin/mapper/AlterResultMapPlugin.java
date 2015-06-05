package com.github.dcendents.mybatis.generator.plugin.mapper;

import java.util.List;
import java.util.regex.Pattern;

import lombok.NoArgsConstructor;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * Mybatis generator plugin to alter the id of the result map returned by all the select methods.
 */
@NoArgsConstructor
public class AlterResultMapPlugin extends PluginAdapter {
	public static final String TABLE_NAME = "fullyQualifiedTableName";
	public static final String RESULT_MAP_ID = "resultMapId";

	private String tableName;
	private String resultMapId;
	
	private static Pattern annotationPattern = Pattern.compile("@ResultMap\\(\".*\"\\)");

	@Override
	public boolean validate(List<String> warnings) {
		tableName = properties.getProperty(TABLE_NAME);
		resultMapId = properties.getProperty(RESULT_MAP_ID);

		String warning = "Property %s not set for plugin %s";
		if (!stringHasValue(tableName)) {
			warnings.add(String.format(warning, TABLE_NAME, this.getClass().getSimpleName()));
		}
		if (!stringHasValue(resultMapId)) {
			warnings.add(String.format(warning, RESULT_MAP_ID, this.getClass().getSimpleName()));
		}

		return stringHasValue(tableName) && stringHasValue(resultMapId);
	}

	private boolean tableMatches(IntrospectedTable introspectedTable) {
		return tableName.equals(introspectedTable.getFullyQualifiedTableNameAtRuntime());
	}

	private void renameResultMapAttribute(XmlElement element) {
		List<Attribute> attributes = element.getAttributes();

		for (int i = 0; i < attributes.size(); i++) {
			Attribute attribute = attributes.get(i);
			if ("resultMap".equals(attribute.getName())) {
				Attribute newAtt = new Attribute("resultMap", resultMapId);
				attributes.remove(i);
				attributes.add(newAtt);
				break;
			}
		}
	}

	private void renameResultMapAttribute(Method method) {
		List<String> annotations = method.getAnnotations();

		for (int i = 0; i < annotations.size(); i++) {
			String annotation = annotations.get(i);
			if (annotationPattern.matcher(annotation).matches()) {
				String newAnnotation = String.format("@ResultMap(\"%s\")", resultMapId);
				annotations.remove(i);
				annotations.add(newAnnotation);
				break;
			}
		}
	}

	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			renameResultMapAttribute(element);
		}

		return true;
	}

	@Override
	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			renameResultMapAttribute(element);
		}

		return true;
	}

	@Override
	public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			renameResultMapAttribute(element);
		}

		return true;
	}

	@Override
	public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			renameResultMapAttribute(element);
		}

		return true;
	}

	@Override
	public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			renameResultMapAttribute(method);
		}

		return true;
	}

	@Override
	public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			renameResultMapAttribute(method);
		}

		return true;
	}

	@Override
	public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			renameResultMapAttribute(method);
		}

		return true;
	}

	@Override
	public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			renameResultMapAttribute(method);
		}

		return true;
	}

	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			renameResultMapAttribute(method);
		}

		return true;
	}

	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			renameResultMapAttribute(method);
		}

		return true;
	}

	@Override
	public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			renameResultMapAttribute(method);
		}

		return true;
	}

	@Override
	public boolean clientSelectAllMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (tableMatches(introspectedTable)) {
			renameResultMapAttribute(method);
		}

		return true;
	}

}
