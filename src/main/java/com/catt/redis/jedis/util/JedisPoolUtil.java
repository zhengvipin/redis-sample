package com.catt.redis.jedis.util;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 线程池工具类
 * 单例模式（双重加锁机制）：
 * 1.类构造器私有
 * 2.持有自己类型的属性
 * 3.对外提供获取实例的静态方法
 *
 * @author zwp
 * @since 2019-12-29 17:58
 */
public class JedisPoolUtil {
    private static volatile JedisPool jedisPool = null;

    private JedisPoolUtil() {
    }

    public static JedisPool getJedisPoolInstance() {
        // 先判断是否存在，不存在再加锁处理
        if (null == jedisPool) {
            synchronized (JedisPoolUtil.class) {
                if (null == jedisPool) {
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxTotal(1000);
                    poolConfig.setMaxIdle(32);
                    poolConfig.setMaxWaitMillis(100 * 1000);
                    poolConfig.setTestOnBorrow(true);

                    jedisPool = new JedisPool(poolConfig, "192.168.211.211", 6379);
                }
            }
        }
        return jedisPool;
    }

    public static void release(JedisPool jedisPool) {
        jedisPool.close();
    }
}
