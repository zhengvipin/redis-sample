package com.catt.redis.jedis.test;

import redis.clients.jedis.Jedis;

/**
 * @author zwp
 * @since 2019-12-29 14:35
 */
public class TestMS {
    public static void main(String[] args) {
        Jedis jedis_M = new Jedis("192.168.211.211", 6379);
        Jedis jedis_S = new Jedis("192.168.211.211", 6380);

        jedis_S.slaveof("127.0.0.1", 6379);

        jedis_M.set("class", "1122V2");

        String result = jedis_S.get("class");
        System.out.println(result);
    }
}
