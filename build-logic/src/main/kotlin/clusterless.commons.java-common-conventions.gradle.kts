@file:Suppress("UnstableApiUsage")

/*
* Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

plugins {
    id("clusterless.commons.java-common-properties")
    java
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

group = extra["group"] as String
version = extra["version"] as String

dependencies {
    constraints {
        implementation("org.jetbrains:annotations:24.0.0")
        implementation("com.google.guava:guava:31.1-jre")
    }
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.9.3")
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }

    withJavadocJar()
    withSourcesJar()
}

var copyright =
    """
        |Copyright &#169; 2023 <a href="https://chris.wensel.net/" target="_blank">Chris K Wensel</a>. All Rights Reserved.
        |&nbsp;A <a href="https://github.com/ClusterlessHQ" target="_blank">ClusterlessHQ</a> project.
    """.trimMargin()

tasks.named<Javadoc>("javadoc") {
    title = "Clusterless Commons ${project.version} API"
    isFailOnError = false

    options.encoding = "UTF8"
    (options as StandardJavadocDocletOptions).linkSource(true)
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    (options as StandardJavadocDocletOptions).bottom("<i>${copyright}</i>")
}

