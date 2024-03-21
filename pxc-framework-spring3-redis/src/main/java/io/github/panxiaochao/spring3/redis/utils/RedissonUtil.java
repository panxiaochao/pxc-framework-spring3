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
package io.github.panxiaochao.spring3.redis.utils;

import io.github.panxiaochao.spring3.core.utils.JacksonUtil;
import io.github.panxiaochao.spring3.core.utils.SpringContextUtil;
import io.github.panxiaochao.spring3.core.utils.StrUtil;
import io.github.panxiaochao.spring3.core.utils.StringPools;
import org.redisson.api.*;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.codec.JsonJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * <p>
 * Redisson 工具类
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-27
 */
public class RedissonUtil {

	/**
	 * LOGGER RedissonUtil.class
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RedissonUtil.class);

	/**
	 * constructor private
	 */
	private RedissonUtil() {
	}

	/**
	 * 初始化
	 */
	private static final RedissonUtil REDISSON_UTIL = new RedissonUtil();

	/**
	 * RedissonClient properties
	 */
	private RedissonClient redissonClient;

	/**
	 * 自己手动初始化 RedissonClient Bean instance
	 * @param redissonClient RedissonClient
	 * @return RedissonUtil
	 */
	public RedissonUtil initRedissonClient(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
		return this;
	}

	/**
	 * Obtain static RedissonUtil instance
	 * @return RedissonUtil
	 */
	public static RedissonUtil INSTANCE() {
		return REDISSON_UTIL;
	}

	/**
	 * Obtain RedissonClient
	 * @return RedissonClient
	 */
	public RedissonClient ofRedissonClient() {
		final RedissonClient redissonClient = Optional.ofNullable(this.redissonClient)
			.orElseGet(() -> SpringContextUtil.getBean(RedissonClient.class));
		Objects.requireNonNull(redissonClient,
				"RedissonClient is null, check env is a web application or iniRedissonClient ！");
		return redissonClient;
	}

	/**
	 * Returns id of this Redisson instance ID
	 * @return String
	 */
	public String getRedissonId() {
		return ofRedissonClient().getId();
	}

	// ------------------------------- Object 类型操作 --------------------------------

	/**
	 * set the value
	 * @param key key
	 * @param value T value
	 */
	public <T> void set(String key, T value) {
        set(key, value, Duration.ofMillis(0));
    }

    /**
     * 设置过期时间
     *
     * @param key      key
     * @param duration expiration duration
     */
    public <T> boolean expire(String key, Duration duration) {
        RBucket<T> rBucket = getRBucket(key);
        return rBucket.expire(duration);
    }

    /**
     * 设置过期时间，当只有key设置过过期时间才会设置
     *
     * @param key      key
     * @param duration expiration duration
     */
    public <T> boolean expireIfSet(String key, Duration duration) {
        RBucket<T> rBucket = getRBucket(key);
        return rBucket.expireIfSet(duration);
    }

    /**
     * 设置过期时间，当只有key没有设置过期时间才会设置
     *
     * @param key      key
     * @param duration expiration duration
     */
    public <T> boolean expireIfNotSet(String key, Duration duration) {
        RBucket<T> rBucket = getRBucket(key);
        return rBucket.expireIfNotSet(duration);
	}

	/**
	 * Obtain the v
	 * @param key key
	 * @return value
	 */
	public <T> T get(String key) {
		RBucket<T> rBucket = getRBucket(key);
		return rBucket.get();
	}

	/**
	 * Sets value with defined duration only if object holder doesn't exist.
	 * @param key key
	 * @param value value to set
	 * @param duration expiration duration
	 * @return {@code true} if successful, or {@code false} if element was already set
	 */
	public <T> boolean setIfAbsent(String key, T value, Duration duration) {
        return getRBucket(key).setIfAbsent(value, duration);
    }

    /**
     * Set the value.
     * @param key key
	 * @param value T value
	 * @param duration expiration duration
     */
    public <T> void set(String key, T value, Duration duration) {
        if (duration.toMillis() <= 0) {
            getRBucket(key).set(value);
        } else {
            RBatch batch = ofRBatch();
            RBucketAsync<T> bucket = batch.getBucket(key);
            bucket.setAsync(value);
            bucket.expireAsync(duration);
			batch.execute();
		}
	}

