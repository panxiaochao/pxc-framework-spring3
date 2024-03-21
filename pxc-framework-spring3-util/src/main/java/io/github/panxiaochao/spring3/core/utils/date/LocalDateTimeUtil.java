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
package io.github.panxiaochao.spring3.core.utils.date;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * <p>
 * JDK8 LocalDateTime和LocalDate 工具类
 * </p>
 *
 * @author Lypxc
 * @since 2022/4/12
 */
public class LocalDateTimeUtil {

	/**
	 * 不能被实例化
	 */
	private LocalDateTimeUtil() {
	}

	/**
	 * 返回 LocalDateTime.now()
	 * @return LocalDateTime
	 */
	public static LocalDateTime nowDateTime() {
		return LocalDateTime.now(ZoneId.systemDefault());
	}

	/**
	 * 返回 LocalDate.now()
	 * @return LocalDate
	 */
	public static LocalDate nowDate() {
		return LocalDate.now(ZoneId.systemDefault());
	}

	/**
	 * new Date() 转 LocalDate
	 * @param date date
	 * @return LocalDate
	 */
	public static LocalDate dateToLocalDate(Date date) {
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		return localDateTime.toLocalDate();
	}

	/**
	 * new Date() 转 LocalDateTime
	 * @param date date
	 * @return LocalDateTime
	 */
	public static LocalDateTime dateToLocalDateTime(Date date) {
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		return LocalDateTime.ofInstant(instant, zone);
	}

	/**
	 * String dateStr 转 LocalDateTime
	 * @param dateStr dateStr 例如: '2022-04-12 00:00:00'
	 * @return LocalDateTime
	 */
	public static LocalDateTime stringToLocalDateTime(String dateStr) {
		return stringToLocalDateTime(dateStr, DatePattern.NORMAL_DATE_TIME_PATTERN);
	}

	/**
	 * String dateStr 转 LocalDateTime
	 * @param dateStr 例如: '2022-04-12 00:00:00'
	 * @param format 例如: 'yyyy-MM-dd HH:mm:ss'
	 * @return LocalDateTime
	 */
	public static LocalDateTime stringToLocalDateTime(String dateStr, String format) {
		return LocalDateTime.parse(dateStr, DatePattern.buildFormatter(format));
	}

	/**
	 * String dateStr 转 LocalDate
	 * @param dateStr dateStr 例如: '2022-04-12 00:00:00'
	 * @return LocalDate
	 */
	public static LocalDate stringToLocalDate(String dateStr) {
		return stringToLocalDate(dateStr, DatePattern.NORMAL_DATE_PATTERN);
	}

	/**
	 * String dateStr 转 LocalDate
	 * @param dateStr 例如: '2022-04-12 00:00:00'
	 * @param format 例如: 'yyyy-MM-dd HH:mm:ss'
	 * @return LocalDate
	 */
	public static LocalDate stringToLocalDate(String dateStr, String format) {
		return LocalDate.parse(dateStr, DatePattern.buildFormatter(format));
	}

	/**
	 * LocalDateTime 转 String dateStr
	 * @param localDateTime localDateTime
	 * @return 例如: '2022-04-12 00:00:00'
	 */
	public static String localDateTimeToString(LocalDateTime localDateTime) {
		return localDateTimeToString(localDateTime, DatePattern.NORMAL_DATE_TIME_PATTERN);
	}

	/**
	 * LocalDateTime 转 String dateStr
	 * @param localDateTime localDateTime
	 * @param format 例如: 'yyyy-MM-dd HH:mm:ss'
	 * @return 例如: '2022-04-12 00:00:00'
	 */
	public static String localDateTimeToString(LocalDateTime localDateTime, String format) {
		return localDateTime.format(DatePattern.buildFormatter(format));
	}

	/**
	 * localDate 转 String dateStr
	 * @param localDate localDate
	 * @return 例如: '2022-04-12'
	 */
	public static String localDateToString(LocalDate localDate) {
		return localDateToString(localDate, DatePattern.NORMAL_DATE_PATTERN);
	}

	/**
	 * localDate 转 String dateStr
	 * @param localDate localDate
	 * @param format 例如: 'yyyy-MM-dd'
	 * @return 例如: '2022-04-12'
	 */
	public static String localDateToString(LocalDate localDate, String format) {
		return localDate.format(DatePattern.buildFormatter(format));
	}

	/**
	 * Timestamp 转 LocalDateTime
	 * @param timestamp timestamp
	 * @return LocalDateTime
	 */
	public static LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
		return timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	/**
	 * Timestamp 转 LocalDate
	 * @param timestamp timestamp
	 * @return LocalDate
	 */
	public static LocalDate timestampToLocalDate(Timestamp timestamp) {
		LocalDateTime localDateTime = timestampToLocalDateTime(timestamp);
		return localDateTime.toLocalDate();
	}

	/**
	 * long 转 LocalDateTime
	 * @param timestamp timestamp
	 * @return LocalDateTime
	 */
	public static LocalDateTime longToLocalDateTime(long timestamp) {
		Instant instant = Instant.ofEpochMilli(timestamp);
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	/**
	 * long 转 LocalDate
	 * @param timestamp timestamp
	 * @return LocalDate
	 */
	public static LocalDate longToLocalDate(long timestamp) {
		LocalDateTime localDateTime = longToLocalDateTime(timestamp);
		return localDateTime.toLocalDate();
	}

	/**
	 * LocalDateTime 转 Timestamp
	 * @param localDateTime localDateTime
	 * @return Timestamp
	 */
	public static Timestamp localDateTimeToTimestamp(LocalDateTime localDateTime) {
		return Timestamp.valueOf(localDateTime);
	}

	/**
	 * LocalDateTime 转 long
	 * @param localDateTime localDateTime
	 * @return long
	 */
	public static long localDateTimeToLong(LocalDateTime localDateTime) {
		return localDateTimeToTimestamp(localDateTime).getTime();
	}

}
