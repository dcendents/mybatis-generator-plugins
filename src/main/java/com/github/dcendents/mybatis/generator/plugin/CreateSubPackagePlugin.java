package com.github.dcendents.mybatis.generator.plugin;

import java.util.List;

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
public class CreateSubPackagePlugin extends PluginAdapter {
    private static final String MODEL_PACKAGE_PROPERTY = "modelSubPackage";
    private static final String MODEL_CLASS_SUFFIX_PROPERTY = "modelClassSuffix";
    private static final String MAPPER_PACKAGE_PROPERTY = "mapperSubPackage";
    private static final String MAPPER_CLASS_SUFFIX_PROPERTY = "mapperClassSuffix";
    private static final String EXAMPLE_PACKAGE_PROPERTY = "exampleSubPackage";
    private static final String EXAMPLE_CLASS_SUFFIX_PROPERTY = "exampleClassSuffix";

    private static final String DOT = ".";
    private static final String ATTRIBUTE_NAMESPACE = "namespace";
    private static final String ATTRIBUTE_TYPE = "type";

    private String modelPackage;
    private String modelClassSuffix;
    private String mapperPackage;
    private String mapperClassSuffix;
    private String examplePackage;
    private String exampleClassSuffix;

    private boolean renameModel;
    private boolean renameMapper;
    private boolean renameExample;

    private String originalModelType;
    private String newModelType;
    private String originalMapperType;
    private String newMapperType;
    private String originalExampleType;
    private String newExampleType;

    public CreateSubPackagePlugin() {
    }

    @Override
    public boolean validate(List<String> warnings) {
        validateModel();
        validateMapper();
        validateExample();

        boolean valid = renameModel || renameMapper || renameExample;

        return valid;
    }

    private void validateModel() {
        modelPackage = properties.getProperty(MODEL_PACKAGE_PROPERTY);
        modelClassSuffix = properties.getProperty(MODEL_CLASS_SUFFIX_PROPERTY);

        renameModel = modelPackage != null || modelClassSuffix != null;

        if (renameModel && modelPackage == null) {
            modelPackage = "";
        }
        else if (renameModel && !modelPackage.startsWith(DOT)) {
            modelPackage = DOT + modelPackage;
        }
        if (renameModel && modelClassSuffix == null) {
            modelClassSuffix = "";
        }
    }

    private void validateMapper() {
        mapperPackage = properties.getProperty(MAPPER_PACKAGE_PROPERTY);
        mapperClassSuffix = properties.getProperty(MAPPER_CLASS_SUFFIX_PROPERTY);

        renameMapper = mapperPackage != null || mapperClassSuffix != null;

        if (renameMapper && mapperPackage == null) {
            mapperPackage = "";
        }
        else if (renameMapper && !mapperPackage.startsWith(DOT)) {
            mapperPackage = DOT + mapperPackage;
        }
        if (renameMapper && mapperClassSuffix == null) {
            mapperClassSuffix = "";
        }
    }

    private void validateExample() {
        examplePackage = properties.getProperty(EXAMPLE_PACKAGE_PROPERTY);
        exampleClassSuffix = properties.getProperty(EXAMPLE_CLASS_SUFFIX_PROPERTY);

        renameExample = examplePackage != null || exampleClassSuffix != null;

        if (renameExample && examplePackage == null) {
            examplePackage = "";
        }
        else if (renameExample && !examplePackage.startsWith(DOT)) {
            examplePackage = DOT + examplePackage;
        }
        if (renameExample && exampleClassSuffix == null) {
            exampleClassSuffix = "";
        }
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        if (renameModel) {
            originalModelType = introspectedTable.getBaseRecordType();
            int lastDot = originalModelType.lastIndexOf('.');
            newModelType = originalModelType.substring(0, lastDot) + modelPackage + originalModelType.substring(lastDot) + modelClassSuffix;
            introspectedTable.setBaseRecordType(newModelType);
        }

        if (renameMapper) {
            originalMapperType = introspectedTable.getMyBatis3JavaMapperType();
            int lastDot = originalMapperType.lastIndexOf('.');
            newMapperType = originalMapperType.substring(0, lastDot) + mapperPackage + originalMapperType.substring(lastDot) + mapperClassSuffix;
            introspectedTable.setMyBatis3JavaMapperType(newMapperType);
        }

        if (renameExample) {
            originalExampleType = introspectedTable.getExampleType();
            int lastDot = originalExampleType.lastIndexOf('.');
            newExampleType = originalExampleType.substring(0, lastDot) + examplePackage + originalExampleType.substring(lastDot) + exampleClassSuffix;
            introspectedTable.setExampleType(newExampleType);
        }
    }

    /**
     * Rename the method types.
     * @param method the method
     * @return true
     */
    private boolean renameMethod(Method method) {
        if (method.getReturnType().getFullyQualifiedName().contains(newModelType)) {
            FullyQualifiedJavaType newType = new FullyQualifiedJavaType(method.getReturnType().getFullyQualifiedName()
                    .replace(newModelType, originalModelType));
            method.setReturnType(newType);
        }

        for (int i = 0; i < method.getParameters().size(); i++) {
            Parameter parameter = method.getParameters().get(i);
            if (parameter.getType().getFullyQualifiedName().contains(newModelType)) {
                FullyQualifiedJavaType newType = new FullyQualifiedJavaType(parameter.getType().getFullyQualifiedName()
                        .replace(newModelType, originalModelType));
                Parameter newParam = new Parameter(newType, parameter.getName(), parameter.isVarargs());
                for (String annotation : parameter.getAnnotations()) {
                    newParam.addAnnotation(annotation);
                }
                method.getParameters().set(i, newParam);
            }
        }

        return true;
    }

    /**
     * Rename the element attribute.
     * @param element the element
     * @param attributeName the attribute name
     * @return true
     */
    private boolean renameElement(XmlElement element, String attributeName) {
        for (int i = 0; i < element.getAttributes().size(); i++) {
            Attribute attribute = element.getAttributes().get(i);
            if (attributeName.equals(attribute.getName())) {
                if (newModelType.equals(attribute.getValue())) {
                    Attribute newAtt = new Attribute(attribute.getName(), originalModelType);
                    element.getAttributes().set(i, newAtt);
                }
                else if (newMapperType.equals(attribute.getValue())) {
                    Attribute newAtt = new Attribute(attribute.getName(), originalMapperType);
                    element.getAttributes().set(i, newAtt);
                }
            }
        }

        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        return renameElement(document.getRootElement(), ATTRIBUTE_NAMESPACE);
    }

    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return renameElement(element, ATTRIBUTE_TYPE);
    }

    @Override
    public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return renameElement(element, ATTRIBUTE_TYPE);
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientCountByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientCountByExampleMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientDeleteByExampleMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientDeleteByExampleMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(originalModelType);
        if (interfaze != null) {
            interfaze.addImportedType(modelType);
        }
        if (topLevelClass != null) {
            topLevelClass.addImportedType(modelType);
        }

        return true;
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean clientSelectAllMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return renameMethod(method);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (renameModel) {
            topLevelClass.setAbstract(true);
        }
        return true;
    }
}
