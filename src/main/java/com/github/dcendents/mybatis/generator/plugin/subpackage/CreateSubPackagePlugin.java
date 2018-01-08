package com.github.dcendents.mybatis.generator.plugin.subpackage;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * Mybatis generator plugin to rename the generated files (extensible design).
 */
@AllArgsConstructor
@Slf4j
public class CreateSubPackagePlugin extends PluginAdapter {
	public static final String MODEL_PACKAGE_PROPERTY = "modelSubPackage";
	public static final String MODEL_CLASS_SUFFIX_PROPERTY = "modelClassSuffix";
	public static final String MAPPER_PACKAGE_PROPERTY = "mapperSubPackage";
	public static final String MAPPER_CLASS_SUFFIX_PROPERTY = "mapperClassSuffix";
	public static final String EXAMPLE_PACKAGE_PROPERTY = "exampleSubPackage";
	public static final String EXAMPLE_CLASS_SUFFIX_PROPERTY = "exampleClassSuffix";

	static final String ATTRIBUTE_NAMESPACE = "namespace";
	static final String ATTRIBUTE_TYPE = "type";

	private RenameProperties modelProperties;
	private RenameProperties mapperProperties;
	private RenameProperties exampleProperties;

	public CreateSubPackagePlugin() {
		modelProperties = new RenameProperties();
		mapperProperties = new RenameProperties();
		exampleProperties = new RenameProperties();
	}

	@Override
	public boolean validate(List<String> warnings) {
		modelProperties.validate(properties.getProperty(MODEL_PACKAGE_PROPERTY),
				properties.getProperty(MODEL_CLASS_SUFFIX_PROPERTY));
		mapperProperties.validate(properties.getProperty(MAPPER_PACKAGE_PROPERTY),
				properties.getProperty(MAPPER_CLASS_SUFFIX_PROPERTY));
		exampleProperties.validate(properties.getProperty(EXAMPLE_PACKAGE_PROPERTY),
				properties.getProperty(EXAMPLE_CLASS_SUFFIX_PROPERTY));

		return modelProperties.isEnabled() || mapperProperties.isEnabled() || exampleProperties.isEnabled();
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		introspectedTable.setBaseRecordType(modelProperties.setTypes(introspectedTable.getBaseRecordType()));
		introspectedTable.setMyBatis3JavaMapperType(mapperProperties.setTypes(introspectedTable
				.getMyBatis3JavaMapperType()));
		introspectedTable.setExampleType(exampleProperties.setTypes(introspectedTable.getExampleType()));
	}

	/**
	 * Rename the method types.
	 *
	 * @param method
	 *            the method
	 * @return true
	 */
	boolean renameMethod(Method method) {
		method.setReturnType(modelProperties.renameType(method.getReturnType()));

		for (int i = 0; i < method.getParameters().size(); i++) {
			Parameter parameter = method.getParameters().get(i);
			FullyQualifiedJavaType parameterType = parameter.getType();
			FullyQualifiedJavaType newParameterType = modelProperties.renameType(parameterType);
			if (parameterType != newParameterType) {
				Parameter newParam = new Parameter(newParameterType, parameter.getName(), parameter.isVarargs());
				for (String annotation : parameter.getAnnotations()) {
					newParam.addAnnotation(annotation);
				}
				method.getParameters().set(i, newParam);
				log.debug("set new parameter: [{}][{}]", parameter, newParam);
			}
		}

		modelProperties.renameAnnotations(method.getAnnotations());
		mapperProperties.renameAnnotations(method.getAnnotations());

		return true;
	}

	/**
	 * Rename the element attribute.
	 *
	 * @param element
	 *            the element
	 * @param attributeName
	 *            the attribute name
	 * @return true
	 */
	boolean renameElementAttribute(XmlElement element, String attributeName) {
		for (int i = 0; i < element.getAttributes().size(); i++) {
			Attribute attribute = element.getAttributes().get(i);
			if (attributeName.equals(attribute.getName())) {
				element.getAttributes().set(i, modelProperties.renameAttribute(element.getAttributes().get(i)));
				element.getAttributes().set(i, mapperProperties.renameAttribute(element.getAttributes().get(i)));
			}
		}

		return true;
	}

	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		return renameElementAttribute(document.getRootElement(), ATTRIBUTE_NAMESPACE);
	}

	@Override
	public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return renameElementAttribute(element, ATTRIBUTE_TYPE);
	}

	@Override
	public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return renameElementAttribute(element, ATTRIBUTE_TYPE);
	}

	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
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
	public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientInsertMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(modelProperties.getOriginalType());
		if (interfaze != null) {
			interfaze.addImportedType(modelType);
		}
		if (topLevelClass != null) {
			topLevelClass.addImportedType(modelType);
		}

		return true;
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
	public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientInsertSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean clientSelectAllMethodGenerated(Method method, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		return renameMethod(method);
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		if (modelProperties.isEnabled()) {
			topLevelClass.setAbstract(true);
		}
		return true;
	}
}
