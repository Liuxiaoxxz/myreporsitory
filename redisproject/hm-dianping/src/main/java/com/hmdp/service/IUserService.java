package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IUserService extends IService<User> {
    //校验手机号
    //如果不符合返回错误信息
    //符合，生成验证码
    //保存验证码到session
    //发送验证码

    Result sendCode(String phone, HttpSession session);

    Result login(LoginFormDTO loginForm,HttpSession session);
}