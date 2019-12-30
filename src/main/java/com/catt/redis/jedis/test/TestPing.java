package com.catt.redis.jedis.test;

import redis.clients.jedis.Jedis;

/**
 * @author zwp
 * @since 2019-12-29 12:55
 */
public class TestPing {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.211.211", 6379);
        System.out.println(jedis.ping());
    }
}
