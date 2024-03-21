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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;

/**
 * <p>
 * 资源工具类
 * </p>
 *
 * @author Lypxc
 * @since 2023-05-29
 */
public class ResourceUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUtil.class);

    /**
     * The default buffer size used when copying bytes.
     */
    public static final int DEFAULT_BUFFER_SIZE = 4096;

    private static final byte[] EMPTY_CONTENT = new byte[0];

	public static String read(InputStream in) {
		if (in == null) {
			return null;
        }
        InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
		return read(reader);
    }

    public static byte[] readByteArray(InputStream in) throws IOException {
        if (in == null) {
            return EMPTY_CONTENT;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
        copy(in, output);
        return output.toByteArray();
	}

	public static String readFromResource(String resource) throws IOException {
		if (resource == null || resource.isEmpty() || resource.contains("..") || resource.contains("?")
				|| resource.contains(":")) {
			return null;
		}

		InputStream in = null;
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			if (in == null) {
				in = ResourceUtil.class.getResourceAsStream(resource);
			}

			if (in == null) {
				return null;
			}
			return read(in);
		}
		finally {
			close(in);
		}
	}

	public static byte[] readByteArrayFromResource(String resource) throws IOException {
		if (resource == null || resource.isEmpty() || resource.contains("..") || resource.contains("?")
				|| resource.contains(":")) {
			return null;
		}

		InputStream in = null;
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			if (in == null) {
				return null;
			}

			return readByteArray(in);
		}
		finally {
			close(in);
        }
    }

    /**
     * Copy the contents of the given byte array to the given OutputStream.
     * <p>
     * Leaves the stream open when done.
     *
     * @param in  the byte array to copy from
     * @param out the OutputStream to copy to
     */
    public static void copy(byte[] in, OutputStream out) {
        try {
            out.write(in);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Copy the contents of the given String to the given OutputStream.
     * <p>
     * Leaves the stream open when done.
     *
     * @param in      the String to copy from
     * @param charset the Charset
     * @param out     the OutputStream to copy to
     */
    public static void copy(String in, Charset charset, OutputStream out) {
        try {
            Writer writer = new OutputStreamWriter(out, charset);
            writer.write(in);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Copy the contents of the given InputStream to the given OutputStream.
     * <p>
     * Leaves both streams open when done.
     *
     * @param in  the InputStream to copy from
     * @param out the OutputStream to copy to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static long copy(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }

    /**
     * Copy the contents of the given InputStream into a String.
     * <p>
     * Leaves the stream open when done.
     * @param reader the InputStreamReader
	 * @return the String that has been copied to (possibly empty)
     */
    public static String read(Reader reader) {
        if (reader == null) {
			return null;
		}

		try {
			StringBuilder out = new StringBuilder(DEFAULT_BUFFER_SIZE);
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int charsRead;
            while ((charsRead = reader.read(buffer)) != -1) {
				out.append(buffer, 0, charsRead);
			}
			return out.toString();
		}
		catch (IOException ex) {
			throw new IllegalStateException("read error", ex);
		}
	}

	public static String read(Reader reader, int length) {
		if (reader == null) {
			return null;
		}
		try {
			char[] buffer = new char[length];

			int offset = 0;
			int rest = length;
			int len;
			while ((len = reader.read(buffer, offset, rest)) != -1) {
				rest -= len;
				offset += len;

				if (rest == 0) {
					break;
				}
			}
			return new String(buffer, 0, length - rest);
        } catch (IOException ex) {
            throw new IllegalStateException("read error", ex);
        }
    }

    /**
     * Obtain content byteCount of the given InputStream.
     *
     * @param in the InputStream
     * @return the number of bytes read
     */
    public static int byteCount(InputStream in) {
        try {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int bytesRead = -1;
            int byteCount = 0;
            while ((bytesRead = in.read(buffer)) != -1) {
                byteCount += bytesRead;
			}
			return byteCount;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getStackTrace(Throwable ex) {
		StringWriter buf = new StringWriter();
		ex.printStackTrace(new PrintWriter(buf));
		return buf.toString();
	}

	public static String toString(StackTraceElement[] stackTrace) {
		StringBuilder buf = new StringBuilder();
		for (StackTraceElement item : stackTrace) {
			buf.append(item.toString());
			buf.append("\n");
		}
		return buf.toString();
	}

	public static Boolean getBoolean(Properties properties, String key) {
		String property = properties.getProperty(key);
		if ("true".equals(property)) {
			return Boolean.TRUE;
		}
		else if ("false".equals(property)) {
			return Boolean.FALSE;
		}
		return null;
	}

	public static Integer getInteger(Properties properties, String key) {
		String property = properties.getProperty(key);

		if (property == null) {
			return null;
		}
		try {
			return Integer.parseInt(property);
		}
		catch (NumberFormatException ex) {
			// skip
		}
		return null;
	}

	public static Long getLong(Properties properties, String key) {
		String property = properties.getProperty(key);

		if (property == null) {
			return null;
		}
		try {
			return Long.parseLong(property);
		}
		catch (NumberFormatException ex) {
			// skip
		}
		return null;
	}

	public static Class<?> loadClass(String className) {
		Class<?> clazz = null;
		if (className == null) {
			return null;
		}
		try {
			return Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			// skip
		}
		ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
		if (ctxClassLoader != null) {
			try {
				clazz = ctxClassLoader.loadClass(className);
			}
			catch (ClassNotFoundException e) {
				// skip
			}
		}
		return clazz;
	}

	public static boolean equals(Object a, Object b) {
		return (a == b) || (a != null && a.equals(b));
	}

	public static String hex(int hash) {
		byte[] bytes = new byte[4];

		bytes[3] = (byte) (hash);
		bytes[2] = (byte) (hash >>> 8);
		bytes[1] = (byte) (hash >>> 16);
		bytes[0] = (byte) (hash >>> 24);

		char[] chars = new char[8];
		for (int i = 0; i < 4; ++i) {
			byte b = bytes[i];

			int a = b & 0xFF;
			int b0 = a >> 4;
			int b1 = a & 0xf;

			chars[i * 2] = (char) (b0 + (b0 < 10 ? 48 : 55));
			chars[i * 2 + 1] = (char) (b1 + (b1 < 10 ? 48 : 55));
		}

		return new String(chars);
	}

	public static String hex(long hash) {
		byte[] bytes = new byte[8];

		bytes[7] = (byte) (hash);
		bytes[6] = (byte) (hash >>> 8);
		bytes[5] = (byte) (hash >>> 16);
		bytes[4] = (byte) (hash >>> 24);
		bytes[3] = (byte) (hash >>> 32);
		bytes[2] = (byte) (hash >>> 40);
		bytes[1] = (byte) (hash >>> 48);
		bytes[0] = (byte) (hash >>> 56);

		char[] chars = new char[16];
		for (int i = 0; i < 8; ++i) {
			byte b = bytes[i];

			int a = b & 0xFF;
			int b0 = a >> 4;
			int b1 = a & 0xf;

			chars[i * 2] = (char) (b0 + (b0 < 10 ? 48 : 55));
			chars[i * 2 + 1] = (char) (b1 + (b1 < 10 ? 48 : 55));
		}

		return new String(chars);
	}

	public static String hex_t(long hash) {
		byte[] bytes = new byte[8];

		bytes[7] = (byte) (hash);
		bytes[6] = (byte) (hash >>> 8);
		bytes[5] = (byte) (hash >>> 16);
		bytes[4] = (byte) (hash >>> 24);
		bytes[3] = (byte) (hash >>> 32);
		bytes[2] = (byte) (hash >>> 40);
		bytes[1] = (byte) (hash >>> 48);
		bytes[0] = (byte) (hash >>> 56);

		char[] chars = new char[18];
		chars[0] = 'T';
		chars[1] = '_';
		for (int i = 0; i < 8; ++i) {
			byte b = bytes[i];

			int a = b & 0xFF;
			int b0 = a >> 4;
			int b1 = a & 0xf;

			chars[i * 2 + 2] = (char) (b0 + (b0 < 10 ? 48 : 55));
			chars[i * 2 + 3] = (char) (b1 + (b1 < 10 ? 48 : 55));
		}

		return new String(chars);
	}

	public static void loadFromFile(String path, Set<String> set) {
		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			reader = new BufferedReader(new InputStreamReader(is));
			for (;;) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}

				line = line.trim().toLowerCase();

				if (line.length() == 0) {
					continue;
				}
				set.add(line);
			}
		}
		catch (Exception ex) {
			// skip
		}
		finally {
			close(is);
			close(reader);
		}
	}

	public static void close(Closeable x) {
		if (x == null) {
			return;
		}

		try {
			x.close();
		}
		catch (Exception e) {
			LOGGER.debug("close error", e);
		}
	}

}
