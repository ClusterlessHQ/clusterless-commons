/*
 * Copyright (c) 2023-2025 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package clusterless.commons.temporal;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

import static java.time.temporal.ChronoField.MINUTE_OF_DAY;

/**
 * Breaks a day into the number of intervals requested.
 * <p>
 * Day is the actual day as an interval where the day component is of the month.
 * <p/>
 * Fourths is a 15-minute duration, there are 4 Fourths in an hour, and 96 Fourths in a day.
 * <p/>
 * Sixths is a 10-minute duration, there are 6 Sixths in an hour, and 144 Sixths in a day.
 * <p/>
 * Twelfths is a 5-minute duration, there are 12 Twelfths in an hour, and 288 Twelfth in a day.
 */
public enum IntervalUnit implements TemporalUnit {
    DAY("Day", Duration.ofDays(1)),
    HOURS("Hours", Duration.ofHours(1)),
    FOURTHS("Fourths", Duration.ofMinutes(15)),
    SIXTHS("Sixths", Duration.ofMinutes(10)),
    TWELFTHS("Twelfths", Duration.ofMinutes(5));

    private final String name;
    private final Duration duration;

    IntervalUnit(String name, Duration estimatedDuration) {
        this.name = name;
        this.duration = estimatedDuration;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean isDurationEstimated() {
        return false;
    }

    @Override
    public boolean isDateBased() {
        return false;
    }

    @Override
    public boolean isTimeBased() {
        return true;
    }

    @Override
    public boolean isSupportedBy(Temporal temporal) {
        return temporal.isSupported(MINUTE_OF_DAY);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends Temporal> R addTo(R temporal, long amount) {
        switch (this) {
            case DAY:
                return (R) temporal.plus(amount, ChronoUnit.DAYS);
            case HOURS:
                return (R) temporal.plus(amount, ChronoUnit.HOURS);
            case FOURTHS:
                return (R) temporal.plus(15 * amount, ChronoUnit.MINUTES);
            case SIXTHS:
                return (R) temporal.plus(10 * amount, ChronoUnit.MINUTES);
            case TWELFTHS:
                return (R) temporal.plus(5 * amount, ChronoUnit.MINUTES);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public long between(Temporal temporal1Inclusive, Temporal temporal2Exclusive) {
        if (temporal1Inclusive.getClass() != temporal2Exclusive.getClass()) {
            return temporal1Inclusive.until(temporal2Exclusive, this);
        }
        switch (this) {
            case DAY:
                return temporal1Inclusive.until(temporal2Exclusive, ChronoUnit.DAYS);
            case HOURS:
                return temporal1Inclusive.until(temporal2Exclusive, ChronoUnit.HOURS);
            case FOURTHS:
                return temporal1Inclusive.until(temporal2Exclusive, ChronoUnit.MINUTES) / 15;
            case SIXTHS:
                return temporal1Inclusive.until(temporal2Exclusive, ChronoUnit.MINUTES) / 10;
            case TWELFTHS:
                return temporal1Inclusive.until(temporal2Exclusive, ChronoUnit.MINUTES) / 5;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
