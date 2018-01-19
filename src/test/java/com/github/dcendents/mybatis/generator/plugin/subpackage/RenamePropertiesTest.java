package com.github.dcendents.mybatis.generator.plugin.subpackage;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;

/**
 * Tests for the class RenameProperties.
 */
@RunWith(MockitoJUnitRunner.class)
public class RenamePropertiesTest {

	private static final String SUB_PACKAGE = ".sub";
	private static final String SUFFIX = "Suffix";
	private static final String ORIGINAL_TYPE = "some.package.Type";
	private static final String NEW_TYPE = "some.package.sub.TypeSuffix";

	private RenameProperties brandNew;
	private RenameProperties disabled;
	private RenameProperties initializedBoth;
	private RenameProperties allSet;

	@Before
	public void init() throws Exception {
		brandNew = new RenameProperties();

		disabled = new RenameProperties();
		disabled.setEnabled(false);

		initializedBoth = new RenameProperties();
		initializedBoth.setEnabled(true);
		initializedBoth.setSubPpackage(SUB_PACKAGE);
		initializedBoth.setClassSuffix(SUFFIX);

		allSet = new RenameProperties();
		allSet.setEnabled(true);
		allSet.setSubPpackage(SUB_PACKAGE);
		allSet.setClassSuffix(SUFFIX);
		allSet.setOriginalType(ORIGINAL_TYPE);
		allSet.setNewType(NEW_TYPE);
	}

	@Test
	public void shouldBeDisabledWhenBothParametersAreNull() throws Exception {
		// Given

		// When
		brandNew.validate(null, null);

		// Then
		assertThat(brandNew.isEnabled()).isFalse();
	}

	@Test
	public void shouldInitializeSubPAckageToEmptyStringWhenNull() throws Exception {
		// Given

		// When
		brandNew.validate(null, "suffix");

		// Then
		assertThat(brandNew.isEnabled()).isTrue();
		assertThat(brandNew.getSubPpackage()).isEqualTo(StringUtils.EMPTY);
	}

	@Test
	public void shouldInitializeClassSuffixToEmptyStringWhenNull() throws Exception {
		RenameProperties props = new RenameProperties();

		// Given

		// When
		props.validate("package", null);

		// Then
		assertThat(props.isEnabled()).isTrue();
		assertThat(props.getClassSuffix()).isEqualTo(StringUtils.EMPTY);
	}

	@Test
	public void shouldPrefixSubPAckageWithDotWhenMissing() throws Exception {
		// Given

		// When
		brandNew.validate("package", "suffix");

		// Then
		assertThat(brandNew.isEnabled()).isTrue();
		assertThat(brandNew.getSubPpackage()).isEqualTo(".package");
	}

	@Test
	public void shouldNotPrefixSubPAckageWithDotWhenPresent() throws Exception {
		// Given

		// When
		brandNew.validate(".package", "suffix");

		// Then
		assertThat(brandNew.isEnabled()).isTrue();
		assertThat(brandNew.getSubPpackage()).isEqualTo(".package");
	}

	@Test
	public void shouldReturnOriginalTypeWhenDisabled() throws Exception {
		// Given
		String type = "type";

		// When
		String renamed = disabled.setTypes(type);

		// Then
		assertThat(renamed).isSameAs(type);
	}

	@Test
	public void shouldRenameType() throws Exception {
		// Given

		// When
		String renamed = initializedBoth.setTypes(ORIGINAL_TYPE);

		// Then
		assertThat(renamed).isEqualTo(NEW_TYPE);
		assertThat(initializedBoth.getOriginalType()).isEqualTo(ORIGINAL_TYPE);
		assertThat(initializedBoth.getNewType()).isEqualTo(renamed);
	}

	@Test
	public void shouldNotRenameTypesThatDontMatch() throws Exception {
		// Given
		String type = "some.other.Type";
		FullyQualifiedJavaType javaType = new FullyQualifiedJavaType(type);

		// When
		FullyQualifiedJavaType renamed = allSet.renameType(javaType);

		// Then
		assertThat(renamed).isSameAs(javaType);
	}

	@Test
	public void shouldRenameTypeThatMatch() throws Exception {
		// Given
		FullyQualifiedJavaType javaType = new FullyQualifiedJavaType(NEW_TYPE);

		// When
		FullyQualifiedJavaType renamed = allSet.renameType(javaType);

		// Then
		assertThat(renamed).isNotNull();
		assertThat(renamed.getFullyQualifiedName()).isEqualTo(ORIGINAL_TYPE);
	}

	@Test
	public void shouldNotRenameAttributesThatDontMatch() throws Exception {
		// Given
		String type = "some.other.Type";
		Attribute attribute = new Attribute("name", type);

		// When
		Attribute renamed = allSet.renameAttribute(attribute);

		// Then
		assertThat(renamed).isSameAs(attribute);
	}

	@Test
	public void shouldRenameAttributeThatMatch() throws Exception {
		// Given
		Attribute attribute = new Attribute("name", NEW_TYPE);

		// When
		Attribute renamed = allSet.renameAttribute(attribute);

		// Then
		assertThat(renamed).isNotNull();
		assertThat(renamed.getValue()).isEqualTo(ORIGINAL_TYPE);
	}

	@Test
	public void shouldNotRenameNullAnnotations() throws Exception {
		// Given
		List<String> annotations = null;

		// When
		allSet.renameAnnotations(annotations);

		// Then
	}

	@Test
	public void shouldNotRenameAnnotationsThatDontMatch() throws Exception {
		// Given
		String type = "some.other.Type";
		List<String> annotations = new ArrayList<>();
		annotations.add("@MultiLine({");
		annotations.add("\"	line1\",");
		annotations.add("\"	line2 " + type + "\",");
		annotations.add("\"	line3\",");
		annotations.add("})");
		annotations.add("@SingleLine(\"" + type + "\")");
		final int annotationsSize = annotations.size();

		// When
		allSet.renameAnnotations(annotations);

		// Then
		assertThat(annotations).hasSize(annotationsSize);
		for (String annotation : annotations) {
			assertThat(annotation).doesNotContain(ORIGINAL_TYPE);
		}
		assertThat(annotations.get(2)).contains(type);
		assertThat(annotations.get(5)).contains(type);
	}

	@Test
	public void shouldRenameAnnotationsThatMatch() throws Exception {
		// Given
		List<String> annotations = new ArrayList<>();
		annotations.add("@MultiLine({");
		annotations.add("\"	line1\",");
		annotations.add("\"	line2 " + NEW_TYPE + "\",");
		annotations.add("\"	line3\",");
		annotations.add("})");
		annotations.add("@SingleLine(\"" + NEW_TYPE + "\")");
		final int annotationsSize = annotations.size();

		// When
		allSet.renameAnnotations(annotations);

		// Then
		assertThat(annotations).hasSize(annotationsSize);
		for (String annotation : annotations) {
			assertThat(annotation).doesNotContain(NEW_TYPE);
		}
		assertThat(annotations.get(2)).contains(ORIGINAL_TYPE);
		assertThat(annotations.get(5)).contains(ORIGINAL_TYPE);
	}
}
