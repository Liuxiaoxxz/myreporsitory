package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import com.hmdp.utils.UserHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;
import static com.hmdp.utils.SystemConstants.USER_NICK_NAME_PREFIX;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result sendCode(String phone, HttpSession session) {
        //TODO 1
        if (RegexUtils.isPhoneInvalid(phone)){
            return Result.fail("手机号格式错误");
        }
        //TODO 2
        String code = RandomUtil.randomNumbers(6);
        //TODO 3
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY+phone,code,LOGIN_CODE_TTL, TimeUnit.MINUTES);

        log.debug("发送短信验证码成功{"+code+"}");
        //TODO 4
        return Result.ok();
    }

    @Override
    public Result login(LoginFormDTO loginForm,HttpSession session) {
        String phone = loginForm.getPhone();
        if(RegexUtils.isPhoneInvalid(phone)){
            return Result.fail("手机号格式错误");
        }
        Object cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY+phone);
        String code = loginForm.getCode();
        if(cacheCode == null || !cacheCode.toString().equals(code)){
            return Result.fail("验证码错误");
        }
        //根据手机号查询用户
        User user = query().eq("phone",phone).one();
        if(user == null){
            user = createUserWith(phone);
        }
        String token = UUID.randomUUID().toString(true);

        UserDTO userDTO = BeanUtil.copyProperties(user,UserDTO.class);
        Map<String, Object> usermap = BeanUtil.beanToMap(userDTO,new HashMap<>(),
                CopyOptions.create().setIgnoreNullValue(true)
                .setFieldValueEditor
                        ((fieldName,fieldValue)->fieldValue.toString()));
        stringRedisTemplate.opsForHash()
                .putAll(LOGIN_USER_KEY+token,usermap);
        stringRedisTemplate
                .expire(LOGIN_USER_KEY+token,LOGIN_USER_TTL,TimeUnit.MINUTES);
//        UserHolder.saveUser(userDTO);
        return Result.ok(token);
    }

    private User createUserWith(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX+RandomUtil.randomString(10));
        save(user);
        return user;
    }
}
