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
package io.github.panxiaochao.spring3.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;

/**
 * <p>
 * Exception信息提取
 * </p>
 *
 * @author Lypxc
 * @since 2022/6/17
 */
public class ExceptionUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionUtil.class);

	/**
	 * 获取报错信息
	 * @param e Exception
	 * @return String
	 */
	public static String getMessage(Exception e) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			return sw.toString();
		}
		catch (Exception ex) {
			LOGGER.error("ExceptionUtil is error", ex);
		}
		finally {
			if (pw != null) {
				pw.close();
			}
			if (sw != null) {
				try {
					sw.close();
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String getAllExceptionMsg(Throwable cause) {
		StringBuilder strBuilder = new StringBuilder();
		while (cause != null && !StrUtil.isEmpty(cause.getMessage())) {
			strBuilder.append("caused: ").append(cause.getMessage()).append(';');
			cause = cause.getCause();
		}

		return strBuilder.toString();
	}

	public static Throwable getCause(final Throwable t) {
		final Throwable cause = t.getCause();
		if (Objects.isNull(cause)) {
			return t;
		}
		return cause;
	}

	public static String getStackTrace(final Throwable t) {
		if (t == null) {
			return "";
		}
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final PrintStream ps = new PrintStream(out);
		t.printStackTrace(ps);
		ps.flush();
		return out.toString();
	}

}
