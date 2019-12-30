package com.catt.redis.jedis.test;

import com.catt.redis.jedis.util.JedisPoolUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author zwp
 * @since 2019-12-29 18:18
 */
public class TestPool {
    public static void main(String[] args) {

        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();

        Jedis jedis;
        try {
            jedis = jedisPool.getResource();
            jedis.set("aa", "bb");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisPoolUtil.release(jedisPool);
        }
    }
}
