package com.yupi.usercenter.service;

import com.yupi.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author yang
 */
public interface UserService extends IService<User> {

    /**
     * 注册时 发送邮箱验证码
     * @param email mail
     * @param request   http
     * @return  1
     */
    long userRegisterMail(String email, HttpServletRequest request);

    /**
     * 用户注册
     * @param userAccount   userAccount
     * @param userPassword  userPassword
     * @param checkPassword checkPassword
     * @param email         email
     * @param userCode      userCode
     * @param request       http
     * @return  id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String email, String userCode, HttpServletRequest request);

    /**
     * 用户根据验证码 修改密码
     * @param userAccount   userAccount
     * @param userPassword  userPassword
     * @param checkPassword checkPassword
     * @param email         email
     * @param userCode      验证码
     * @param request       http
     * @return  id
     */
    Long userResetPassword(String userAccount, String userPassword, String checkPassword, String email, String userCode, HttpServletRequest request);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser    原User
     * @return  SafetyUser
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     *
     * @param request   http
     * @return  1
     */
    int userLogout(HttpServletRequest request);


    /**
     * 查询非空
     *
     * @param userAccount   userAccount
     * @param userPassword  userPassword
     * @param checkPassword checkPassword
     */
    void userCheck(String userAccount, String userPassword, String checkPassword);
}
