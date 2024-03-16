package com.heima;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisDemoApplicationTests {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Test
    void contextLoads() {
        //写入一条String数据
        redisTemplate.opsForValue().set("name","小张");
        //获取String数据
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println("name = "+name);
    }

    @Test
    void testSaveUser(){
        redisTemplate.opsForValue().set("user:100",new User("xxz",18));
        User o = (User) redisTemplate.opsForValue().get("user:100");
        System.out.println(o);
    }

}
