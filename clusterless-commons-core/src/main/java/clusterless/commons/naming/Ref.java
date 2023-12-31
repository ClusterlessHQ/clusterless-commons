/*
 * Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package clusterless.commons.naming;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Creates an identifier to use as an export/import key for provider output/export values.
 * <p/>
 * <pre>
 * aws:qualifier:[stage:]scopeName:scopeVersion:resourceNS:resourceType:resourceName
 * </pre>
 * <p/>
 * Where the qualifier represents the type of value being exported/imported:
 * - id
 * - name
 * - arn
 * <p/>
 * Where stage is the stage of the deployment, such as dev, test, prod, etc. It is optional.
 * <p/>
 * scopeName and scopeVersion are analogous to project names and versions.
 * <p/>
 * Resources are identified by a namespace, type, and name.
 * <p/>
 *
 * <pre>
 *   ref:aws:id:project-a:20230101:core:compute:spot
 *   ref:aws:id:dev:project-a:20230101:core:compute:spot
 * </pre>
 */
public final class Ref {
    public static Ref ref() {
        return new Ref();
    }

    public static Ref idRef() {
        return new Ref().withQualifier(Qualifier.Id);
    }

    public static Ref arnRef() {
        return new Ref().withQualifier(Qualifier.Arn);
    }

    public static Ref nameRef() {
        return new Ref().withQualifier(Qualifier.Name);
    }

    public static boolean isRef(String value) {
        return value != null && value.startsWith("ref:");
    }

    public static Optional<Qualifier> qualifier(String value) {
        if (!isRef(value)) {
            return Optional.empty();
        }

        return Qualifier.lookup(value.split(":")[2]);
    }

    public static Optional<String> provider(String value) {
        if (!isRef(value)) {
            return Optional.empty();
        }

        return Optional.ofNullable(value.split(":")[1]);
    }

    final Fixed provider;
    final Qualifier qualifier;
    final Stage stage;
    final Fixed scope;
    final Version scopeVersion;
    final Fixed resourceNs;
    final Fixed resourceType;
    final Fixed resourceName;

    public Ref() {
        provider = Fixed.fixedNull();
        qualifier = null;
        stage = Stage.nullStage();
        scope = Fixed.fixedNull();
        scopeVersion = Version.versionNull();
        resourceNs = Fixed.fixedNull();
        resourceType = Fixed.fixedNull();
        resourceName = Fixed.fixedNull();
    }

    private Ref(Fixed provider, Qualifier qualifier, Stage stage, Fixed scope, Version scopeVersion, Fixed resourceNs, Fixed resourceType, Fixed resourceName) {
        this.provider = provider;
        this.qualifier = qualifier;
        this.stage = stage == null ? Stage.nullStage() : stage.asLower();
        this.scope = scope;
        this.scopeVersion = scopeVersion;
        this.resourceNs = resourceNs;
        this.resourceType = resourceType;
        this.resourceName = resourceName;
    }

    public Ref withProvider(String provider) {
        Objects.requireNonNull(provider);
        return withProvider(Label.of(provider));
    }

    public Ref withProvider(Label provider) {
        Objects.requireNonNull(provider);
        return withProvider(Fixed.of(provider.lowerHyphen()));
    }

    public Ref withProvider(Fixed provider) {
        Label.requireNonEmpty(provider);
        return new Ref(provider, qualifier, stage, scope, scopeVersion, resourceNs, resourceType, resourceName);
    }

    public Ref withStage(Stage stage) {
        return new Ref(provider, qualifier, stage, scope, scopeVersion, resourceNs, resourceType, resourceName);
    }

    public Ref withScope(String scope) {
        return withScope(Label.of(scope));
    }

    public Ref withScope(Label scope) {
        return withScope(Fixed.of(scope.lowerHyphen()));
    }

    public Ref withScope(Fixed scope) {
        return new Ref(provider, qualifier, stage, scope, scopeVersion, resourceNs, resourceType, resourceName);
    }

    public Ref withScopeVersion(String scopeVersion) {
        return withScopeVersion(Version.of(scopeVersion));
    }

    public Ref withScopeVersion(Fixed scopeVersion) {
        return withScopeVersion(Version.of(scopeVersion.lowerHyphen()));
    }

    public Ref withScopeVersion(Version scopeVersion) {
        return new Ref(provider, qualifier, stage, scope, scopeVersion, resourceNs, resourceType, resourceName);
    }

