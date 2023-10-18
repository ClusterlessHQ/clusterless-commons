# Clusterless Commons

A set of APIs for building cloud based applications.

## Label

The Label class is for making labels, String for use in different contexts.

```java
Label id=Label.of("project").with("version");

        id.camelCase(); // projectVersion
        id.lowerHyphen(); // project-version
        id.lowerColonPath(); // project:version
```

There are a few flavors of Label:

- Fixed - retains the original value
- Partition - allows for key=value pairs within each path element
- Region - for use in AWS regions
- Stage - for use to label deployment stages
- Version - for use to label versions

## Temporal

Provides IntervalUnit implementations of:

- Fourths - 15 minute intervals
- Sixths - 10 minute intervals
- Twelfths - 5 minute intervals

These intervals are used by Clusterless to label lots.

## Collection

Provides a few Collection helpers:

- OrderedMaps - The same as Maps.of() but retains the order of the keys
- OrderedSafeMaps - The same as OrderedMaps.of() but retains the order of the keys and ignores null values
- SafeList - The same as List.of() but ignores null values

OrderedMaps is especially useful when the hashing order could change even if the key values don't. When creating many
CDK Constructs, Map instances are required. Using OrderedMaps.of() will ensure the order of the keys is retained to
prevent a deployment from being unnecessarily triggered.
