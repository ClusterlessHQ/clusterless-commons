/*
 * Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package clusterless.commons.naming;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RefTest {
    @Test
    void name() {
        Label label = new Ref()
                .withProvider("aws")
                .withQualifier(Ref.Qualifier.Id)
                .withScope("projectA")
                .withScopeVersion("20230101")
                .withResourceNs("core")
                .withResourceType("compute")
                .withResourceName("spot")
                .label();

        Assertions.assertEquals("ref:aws:id:project-a:20230101:core:compute:spot", label.lowerColonPath());
    }

    @Test
    void staged() {
        Label label = new Ref()
                .withProvider("aws")
                .withStage(Stage.of("dev"))
                .withQualifier(Ref.Qualifier.Id)
                .withScope("projectA")
                .withScopeVersion("20230101")
                .withResourceNs("core")
                .withResourceType("compute")
                .withResourceName("spot")
                .label();

        Assertions.assertEquals("ref:aws:id:dev:project-a:20230101:core:compute:spot", label.lowerColonPath());
    }
}
