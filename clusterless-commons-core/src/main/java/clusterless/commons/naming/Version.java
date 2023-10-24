/*
 * Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package clusterless.commons.naming;

import java.util.Objects;

/**
 * Version is a {@link Fixed} value that represents a version.
 */
public class Version extends Fixed {
    private static final Version NULL_VERSION = new Version(null);

    public static Version of(String version) {
        Objects.requireNonNull(version, "version may not be null");
        return new Version(version);
    }

    public static Version versionNull() {
        return NULL_VERSION;
    }

    protected Version(String value) {
        super(value);
    }

}
