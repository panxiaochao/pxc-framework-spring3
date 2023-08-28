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

/**
 * <p>
 * 字符串池.
 * </p>
 *
 * @author Lypxc
 * @since 2023-02-17
 */
public interface StringPools {

	String AMPERSAND = "&";

	String AND = "and";

	String AT = "@";

	String ASTERISK = "*";

	String STAR = ASTERISK;

	String BACK_SLASH = "\\";

	String COLON = ":";

	String COMMA = ",";

	String DASH = "-";

	String DOLLAR = "$";

	String DOT = ".";

	String DOT_DOT = "..";

	String DOT_CLASS = ".class";

	String DOT_JAVA = ".java";

	String DOT_XML = ".xml";

	String EMPTY = "";

	String EQUALS = "=";

	String FALSE = "false";

	String SLASH = "/";

	String HASH = "#";

	String HAT = "^";

	String LEFT_BRACE = "{";

	String LEFT_BRACKET = "(";

	String LEFT_CHEV = "<";

	String DOT_NEWLINE = ",\n";

	String NEWLINE = "\n";

	String N = "n";

	String NO = "no";

	String NULL = "null";

	String NUM = "NUM";

	String OFF = "off";

	String ON = "on";

	String PERCENT = "%";

	String PIPE = "|";

	String PLUS = "+";

	String QUESTION_MARK = "?";

	String EXCLAMATION_MARK = "!";

	String QUOTE = "\"";

	String RETURN = "\r";

	String TAB = "\t";

	String RIGHT_BRACE = "}";

	String RIGHT_BRACKET = ")";

	String RIGHT_CHEV = ">";

	String SEMICOLON = ";";

	String SINGLE_QUOTE = "'";

	String BACKTICK = "`";

	String SPACE = " ";

	String SQL = "sql";

	String TILDA = "~";

	String LEFT_SQ_BRACKET = "[";

	String RIGHT_SQ_BRACKET = "]";

	String TRUE = "true";

	String UNDERSCORE = "_";

	String UTF_8 = "UTF-8";

	String US_ASCII = "US-ASCII";

	String ISO_8859_1 = "ISO-8859-1";

	String Y = "y";

	String YES = "yes";

	String ONE = "1";

	String ZERO = "0";

	String DOLLAR_LEFT_BRACE = "${";

	String HASH_LEFT_BRACE = "#{";

	String CRLF = "\r\n";

	String HTML_NBSP = "&nbsp;";

	String HTML_AMP = "&amp";

	String HTML_QUOTE = "&quot;";

	String HTML_LT = "&lt;";

	String HTML_GT = "&gt;";

	String[] EMPTY_ARRAY = new String[0];

	byte[] BYTES_NEW_LINE = NEWLINE.getBytes();

}
