package com.heima;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisStringTests {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testString(){
        stringRedisTemplate.opsForValue().set("name","虎哥");
        Object name = stringRedisTemplate.opsForValue().get("name");
        System.out.println("name = "+name);
    }

    @Test
    void testSaveUser() throws JsonProcessingException {
        User user = new User("testUser", 28);
        String json = mapper.writeValueAsString(user);
        stringRedisTemplate.opsForValue().set("user:100",json);
        String jsonUser= stringRedisTemplate.opsForValue().get("user:100");
        User user1 = mapper.readValue(jsonUser,User.class);
        System.out.println(user1);
    }
    
}