	/**
	 * delete the object from the key.
	 * @param key key
	 * @return true or false
	 */
	public boolean delete(String key) {
		return getRBucket(key).delete();
	}

	/**
	 * delete the Collection object from the key
	 * @param collection collection
	 */
	public void delete(Collection<?> collection) {
		RBatch batch = ofRBatch();
		collection.forEach(key -> {
			batch.getBucket(key.toString()).deleteAsync();
        });
        batch.execute();
    }

    /**
     * Check object existence
     *
     * @return <code>true</code> if object exists and <code>false</code> otherwise
     */
    public boolean isExists(String key) {
		return getRBucket(key).isExists();
	}

	/**
	 * Obtain the RBucket.
	 * @param name name of object
	 * @return RBucket
	 */
	private <T> RBucket<T> getRBucket(String name) {
		return ofRedissonClient().getBucket(name);
	}

	// ------------------------------- 管道 类型操作 --------------------------------

	/**
	 * Obtain the RBatch
	 * @return RBatch
	 */
	private RBatch ofRBatch() {
		return ofRedissonClient().createBatch();
    }

    // ------------------------------- 限流 类型操作 --------------------------------

	/**
	 * 限流.
	 * @param key 限流key
	 * @param rateType 限流类型
     * @param rate 速率
     * @param rateInterval 速率间隔
     * @return -1 表示失败
     */
    public long tryRateLimiter(String key, RateType rateType, long rate, long rateInterval) {
        RRateLimiter rateLimiter = getRRateLimiter(key);
        boolean trySetRateSuccess = rateLimiter.trySetRate(rateType, rate, rateInterval, RateIntervalUnit.MILLISECONDS);
		// 第一次成功 拿锁后进行设置过期时间
		if (trySetRateSuccess) {
			// 设置过期时间，和速率一样，防止缓存残留
			rateLimiter.expire(Duration.ofSeconds(rateInterval));
		}
		if (rateLimiter.tryAcquire()) {
            return rateLimiter.availablePermits();
		}
		else {
			return -1L;
		}
	}

	/**
	 * Obtain the RRateLimiter.
	 * @param name name of object
	 * @return RRateLimiter
	 */
	private RRateLimiter getRRateLimiter(String name) {
		return ofRedissonClient().getRateLimiter(name);
    }

    // ------------------------------- 二进制流 类型操作 --------------------------------

	/**
	 * Obtain the RBinaryStream.
	 * @param name name of object
	 * @return RBinaryStream
	 */
	private RBinaryStream getRBinaryStream(String name) {
		return ofRedissonClient().getBinaryStream(name);
    }

    // ------------------------------- List 类型操作 --------------------------------

	/**
	 * Obtain the getRList.
	 * @param name name of object
	 * @return getRList
	 */
	private <T> RList<T> getRList(String name) {
		return ofRedissonClient().getList(name);
    }

    // ------------------------------- Set 类型操作 --------------------------------

	/**
	 * Obtain the RSet.
	 * @param name name of object
	 * @return RSet
	 */
	private <T> RSet<T> getRSet(String name) {
		return ofRedissonClient().getSet(name);
    }

    // ------------------------------- Map 类型操作 --------------------------------

	/**
	 * Obtain the RMap.
	 * @param name name of object
	 * @return RMap
	 */
	private <K, V> RMap<K, V> getRMap(String name) {
		return ofRedissonClient().getMap(name);
	}

    // ------------------------------- MapCache 类型操作 --------------------------------

	/**
	 * Obtain the RMapCache.
	 * @param name name of object
	 * @return RMapCache
	 */
	private <K, V> RMapCache<K, V> getRMapCache(String name) {
		return ofRedissonClient().getMapCache(name);
	}

    // ------------------------------- 原子Long 类型操作 --------------------------------

	/**
	 * Obtain the RAtomicLong.
	 * @param name name of object
	 * @return RAtomicLong
	 */
	private RAtomicLong getRAtomicLong(String name) {
		return ofRedissonClient().getAtomicLong(name);
    }

    // ------------------------------- 字节 类型操作 --------------------------------

	/**
	 * Obtain the RBitSet.
	 * @param name name of object
	 * @return RBitSet
	 */
	private RBitSet getRBitSet(String name) {
        return ofRedissonClient().getBitSet(name);
    }

