/*
 * Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package clusterless.commons.substrate.aws.cdk.construct;

import clusterless.commons.naming.Label;
import clusterless.commons.naming.Ref;
import clusterless.commons.substrate.aws.cdk.scoped.ScopedConstruct;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.CfnOutputProps;
import software.constructs.Construct;

/**
 * Creates a CloudFormation Output for a given Construct and a {@link Ref} used to name the export.
 */
public class OutputConstruct extends ScopedConstruct {
    private final String exportName;

    public OutputConstruct(@NotNull Construct construct, Ref ref, String value, String description) {
        super(construct, Label.of("Output").with(ref.resourceLabel()).with(ref.qualifier()).camelCase());

        exportName = createOutputFor(ref, value, description);
    }

    public String exportName() {
        return exportName;
    }

    protected String createOutputFor(Ref ref, String value, String description) {
        String exportName = ref.exportName();

        new CfnOutput(this, ref.resourceLabel().camelCase(), new CfnOutputProps.Builder()
                .exportName(exportName)
                .value(value)
                .description(description)
                .build());

        return exportName;
    }
}
