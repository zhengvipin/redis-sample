package com.catt.redis.jedis.test;

import redis.clients.jedis.Jedis;

/**
 * @author zwp
 * @since 2019-12-29 14:35
 */
public class TestMasterSlave {
    public static void main(String[] args) {
        Jedis jedisMaster = new Jedis("192.168.211.211", 6379);
        // 配从不配主
        Jedis jedisSlave = new Jedis("192.168.211.211", 6380);
        jedisSlave.slaveof("192.168.211.211", 6379);

        jedisMaster.set("class", "1122V2");
        String result = jedisSlave.get("class");
        System.out.println(result);
    }
}
