package com.hmdp.config;

import com.hmdp.utils.Logininterceptor;
import com.hmdp.utils.Refreshinterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.rmi.registry.Registry;
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Logininterceptor())
                .excludePathPatterns(
                        "/user/code",
                        "/user/login",
                        "/blog/hot",
                        "/upload/**",
                        "/shop-type/**",
                        "/voucher/**",
                        "/shop/**"
                ).order(1);
        registry.addInterceptor(new Refreshinterceptor(stringRedisTemplate))
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login","/user/code")
                .order(0);

    }
}
