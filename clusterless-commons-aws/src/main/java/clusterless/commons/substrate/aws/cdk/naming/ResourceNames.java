/*
 * Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package clusterless.commons.substrate.aws.cdk.naming;

import clusterless.commons.naming.Fixed;
import clusterless.commons.naming.Label;
import clusterless.commons.naming.Region;
import clusterless.commons.naming.Stage;
import clusterless.commons.substrate.aws.cdk.scoped.ScopedApp;
import software.amazon.awscdk.Stack;
import software.constructs.Construct;

import java.util.Objects;

/**
 * Simplifies the creation of resources names.
 * <p>
 * Names are scoped to the stage, account, and region, and optionally to the application name and version.
 * <p>
 * Region unique names include the stage and any additional qualifiers.
 * <p>
 * Account unique names include the region.
 * <p>
 * Global unique names include the account id.
 * <p>
 * The {@link Label} returned can be used in different contexts. Where the name String is lower-hyphenated.
 */
public class ResourceNames {
    /**
     * Returns a global unique name for the given scope and name.
     *
     * @param scope the scope
     * @param name  the name
     * @return a global unique name
     */
    public static String globalUniqueName(Construct scope, String name) {
        return globalUniqueLabel(scope, name).lowerHyphen();
    }

    /**
     * Returns a global unique {@link Label} for the given scope and name.
     *
     * @param scope the scope
     * @param name  the name
     * @return a global unique {@link Label}
     */
    public static Label globalUniqueLabel(Construct scope, String name) {
        Objects.requireNonNull(name, "name may not be null");

        Label region = Region.of(Stack.of(scope).getRegion());
        Label account = Fixed.of(Stack.of(scope).getAccount());
        Label stage = ScopedApp.scopedOf(scope).stage();

        return stage.upperOnly()
                .with(name)
                .with(account)
                .with(region);
    }

    /**
     * Returns a global unique scoped name for the given scope and name.
     *
     * @param scope the scope
     * @param name  the name
     * @return a global unique scoped name
     */
    public static String globalUniqueScopedName(Construct scope, String name) {
        return globalUniqueScopedLabel(scope, name).lowerHyphen();
    }

    /**
     * Returns a global unique scoped {@link Label} for the given scope and name.
     *
     * @param scope the scope
     * @param name  the name
     * @return a global unique scoped {@link Label}
     */
    public static Label globalUniqueScopedLabel(Construct scope, String name) {
        Objects.requireNonNull(name, "name may not be null");

        Label region = Region.of(Stack.of(scope).getRegion());
        Label account = Fixed.of(Stack.of(scope).getAccount());
        Label stage = ScopedApp.scopedOf(scope).stage();
        Label scopedName = ScopedApp.scopedOf(scope).name();
        Label scopedVersion = ScopedApp.scopedOf(scope).version();

        return stage.upperOnly()
                .with(scopedName)
                .with(name)
                .with(scopedVersion)
                .with(account)
                .with(region);
    }

    /**
     * Returns an account unique name for the given scope and name.
     *
     * @param scope the scope
     * @param name  the name
     * @return an account unique name
     */
    public static String accountUniqueName(Construct scope, String name) {
        return accountUniqueLabel(scope, name).lowerHyphen();
    }

    /**
     * Returns an account unique {@link Label} for the given scope and name.
     *
     * @param scope the scope
     * @param name  the name
     * @return an account unique {@link Label}
     */
    public static Label accountUniqueLabel(Construct scope, String name) {
        Objects.requireNonNull(name, "name may not be null");

        Label region = Region.of(Stack.of(scope).getRegion());
        Label stage = ScopedApp.scopedOf(scope).stage();

        return stage.upperOnly()
                .with(name)
                .with(region);
    }

    /**
     * Returns an account unique scoped name for the given scope and name.
     *
     * @param scope the scope
     * @param name  the name
     * @return an account unique scoped name
     */
    public static String accountUniqueScopedName(Construct scope, String name) {
        return accountUniqueScopedLabel(scope, name).lowerHyphen();
    }

    /**
     * Returns an account unique scoped {@link Label} for the given scope and name.
     *
     * @param scope the scope
     * @param name  the name
     * @return an account unique scoped {@link Label}
     */
    public static Label accountUniqueScopedLabel(Construct scope, String name) {
        Objects.requireNonNull(name, "name may not be null");

        Label region = Region.of(Stack.of(scope).getRegion());
        Label stage = ScopedApp.scopedOf(scope).stage();
        Label scopedName = ScopedApp.scopedOf(scope).name();
        Label scopedVersion = ScopedApp.scopedOf(scope).version();

        return stage.upperOnly()
                .with(scopedName)
                .with(name)
                .with(scopedVersion)
                .with(region);
    }

    /**
     * Returns a region unique name for the given scope and name.
     *
     * @param scope the scope
     * @param name  the name
     * @return a region unique name
     */
    public static String regionUniqueName(Construct scope, String name) {
        return regionUniqueLabel(scope, Label.of(name), null).lowerHyphen();
    }

    /**
     * Returns a region unique {@link Label} for the given scope and name.
     *
     * @param scope the scope
     * @param name  the name
     * @return a region unique {@link Label}
     */
    public static Label regionUniqueLabel(Construct scope, Label name) {
        return regionUniqueLabel(scope, name, Label.NULL);
    }

    /**
     * Returns a region unique {@link Label} for the given scope and name.
     *
     * @param scope     the scope
     * @param name      the name
     * @param qualifier the qualifier
     * @return a region unique {@link Label}
     */
    public static Label regionUniqueLabel(Construct scope, Label name, Label qualifier) {
        Objects.requireNonNull(name, "name may not be null");

        Stage stage = ScopedApp.scopedOf(scope).stage();

        return stage.upperOnly()
                .with(name)
                .with(qualifier);
    }

    /**
     * Returns a region unique scoped name for the given scope and name.
     *
     * @param scope the scope
     * @param name  the name
     * @return a region unique scoped name
     */
    public static String regionUniqueScopedName(Construct scope, String name) {
        Label label = regionUniqueScopedLabel(scope, Label.of(name));

        return label
                .lowerHyphen();
    }

    /**
     * Returns a region unique scoped {@link Label} for the given scope and name.
     *
     * @param scope the scope
     * @param name  the name
     * @return a region unique scoped {@link Label}
     */
    public static Label regionUniqueScopedLabel(Construct scope, Label name) {
        return regionUniqueScopedLabel(scope, name, Label.NULL);
    }

    /**
     * Returns a region unique scoped {@link Label} for the given scope, name, and qualifier.
     *
     * @param scope     the scope
     * @param name      the name
     * @param qualifier the qualifier
     * @return a region unique scoped {@link Label}
     */
    public static Label regionUniqueScopedLabel(Construct scope, Label name, Label qualifier) {
        Objects.requireNonNull(name, "name may not be null");
        Objects.requireNonNull(qualifier, "qualifier may not be null");

        Label stage = ScopedApp.scopedOf(scope).stage();
        Label scopeName = ScopedApp.scopedOf(scope).name();
        Label scopeVersion = ScopedApp.scopedOf(scope).version();

        return stage.upperOnly()
                .with(scopeName)
                .with(name)
                .with(scopeVersion)
                .with(qualifier);
    }
}
