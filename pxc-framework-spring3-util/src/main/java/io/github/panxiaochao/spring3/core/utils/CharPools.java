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

/**
 * <p>
 * 字符池.
 * </p>
 *
 * @author Lypxc
 * @since 2023-08-31
 */
public interface CharPools {

    /**
     * 字符常量：空格符 {@code ' '}
     */
    char SPACE = ' ';

    /**
     * 字符常量：制表符 {@code '\t'}
     */
    char TAB = '	';

    /**
     * 字符常量：点 {@code '.'}
     */
    char DOT = '.';

    /**
     * 字符常量：斜杠 {@code '/'}
     */
    char SLASH = '/';

    /**
     * 字符常量：反斜杠 {@code '\\'}
     */
    char BACKSLASH = '\\';

    /**
     * 字符常量：回车符 {@code '\r'}
     */
    char CR = '\r';

    /**
     * 字符常量：换行符 {@code '\n'}
     */
    char LF = '\n';

    /**
     * 字符常量：减号（连接符） {@code '-'}
     */
    char DASHED = '-';

    /**
     * 字符常量：下划线 {@code '_'}
     */
    char UNDERLINE = '_';

    /**
     * 字符常量：逗号 {@code ','}
     */
    char COMMA = ',';

    /**
     * 字符常量：花括号（左） <code>'{'</code>
     */
    char DELIM_START = '{';

    /**
     * 字符常量：花括号（右） <code>'}'</code>
     */
    char DELIM_END = '}';

    /**
     * 字符常量：中括号（左） {@code '['}
     */
    char BRACKET_START = '[';

    /**
     * 字符常量：中括号（右） {@code ']'}
     */
    char BRACKET_END = ']';

    /**
     * 字符常量：双引号 {@code '"'}
     */
    char DOUBLE_QUOTES = '"';

    /**
     * 字符常量：单引号 {@code '\''}
     */
    char SINGLE_QUOTE = '\'';

    /**
     * 字符常量：与 {@code '&'}
     */
    char AMP = '&';

    /**
     * 字符常量：冒号 {@code ':'}
     */
    char COLON = ':';

    /**
     * 字符常量：艾特 {@code '@'}
     */
    char AT = '@';

    char HASH = '#';

    char PERCENT = '%';

    char PIPE = '|';

    char PLUS = '+';

    char QUESTION_MARK = '?';

    char EXCLAMATION_MARK = '!';

    char EQUALS = '=';

    char AMPERSAND = '&';

    char ASTERISK = '*';

    char STAR = ASTERISK;

    char DOLLAR = '$';

    char HAT = '^';

    char LEFT_CHEV = '<';

    char RIGHT_CHEV = '>';

    char SEMICOLON = ';';

    char BACKTICK = '`';

    char TILDA = '~';

}
