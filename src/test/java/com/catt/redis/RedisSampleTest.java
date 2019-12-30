package com.catt.redis;

import com.catt.redis.boot.bean.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 主测试类
 *
 * @author zwp
 * @since 2019-12-28 23:25
 */
@SpringBootTest
class RedisSampleTest {

    // 1.直接使用注入的方式
    // @Autowired
    // private RedisTemplate redisTemplate;

    // 异常现象：
    // key会出现乱码："\xac\xed\x00\x05t\x00\x02k1"
    // 原因：
    // 1.由RedisAutoConfiguration类可以知道：SpringBoot自动帮我们在容器中生成了一个RedisTemplate和一个StringRedisTemplate。但是，这个RedisTemplate的泛型是<Object,Object>
    // 2.RedisTemplate<K, V>模板类在操作redis时默认使用JdkSerializationRedisSerializer来进行序列化，存储二进制字节码
    // 解决：
    // 自定义RestTemplate，使用GenericJackson2JsonRedisSerializer解决序列化问题

    // 2.自定义redisTemplate注入的方式
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testString() {
        redisTemplate.opsForValue().set("k1", "v1");
        System.out.println(redisTemplate.opsForValue().get("k1"));
    }

    @Test
    void testStringObject() {
        User setUser = new User(10010, "马小云", 1, "CEO");
        redisTemplate.opsForValue().set("user", setUser);

        User getUser = (User) redisTemplate.opsForValue().get("user");
        if (getUser != null) {
            System.out.println(getUser.getId() + "-" + getUser.getName() + "-" + getUser.getJob());
        }
    }

    @Test
    void testHash() {
        Map<String, Object> map = new HashMap<>(3);
        map.put("id", 1);
        map.put("name", "史蒂芬");
        map.put("sex", 1);
        redisTemplate.opsForHash().putAll("map01", map);
        System.out.println(redisTemplate.opsForHash().get("map01", "name"));
    }

    @Test
    void testCluster() {
        System.out.println(redisTemplate.opsForValue().get("hi"));
    }
}
