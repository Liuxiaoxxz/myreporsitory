package org.example.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisConnectionFactory {
    private static final JedisPool jedisPool;
    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //资源池中的最大连接数    默认8
        poolConfig.setMaxTotal(8);
        //资源池允许的最大空闲连接数 默认8
        poolConfig.setMaxIdle(8);
        //资源池确保的最少空闲连接数 默认0
        poolConfig.setMinIdle(0);
        //当资源池连接用尽后，调用者的最大等待时间（单位为毫秒）。  默认-1 永不超时
        poolConfig.setMaxWaitMillis(1000);
        jedisPool = new JedisPool(poolConfig,
                "127.0.0.1",6379,1000,"951026");
    }
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }
}
