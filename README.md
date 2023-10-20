# Clusterless Commons

[![Maven Central](https://img.shields.io/maven-central/v/io.clusterless/clusterless-commons-core)](https://search.maven.org/search?q=g:io.clusterless)
[![javadoc](https://javadoc.io/badge2/io.clusterless/clusterless-commons-core/javadoc.svg?label=javadoc+commons-core)](https://javadoc.io/doc/io.clusterless/clusterless-commons-core)
[![javadoc](https://javadoc.io/badge2/io.clusterless/clusterless-commons-aws/javadoc.svg?label=javadoc+commons-aws)](https://javadoc.io/doc/io.clusterless/clusterless-commons-aws)

A set of Java APIs for building cloud based applications.

- `clusterless-commons-core` - Utilities and helpers for consistent naming and temporal operations.
- `clusterless-commons-aws` - Utilities and base Constructs for AWS CDK applications.

## Core

### Naming

Consistent, human-readable, names are important for usability and maintainability of cloud applications. In many cases
names must be unique to a region, account, or globally.

#### Label

The Label class is for making labels, String for use in different contexts.

```java
Label id=Label.of("project").with("version");

        id.camelCase(); // projectVersion
        id.lowerHyphen(); // project-version
        id.lowerColonPath(); // project:version
```

There are a few flavors of Label:

- [Fixed](clusterless-commons-core/src/main/java/clusterless/commons/naming/Fixed.java) - retains the original value
- [Partition](clusterless-commons-core/src/main/java/clusterless/commons/naming/Partition.java) - allows for key=value
  pairs within each path element
- [Region](clusterless-commons-core/src/main/java/clusterless/commons/naming/Region.java) - for use in AWS regions
- [Stage](clusterless-commons-core/src/main/java/clusterless/commons/naming/Stage.java) - for use to label deployment
  stages
- [Version](clusterless-commons-core/src/main/java/clusterless/commons/naming/Version.java) - for use to label versions

`Label` is an interface that can be implemented by specialized classes. Use `Label.EnumLabel` to create label enums.

#### Ref

[Ref](clusterless-commons-core/src/main/java/clusterless/commons/naming/Ref.java) is used to create a unique export
name for a resource.

A Ref can represent

- **id** - the id of the exported resource (like a vpc id)
- **name** - the physical name of the exported resource (like S3 bucket name)
- **arn** - the AWS Arn of the exported resource

A Ref takes the form of:

> aws:qualifier:[stage:]scopeName:scopeVersion:resourceNS:resourceType:resourceName

The [ScopedConstruct](clusterless-commons-aws/src/main/java/clusterless/commons/substrate/aws/cdk/scoped/ScopedConstruct.java)
class provides a way to create Refs for child resources of the construct.

### Temporal

Provides [IntervalUnit](clusterless-commons-core/src/main/java/clusterless/commons/temporal/IntervalUnit.java)
implementations of:

- Fourths - 15 minute intervals
- Sixths - 10 minute intervals
- Twelfths - 5 minute intervals

These intervals are used by Clusterless to label lots.

There is also a
[IntervalDateTimeFormatter](clusterless-commons-core/src/main/java/clusterless/commons/temporal/IntervalDateTimeFormatter.java)
for formatting dates and times of these intervals.

### Collection

Provides a few Collection helpers:

- [OrderedMaps](clusterless-commons-core/src/main/java/clusterless/commons/collection/OrderedMaps.java) - The same as
  Maps.of() but retains the order of the keys
- [OrderedSafeMaps](clusterless-commons-core/src/main/java/clusterless/commons/collection/OrderedSafeMaps.java) - The
  same as OrderedMaps.of() but retains the order of the keys and ignores null values
- [SafeList](clusterless-commons-core/src/main/java/clusterless/commons/collection/SafeList.java) - The same as
  List.of() but ignores null values

`OrderedMaps` is especially useful when the hashing order could change even if the key values don't. When creating many
CDK Constructs, Map instances are required. Using `OrderedMaps.of()` will ensure the order of the keys is retained to
prevent a deployment from being unnecessarily triggered.

## AWS

This module provides AWS specific related classes centered around simplifying the naming, exporting, and tagging of
resources.

### Naming

- [ArnsRefs](clusterless-commons-aws/src/main/java/clusterless/commons/substrate/aws/cdk/naming/ArnRefs.java) - ArnRefs
  provides a utility for resolving ARN references from a Ref or ARN string.
- [ResourceNames](clusterless-commons-aws/src/main/java/clusterless/commons/substrate/aws/cdk/naming/ResourceNames.java) -
  Provides methods for creating unique resource names.

### Scoped

A set of classes for creating Stacks and Constructs scoped to:

- **stage** - the current deployment stage
- **name** - the name of the application
- **version** - the version of the application

There are also methods to create Refs for exported resources, or to import resources via a stringified Ref or Arn.

Names are generated from these values to prevent collisions across deployments within an account, or across accounts.

- [ScopedApp](clusterless-commons-aws/src/main/java/clusterless/commons/substrate/aws/cdk/scoped/ScopedApp.java) - A CDK
  App scoped to stage, name, and version.
- [ScopedStack](clusterless-commons-aws/src/main/java/clusterless/commons/substrate/aws/cdk/scoped/ScopedStack.java) - A
  CDK Stack scoped to stage, name, and version by virtue of being a child of a ScopedApp.
- [ScopedConstruct](clusterless-commons-aws/src/main/java/clusterless/commons/substrate/aws/cdk/scoped/ScopedConstruct.java) -
  A CDK Construct scoped to stage, name, and version by virtue of being a child of a ScopedStack.

### Construct

- [LambdaLogGroupConstruct](clusterless-commons-aws/src/main/java/clusterless/commons/substrate/aws/cdk/construct/LambdaLogGroupConstruct.java) -
  A construct to simplify creating a CloudWatch LogGroup for a Lambda function.
- [OutputConstruct](clusterless-commons-aws/src/main/java/clusterless/commons/substrate/aws/cdk/construct/OutputConstruct.java) -
  A construct to simplify creating Stack outputs.

### Util

- [TagsUtil](clusterless-commons-aws/src/main/java/clusterless/commons/substrate/aws/cdk/util/TagsUtil.java) - A utility
  for applying tags to AWS resources.

The `TagsUtil` can be disabled to speed up deployments for stacks that have a large number of resources, in order to
speed the deployment during development.
