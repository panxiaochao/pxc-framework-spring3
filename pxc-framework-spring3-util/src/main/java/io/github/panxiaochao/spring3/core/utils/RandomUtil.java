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
package io.github.panxiaochao.spring3.core.utils;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * 随机数工具
 * </p>
 *
 * @author Lypxc
 * @since 2022/8/25
 */
public class RandomUtil {

	private RandomUtil() {
	}

	private static final RandomUtil INST = new RandomUtil();

	private final ThreadLocalRandom random = ThreadLocalRandom.current();

	public static RandomUtil INSTANCE() {
		return INST;
	}

	/**
	 * 默认长度4
	 */
	private static final int DEFAULT_LEN = 4;

	/**
	 * 大小写、数字
	 */
	private static final char[] SOURCES = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9' };

	/**
	 * 获取字符串随机数，默认长度4
	 * @return 返回字符串
	 */
	public String getStringRandom() {
		return getStringRandom(DEFAULT_LEN);
	}

	/**
	 * 获取字符串随机数
	 * @param len 随机长度
	 * @return 返回字符串
	 */
	public String getStringRandom(int len) {
		int length = SOURCES.length;
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < len; j++) {
			sb.append(SOURCES[random.nextInt(length)]);
		}
		return sb.toString();
	}

	/**
	 * 获取字符串随机数数组
	 * @param len 随机长度
	 * @param size 数组长度
	 * @return List<String>
	 */
	public List<String> getStringRandoms(int len, int size) {
		Assert.isTrue(size > 1, "size must not be less than one");
		String[] randoms = new String[size];
		int length = SOURCES.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < len; j++) {
				sb.append(SOURCES[random.nextInt(length)]);
			}
			randoms[i] = sb.toString();
			// 清空数据
			sb.setLength(0);
		}
		return Arrays.asList(randoms);
	}

    /**
     * 获取数组随机数数组
     *
     * @param len 随机长度
     * @return byte[]
     */
    public byte[] getBytesRandom(int len) {
        byte[] bytes = new byte[len];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * <p>
     * Returns a random boolean value
     * </p>
     *
     * @return the random boolean
     */
    public boolean nextBoolean() {
        return random.nextBoolean();
    }

	/**
	 * Returns a random long within the specified range.
	 * @param startInclusive the smallest value that can be returned, must be non-negative
	 * @param endExclusive the upper bound (not included)
	 * @return the random long
	 * @throws IllegalArgumentException if startInclusive or endExclusive illegal
	 */
	public long nextLong(final long startInclusive, final long endExclusive) {
		checkParameters(startInclusive, endExclusive);
		long diff = endExclusive - startInclusive;
		if (diff == 0) {
			return startInclusive;
		}
		return random.longs(startInclusive, (endExclusive + 1)).limit(1).findFirst().getAsLong();
	}

	/**
	 * Returns a random integer within the specified range.
	 * @param startInclusive lower limit, must be non-negative
	 * @param endExclusive the upper bound (not included)
	 * @return the random integer
	 * @throws IllegalArgumentException if startInclusive or endExclusive illegal
	 */
	public int nextInt(final int startInclusive, final int endExclusive) {
		checkParameters(startInclusive, endExclusive);
		int diff = endExclusive - startInclusive;
		if (diff == 0) {
			return startInclusive;
		}
		return random.ints(startInclusive, (endExclusive + 1)).limit(1).findFirst().getAsInt();
	}

	/**
	 * Check input parameters.
	 * @param startInclusive lower limit, must be non-negative
	 * @param endExclusive the upper bound (not included)
	 */
	private void checkParameters(final long startInclusive, final long endExclusive) {
		if (endExclusive < startInclusive) {
			throw new IllegalArgumentException("startInclusive must be less than or equal to the endExclusive.");
		}
		if (startInclusive < 0) {
			throw new IllegalArgumentException("Both parameters must be non-negative");
		}
	}

}