    // ------------------------------- 地理位置GEO 类型操作 --------------------------------

    /**
     * 将指定的地理空间位置（纬度、经度、名称）添加到指定的key中.
     *
     * @param key    名称KEY
     * @param lng    经度
     * @param lat    纬度
     * @param member 成员名称
     * @return 添加元素个数
     */
    public Long geoAdd(String key, double lng, double lat, Object member) {
        RGeo<Object> geo = getRGeo(key);
        return geo.add(lng, lat, member);
    }

    /**
     * 将指定的地理空间位置（纬度、经度、名称）添加到指定的key中.
     *
     * @param key     名称KEY
     * @param entries 包含精度、维度、成员集合
     * @return 添加元素个数
     */
    public Long geoAdd(String key, GeoEntry... entries) {
        RGeo<String> geo = getRGeo(key);
        return geo.add(entries);
    }

    /**
     * 返回成员映射的GeoHash值.
     *
     * @param key     名称KEY
     * @param members - objects
     * @return hash mapped by object
     */
    public Map<String, String> hash(String key, String... members) {
        RGeo<String> geo = getRGeo(key);
        return geo.hash(members);
    }

    /**
     * 返回成员的地址位置信息.
     *
     * @param key     名称KEY
     * @param members - objects
     * @return geo position mapped by object
     */
    public Map<String, GeoPosition> position(String key, String... members) {
        RGeo<String> geo = getRGeo(key);
        return geo.pos(members);
    }

    /**
     * 返回指定两个对象的距离，通过指定距离单位，比如：米m，千米km，英里mi，英尺ft.
     *
     * @param key          名称KEY
     * @param firstMember  - first object
     * @param secondMember - second object
     * @param geoUnit      - geo unit
     * @return distance
     */
    public Double distance(String key, String firstMember, String secondMember, GeoUnit geoUnit) {
        RGeo<String> geo = getRGeo(key);
        return geo.dist(firstMember, secondMember, geoUnit);
    }

    /**
     * 返回成员周围半径内指定搜索条件内的排序集合, 默认升序.
     *
     * @param key     名称KEY
     * @param member  成员
     * @param radius  单位内半径
     * @param geoUnit 单位
     * @param count   返回数量
     * @return 返回集合
     */
    public List<String> search(String key, String member, double radius, GeoUnit geoUnit, int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildRadiusGeoSearchArgs(member, 0, 0, radius, geoUnit, GeoOrder.ASC, count);
        return geo.search(geoSearchArgs);
    }

    /**
     * 返回成员周围半径内指定搜索条件内的排序集合.
     *
     * @param key      名称KEY
     * @param member   成员
     * @param radius   单位内半径
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回集合
     */
    public List<String> search(String key, String member, double radius, GeoUnit geoUnit, GeoOrder geoOrder,
                               int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildRadiusGeoSearchArgs(member, 0, 0, radius, geoUnit, geoOrder, count);
        return geo.search(geoSearchArgs);
    }

    /**
     * 返回经纬度周围半径内指定搜索条件的排序集合, 默认升序.
     *
     * @param key     名称KEY
     * @param lng     经度
     * @param lat     维度
     * @param radius  单位内半径
     * @param geoUnit 单位
     * @param count   返回数量
     * @return 返回集合
     */
    public List<String> search(String key, double lng, double lat, double radius, GeoUnit geoUnit, int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildRadiusGeoSearchArgs(StringPools.EMPTY, lng, lat, radius, geoUnit,
                GeoOrder.ASC, count);
        return geo.search(geoSearchArgs);
    }

    /**
     * 返回经纬度周围半径内指定搜索条件的排序集合.
     *
     * @param key      名称KEY
     * @param lng      经度
     * @param lat      维度
     * @param radius   单位内半径
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回集合
     */
    public List<String> search(String key, double lng, double lat, double radius, GeoUnit geoUnit, GeoOrder geoOrder,
                               int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildRadiusGeoSearchArgs(StringPools.EMPTY, lng, lat, radius, geoUnit, geoOrder,
                count);
        return geo.search(geoSearchArgs);
    }

