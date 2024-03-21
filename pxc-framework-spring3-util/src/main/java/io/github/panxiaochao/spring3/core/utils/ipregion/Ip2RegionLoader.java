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
package io.github.panxiaochao.spring3.core.utils.ipregion;

import lombok.Getter;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * Ip2RegionLoader 资源加载
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-10
 */
@Getter
public class Ip2RegionLoader {

	/**
	 * LOGGER Ip2RegionLoader.class
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Ip2RegionLoader.class);

	/**
	 * ip2region.db 文件路径
	 */
	private static final String IP2REGION_DB_FILE_LOCATION = "classpath:ip2region/ip2region.xdb";

	private static final Ip2RegionLoader IP2REGION_LOADER = new Ip2RegionLoader();

	private final Searcher searcher;

	private Ip2RegionLoader() {
		this.searcher = defaultSearcherByDb();
	}

	public static Ip2RegionLoader INSTANCE() {
		return IP2REGION_LOADER;
	}

	/**
	 * 从内存加载DB数据
	 * @param filePath 路径
	 * @return byte[]
	 */
	public byte[] loadByteFromFile(String filePath) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(filePath);
		try (InputStream inputStream = resource.getInputStream()) {
			return StreamUtils.copyToByteArray(inputStream);
		}
		catch (IOException e) {
			throw new RuntimeException("load ip2region file db is error", e);
		}
	}

	/**
	 * 静态默认获取本地数据库
	 * @return Searcher
	 */
	private Searcher defaultSearcherByDb() {
		try {
			byte[] ip2regionBytes = loadByteFromFile(IP2REGION_DB_FILE_LOCATION);
			Searcher searcher = Searcher.newWithBuffer(ip2regionBytes);
			LOGGER.info("配置[ip2region]成功！");
			return searcher;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
