/*
 * Copyright (c) 2023-2025 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package clusterless.commons.temporal;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalUnit;

import static clusterless.commons.temporal.IntervalField.*;
import static clusterless.commons.temporal.IntervalField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.*;

/**
 * IntervalDateTimeFormatter provided formatters for the {@link IntervalUnit} units.
 * <p>
 * See {@link IntervalUnits#formatter(TemporalUnit)} for looking up an appropriate formatter.
 */
public class IntervalDateTimeFormatter {
    public static final DateTimeFormatter DAY_FORMATTER;
    public static final DateTimeFormatter HOUR_FORMATTER;
    public static final DateTimeFormatter FOURTH_FORMATTER;
    public static final DateTimeFormatter SIXTH_FORMATTER;
    public static final DateTimeFormatter TWELFTH_FORMATTER;

    static {
        DAY_FORMATTER = new DateTimeFormatterBuilder()
                .parseStrict()
                .appendValue(YEAR, 4)
                .appendValue(MONTH_OF_YEAR, 2)
                .appendValue(DAY_OF_MONTH, 2)
                .appendLiteral(DAY.getBaseUnit().getDuration().toString())
                // ensure that the TemporalAccessor is set to midnight
                .parseDefaulting(HOUR_OF_DAY, 0)
                .parseDefaulting(MINUTE_OF_HOUR, 0)
                .parseDefaulting(SECOND_OF_MINUTE, 0)
                .toFormatter()
                .withZone(ZoneOffset.UTC);
    }

    static {
        HOUR_FORMATTER = new DateTimeFormatterBuilder()
                .parseStrict()
                .appendValue(YEAR, 4)
                .appendValue(MONTH_OF_YEAR, 2)
                .appendValue(DAY_OF_MONTH, 2)
                .appendLiteral(HOUR_OF_DAY.getBaseUnit().getDuration().toString())
                .appendValue(HOUR_OF_DAY, 2)
                .toFormatter()
                .withZone(ZoneOffset.UTC);
    }

    static {
        FOURTH_FORMATTER = new DateTimeFormatterBuilder()
                .parseStrict()
                .appendValue(YEAR, 4)
                .appendValue(MONTH_OF_YEAR, 2)
                .appendValue(DAY_OF_MONTH, 2)
                .appendLiteral(FOURTH_OF_DAY.getBaseUnit().getDuration().toString())
                .appendValue(FOURTH_OF_DAY, 2) // max 96
                .toFormatter()
                .withZone(ZoneOffset.UTC);
    }

    static {
        SIXTH_FORMATTER = new DateTimeFormatterBuilder()
                .parseStrict()
                .appendValue(YEAR, 4)
                .appendValue(MONTH_OF_YEAR, 2)
                .appendValue(DAY_OF_MONTH, 2)
                .appendLiteral(SIXTH_OF_DAY.getBaseUnit().getDuration().toString())
                .appendValue(SIXTH_OF_DAY, 3)
                .toFormatter()
                .withZone(ZoneOffset.UTC);
    }

    static {
        TWELFTH_FORMATTER = new DateTimeFormatterBuilder()
                .parseStrict()
                .appendValue(YEAR, 4)
                .appendValue(MONTH_OF_YEAR, 2)
                .appendValue(DAY_OF_MONTH, 2)
                .appendLiteral(TWELFTH_OF_DAY.getBaseUnit().getDuration().toString())
                .appendValue(TWELFTH_OF_DAY, 3)
                .toFormatter()
                .withZone(ZoneOffset.UTC);
    }
}
