/*
 * Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

plugins {
    id("clusterless.commons.java-common-properties")
    id("clusterless.commons.java-common-conventions")

    `java-library`
    `maven-publish`
    signing
}

dependencies {
    implementation("org.jetbrains:annotations")
    implementation("com.google.guava:guava")

    api("org.slf4j:slf4j-api")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl")
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ClusterlessHQ/clusterless-commons")
            credentials {
                username = extra["repoUserName"] as String? ?: System.getenv("GPR_USERNAME")
                password = extra["repoPassword"] as String? ?: System.getenv("GPR_TOKEN")
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {

            from(components["java"])

            pom {
                name = "Clusterless Commons"
                description = "APIs for building things in the cloud."
                url = "https://docs.clusterless.io/"
                inceptionYear = "2023"
                licenses {
                    license {
                        name = "Mozilla Public License, v. 2.0"
                        url = "https://mozilla.org/MPL/2.0/"
                        distribution = "repo"
                    }
                }
                developers {
                    developer {
                        id = "cwensel"
                        name = "Chris K Wensel"
                        email = "chris@wensel.net"
                    }
                }
                scm {
                    url = "https://github.com/ClusterlessHQ/clusterless-commons/"
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}
