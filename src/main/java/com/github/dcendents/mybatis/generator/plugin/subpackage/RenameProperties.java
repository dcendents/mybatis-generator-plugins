package com.github.dcendents.mybatis.generator.plugin.subpackage;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@Slf4j
public class RenameProperties {

	private static final String DOT = ".";

	private boolean enabled;

	private String subPpackage;
	private String classSuffix;

	private String originalType;
	private String newType;

	public void validate(String theSubPpackage, String theClassSuffix) {
		enabled = theSubPpackage != null || theClassSuffix != null;

		if (enabled) {
			if (theSubPpackage == null) {
				subPpackage = StringUtils.EMPTY;
			} else if (!theSubPpackage.startsWith(DOT)) {
				subPpackage = DOT + theSubPpackage;
			} else {
				subPpackage = theSubPpackage;
			}

			classSuffix = theClassSuffix == null ? StringUtils.EMPTY : theClassSuffix;
		}
	}

	public String setTypes(String theOriginalType) {
		if (enabled) {
			this.originalType = theOriginalType;
			int lastDot = originalType.lastIndexOf(DOT);
			newType = originalType.substring(0, lastDot) + subPpackage + originalType.substring(lastDot) + classSuffix;
			log.debug("replace type [{}][{}]", originalType, newType);
			return newType;
		}

		return theOriginalType;
	}

	public FullyQualifiedJavaType renameType(FullyQualifiedJavaType theJavaType) {
		if (theJavaType.getFullyQualifiedName().contains(newType)) {
			log.debug("set new return type: [{}][{}]", newType, originalType);
			return new FullyQualifiedJavaType(theJavaType.getFullyQualifiedName().replace(newType, originalType));
		} else {
			return theJavaType;
		}
	}

	public Attribute renameAttribute(Attribute attribute) {
		if (newType.equals(attribute.getValue())) {
			log.debug("set new model attribute: [{}][{}][{}]", attribute.getName(), newType, originalType);
			return new Attribute(attribute.getName(), originalType);
		} else {
			return attribute;
		}
	}

	public void renameAnnotations(List<String> lines) {
		if (lines != null) {
			for (int i = 0; i < lines.size(); i++) {
				String line = lines.get(i);
				while (line.contains(newType)) {
					line = line.replace(newType, originalType);
					log.debug("set new annotation line: [{}] -> [{}]", lines.get(i), line);
					lines.set(i, line);
				}
			}
		}
	}
}
