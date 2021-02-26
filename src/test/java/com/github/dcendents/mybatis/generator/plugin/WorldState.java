package com.github.dcendents.mybatis.generator.plugin;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.mockito.Mockito;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.TableConfiguration;

import lombok.Data;

@Data
@ApplicationScoped
public class WorldState {

	private PluginAdapter plugin;

	private IntrospectedTable introspectedTable = Mockito.mock(IntrospectedTable.class);
	private TableConfiguration tableConfiguration = Mockito.mock(TableConfiguration.class);
	private TopLevelClass topLevelClass = Mockito.mock(TopLevelClass.class);
	private XmlElement xmlElement = Mockito.mock(XmlElement.class);
	private Method method = Mockito.mock(Method.class);
	private Interface interfaze = Mockito.mock(Interface.class);
	private List<String> warnings = new ArrayList<>();
	private Map<String, Object> results = new HashMap<>();
	private boolean executionResult;

	public WorldState() {
		given(introspectedTable.getTableConfiguration()).willReturn(tableConfiguration);
		given(xmlElement.getAttributes()).willReturn(new ArrayList<>());
		given(method.getAnnotations()).willReturn(new ArrayList<>());
		given(method.getParameters()).willReturn(new ArrayList<>());
	}

	public Object getResult(String result) {
		return results.get(result);
	}
}
