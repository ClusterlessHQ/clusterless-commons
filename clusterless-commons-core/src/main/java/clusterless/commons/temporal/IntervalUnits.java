/*
 * Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package clusterless.commons.temporal;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Utility class for looking up a formatter given a unit, or a unit given a name.
 */
public class IntervalUnits {

    /**
     * Finds an appropriate DateTimeFormatter for the given unit
     *
     * @param unit a TemporalUnit instance to lookup
     * @throws IllegalArgumentException if the given unit does not have an associated formatter
     */
    public static void verifyHasFormatter(TemporalUnit unit) {
        //noinspection ResultOfMethodCallIgnored
        formatter(unit);
    }

    /**
     * Finds an appropriate DateTimeFormatter for the given unit
     *
     * @param unit a TemporalUnit instance to lookup
     * @return DateTimeFormatter
     * @throws IllegalArgumentException if the given unit does not have an associated formatter
     */
    public static DateTimeFormatter formatter(TemporalUnit unit) {
        if (unit == IntervalUnit.FOURTHS) {
            return IntervalDateTimeFormatter.FOURTH_FORMATTER;
        }
        if (unit == IntervalUnit.SIXTHS) {
            return IntervalDateTimeFormatter.SIXTH_FORMATTER;
        }
        if (unit == IntervalUnit.TWELFTHS) {
            return IntervalDateTimeFormatter.TWELFTH_FORMATTER;
        }

        throw new IllegalArgumentException("unsupported unit: " + unit);
    }

    /**
     * Finds a TemporalUnit with the given name.
     *
     * @param name of a TemporaUnit
     * @return TemporalUnit
     * @throws IllegalArgumentException if the named unit is not found
     */
    public static TemporalUnit find(String name) {
        Objects.requireNonNull(name, "name");

        try {
            return ChronoUnit.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return IntervalUnit.valueOf(name.toUpperCase(Locale.ROOT));
        }
    }

    /**
     * Given a string with an embedded duration, find the TemporalUnit associated with that duration.
     *
     * @param string with an embedded duration
     * @return TemporalUnit or null
     */
    public static Optional<TemporalUnit> findDurationWithin(String string) {
        Objects.requireNonNull(string, "string");

        for (IntervalUnit value : IntervalUnit.values()) {
            if (string.contains(value.getDuration().toString())) {
                return Optional.of(value);
            }
        }

        for (ChronoUnit value : ChronoUnit.values()) {
            if (string.contains(value.getDuration().toString())) {
                return Optional.of(value);
            }
        }

        return Optional.empty();
    }
}
