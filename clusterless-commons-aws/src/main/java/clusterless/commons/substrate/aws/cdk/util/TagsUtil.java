/*
 * Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package clusterless.commons.substrate.aws.cdk.util;

import software.amazon.awscdk.TagProps;
import software.amazon.awscdk.Tags;
import software.constructs.IConstruct;

import java.util.Map;

/**
 * The TagsUil class is a helper for applying tags to a {@link IConstruct}.
 * <p>
 * Tag application may be disabled by calling {@link #disable()}.
 */
public class TagsUtil {
    private static boolean enabled = true;

    public static void disable() {
        enabled = false;
    }

    public static void applyTags(IConstruct target, Map<String, String> tagMap) {
        applyTags(target, tagMap, null);
    }

    public static void applyTags(IConstruct target, Map<String, String> tagMap, TagProps tagProps) {
        if (!enabled || tagMap == null || tagMap.isEmpty()) {
            return;
        }

        Tags tags = Tags.of(target);

        tagMap.forEach((key, value) -> tags.add(key, value, tagProps));
    }
}
