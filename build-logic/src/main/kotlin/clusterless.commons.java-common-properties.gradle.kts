/*
* Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

extra["group"] = "io.clusterless"
extra["version"] = "0.11"

extra["repoUserName"] = System.getProperty("publish.repo.userName")
extra["repoPassword"] = System.getProperty("publish.repo.password")

extra["currentCommit"] = System.getProperty("build.vcs.number")
extra["currentBranch"] = System.getProperty("build.vcs.branch")
