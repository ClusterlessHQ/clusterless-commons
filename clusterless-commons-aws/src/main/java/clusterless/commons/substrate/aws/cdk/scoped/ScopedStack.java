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
import clusterless.commons.substrate.aws.cdk.construct.OutputConstruct;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Token;
import software.constructs.Construct;

import java.util.stream.Stream;

/**
 * A {@link Stack} that is scoped to a {@link Stage}, name {@link Label}, and {@link Version}.
 * <p>
 * For use within a {@link ScopedApp}.
 */
public class ScopedStack extends Stack {
    public static ScopedStack scopedOf(Construct construct) {
        Stack stack = Stack.of(construct);
        if (stack instanceof ScopedStack) {
            return (ScopedStack) stack;
        }

        throw new IllegalArgumentException("construct does not belong to a ScopedStack, found: " + stack.getClass().getName());
    }

    private final Stage stage;

    public ScopedStack(@NotNull ScopedApp app, @Nullable String id, @Nullable StackProps props) {
        super(app, id, props);
        stage = app.stage();
    }

    public ScopedStack(@NotNull ScopedApp app, @Nullable String id) {
        super(app, id);
        stage = app.stage();
    }

    public ScopedStack(@NotNull ScopedApp app) {
        super(app);
        stage = app.stage();
    }

    /**
     * The {@link Stage} of the {@link ScopedApp} that this construct is scoped to.
     *
     * @return the {@link Stage} of the {@link ScopedApp} that this construct is scoped to.
     */
    public Stage stage() {
        return stage;
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

    protected void exportNameRefFor(Ref ref, String value, String description) {
        exportNameRefFor(ref, null, value, description);
    }

    public void exportNameRefFor(Ref ref, Construct construct, String value, String description) {
        Ref.Qualifier qualifier = Ref.Qualifier.Name;
        Ref qualifiedRef = withContext(ref).withQualifier(qualifier);

        OutputConstruct outputConstruct = new OutputConstruct(this, qualifiedRef, value, description);

        if (!Token.isUnresolved(value)) {
            ScopedApp.scopedOf(this)
                    .stagedMeta()
                    .setName(ref.resourceType().value(), value);
        }

        ScopedApp.scopedOf(this)
                .stagedMeta()
                .setNameRef(ref.resourceType().value(), outputConstruct.exportName());

        if (construct != null) {
            ScopedApp.scopedOf(this)
                    .addLocalConstruct(qualifiedRef, construct);
        }
    }

    protected void exportIdRefFor(Ref ref, String value, String description) {
        exportIdRefFor(ref, null, value, description);
    }

    public void exportIdRefFor(Ref ref, Construct construct, String value, String description) {
        Ref.Qualifier qualifier = Ref.Qualifier.Id;
        Ref qualifiedRef = withContext(ref).withQualifier(qualifier);

        OutputConstruct outputConstruct = new OutputConstruct(this, qualifiedRef, value, description);

        if (!Token.isUnresolved(value)) {
            ScopedApp.scopedOf(this)
                    .stagedMeta()
                    .setId(ref.resourceType().value(), value);
        }

        ScopedApp.scopedOf(this)
                .stagedMeta()
                .setIdRef(ref.resourceType().value(), outputConstruct.exportName());

        if (construct != null) {
            ScopedApp.scopedOf(this)
                    .addLocalConstruct(qualifiedRef, construct);
        }
    }

    protected void exportArnRefFor(Ref ref, String value, String description) {
        exportArnRefFor(ref, null, value, description);
    }

    public void exportArnRefFor(Ref ref, Construct construct, String value, String description) {
        Ref.Qualifier qualifier = Ref.Qualifier.Arn;
        Ref qualifiedRef = withContext(ref).withQualifier(qualifier);

        OutputConstruct outputConstruct = new OutputConstruct(this, qualifiedRef, value, description);

        ScopedApp.scopedOf(this)
                .stagedMeta()
                .setArnRef(ref.resourceType().value(), outputConstruct.exportName());

        if (construct != null) {
            ScopedApp.scopedOf(this)
                    .addLocalConstruct(qualifiedRef, construct);
        }
    }

    /**
     * Adds `aws` and the current stage to the given ref.
     *
     * @param ref the ref
     * @return the ref with `aws` and the current stage
     */
    protected Ref withContext(Ref ref) {
        return ref
                .withProvider("aws")
                .withStage(stage());
    }

    /**
     * Adds the scoped name and version to the given ref.
     *
     * @param ref the ref
     * @return the ref with the scoped name and version
     */
    protected Ref withScope(Ref ref) {
        return ref
                .withScope(scopedName())
                .withScopeVersion(scopedVersion());
    }

    public <T extends Construct> Stream<T> findHaving(Class<T> type) {
        return getNode().findAll().stream()
                .filter(node -> type.isAssignableFrom(node.getClass()))
                .map(type::cast);
    }
}
