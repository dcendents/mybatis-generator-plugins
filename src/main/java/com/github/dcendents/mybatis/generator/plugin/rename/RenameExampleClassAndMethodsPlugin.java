package com.github.dcendents.mybatis.generator.plugin.rename;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * Mybatis generator plugin to rename Example classes and methods.
 */
@Slf4j
@NoArgsConstructor
public class RenameExampleClassAndMethodsPlugin extends PluginAdapter {
	public static final String VALIDATION_ERROR_MESSAGE = "ValidationError.18";
	public static final String CLASS_SEARCH_PROPERTY = "classMethodSearchString";
	public static final String CLASS_REPLACE_PROPERTY = "classMethodReplaceString";
	public static final String PARAM_SEARCH_PROPERTY = "parameterSearchString";
	public static final String PARAM_REPLACE_PROPERTY = "parameterReplaceString";

	static final String XML_ID_ATTRIBUTE = "id";

	private String classMethodReplaceString;
	private Pattern classMethodPattern;
	private String parameterReplaceString;
	private Pattern parameterPattern;

	@Override
	public boolean validate(List<String> warnings) {

		String classMethodSearchString = properties.getProperty(CLASS_SEARCH_PROPERTY);
		classMethodReplaceString = properties.getProperty(CLASS_REPLACE_PROPERTY);

		String parameterSearchString = properties.getProperty(PARAM_SEARCH_PROPERTY);
		parameterReplaceString = properties.getProperty(PARAM_REPLACE_PROPERTY);

		boolean valid = stringHasValue(classMethodSearchString) && stringHasValue(classMethodReplaceString)
				&& stringHasValue(parameterSearchString) && stringHasValue(parameterReplaceString);

		if (valid) {
			classMethodPattern = Pattern.compile(classMethodSearchString);
			parameterPattern = Pattern.compile(parameterSearchString);
		} else {
			if (!stringHasValue(classMethodSearchString)) {
				warnings.add(getString(VALIDATION_ERROR_MESSAGE,
						RenameExampleClassAndMethodsPlugin.class.getSimpleName(), CLASS_SEARCH_PROPERTY));
			}
			if (!stringHasValue(classMethodReplaceString)) {
				warnings.add(getString(VALIDATION_ERROR_MESSAGE,
						RenameExampleClassAndMethodsPlugin.class.getSimpleName(), CLASS_REPLACE_PROPERTY));
			}
			if (!stringHasValue(parameterSearchString)) {
				warnings.add(getString(VALIDATION_ERROR_MESSAGE,
						RenameExampleClassAndMethodsPlugin.class.getSimpleName(), PARAM_SEARCH_PROPERTY));
			}
			if (!stringHasValue(parameterReplaceString)) {
				warnings.add(getString(VALIDATION_ERROR_MESSAGE,
						RenameExampleClassAndMethodsPlugin.class.getSimpleName(), PARAM_REPLACE_PROPERTY));
			}
		}

		return valid;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		String oldType = introspectedTable.getExampleType();
		Matcher matcher = classMethodPattern.matcher(oldType);
		oldType = matcher.replaceAll(classMethodReplaceString);

		introspectedTable.setExampleType(oldType);
	}

	boolean renameMethod(Method method) {
		String oldMethodName = method.getName();
		Matcher matcher = classMethodPattern.matcher(oldMethodName);
		String newMethodName = matcher.replaceAll(classMethodReplaceString);
		method.setName(newMethodName);

		for (int i = 0; i < method.getParameters().size(); i++) {
			Parameter parameter = method.getParameters().get(i);
			String oldParamName = parameter.getName();
			matcher = parameterPattern.matcher(oldParamName);
			if (matcher.lookingAt()) {
				String newName = matcher.replaceAll(parameterReplaceString);
				Parameter newParam = new Parameter(parameter.getType(), newName, parameter.isVarargs());
				for (String annotation : parameter.getAnnotations()) {
					newParam.addAnnotation(annotation);
				}
				method.getParameters().set(i, newParam);
			}
		}

		return true;
	}

	boolean renameElement(XmlElement element) {
		for (int i = 0; i < element.getAttributes().size(); i++) {
			Attribute attribute = element.getAttributes().get(i);
			if (XML_ID_ATTRIBUTE.equals(attribute.getName())) {
				String oldValue = attribute.getValue();
				Matcher matcher = classMethodPattern.matcher(oldValue);
				String newValue = matcher.replaceAll(classMethodReplaceString);
				Attribute newAtt = new Attribute(attribute.getName(), newValue);
				element.getAttributes().set(i, newAtt);
			}
		}

		return true;
	}

	/**
	 * Remove the id columns from the sql statement. Useful when the generated update statement is trying to update an
	 * id column.
	 * 
	 * @param introspectedTable
	 *            the table
	 * @param element
	 *            the element
	 */
	void removeIdColumns(IntrospectedTable introspectedTable, XmlElement element) {
		List<String> updates = new ArrayList<>();

		String alias = introspectedTable.getTableConfiguration().getAlias();
		if (alias == null) {
			alias = "";
		} else {
			alias = alias + ".";
		}

		List<IntrospectedColumn> ids = introspectedTable.getPrimaryKeyColumns();
		for (IntrospectedColumn column : ids) {
			String update = String.format("%4$s%1$s = #{record.%2$s,jdbcType=%3$s},", column.getActualColumnName(),
					column.getJavaProperty(), column.getJdbcTypeName(), alias);
			log.debug("update: {}", update);
			updates.add(update);
		}

		if (!updates.isEmpty()) {
			removeIdColumns(updates, element, null, -1);
		}
	}

	/**
	 * Remove the id columns from the sql statement. Useful when the generated update statement is trying to update an
	 * id column.
	 * 
	 * @param idColumnsWithType
	 *            the id columns with the type as used in an update statement
	 * @param element
	 *            the element
	 * @param parent
	 *            the parent element
	 * @param index
	 *            the index of the element in the parent list
	 */
	void removeIdColumns(List<String> idColumnsWithType, Element element, XmlElement parent, int index) {
		log.debug("element type: {}", element.getClass().getSimpleName());
		log.debug("element: {}", element.getFormattedContent(0));

		if (element instanceof TextElement) {
			TextElement textElement = (TextElement) element;
			for (String idColumnWithType : idColumnsWithType) {
				if (textElement.getContent().contains(idColumnWithType)) {
					TextElement newElement = new TextElement(textElement.getContent().replace(idColumnWithType, ""));
					parent.getElements().set(index, newElement);
				}
			}
		} else if (element instanceof XmlElement) {
			XmlElement xmlElement = (XmlElement) element;
			for (int i = 0; i < xmlElement.getElements().size(); i++) {
				Element e = xmlElement.getElements().get(i);
				removeIdColumns(idColumnsWithType, e, xmlElement, i);
			}
		}
	}

	@Override
	public boolean clientCountByExampleMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientCountByExampleMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientDeleteByExampleMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientDeleteByExampleMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean sqlMapCountByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return renameElement(element);
	}

	@Override
	public boolean sqlMapDeleteByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return renameElement(element);
	}

	@Override
	public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		return renameElement(element);
	}

	@Override
	public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		return renameElement(element);
	}

	@Override
	public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		removeIdColumns(introspectedTable, element);
		return renameElement(element);
	}

	@Override
	public boolean sqlMapUpdateByExampleWithBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		removeIdColumns(introspectedTable, element);
		return renameElement(element);
	}

	@Override
	public boolean sqlMapUpdateByExampleWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		removeIdColumns(introspectedTable, element);
		return renameElement(element);
	}
}
