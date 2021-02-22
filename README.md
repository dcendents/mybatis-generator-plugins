[![GitHub license](https://img.shields.io/github/license/dcendents/mybatis-generator-plugins.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/dcendents/mybatis-generator-plugins.svg?branch=master)](https://travis-ci.org/dcendents/mybatis-generator-plugins) 
[![codecov.io](http://codecov.io/github/dcendents/mybatis-generator-plugins/coverage.svg?branch=master)](http://codecov.io/github/dcendents/mybatis-generator-plugins?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.dcendents/mybatis-generator-plugins.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22mybatis-generator-plugins%22)
[![Dependabot Status](https://api.dependabot.com/badges/status?host=github&repo=dcendents/mybatis-generator-plugins)](https://dependabot.com)
[![Dependabot SemVer Compatibility](https://img.shields.io/dependabot/semver/maven/mybatis-generator-plugins.svg)](https://dependabot.com/compatibility-score/?dependency-name=mybatis-generator-plugins&package-manager=maven&version-scheme=semver)

MyBatis Generator Plugins
====================
Set of plugins for the mybatis-generator to further tweak the generated code.

*Note*: Most of these plugins have been tested using the targetRuntime `MyBatis3` and the java client type `MIXEDMAPPER`.


## CreateSubPackagePlugin

Powerful plugin that will rename the generated model, mappers and examples by moving them in a sub-package and/or append a suffix. This is intended to keep generated code apart from the final classes to allow the geneator plugin to overwrite them every time.

e.g.: some.package.Actor will now be generated as some.package.sub.ActorSuffix and will be abstract.
some.package.ActorMapper will now be generated as some.package.sub.ActorMapperSuffix but methods will still expect and object of type some.package.Actor (to be created manually).

There are 6 optional parameters to set:
  - *modelSubPackage*: The sub package to create for model classes.
  - *modelClassSuffix*: The suffix to add to model classes.
  - *mapperSubPackage*: The sub package to create for mapper interfaces.
  - *mapperClassSuffix*: The suffix to add to mapper interfaces.
  - *exampleSubPackage*: The sub package to create for example classes.
  - *exampleClassSuffix*: The suffix to add to example classes.

e.g.:
```xml
<plugin type="com.github.dcendents.mybatis.generator.plugin.subpackage.CreateSubPackagePlugin">
	<property name="modelSubPackage" value="gen" />
	<property name="modelClassSuffix" value="Gen" />
	<property name="mapperSubPackage" value="gen" />
	<property name="mapperClassSuffix" value="Gen" />
	<property name="exampleSubPackage" value="filter" />
</plugin>
```

## RenameExampleClassAndMethodsPlugin

Plugin that will rename the example classes and parameters to give them a more suitable name for a production environment. Also this plugin will fix the update statements and remove the id column(s). There are 4 mandatory parameters to set:
  - **classMethodSearchString**: The string to search in class names.
  - **classMethodReplaceString**: The replace value.
  - **parameterSearchString**: The string to search for parameter names.
  - **parameterReplaceString**: The replace value.

e.g.:
```xml
<plugin type="com.github.dcendents.mybatis.generator.plugin.rename.RenameExampleClassAndMethodsPlugin">
	<property name="classMethodSearchString" value="Example" />
	<property name="classMethodReplaceString" value="Filter" />
	<property name="parameterSearchString" value="example" />
	<property name="parameterReplaceString" value="filter" />
</plugin>
```

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

## AlterModelPlugin

A simple plugin to modify the generated model. Currently it can add interfaces to the specified generated model class. There are 2 mandatory parameters to set:
  - **fullyQualifiedTableName**: The name of the database table including the schema.
    - Will accept a regex expression
  - **addInterfaces**: A coma delimited list of interfaces to add to the model class implementations.

e.g.:
```xml
<plugin type="com.github.dcendents.mybatis.generator.plugin.model.AlterModelPlugin">
	<property name="fullyQualifiedTableName" value="public.car" />
	<property name="addInterfaces" value="com.github.dcendents.mybatis.jaxws.db.model.Vehicle" />
</plugin>
<plugin type="com.github.dcendents.mybatis.generator.plugin.model.AlterModelPlugin">
	<property name="fullyQualifiedTableName" value="public.chopper" />
	<property name="addInterfaces" value="com.github.dcendents.mybatis.jaxws.db.model.Vehicle" />
</plugin>
```

## WrapObjectPlugin

Plugin that can be used to make a generated class wrap another java bean. For each property to wrap, the field will not be generated and the getter/setter will simply redirect to the wrapped java bean methods instead. This strategy can be used when you need to persist some third party objects but still want the flexibility to add new properties (like a database id). This pattern is more flexible than trying to extend the class. There are 2 mandatory and 3 optional parameters to set:
  - **fullyQualifiedTableName**: The name of the database table including the schema.
  - **objectClass**: The class of the object to be wrapped.
  - *objectFieldName*: The name of the field to add, will default to the class name starting with a lower case.
  - *includes*: A coma separated list of fields to delegate to the wrapped object, everything else will be excluded. If left blank all fields are included.
  - *excludes*: A coma separated list of fields to exclude.

The plugin need to be added for each table as needed.

e.g.:
```xml
<plugin type="com.github.dcendents.mybatis.generator.plugin.wrap.WrapObjectPlugin">
	<property name="fullyQualifiedTableName" value="public.film" />
	<property name="objectClass" value="com.github.dcendents.mybatis.jaxws.api.Film" />
	<property name="excludes" value="language" />
</plugin>
<plugin type="com.github.dcendents.mybatis.generator.plugin.wrap.WrapObjectPlugin">
	<property name="fullyQualifiedTableName" value="public.actor" />
	<property name="objectClass" value="com.github.dcendents.mybatis.jaxws.api.Actor" />
</plugin>
```

## AlterResultMapPlugin

A simple plugin to modify the generated client to use a different ResultMap. There are 2 mandatory parameters to set:
  - **fullyQualifiedTableName**: The name of the database table including the schema.
  - **resultMapId**: The id of the result map to be used.

e.g.:
```xml
<plugin type="com.github.dcendents.mybatis.generator.plugin.client.AlterResultMapPlugin">
	<property name="fullyQualifiedTableName" value="public.film" />
	<property name="resultMapId" value="FullResultMap" />
</plugin>
```

## DynamicSqlPlugin

If you are not ready to switch to the new `MyBatis3DynamicSql` targetRuntime but would still enjoy the creation of the SqlTable and SqlColumn structures.
Then you can use this plugin while keeping your targetRuntime. There are 4 optional parameters to set:
  - *tableClassSuffix*: A suffix to append to the generated SqlTable class (so the name does not collide with your existing model class).
  - *addAliasedColumns*: For each SqlColumn, add a second field with its name prefixed by the table alias. Useful when using static imports and different tables have identical column names.
  - *addTableAlias*: Also add an entry for the table alias as configured on the table element of the mybatis-generator configuration.
  - *tableAliasFieldName*: The name to use for the table alias field if enabled. Will default to `tableAlias` if not set.

Additionally it is possible to add more aliases (or any other String constant) to the generated tables by adding properties with the format `fullyQualifiedTableName.aliasField`.

e.g.:
```xml
<plugin type="com.github.dcendents.mybatis.generator.plugin.dynamic.sql.DynamicSqlPlugin">
	<property name="tableClassSuffix" value="Table" />
	<property name="addAliasedColumns" value="true" />
	<property name="addTableAlias" value="true" />
	<property name="tableAliasFieldName" value="tableAlias" />
	<property name="public.table_name.otherAlias" value="ot" />
	<property name="public.table_name.toherConstant" value="any string" />
</plugin>
```

## OptimisticLockingPlugin

This plugin will add a method `updateByPrimaryKeyWithOptimisticLocking` using the provided column as an optimistic lock (see https://en.wikipedia.org/wiki/Optimistic_concurrency_control). It requires the generation of the method `updateByPrimaryKey` using java annotations as it will copy it and add a condition to the where clause. There are 2 mandatory and 1 optional parameters to set:
  - **fullyQualifiedTableName**: The name of the database table including the schema.
    - Will accept a regex expression
  - **lockColumn**: The column to use for optimistic locking.
  - *lockColumnFunction*: if specified, this will be used in the where clause instead of the column name.

e.g.:
```xml
<plugin type="com.github.dcendents.mybatis.generator.plugin.locking.OptimisticLockingPlugin">
	<property name="fullyQualifiedTableName" value=".*" />
	<property name="lockColumn" value="modification_date" />
	<property name="lockColumnFunction" value="date_trunc('milliseconds', modification_date)" />
</plugin>
```

## CreateGenericInterfacePlugin

This plugin will create a Mapper interface using java generics as method arguments and return types. It will modify the mappers to extend it with the concrete types. It makes it easier to add utility methods that are agnostic of the exact mapper they are using. e.g.: Maybe a method that will call insert when the id field is null and update when it is not for any model/mapper combination. There is 1 mandatory parameter to set:
  - **interface**: The fully qualified name of the interface to create.

e.g.:
```xml
<plugin type="com.github.dcendents.mybatis.generator.plugin.client.CreateGenericInterfacePlugin">
	<property name="interface" value="some.package.InterfaceName" />
</plugin>
```

Demo
====================

See the following project for a demo of most of these plugins: https://github.com/dcendents/mybatis-jaxws

Build Metrics
====================

[![Build Status](https://travis-ci.org/dcendents/mybatis-generator-plugins.svg?branch=master)](https://travis-ci.org/dcendents/mybatis-generator-plugins) 
[![codecov.io](http://codecov.io/github/dcendents/mybatis-generator-plugins/coverage.svg?branch=master)](http://codecov.io/github/dcendents/mybatis-generator-plugins?branch=master)
[![BCH compliance](https://bettercodehub.com/edge/badge/dcendents/mybatis-generator-plugins?branch=master)](https://bettercodehub.com/)
[![DepShield Badge](https://depshield.sonatype.org/badges/dcendents/mybatis-generator-plugins/depshield.svg)](https://depshield.github.io)
![codecov.io](http://codecov.io/github/dcendents/mybatis-generator-plugins/branch.svg?branch=master)

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

