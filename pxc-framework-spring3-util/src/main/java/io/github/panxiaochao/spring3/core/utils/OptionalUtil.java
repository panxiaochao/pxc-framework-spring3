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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * <p>
 * Optional 自定义工具类
 * </p>
 *
 * @author Lypxc
 * @since 2022/1/29
 */
public class OptionalUtil {

    /**
     * list == null = true list == new ArrayList<>() = true
     *
     * @param list list
     * @return true or false
     */
    public static boolean isCollectionEmpty(List<?> list) {
        List<?> newList = Optional.ofNullable(list).filter(t -> !t.isEmpty()).orElseGet(ArrayList::new);
        return newList.isEmpty();
    }

    /**
     * list != null = true list != new ArrayList<>() = true
     *
     * @param list list
     * @return true or false
     */
    public static boolean isCollectionNotEmpty(List<?> list) {
        return !isCollectionEmpty(list);
    }

    /**
     * list == null = true list == new ArrayList<>() = true
     *
     * @param list list
     * @return true or false
     */
    public static boolean isCollectionEmpty(ArrayList<?> list) {
        List<?> newList = Optional.ofNullable(list).filter(t -> !t.isEmpty()).orElseGet(ArrayList::new);
        return newList.isEmpty();
    }

    /**
     * list != null = true list != new ArrayList<>() = true
     *
     * @param list list
     * @return true or false
     */
    public static boolean isCollectionNotEmpty(ArrayList<?> list) {
        return !isCollectionEmpty(list);
    }

    /**
     * value == null true
     *
     * @param value value
     * @return true or false
     */
    public static boolean isIntEmpty(int value) {
        return OptionalInt.of(value).isPresent();
    }

    /**
     * value == null true
     *
     * @param value value
     * @return true or false
     */
    public static boolean isIntEmpty(Integer value) {
        return OptionalInt.of(value).isPresent();
    }

    /**
     * value == null true
     *
     * @param value value
     * @return true or false
     */
    public static boolean isDoubleEmpty(double value) {
        return OptionalDouble.of(value).isPresent();
    }

    /**
     * value == null true
     *
     * @param value value
     * @return true or false
     */
    public static boolean isDoubleEmpty(Double value) {
        return OptionalDouble.of(value).isPresent();
    }

    /**
     * value == null true
     *
     * @param value value
     * @return true or false
     */
    public static boolean isLongEmpty(long value) {
        return OptionalLong.of(value).isPresent();
    }

    /**
     * value == null true
     *
     * @param value value
     * @return true or false
     */
    public static boolean isLongEmpty(Long value) {
        return OptionalLong.of(value).isPresent();
    }

}
