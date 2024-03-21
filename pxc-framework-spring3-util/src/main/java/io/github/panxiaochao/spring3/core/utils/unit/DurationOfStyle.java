/*
 * Copyright © 2024-2025 Lypxc(潘) (545685602@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.panxiaochao.spring3.core.utils.unit;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Duration format styles.
 * </p>
 *
 * @author Lypxc
 * @since 2023-08-01
 */
public enum DurationOfStyle {

	/**
	 * Simple formatting, for example '1s'.
	 */
	SIMPLE("^([+-]?\\d+)([a-zA-Z]{0,2})$") {

		@Override
		public Duration parse(String value, ChronoUnit unit) {
			try {
				Matcher matcher = matcher(value);
				Assert.state(matcher.matches(), "Does not match simple duration pattern");
				String suffix = matcher.group(2);
				return (StringUtils.hasLength(suffix) ? DurationOfUnit.fromSuffix(suffix)
						: DurationOfUnit.fromChronoUnit(unit))
					.parse(matcher.group(1));
			}
			catch (Exception ex) {
				throw new IllegalArgumentException("'" + value + "' is not a valid simple duration", ex);
			}
		}

		@Override
		public String print(Duration value, ChronoUnit unit) {
			return DurationOfUnit.fromChronoUnit(unit).print(value);
		}

	},

	/**
	 * ISO-8601 formatting.
	 */
	ISO8601("^[+-]?[pP].*$") {

		@Override
		public Duration parse(String value, ChronoUnit unit) {
			try {
				return Duration.parse(value);
			}
			catch (Exception ex) {
				throw new IllegalArgumentException("'" + value + "' is not a valid ISO-8601 duration", ex);
			}
		}

		@Override
		public String print(Duration value, ChronoUnit unit) {
			return value.toString();
		}

	};

	private final Pattern pattern;

	DurationOfStyle(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	protected final boolean matches(String value) {
		return this.pattern.matcher(value).matches();
	}

	protected final Matcher matcher(String value) {
		return this.pattern.matcher(value);
	}

	/**
	 * Parse the given value to a duration.
	 * @param value the value to parse
	 * @return a duration
	 */
	public Duration parse(String value) {
		return parse(value, null);
	}

	/**
	 * Parse the given value to a duration.
	 * @param value the value to parse
	 * @param unit the duration unit to use if the value doesn't specify one ({@code null}
	 * will default to ms)
	 * @return a duration
	 */
	public abstract Duration parse(String value, ChronoUnit unit);

	/**
	 * Print the specified duration.
	 * @param value the value to print
	 * @return the printed result
	 */
	public String print(Duration value) {
		return print(value, null);
	}

	/**
	 * Print the specified duration using the given unit.
	 * @param value the value to print
	 * @param unit the value to use for printing
	 * @return the printed result
	 */
	public abstract String print(Duration value, ChronoUnit unit);

	/**
	 * Detect the style then parse the value to return a duration.
	 * @param value the value to parse
	 * @return the parsed duration
	 * @throws IllegalArgumentException if the value is not a known style or cannot be
	 * parsed
	 */
	public static Duration detectAndParse(String value) {
		return detectAndParse(value, null);
	}

	/**
	 * Detect the style then parse the value to return a duration.
	 * @param value the value to parse
	 * @param unit the duration unit to use if the value doesn't specify one ({@code null}
	 * will default to ms)
	 * @return the parsed duration
	 * @throws IllegalArgumentException if the value is not a known style or cannot be
	 * parsed
	 */
	public static Duration detectAndParse(String value, ChronoUnit unit) {
		return detect(value).parse(value, unit);
	}

	/**
	 * Detect the style from the given source value.
	 * @param value the source value
	 * @return the duration style
	 * @throws IllegalArgumentException if the value is not a known style
	 */
	public static DurationOfStyle detect(String value) {
		Assert.notNull(value, "Value must not be null");
		for (DurationOfStyle candidate : values()) {
			if (candidate.matches(value)) {
				return candidate;
			}
		}
		throw new IllegalArgumentException("'" + value + "' is not a valid duration");
	}

}