    /**
     * 返回经纬度周围矩形大小范围内指定搜索条件的排序集合.
     *
     * @param key      名称KEY
     * @param member   成员
     * @param width    矩形宽度
     * @param height   矩形高度
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回集合
     */
    public List<String> search(String key, String member, double width, double height, GeoUnit geoUnit,
                               GeoOrder geoOrder, int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildBoxGeoSearchArgs(member, 0, 0, width, height, geoUnit, geoOrder, count);
        return geo.search(geoSearchArgs);
    }

    /**
     * 返回经纬度周围矩形大小范围内指定搜索条件的排序集合.
     *
     * @param key      名称KEY
     * @param lng      经度
     * @param lat      维度
     * @param width    矩形宽度
     * @param height   矩形高度
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回集合
     */
    public List<String> search(String key, double lng, double lat, double width, double height, GeoUnit geoUnit,
                               GeoOrder geoOrder, int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildBoxGeoSearchArgs(StringPools.EMPTY, lng, lat, width, height, geoUnit,
                geoOrder, count);
        return geo.search(geoSearchArgs);
    }

    /**
     * 返回指定成员周围半径内指定搜索条件的元素, 并返回距离, 默认正序.
     *
     * @param key     名称KEY
     * @param member  成员
     * @param radius  单位内半径
     * @param geoUnit 单位
     * @param count   返回数量
     * @return 返回集合
     */
    public Map<String, Double> searchWithDistance(String key, String member, double radius, GeoUnit geoUnit,
                                                  int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildRadiusGeoSearchArgs(member, 0, 0, radius, geoUnit, GeoOrder.ASC, count);
        return geo.searchWithDistance(geoSearchArgs);
    }

    /**
     * 返回指定成员周围半径内指定搜索条件的元素，并返回距离.
     *
     * @param key      名称KEY
     * @param member   成员
     * @param radius   单位内半径
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回集合
     */
    public Map<String, Double> searchWithDistance(String key, String member, double radius, GeoUnit geoUnit,
                                                  GeoOrder geoOrder, int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildRadiusGeoSearchArgs(member, 0, 0, radius, geoUnit, geoOrder, count);
        return geo.searchWithDistance(geoSearchArgs);
    }

    /**
     * 返回经纬度周围半径内指定搜索条件的元素, 并返回距离, 默认正序.
     *
     * @param key     名称KEY
     * @param lng     经度
     * @param lat     维度
     * @param radius  单位内半径
     * @param geoUnit 单位
     * @param count   返回数量
     * @return 返回集合
     */
    public Map<String, Double> searchWithDistance(String key, double lng, double lat, double radius, GeoUnit geoUnit,
                                                  int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildRadiusGeoSearchArgs(StringPools.EMPTY, lng, lat, radius, geoUnit,
                GeoOrder.ASC, count);
        return geo.searchWithDistance(geoSearchArgs);
    }

    /**
     * 返回经纬度周围半径内指定搜索条件的元素，并返回距离.
     *
     * @param key      名称KEY
     * @param lng      经度
     * @param lat      维度
     * @param radius   单位内半径
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回集合
     */
    public Map<String, Double> searchWithDistance(String key, double lng, double lat, double radius, GeoUnit geoUnit,
                                                  GeoOrder geoOrder, int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildRadiusGeoSearchArgs(StringPools.EMPTY, lng, lat, radius, geoUnit, geoOrder,
                count);
        return geo.searchWithDistance(geoSearchArgs);
    }

    /**
     * 返回指定成员周围矩形大小内指定搜索条件的元素，并返回距离.
     *
     * @param key      名称KEY
     * @param member   成员
     * @param width    矩形宽度
     * @param height   矩形高度
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回集合
     */
    public Map<String, Double> searchWithDistance(String key, String member, double width, double height,
                                                  GeoUnit geoUnit, GeoOrder geoOrder, int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildBoxGeoSearchArgs(member, 0, 0, width, height, geoUnit, geoOrder, count);
        return geo.searchWithDistance(geoSearchArgs);
    }

    /**
     * 返回经纬度周围矩形大小内指定搜索条件的元素，并返回距离.
     *
     * @param key      名称KEY
     * @param lng      经度
     * @param lat      维度
     * @param width    矩形宽度
     * @param height   矩形高度
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回集合
     */
    public Map<String, Double> searchWithDistance(String key, double lng, double lat, double width, double height,
                                                  GeoUnit geoUnit, GeoOrder geoOrder, int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildBoxGeoSearchArgs(StringPools.EMPTY, lng, lat, width, height, geoUnit,
                geoOrder, count);
        return geo.searchWithDistance(geoSearchArgs);
    }

