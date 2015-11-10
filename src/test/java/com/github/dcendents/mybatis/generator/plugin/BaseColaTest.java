package com.github.dcendents.mybatis.generator.plugin;

import org.junit.Test;

import com.github.bmsantos.core.cola.story.annotations.IdeEnabler;

public abstract class BaseColaTest {
    @Test
    @IdeEnabler
    public void iWillBeRemoved() {
        System.out.println("This is a simple test!");
    }
}
