package com.catt.redis.jedis.test;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zwp
 * @since 2019-12-29 12:57
 */
public class TestAPI {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.211.211", 6379);

        System.out.println("flushdb：" + jedis.flushDB());

        System.out.println("********************String*****************");
        jedis.set("k1", "v1");
        String v1 = jedis.get("k1");
        System.out.println(v1);
        jedis.mset("z3", "18", "l4", "20");
        List<String> vals = jedis.mget("z3", "l4");
        System.out.println(vals.toString());

        System.out.println("********************List*****************");
        jedis.rpush("list1", "java", "oracle", "redis");
        List<String> list1 = jedis.lrange("list1", 0, -1);
        System.out.println(list1.toString());

        System.out.println("********************Set*****************");
        jedis.sadd("set1", "a", "b", "c", "a");
        String set1 = jedis.smembers("set1").toString();
        System.out.println(set1);

        System.out.println("********************Hash*****************");
        Map<String, String> hash = new HashMap<>();
        hash.put("id", "9527");
        hash.put("name", "zxx");
        hash.put("age", "27");
        jedis.hset("hash1", hash);
        Map<String, String> hash1 = jedis.hgetAll("hash1");
        System.out.println(hash1.values().toString());

        System.out.println("********************Zset*****************");
        jedis.zadd("zset1", 10d, "v1");
        jedis.zadd("zset1", 20d, "v2");
        jedis.zadd("zset1", 30d, "v3");
        Set<String> zset1 = jedis.zrange("zset1", 0, -1);
        System.out.println(zset1.toString());

        System.out.println(jedis.keys("*").toString());
    }
}
