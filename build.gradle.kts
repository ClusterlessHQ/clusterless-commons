/*
 * Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

plugins {
    id("clusterless.commons.java-common-properties")
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0-rc-1"
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

            username = extra["repoUserName"] as String? ?: System.getenv("MCR_USERNAME")
            password = extra["repoPassword"] as String? ?: System.getenv("MCR_PASSWORD")
        }
    }
}