    /**
     * 返回指定成员周围半径内指定搜索条件的元素，并返回经纬度，默认正序.
     *
     * @param key     名称KEY
     * @param member  成员
     * @param radius  单位内半径
     * @param geoUnit 单位
     * @param count   返回数量
     * @return 返回集合
     */
    public Map<String, GeoPosition> searchWithPosition(String key, String member, double radius, GeoUnit geoUnit,
                                                       int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildRadiusGeoSearchArgs(member, 0, 0, radius, geoUnit, GeoOrder.ASC, count);
        return geo.searchWithPosition(geoSearchArgs);
    }

    /**
     * 返回指定成员周围半径内指定搜索条件的元素，并返回经纬度.
     *
     * @param key      名称KEY
     * @param member   成员
     * @param radius   单位内半径
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回集合
     */
    public Map<String, GeoPosition> searchWithPosition(String key, String member, double radius, GeoUnit geoUnit,
                                                       GeoOrder geoOrder, int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildRadiusGeoSearchArgs(member, 0, 0, radius, geoUnit, geoOrder, count);
        return geo.searchWithPosition(geoSearchArgs);
    }

    /**
     * 返回经纬度周围半径内指定搜索条件的元素，并返回经纬度，默认正序.
     *
     * @param key     名称KEY
     * @param lng     经度
     * @param lat     维度
     * @param radius  单位内半径
     * @param geoUnit 单位
     * @param count   返回数量
     * @return 返回集合
     */
    public Map<String, GeoPosition> searchWithPosition(String key, double lng, double lat, double radius,
                                                       GeoUnit geoUnit, int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildRadiusGeoSearchArgs(StringPools.EMPTY, lng, lat, radius, geoUnit,
                GeoOrder.ASC, count);
        return geo.searchWithPosition(geoSearchArgs);
    }

    /**
     * 返回经纬度周围半径内指定搜索条件的元素，并返回经纬度.
     *
     * @param key      名称KEY
     * @param lng      经度
     * @param lat      维度
     * @param radius   单位内半径
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回集合
     */
    public Map<String, GeoPosition> searchWithPosition(String key, double lng, double lat, double radius,
                                                       GeoUnit geoUnit, GeoOrder geoOrder, int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildRadiusGeoSearchArgs(StringPools.EMPTY, lng, lat, radius, geoUnit, geoOrder,
                count);
        return geo.searchWithPosition(geoSearchArgs);
    }

    /**
     * 返回指定成员周围矩形大小内指定搜索条件的元素，并返回经纬度.
     *
     * @param key      名称KEY
     * @param member   成员
     * @param width    矩形宽度
     * @param height   矩形高度
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回集合
     */
    public Map<String, GeoPosition> searchWithPosition(String key, String member, double width, double height,
                                                       GeoUnit geoUnit, GeoOrder geoOrder, int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildBoxGeoSearchArgs(member, 0, 0, width, height, geoUnit, geoOrder, count);
        return geo.searchWithPosition(geoSearchArgs);
    }

    /**
     * 返回经纬度周围矩形大小内指定搜索条件的元素，并返回经纬度.
     *
     * @param key      名称KEY
     * @param lng      经度
     * @param lat      维度
     * @param width    矩形宽度
     * @param height   矩形高度
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回集合
     */
    public Map<String, GeoPosition> searchWithPosition(String key, double lng, double lat, double width, double height,
                                                       GeoUnit geoUnit, GeoOrder geoOrder, int count) {
        RGeo<String> geo = getRGeo(key);
        GeoSearchArgs geoSearchArgs = buildBoxGeoSearchArgs(StringPools.EMPTY, lng, lat, width, height, geoUnit,
                geoOrder, count);
        return geo.searchWithPosition(geoSearchArgs);
    }

