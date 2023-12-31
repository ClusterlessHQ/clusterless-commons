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
import clusterless.commons.substrate.aws.cdk.naming.ArnRefs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.amazon.awscdk.App;
import software.amazon.awscdk.AppProps;
import software.constructs.Construct;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link App} that is scoped to a {@link Stage}, name {@link Label}, and {@link Version}.
 */
public class ScopedApp extends App {
    public static ScopedApp scopedOf(Construct scope) {
        return (ScopedApp) scope.getNode().getRoot();
    }

    private final Stage stage;
    private final Label name;
    private final Version version;

    private final ScopedMeta scopedMeta;

    private final Map<Ref, Construct> refConstructs = new HashMap<>();

    public ScopedApp(@NotNull AppProps props, @NotNull Stage stage, @NotNull Label name, @NotNull Version version) {
        this(props, stage, name, version, new ScopedMeta());
    }

    protected ScopedApp(@Nullable AppProps props, Stage stage, @NotNull Label name, @NotNull Version version, @NotNull ScopedMeta scopedMeta) {
        super(props);
        this.stage = stage;
        this.name = name;
        this.version = version;
        this.scopedMeta = scopedMeta;
    }

    public Stage stage() {
        return stage;
    }

    public Label name() {
        return name;
    }

    public Version version() {
        return version;
    }

    public ScopedMeta stagedMeta() {
        return scopedMeta;
    }

    public void addLocalConstruct(Ref ref, Construct construct) {
        refConstructs.put(ref, construct);
    }

    public Construct getLocalConstruct(Ref ref) {
        return refConstructs.get(ref);
    }

    public <T> T importArnRef(String ref, Function<String, T> resolver) {
        Construct construct = resolveLocalConstruct(ref);

        if (construct != null) {
            return (T) construct;
        }

        Optional<String> arn = ArnRefs.resolveArn(this, ref);

        return resolver.apply(arn.orElseThrow(() -> new IllegalArgumentException("ref or arn are required" + ref)));
    }

    /**
     * Resolve a local construct from a relative type ref.
     *
     * @param relativeTypeRef the relative type ref
     * @param <T>             the type of the construct
     * @return the construct
     */
    public <T extends Construct> T resolveLocalConstruct(String relativeTypeRef) {
        Objects.requireNonNull(relativeTypeRef, "relativeTypeRef must not be null");

        String[] split = relativeTypeRef.split(":");

        if (split.length > 4) {
            throw new IllegalStateException("invalid local ref: " + relativeTypeRef);
        }

        String provider = split.length == 4 ? split[0] : null;
        String resourceNs = split.length == 4 ? split[1] : split.length == 3 ? split[0] : null;
        String resourceType = split.length == 4 ? split[2] : split.length == 3 ? split[1] : split[0];
        String resourceName = split.length == 4 ? split[3] : split.length == 3 ? split[2] : split.length == 2 ? split[1] : split[0];

        Set<Map.Entry<Ref, Construct>> results = refConstructs.entrySet();

        results = results
                .stream()
                .filter(ref -> Label.of(resourceName).camelCase().equalsIgnoreCase(ref.getKey().resourceName().camelCase()))
                .collect(Collectors.toSet());

        if (results.isEmpty()) {
            throw new IllegalArgumentException("no constructs found for: " + relativeTypeRef + ", available: " + refConstructs.keySet());
        }

        if (results.size() == 1) {
            return (T) results.stream().findFirst().get().getValue();
        }

        if (resourceType == null) {
            throw new IllegalArgumentException("too many constructs found for: " + relativeTypeRef + ", available: " + results.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
        }

        results = results
                .stream()
                .filter(ref -> Label.of(resourceType).camelCase().equalsIgnoreCase(ref.getKey().resourceType().camelCase()))
                .collect(Collectors.toSet());

        if (results.isEmpty()) {
            throw new IllegalArgumentException("no constructs found for: " + relativeTypeRef + ", available: " + refConstructs.keySet());
        }

        if (results.size() == 1) {
            return (T) results.stream().findFirst().get().getValue();
        }

        if (resourceNs == null) {
            throw new IllegalArgumentException("too many constructs found for: " + relativeTypeRef + ", available: " + results.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
        }

        results = results
                .stream()
                .filter(ref -> Label.of(resourceNs).camelCase().equalsIgnoreCase(ref.getKey().resourceNs().camelCase()))
                .collect(Collectors.toSet());

        if (results.isEmpty()) {
            throw new IllegalArgumentException("no constructs found for: " + relativeTypeRef + ", available: " + refConstructs.keySet());
        }

        if (results.size() == 1) {
            return (T) results.stream().findFirst().get().getValue();
        }

        if (provider == null) {
            throw new IllegalArgumentException("too many constructs found for: " + relativeTypeRef + ", available: " + results.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
        }

        results = results
                .stream()
                .filter(ref -> Label.of(provider).camelCase().equalsIgnoreCase(ref.getKey().provider().camelCase()))
                .collect(Collectors.toSet());

        if (results.size() == 1) {
            return (T) results.stream().findFirst().get().getValue();
        }

        throw new IllegalArgumentException("no constructs found for: " + relativeTypeRef + ", available: " + refConstructs.keySet());
    }
}
