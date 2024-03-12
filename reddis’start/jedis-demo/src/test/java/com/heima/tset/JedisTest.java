package com.heima.tset;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class JedisTest {
    private Jedis jedis;
    @BeforeEach
    void setUb(){
        jedis = new Jedis("127.0.0.1",6379);
        jedis.auth("951026");
        jedis.select(0);
    }

    @Test
    void testString(){
        String result = jedis.set("name","xxxz");
        System.out.println(result);
        String name = jedis.get("name");
        System.out.println(name);
    }

    @Test
    void testHash(){
        jedis.hset("user:1","name","Jack");
        jedis.hset("user:1","agr","18");
        Map<String,String> map = jedis.hgetAll("user:1");
        System.out.println(map);
    }

    @AfterEach
    void rearDown(){
        if(jedis != null){
            jedis.close();
        }
    }
}
