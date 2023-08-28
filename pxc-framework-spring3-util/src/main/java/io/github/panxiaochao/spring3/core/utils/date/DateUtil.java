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

import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

/**
 * <p>
 * JDK Date 工具类.
 * </p>
 *
 * @author Lypxc
 * @since 2022/4/12
 */
public class DateUtil {

	/**
	 * 不能被实例化
	 */
	private DateUtil() {
	}

	/**
	 * 获取SimpleDateFormat实例，避免多线程问题
	 * @param format format
	 * @return SimpleDateFormat
	 */
	private static SimpleDateFormat getDateFormat(String format) {
		SimpleDateFormat simpleDateFormat = DateContext.get();
		if (null == simpleDateFormat) {
			simpleDateFormat = new SimpleDateFormat(format);
			DateContext.set(simpleDateFormat);
		}
		return simpleDateFormat;
	}

	/**
	 * 获取当前时间
	 * @return Date
	 */
	public static Date getDate() {
		return new Date();
	}

	/**
	 * 根据毫秒转换时间
	 * @param millis 毫秒
	 * @return Date
	 */
	public static Date getDate(long millis) {
		return new Date(millis);
	}

	/**
	 * LocalDateTime 转 new Date()
	 * @param localDateTime localDateTime
	 * @return Date
	 */
	public static Date localDateTimeToDate(LocalDateTime localDateTime) {
		Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

	/**
	 * localDate 转 new Date()
	 * @param localDate localDate
	 * @return Date
	 */
	public static Date localDateToDate(LocalDate localDate) {
		Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

	/**
	 * LocalTime 转 new Date()
	 * @param localTime localTime
	 * @return Date
	 */
	public static Date localTimeToDate(LocalTime localTime) {
		LocalDate localDate = LocalDate.now();
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = LocalDateTime.of(localDate, localTime).atZone(zone).toInstant();
		return Date.from(instant);
	}

	/**
	 * String 转 new Date()
	 * @param dateStr 例如: '2022-04-12 00:00:00'
	 * @return Date
	 */
	public static Date stringToDate(String dateStr) {
		return stringToDate(dateStr, DatePattern.NORMAL_DATE_TIME_PATTERN);
	}

	/**
	 * String 转 new Date()
	 * @param dateStr 例如: '2022-04-12 00:00:00'
	 * @param format 例如: 'yyyy-MM-dd HH:mm:ss'
	 * @return Date
	 */
	public static Date stringToDate(String dateStr, String format) {
		ParsePosition pos = new ParsePosition(0);
		return getDateFormat(format).parse(dateStr, pos);
	}

	/**
	 * new Date() 转 String
	 * @param date date
	 * @return 例如: '2022-04-12 00:00:00'
	 */
	public static String dateToString(Date date) {
		return dateToString(date, DatePattern.NORMAL_DATE_TIME_PATTERN);
	}

	/**
	 * new Date() 转 String
	 * @param date date
	 * @param format 例如: 'yyyy-MM-dd HH:mm:ss'
	 * @return 例如: '2022-04-12 00:00:00'
	 */
	public static String dateToString(Date date, String format) {
		return getDateFormat(format).format(date);
	}

	/**
	 * Timestamp 转 new Date()
	 * @param timestamp timestamp
	 * @return Date
	 */
	public static Date timestampToDate(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}

	/**
	 * new Date() 转 Timestamp
	 * @param date date
	 * @return Timestamp
	 */
	public static Timestamp dateToTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}

}
