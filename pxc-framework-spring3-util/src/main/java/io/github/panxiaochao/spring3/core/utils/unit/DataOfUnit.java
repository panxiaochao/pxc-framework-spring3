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

/**
 * <p>
 * A standard set of {@link DataOfUnit} units.
 * </p>
 * <p>
 * <table border="1">
 * <tr>
 * <th>Constant</th>
 * <th>Data Size</th>
 * <th>Power&nbsp;of&nbsp;2</th>
 * <th>Size in Bytes</th>
 * </tr>
 * <tr>
 * <td>{@link #BYTES}</td>
 * <td>1B</td>
 * <td>2^0</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>{@link #KILOBYTES}</td>
 * <td>1KB</td>
 * <td>2^10</td>
 * <td>1,024</td>
 * </tr>
 * <tr>
 * <td>{@link #MEGABYTES}</td>
 * <td>1MB</td>
 * <td>2^20</td>
 * <td>1,048,576</td>
 * </tr>
 * <tr>
 * <td>{@link #GIGABYTES}</td>
 * <td>1GB</td>
 * <td>2^30</td>
 * <td>1,073,741,824</td>
 * </tr>
 * <tr>
 * <td>{@link #TERABYTES}</td>
 * <td>1TB</td>
 * <td>2^40</td>
 * <td>1,099,511,627,776</td>
 * </tr>
 * </table>
 *
 * @author Lypxc
 * @since 2023-03-20
 */
public enum DataOfUnit {

	/**
	 * Bytes, represented by suffix {@code B}.
	 */
	BYTES("B", DataOfSize.ofBytes(1)),

	/**
	 * Kilobytes, represented by suffix {@code KB}.
	 */
	KILOBYTES("KB", DataOfSize.ofKilobytes(1)),

	/**
	 * Megabytes, represented by suffix {@code MB}.
	 */
	MEGABYTES("MB", DataOfSize.ofMegabytes(1)),

	/**
	 * Gigabytes, represented by suffix {@code GB}.
	 */
	GIGABYTES("GB", DataOfSize.ofGigabytes(1)),

	/**
	 * Terabytes, represented by suffix {@code TB}.
	 */
	TERABYTES("TB", DataOfSize.ofTerabytes(1));

	private final String suffix;

	private final DataOfSize size;

	DataOfUnit(String suffix, DataOfSize size) {
		this.suffix = suffix;
		this.size = size;
	}

	DataOfSize size() {
		return this.size;
	}

	/**
	 * Return the {@link DataOfUnit} matching the specified {@code suffix}.
	 * @param suffix one of the standard suffixes
	 * @return the {@link DataOfUnit} matching the specified {@code suffix}
	 * @throws IllegalArgumentException if the suffix does not match the suffix of any of
	 * this enum's constants
	 */
	public static DataOfUnit fromSuffix(String suffix) {
		for (DataOfUnit candidate : values()) {
			if (candidate.suffix.equalsIgnoreCase(suffix)) {
				return candidate;
			}
		}
		throw new IllegalArgumentException("Unknown data unit suffix '" + suffix + "'");
	}

}
