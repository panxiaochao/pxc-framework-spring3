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
package io.github.panxiaochao.spring3.core.utils.date;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * <p>
 * 日期格式化相关统一格式类.
 * </p>
 *
 * @author Lypxc
 * @since 2022/4/12
 */
public class DatePattern {

	public static final long DAY_MILLI = 24 * 60 * 60 * 1000;

	public static final long HOUR_MILLI = 60 * 60 * 1000;

	public static final long MINUTE_MILLI = 60 * 1000;

	public static final long SECOND_MILLI = 1000;

	public static final String NORMAL_YEAR_PATTERN = "yyyy";

	public static final String NORMAL_YEAR_MONTH_PATTERN = "yyyy-MM";

	public static final String NORMAL_DATE_PATTERN = "yyyy-MM-dd";

	public static final DateTimeFormatter NORMAL_DATE_FORMATTER = buildFormatter(NORMAL_DATE_PATTERN);

	public static final String NORMAL_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final DateTimeFormatter NORMAL_DATE_TIME_FORMATTER = buildFormatter(NORMAL_DATE_TIME_PATTERN);

	public static final String SIMPLE_DATE_TIME_PATTERN = "yyyy/MM/dd HH:mm:ss";

	public static final String CN_DATE_PATTERN = "yyyy年MM月dd日";

	public static DateTimeFormatter buildFormatter(String datePattern) {
		return DateTimeFormatter.ofPattern(datePattern, Locale.getDefault()).withZone(ZoneId.systemDefault());
	}

}
