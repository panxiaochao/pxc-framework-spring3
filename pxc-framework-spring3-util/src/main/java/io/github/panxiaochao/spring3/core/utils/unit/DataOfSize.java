/*
 * Copyright © 2023-2024 Lypxc (545685602@qq.com)
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

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * A data size, such as '12MB'.
 * </p>
 * <table border="1">
 * <tr>
 * <th>Term</th>
 * <th>Data Size</th>
 * <th>Size in Bytes</th>
 * </tr>
 * <tr>
 * <td>byte</td>
 * <td>1B</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>kilobyte</td>
 * <td>1KB</td>
 * <td>1,024</td>
 * </tr>
 * <tr>
 * <td>megabyte</td>
 * <td>1MB</td>
 * <td>1,048,576</td>
 * </tr>
 * <tr>
 * <td>gigabyte</td>
 * <td>1GB</td>
 * <td>1,073,741,824</td>
 * </tr>
 * <tr>
 * <td>terabyte</td>
 * <td>1TB</td>
 * <td>1,099,511,627,776</td>
 * </tr>
 * </table>
 *
 * @author Lypxc
 * @since 2023-03-20
 */
@SuppressWarnings("serial")
public final class DataOfSize implements Comparable<DataOfSize>, Serializable {

	/**
	 * Bytes per Kilobyte.
	 */
	private static final long BYTES_PER_KB = 1024;

	/**
	 * Bytes per Megabyte.
	 */
	private static final long BYTES_PER_MB = BYTES_PER_KB * 1024;

	/**
	 * Bytes per Gigabyte.
	 */
	private static final long BYTES_PER_GB = BYTES_PER_MB * 1024;

	/**
	 * Bytes per Terabyte.
	 */
	private static final long BYTES_PER_TB = BYTES_PER_GB * 1024;

	private final long bytes;

	private DataOfSize(long bytes) {
		this.bytes = bytes;
	}

	/**
	 * Obtain a {@link DataOfSize} representing the specified number of bytes.
	 * @param bytes the number of bytes, positive or negative
	 * @return a {@link DataOfSize}
	 */
	public static DataOfSize ofBytes(long bytes) {
		return new DataOfSize(bytes);
	}

	/**
	 * Obtain a {@link DataOfSize} representing the specified number of kilobytes.
	 * @param kilobytes the number of kilobytes, positive or negative
	 * @return a {@link DataOfSize}
	 */
	public static DataOfSize ofKilobytes(long kilobytes) {
		return new DataOfSize(Math.multiplyExact(kilobytes, BYTES_PER_KB));
	}

	/**
	 * Obtain a {@link DataOfSize} representing the specified number of megabytes.
	 * @param megabytes the number of megabytes, positive or negative
	 * @return a {@link DataOfSize}
	 */
	public static DataOfSize ofMegabytes(long megabytes) {
		return new DataOfSize(Math.multiplyExact(megabytes, BYTES_PER_MB));
	}

	/**
	 * Obtain a {@link DataOfSize} representing the specified number of gigabytes.
	 * @param gigabytes the number of gigabytes, positive or negative
	 * @return a {@link DataOfSize}
	 */
	public static DataOfSize ofGigabytes(long gigabytes) {
		return new DataOfSize(Math.multiplyExact(gigabytes, BYTES_PER_GB));
	}

	/**
	 * Obtain a {@link DataOfSize} representing the specified number of terabytes.
	 * @param terabytes the number of terabytes, positive or negative
	 * @return a {@link DataOfSize}
	 */
	public static DataOfSize ofTerabytes(long terabytes) {
		return new DataOfSize(Math.multiplyExact(terabytes, BYTES_PER_TB));
	}

	/**
	 * Obtain a {@link DataOfSize} representing an amount in the specified
	 * {@link DataOfUnit}.
	 * @param amount the amount of the size, measured in terms of the unit, positive or
	 * negative
	 * @return a corresponding {@link DataOfSize}
	 */
	public static DataOfSize of(long amount, DataOfUnit unit) {
		Assert.notNull(unit, "Unit must not be null");
		return new DataOfSize(Math.multiplyExact(amount, unit.size().toBytes()));
	}

