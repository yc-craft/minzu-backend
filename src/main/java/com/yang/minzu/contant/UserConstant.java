package com.yang.minzu.contant;

/**
 * 用户常量
 *
 * @author yang
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 用户注册验证码
     */
    String USER_REGISTER_CODE = "userRegisterCode";

    /**
     * 用户注册验证码 过期时间
     */
    String USER_REGISTER_CODE_OUTDATE = "userRegisterCodeOutdate";

    /**
     * 邮件发送者
     */
    String MAIL_FROM = "596268641@qq.com";



    //  ------- 权限 --------

    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;

}
