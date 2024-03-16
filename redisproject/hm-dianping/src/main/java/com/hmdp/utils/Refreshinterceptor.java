package com.hmdp.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Refreshinterceptor implements HandlerInterceptor {
    private StringRedisTemplate stringRedisTemplate;
    public Refreshinterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //TODO 1.获取请求头中的token
        String token = request.getHeader("authorization");
        if(StrUtil.isBlank(token)){
            response.setStatus(401);
            return false;
        }
        //TODO 2.基于token获取Redis中的用户
        String key = RedisConstants.LOGIN_USER_KEY+token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        if(userMap.isEmpty()){
            response.setStatus(401);
            return false;
        }
        UserDTO user = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);

        UserHolder.saveUser(user);

        stringRedisTemplate.expire(key,RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        UserHolder.removeUser();
        return;
    }
}
