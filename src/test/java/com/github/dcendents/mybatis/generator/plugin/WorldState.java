package com.github.dcendents.mybatis.generator.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.mockito.Mockito;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import lombok.Data;

@Data
@ApplicationScoped
public class WorldState {

	private PluginAdapter plugin;
	
	private IntrospectedTable introspectedTable = Mockito.mock(IntrospectedTable.class);
	private TopLevelClass topLevelClass = Mockito.mock(TopLevelClass.class);
	private List<String> warnings = new ArrayList<>();
	private Map<String, Object> results = new HashMap<>();
	private boolean executionResult;

	public Object getResult(String result) {
		return results.get(result);
	}
}
