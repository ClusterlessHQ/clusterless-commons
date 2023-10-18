/*
 * Copyright (c) 2023 Chris K Wensel <chris@wensel.net>. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package clusterless.commons.naming;

import clusterless.commons.util.Strings;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

/**
 * Label simplifies creating complex strings used for naming, displays, and paths.
 * <p/>
 * An Enum can be a Label by implementing {@link EnumLabel}.
 *
 * <pre>
 *  Label.of("foo").with("bar").with("baz").camelCase() // "fooBarBaz"
 *  Label.of("foo").with("bar").with("baz").lowerHyphen() // "foo-bar-baz"
 *  Label.of("foo").with("barBar").with("baz").lowerHyphenPath() // "foo/bar-bar/baz"
 * </pre>
 */
public interface Label {
    /**
     * Concatenate all the given labels .
     *
     * @param labels the labels to concatenate
     * @return a concatenated Label instance
     */
    static Label concat(Label... labels) {
        return Arrays.stream(labels).reduce(Label.NULL, Label::with);
    }

    static void requireNonEmpty(Label label) {
        if (label == null || label.isNull()) {
            throw new NullPointerException();
        }
    }

    interface EnumLabel extends Label {
        String name();

        default String camelCase() {
            return name();
        }
    }

    Label NULL = () -> null;

    static String nameOrNull(Label value) {
        return value == null ? null : value.camelCase();
    }

    /**
     * Convert the given value to a Label.
     *
     * @param value the value to convert to a Label
     * @return
     */
    static Label of(Object value) {
        if (value == null) {
            return NULL;
        }

        if (value instanceof String) {
            return of((String) value);
        }

        if (value instanceof Label) {
            return of((Label) value);
        }

        return of(value.toString());
    }

    private static Label of(Label value) {
        if (value == null) {
            return NULL;
        }

        return value;
    }

    static Label of(Label value, Label abbr) {
        if (value == null) {
            return NULL;
        }

        if (abbr == null || abbr.isNull()) {
            return value;
        }

        return value.abbreviated(abbr);
    }

    /**
     * Convert the given value to a Label with an abbreviation.
     *
     * @param full the full name
     * @param abbr the abbreviated name
     * @return a Label instance
     */
    static Label of(String full, String abbr) {
        return of(of(full), of(abbr));
    }

    /**
     * Convert the given value to a {@link Fixed} Label.
     *
     * @param fixed the value to fix the format of
     * @return a Label instance
     */
    static Label fixed(String fixed) {
        return new Fixed(fixed);
    }

    default Label abbreviated(Label abbr) {
        return new Label() {
            @Override
            public String camelCase() {
                return Label.this.camelCase();
            }

            @Override
            public Label abbreviated() {
                return abbr;
            }

            @Override
            public String toString() {
                return camelCase();
            }
        };
    }

    /**
     * Return a Label instance with the value to upper case.
     *
     * @return a Label instance
     */
    default Label upperOnly() {
        return new Fixed(Label.this.camelCase() != null ? Label.this.camelCase().toUpperCase(Locale.ROOT) : null);
    }

    private static Label of(String value) {
        if (value == null) {
            return NULL;
        }

        if (value.contains("-")) {
            return fromLowerHyphen(value);
        }

        if (value.contains("_")) {
            return fromLowerUnderscore(value);
        }

        return () -> Strings.upperCamel(value);
    }

    /**
     * Convert the given value in lower hyphen format to a Label.
     *
     * @param value in lower hyphen format
     * @return a Label instance
     */
    static Label fromLowerHyphen(String value) {
        return () -> Strings.lowerHyphenToUpperCamel(value);
    }

    /**
     * Convert the given value in lower underscore format to a Label.
     *
     * @param value in lower underscore format
     * @return a Label instance
     */
    static Label fromLowerUnderscore(String value) {
        return () -> Strings.lowerUnderscoreToCamelCase(value);
    }

    /**
     * Return true if the Label is a "null" Label.
     *
     * @return true if the Label is a "null" Label
     */
    default boolean isNull() {
        return camelCase() == null;
    }

    /**
     * Return a Label that concatenates the given values.
     *
     * @param values the values to concatenate
     * @return
     */
    default Label having(String... values) {
        return Arrays.stream(values)
                .map(Label::of)
                .reduce(this, Label::with);
    }

