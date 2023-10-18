@file:Suppress("UnstableApiUsage")

/*
* Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

plugins {
    java
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

group = "io.clusterless"
version = "0.1"

dependencies {
    constraints {
        implementation("org.jetbrains:annotations:24.0.0")
        implementation("com.google.guava:guava:31.1-jre")

        implementation("org.slf4j:slf4j-api:2.0.9")

        val log4j = "2.20.0"
        implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4j")
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
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    withJavadocJar()
    withSourcesJar()
}

var copyright = "Copyright &#169; 2023 <a href=\"https://chris.wensel.net/\">Chris K Wensel</a>. All Rights Reserved."

tasks.named<Javadoc>("javadoc") {
    title = "Clusterless Commons"
    isFailOnError = false

    options.encoding = "UTF8"
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    (options as StandardJavadocDocletOptions).bottom("<i>${copyright}</i>")
}

