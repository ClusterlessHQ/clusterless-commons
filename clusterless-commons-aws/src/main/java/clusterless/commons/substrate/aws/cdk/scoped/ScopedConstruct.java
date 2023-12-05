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

    /**
     * Exports the given arn for the construct under the {@link Ref} name.
     *
     * @param ref         the {@link Ref} to use as the name
     * @param construct   the construct to export
     * @param arn         the arn to export
     * @param description the description of the export
     */
    protected void exportArnRefFor(Ref ref, Construct construct, String arn, String description) {
        ScopedStack.scopedOf(this)
                .exportArnRefFor(ref, construct, arn, description);
    }

    /**
     * Exports the given id for the construct under the {@link Ref} name.
     *
     * @param ref         the {@link Ref} to use as the name
     * @param construct   the construct to export
     * @param id          the id to export
     * @param description the description of the export
     */
    protected void exportIdRefFor(Ref ref, Construct construct, String id, String description) {
        ScopedStack.scopedOf(this).exportIdRefFor(ref, construct, id, description);
    }

    /**
     * Exports the given name for the construct under the {@link Ref} name.
     *
     * @param ref         the {@link Ref} to use as the name
     * @param construct   the construct to export
     * @param name        the name to export
     * @param description the description of the export
     */
    protected void exportNameRefFor(Ref ref, Construct construct, String name, String description) {
        ScopedStack.scopedOf(this)
                .exportNameRefFor(ref, construct, name, description);
    }

    /**
     * Import the arn value represented by the given {@link Ref} and return the resolved value.
     *
     * @param ref      the {@link Ref} to resolve
     * @param resolver the resolver to use to resolve the value
     * @param <T>      the type of the resolved value, usually a Construct
     * @return the resolved value, usually a Construct
     */
    protected <T> T importArnRef(String ref, Function<String, T> resolver) {
        return ScopedApp.scopedOf(this)
                .importArnRef(ref, resolver);
    }

    /**
     * Resolve a local construct from a relative type ref.
     *
     * @param relativeTypeRef the relative type ref
     * @param <T>             the type of the resolved construct
     * @return the resolved construct
     */
    protected <T extends Construct> T resolveLocalConstruct(String relativeTypeRef) {
        Construct construct = ScopedApp.scopedOf(this)
                .resolveLocalConstruct(relativeTypeRef);

        return (T) construct;
    }
}