    /**
     * Return a Label that concatenates the given value.
     * <p/>
     * This method accepts a Label or an Object which is converted to a String before concatenation.
     * <p>
     * Use this method to build reusable Labels.
     *
     * <pre>
     *     Label end = Label.of("endOfLabel");
     *     Label label = Label.of("foo").with("bar").with("baz").with(scope);
     * </pre>
     *
     * @param object the value to concatenate
     * @return a Label instance
     */
    default Label with(Object object) {
        if (object == null) {
            return this;
        }

        if (!(object instanceof Label)) {
            return with(Label.of(object.toString()));
        }

        Label label = (Label) object;

        if (label.isNull()) {
            return this;
        }

        // if first in chain in already null, return the next
        if (this.isNull()) {
            return label;
        }

        return new Label() {
            @Override
            public String camelCase() {
                return String.format("%s%s", Label.this.camelCase(), label.camelCase());
            }

            @Override
            public String lowerCamelCase() {
                return String.format("%s%s", Label.this.lowerCamelCase(), label.camelCase());
            }

            @Override
            public String lowerColonPath() {
                return String.format("%s:%s", Label.this.lowerColonPath(), label.lowerColonPath());
            }

            @Override
            public String lowerHyphen() {
                return String.format("%s-%s", Label.this.lowerHyphen(), label.lowerHyphen());
            }

            @Override
            public String lowerHyphenPath() {
                return String.format("%s/%s", Label.this.lowerHyphenPath(), label.lowerHyphenPath());
            }

            @Override
            public String lowerUnderscore() {
                return String.format("%s_%s", Label.this.lowerUnderscore(), label.lowerUnderscore());
            }

            @Override
            public String upperUnderscore() {
                return String.format("%s_%s", Label.this.upperUnderscore(), label.upperUnderscore());
            }

            @Override
            public String shortCamelCase() {
                return String.format("%s%s", Label.this.shortCamelCase(), label.shortCamelCase());
            }

            @Override
            public String shortLowerHyphen() {
                return String.format("%s-%s", Label.this.shortLowerHyphen(), label.shortLowerHyphen());
            }

            @Override
            public String shortLowerUnderscore() {
                return String.format("%s_%s", Label.this.shortLowerUnderscore(), label.shortLowerUnderscore());
            }

            @Override
            public String toString() {
                return camelCase();
            }
        };
    }

    /**
     * Return the given Label if it does not represent "null" otherwise return this instance.
     *
     * @param label the Label to return if not null
     * @return the given Label if not null otherwise this instance
     */
    default Label thisIfNull(Label label) {
        if (label == null || label.isNull()) {
            return this;
        }

        return label;
    }

    /**
     * Return a String that concatenates all the nested Label instances into a camel case formatted String.
     *
     * @return a camel case formatted String
     */
    String camelCase();

    /**
     * If provided, the abbreviated version of the Label.
     *
     * @return the abbreviated version of the Label
     */
    default Label abbreviated() {
        return Label.this::camelCase;
    }

    /**
     * Returns an abbreviated version of the Label in camel case format.
     *
     * @return an abbreviated version of the Label in camel case format
     */
    default String shortCamelCase() {
        return abbreviated().camelCase();
    }

    /**
     * Returns a lower camel case formatted String.
     *
     * @return a lower camel case formatted String
     */
    default String lowerCamelCase() {
        return Strings.camelToLowerCamel(camelCase());
    }

    /**
     * Returns a lower colon path formatted String.
     *
     * @return a lower colon path formatted String
     */
    default String lowerColonPath() {
        return Strings.camelToLowerHyphen(camelCase());
    }

    /**
     * Returns a lower hyphen formatted String.
     *
     * @return a lower hyphen formatted String
     */
    default String lowerHyphen() {
        return Strings.camelToLowerHyphen(camelCase());
    }

    /**
     * Returns a lower hyphen formatted String with slash delimiters.
     *
     * @return a lower hyphen formatted String with slash delimiters
     */
    default String lowerHyphenPath() {
        return Strings.camelToLowerHyphen(camelCase());
    }

    /**
     * Returns a lower hyphen formatted String with slash delimiters.
     *
     * @param trailingSlash if true, a trailing slash is appended
     * @return a lower hyphen formatted String with slash delimiters
     */
    default String lowerHyphenPath(boolean trailingSlash) {
        return trailingSlash ? lowerHyphenPath().concat("/") : lowerHyphenPath();
    }

    /**
     * Returns a lower underscore formatted String.
     *
     * @return a lower underscore formatted String
     */
    default String lowerUnderscore() {
        return Strings.camelToLowerUnderscore(camelCase());
    }

    /**
     * Returns an upper underscore formatted String.
     *
     * @return an upper underscore formatted String
     */
    default String upperUnderscore() {
        return Strings.camelToUpperUnderscore(camelCase());
    }

    /**
     * Returns an abbreviated lower hyphen formatted String.
     *
     * @return an abbreviated  lower hyphen formatted String
     */
    default String shortLowerHyphen() {
        return Strings.camelToLowerHyphen(shortCamelCase());
    }

    /**
     * Returns an abbreviated lower underscore formatted String.
     *
     * @return an abbreviated lower underscore formatted String
     */
    default String shortLowerUnderscore() {
        return Strings.camelToLowerUnderscore(shortCamelCase());
    }

    /**
     * Compares the given Label to this Label using camel case formatted Strings.
     *
     * @param o the Label to compare to
     * @return a negative integer, zero, or a positive integer as this Label is less than, equal to, or greater than the specified Label.
     */
    default int compareTo(Label o) {
        return Objects.compare(camelCase(), o.camelCase(), String::compareTo);
    }
}
