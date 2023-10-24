/*
 * Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package clusterless.commons.substrate.aws.cdk.scoped;

import clusterless.commons.naming.Label;
import clusterless.commons.naming.Ref;
import clusterless.commons.naming.Stage;
import clusterless.commons.naming.Version;
import org.jetbrains.annotations.NotNull;
import software.constructs.Construct;

import java.util.function.Function;

/**
 * A {@link Construct} that is used with a {@link ScopedStack} to provide scoped references to other constructs
 * that can be imported or exported.
 */
public class ScopedConstruct extends Construct {
    public ScopedConstruct(@NotNull Construct scope, @NotNull String id) {
        super(scope, id);
    }

    /**
     * The {@link Stage} of the {@link ScopedApp} that this construct is scoped to.
     *
     * @return the {@link Stage} of the {@link ScopedApp} that this construct is scoped to.
     */
    protected Stage stage() {
        return ScopedApp.scopedOf(this).stage();
    }

    /**
     * The name of the {@link ScopedApp} that this construct is scoped to.
     *
     * @return the name of the {@link ScopedApp} that this construct is scoped to.
     */
    protected Label scopedName() {
        return ScopedApp.scopedOf(this).name();
    }

    /**
     * The version of the {@link ScopedApp} that this construct is scoped to.
     *
     * @return the version of the {@link ScopedApp} that this construct is scoped to.
     */
    protected Version scopedVersion() {
        return ScopedApp.scopedOf(this).version();
    }

    protected void addArnRefFor(Ref ref, Construct construct, String value, String description) {
        ScopedStack.scopedOf(this)
                .addArnRef(ref, construct, value, description);
    }

    protected void addIdRefFor(Ref ref, Construct construct, String value, String description) {
        ScopedStack.scopedOf(this).addIdRefFor(ref, construct, value, description);
    }

    protected void addNameRefFor(Ref ref, Construct construct, String value, String description) {
        ScopedStack.scopedOf(this)
                .addNameRefFor(ref, construct, value, description);
    }

    protected <T> T resolveArnRef(String ref, Function<String, T> resolver) {
        return ScopedApp.scopedOf(this).resolveArnRef(ref, resolver);
    }
}