	/**
	 * Obtain a {@link DataOfSize} from a text string such as {@code 12MB} using
	 * {@link DataOfUnit#BYTES} if no unit is specified.
	 * <p>
	 * Examples: <pre>
	 * "12KB" -- parses as "12 kilobytes"
	 * "5MB"  -- parses as "5 megabytes"
	 * "20"   -- parses as "20 bytes"
	 * </pre>
	 * @param text the text to parse
	 * @return the parsed {@link DataOfSize}
	 * @see #parse(CharSequence, DataOfUnit)
	 */
	public static DataOfSize parse(CharSequence text) {
		return parse(text, null);
	}

	/**
	 * Obtain a {@link DataOfSize} from a text string such as {@code 12MB} using the
	 * specified default {@link DataOfUnit} if no unit is specified.
	 * <p>
	 * The string starts with a number followed optionally by a unit matching one of the
	 * supported {@linkplain DataOfUnit suffixes}.
	 * <p>
	 * Examples: <pre>
	 * "12KB" -- parses as "12 kilobytes"
	 * "5MB"  -- parses as "5 megabytes"
	 * "20"   -- parses as "20 kilobytes" (where the {@code defaultUnit} is {@link DataOfUnit#KILOBYTES})
	 * </pre>
	 * @param text the text to parse
	 * @return the parsed {@link DataOfSize}
	 */
	public static DataOfSize parse(CharSequence text, DataOfUnit defaultUnit) {
		Assert.notNull(text, "Text must not be null");
		try {
			Matcher matcher = DataSizeUtils.PATTERN.matcher(StringUtils.trimAllWhitespace(text));
			Assert.state(matcher.matches(), "Does not match data size pattern");
			DataOfUnit unit = DataSizeUtils.determineDataOfUnit(matcher.group(2), defaultUnit);
			long amount = Long.parseLong(matcher.group(1));
			return DataOfSize.of(amount, unit);
		}
		catch (Exception ex) {
			throw new IllegalArgumentException("'" + text + "' is not a valid data size", ex);
		}
	}

	/**
	 * Checks if this size is negative, excluding zero.
	 * @return true if this size has a size less than zero bytes
	 */
	public boolean isNegative() {
		return this.bytes < 0;
	}

	/**
	 * Return the number of bytes in this instance.
	 * @return the number of bytes
	 */
	public long toBytes() {
		return this.bytes;
	}

	/**
	 * Return the number of kilobytes in this instance.
	 * @return the number of kilobytes
	 */
	public long toKilobytes() {
		return this.bytes / BYTES_PER_KB;
	}

	/**
	 * Return the number of megabytes in this instance.
	 * @return the number of megabytes
	 */
	public long toMegabytes() {
		return this.bytes / BYTES_PER_MB;
	}

	/**
	 * Return the number of gigabytes in this instance.
	 * @return the number of gigabytes
	 */
	public long toGigabytes() {
		return this.bytes / BYTES_PER_GB;
	}

	/**
	 * Return the number of terabytes in this instance.
	 * @return the number of terabytes
	 */
	public long toTerabytes() {
		return this.bytes / BYTES_PER_TB;
	}

	@Override
	public int compareTo(DataOfSize other) {
		return Long.compare(this.bytes, other.bytes);
	}

	@Override
	public String toString() {
		return String.format("%dB", this.bytes);
	}

	@Override
	public boolean equals(@Nullable Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		DataOfSize otherSize = (DataOfSize) other;
		return (this.bytes == otherSize.bytes);
	}

	@Override
	public int hashCode() {
		return Long.hashCode(this.bytes);
	}

	/**
	 * Static nested class to support lazy loading of the {@link #PATTERN}.
	 *
	 * @since 5.3.21
	 */
	private static class DataSizeUtils {

		/**
		 * The pattern for parsing.
		 */
		private static final Pattern PATTERN = Pattern.compile("^([+\\-]?\\d+)([a-zA-Z]{0,2})$");

		private static DataOfUnit determineDataOfUnit(String suffix, DataOfUnit defaultUnit) {
			DataOfUnit defaultUnitToUse = (defaultUnit != null ? defaultUnit : DataOfUnit.BYTES);
			return (StringUtils.hasLength(suffix) ? DataOfUnit.fromSuffix(suffix) : defaultUnitToUse);
		}

	}

}
