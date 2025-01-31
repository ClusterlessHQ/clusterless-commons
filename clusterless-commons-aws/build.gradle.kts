/*
 * Copyright (c) 2023-2025 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

plugins {
    id("clusterless.commons.java-library-conventions")
}

dependencies {
    implementation(project(":clusterless-commons-core"))

    // https://mvnrepository.com/artifact/software.amazon.awscdk/aws-cdk-lib
    val cdkVersion = "2.177.0"
    api("software.amazon.awscdk:aws-cdk-lib:$cdkVersion")
    // https://mvnrepository.com/artifact/software.constructs/constructs
    val constructsVersion = "10.4.2"
    api("software.constructs:constructs:$constructsVersion")
}