    /**
     * 构造以成员或者经纬度周围半径大小查为询条件.
     *
     * @param member   成员
     * @param lng      经度
     * @param lat      维度
     * @param radius   单位内半径
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回查询对象
     */
    private GeoSearchArgs buildRadiusGeoSearchArgs(String member, double lng, double lat, double radius,
                                                   GeoUnit geoUnit, GeoOrder geoOrder, int count) {
        if (StrUtil.isNotBlank(member)) {
            return GeoSearchArgs.from(member).radius(radius, geoUnit).order(geoOrder).count(count);
        }
        return GeoSearchArgs.from(lng, lat).radius(radius, geoUnit).order(geoOrder).count(count);
    }

    /**
     * 构造以成员或者经纬度周围矩形大小查为询条件.
     *
     * @param member   成员
     * @param lng      经度
     * @param lat      维度
     * @param width    矩形宽度
     * @param height   矩形高度
     * @param geoUnit  单位
     * @param geoOrder 排序
     * @param count    返回数量
     * @return 返回查询对象
     */
    private GeoSearchArgs buildBoxGeoSearchArgs(String member, double lng, double lat, double width, double height,
                                                GeoUnit geoUnit, GeoOrder geoOrder, int count) {
        if (StrUtil.isNotBlank(member)) {
            return GeoSearchArgs.from(member).box(width, height, geoUnit).order(geoOrder).count(count);
        }
        return GeoSearchArgs.from(lng, lat).box(width, height, geoUnit).order(geoOrder).count(count);
    }

    /**
     * Obtain the RGeo.
     *
     * @param name name of object
     * @return RGeo
     */
    private <T> RGeo<T> getRGeo(String name) {
        return ofRedissonClient().getGeo(name, new JsonJacksonCodec(JacksonUtil.objectMapper()));
    }

    // ------------------------------- 可重入锁 类型操作 --------------------------------

    /**
     * tryLock by RLock.
     *
     * @param lock      the RLock object
     * @param waitTime  the maximum time to acquire the lock
     * @param leaseTime lease time
     * @param unit      time unit
     * @return <code>true</code> if lock is successfully acquired, otherwise
     * <code>false</code> if lock is already set.
     */
    public boolean tryLock(RLock lock, long waitTime, long leaseTime, TimeUnit unit) {
        boolean tryLockSuccess = false;
        try {
            tryLockSuccess = lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            LOGGER.error("Exception tryLock", e);
        }
        return tryLockSuccess;
    }

    /**
     * Releases the lock.
     *
     * @param lock the RLock Object
     */
    public void unLock(RLock lock) {
        // 是否上锁 && 是否同一个线程
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * Obtain RLock.
     *
     * @param lockName the lock name
     * @return RLock object
     */
    public RLock rLock(String lockName) {
        return ofRedissonClient().getLock(lockName);
    }

    // ------------------------------- 发布/订阅 类型操作 --------------------------------

    /**
     * 发布通道消息.
     * @param channelKey 通道key
     * @param msg 发送数据
	 */
	public <T> void publish(String channelKey, T msg) {
		RTopic topic = getRTopic(channelKey);
		topic.publish(msg);
	}

	/**
	 * 发布通道消息.
	 * @param channelKey 通道key
	 * @param msg 发送数据
	 * @param consumer 自定义处理
	 */
	public <T> void publish(String channelKey, T msg, Consumer<T> consumer) {
		RTopic topic = getRTopic(channelKey);
		topic.publish(msg);
		consumer.accept(msg);
	}

	/**
	 * 订阅通道接收消息 - key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置.
	 * @param channelKey 通道key
	 * @param clazz 消息类型
	 * @param consumer 自定义处理
	 * @return locally unique listener id
	 */
	public <T> int subscribe(String channelKey, Class<T> clazz, Consumer<T> consumer) {
		RTopic topic = getRTopic(channelKey);
		return topic.addListener(clazz, (channel, msg) -> consumer.accept(msg));
	}

	/**
	 * Removes the listener by <code>id</code> for listening this topic.
	 * @param channelKey 通道key
	 * @param listenerIds - listener ids
	 */
	public void removeListener(String channelKey, Integer... listenerIds) {
		RTopic topic = getRTopic(channelKey);
		topic.removeListener(listenerIds);
	}

	/**
	 * Obtain the RTopic.
	 * @param name name of object
	 * @return RTopic
	 */
	private RTopic getRTopic(String name) {
		return ofRedissonClient().getTopic(name, new JsonJacksonCodec(JacksonUtil.objectMapper()));
	}

}