    public Ref withResourceNs(String resourceNs) {
        return withResourceNs(Label.of(resourceNs));
    }

    public Ref withResourceNs(Label resourceNs) {
        return withResourceNs(Fixed.of(resourceNs.lowerHyphen()));
    }

    public Ref withResourceNs(Fixed resourceNs) {
        return new Ref(provider, qualifier, stage, scope, scopeVersion, resourceNs, resourceType, resourceName);
    }

    public Ref withResourceType(String resourceType) {
        return withResourceType(Label.of(resourceType));
    }

    public Ref withResourceType(Label resourceType) {
        return withResourceType(Fixed.of(resourceType.lowerHyphen()));
    }

    public Ref withResourceType(Fixed resourceType) {
        return new Ref(provider, qualifier, stage, scope, scopeVersion, resourceNs, resourceType, resourceName);
    }

    public Ref withResourceName(String resourceName) {
        return withResourceName(Label.of(resourceName));
    }

    public Ref withResourceName(Label resourceName) {
        return withResourceName(Fixed.of(resourceName.lowerHyphen()));
    }

    public Ref withResourceName(Fixed resourceName) {
        return new Ref(provider, qualifier, stage, scope, scopeVersion, resourceNs, resourceType, resourceName);
    }

    public Ref withQualifier(Qualifier qualifier) {
        return new Ref(provider, qualifier, stage, scope, scopeVersion, resourceNs, resourceType, resourceName);
    }

    public Label provider() {
        return provider;
    }

    public Stage stage() {
        return stage;
    }

    public Fixed scope() {
        return scope;
    }

    public Fixed scopeVersion() {
        return scopeVersion;
    }

    public Fixed resourceNs() {
        return resourceNs;
    }

    public Fixed resourceType() {
        return resourceType;
    }

    public Fixed resourceName() {
        return resourceName;
    }

    public Qualifier qualifier() {
        return qualifier;
    }

    public Label resourceLabel() {
        requireNonNull(resourceNs, "resourceNs required");
        requireNonNull(resourceType, "resourceType required");
        requireNonNull(resourceName, "resourceName required");

        return Label.NULL
                .with(resourceNs)
                .with(Label.of(resourceType.value()))
                .with(resourceName);
    }

    public Label label() {
        requireNonNull(provider, "provider required");
        requireNonNull(qualifier, "qualifier required");
        requireNonNull(scope, "scope required");
        requireNonNull(scopeVersion, "scopeVersion required");
        requireNonNull(resourceNs, "resourceNs required");
        requireNonNull(resourceType, "resourceType required");
        requireNonNull(resourceName, "resourceName required");

        return Label.of("ref")
                .with(provider)
                .with(qualifier)
                .with(stage)
                .with(scope)
                .with(scopeVersion)
                .with(resourceNs)
                .with(resourceType)
                .with(resourceName);
    }

    private static void requireNonNull(Label label, String message) {
        Objects.requireNonNull(label, message);

        if (label.isNull()) {
            throw new NullPointerException(message);
        }
    }


    public String exportName() {
        return label().lowerColonPath();
    }

    @Override
    public String toString() {
        return Label.of("ref")
                .with(provider)
                .with(qualifier)
                .with(stage)
                .with(scope)
                .with(scopeVersion)
                .with(resourceNs)
                .with(resourceType)
                .with(resourceName).lowerColonPath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ref ref = (Ref) o;
        return Objects.equals(provider, ref.provider) && Objects.equals(stage, ref.stage) && Objects.equals(scope, ref.scope) && Objects.equals(scopeVersion, ref.scopeVersion) && Objects.equals(resourceNs, ref.resourceNs) && Objects.equals(resourceType, ref.resourceType) && Objects.equals(resourceName, ref.resourceName) && qualifier == ref.qualifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(provider, stage, scope, scopeVersion, resourceNs, resourceType, resourceName, qualifier);
    }

    public enum Qualifier implements Label.EnumLabel {
        Name,
        Id,
        Arn;

        static final Map<String, Qualifier> map = new HashMap<>() {
            {
                put(Name.lowerHyphen(), Name);
                put(Id.lowerHyphen(), Id);
                put(Arn.lowerHyphen(), Arn);
            }
        };

        public static Optional<Qualifier> lookup(String value) {
            if (value == null) {
                return null;
            }

            return Optional.ofNullable(map.get(value.toLowerCase()));
        }
    }
}
