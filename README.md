[![GitHub license](https://img.shields.io/github/license/dcendents/mybatis-generator-plugins.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/dcendents/mybatis-generator-plugins.svg?branch=refactoring)](https://travis-ci.org/dcendents/mybatis-generator-plugins) 
[![Stories in Ready](https://badge.waffle.io/dcendents/mybatis-generator-plugins.png?label=ready&title=Ready)](https://waffle.io/dcendents/mybatis-generator-plugins) 
[![codecov.io](http://codecov.io/github/dcendents/mybatis-generator-plugins/coverage.svg?branch=refactoring)](http://codecov.io/github/dcendents/mybatis-generator-plugins?branch=refactoring)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.dcendents/mybatis-generator-plugins.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22mybatis-generator-plugins%22)

MyBatis Generator Plugins
====================
Set of plugins for the mybatis-generator to further tweak the generated code.


## AddClassAnnotationsPlugin

Plugin that will add the specified annotation to every generated class. There are 2 mandatory parameters to set:
  - **annotationClass**: The class of the annotation, this will be added as an import statement.
  - **annotationString**: The literal string that will be added, complete will all values

If you need to add multiple annotations, configure this plugin many times, one per annotation to add.

e.g.:
```xml
<plugin type="com.github.dcendents.mybatis.generator.plugin.annotation.AddClassAnnotationsPlugin">
	<property name="annotationClass" value="lombok.ToString" />
	<property name="annotationString" value="@ToString(callSuper = true)" />
</plugin>
<plugin type="com.github.dcendents.mybatis.generator.plugin.annotation.AddClassAnnotationsPlugin">
	<property name="annotationClass" value="lombok.EqualsAndHashCode" />
	<property name="annotationString" value="@EqualsAndHashCode(callSuper = true)" />
</plugin>
```

Build Metrics
====================

[![Build Status](https://travis-ci.org/dcendents/mybatis-generator-plugins.svg?branch=refactoring)](https://travis-ci.org/dcendents/mybatis-generator-plugins) 
[![codecov.io](http://codecov.io/github/dcendents/mybatis-generator-plugins/coverage.svg?branch=refactoring)](http://codecov.io/github/dcendents/mybatis-generator-plugins?branch=refactoring)
![codecov.io](http://codecov.io/github/dcendents/mybatis-generator-plugins/branch.svg?branch=refactoring)
[![Throughput Graph](https://graphs.waffle.io/dcendents/mybatis-generator-plugins/throughput.svg)](https://waffle.io/dcendents/mybatis-generator-plugins/metrics)

License
====================

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

