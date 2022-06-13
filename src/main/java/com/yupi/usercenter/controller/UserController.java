package com.yupi.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.yupi.usercenter.common.BaseResponse;
import com.yupi.usercenter.common.ErrorCode;
import com.yupi.usercenter.common.ResultUtils;
import com.yupi.usercenter.exception.BusinessException;
import com.yupi.usercenter.model.domain.User;
import com.yupi.usercenter.model.domain.request.UserLoginRequest;
import com.yupi.usercenter.model.domain.request.UserRegisterMailRequest;
import com.yupi.usercenter.model.domain.request.UserRegisterRequest;
import com.yupi.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.yupi.usercenter.contant.UserConstant.ADMIN_ROLE;
import static com.yupi.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author yang
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注册
     * @param userRegisterRequest   注册请求体
     * @param request   http
     * @return  id
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest,HttpServletRequest request) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String email = userRegisterRequest.getEmail();
        String userCode = userRegisterRequest.getUserCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, email,userCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, email,userCode,request);
        return ResultUtils.success(result);
    }

    /**
     * 修改密码
     * @param userRegisterRequest  同注册请求体
     * @param request   http
     * @return  id
     */
    @PostMapping("/password")
    public BaseResponse<Long> userResetPassword(@RequestBody UserRegisterRequest userRegisterRequest,HttpServletRequest request) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String email = userRegisterRequest.getEmail();
        String userCode = userRegisterRequest.getUserCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, email,userCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        long result = userService.userResetPassword(userAccount, userPassword, checkPassword, email,userCode,request);
        return ResultUtils.success(result);
    }

    /**
     * 发送 验证码 到邮箱
     * @param userRegisterMailRequest   email
     * @param request   http
     * @return  1 成功
     */
    @PostMapping("/registerMail")
    public BaseResponse<Long> userRegisterMail(@RequestBody UserRegisterMailRequest userRegisterMailRequest, HttpServletRequest request) {
        if (userRegisterMailRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱为空");
        }
        String email = userRegisterMailRequest.getEmail();
        long result = userService.userRegisterMail(email,request);
        return ResultUtils.success(result);
    }

    /**
     * 登录
     * @param userLoginRequest  登录请求体
     * @param request   http
     * @return  User
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 退出登录
     * @param request   gttp
     * @return  1
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户数据
     * @param request   http
     * @return  User
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 查询用户信息 仅管理员
     * @param username  username 可无
     * @param request   http
     * @return  list
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request)) {
           throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 单点查询用户信息
     * @param searchUser    User类
     * @param request   http
     * @return  list
     */
    @PostMapping("/searchByOne")
    public BaseResponse<List<User>> searchUsersByOne(@RequestBody User searchUser, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = searchUser.getUserAccount();
        String username = searchUser.getUsername();
        Integer gender = searchUser.getGender();
        String phone = searchUser.getPhone();
        String email = searchUser.getEmail();
        Integer userStatus = searchUser.getUserStatus();
        Integer userRole = searchUser.getUserRole();
        Date createTime = searchUser.getCreateTime();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userAccount)) {
            queryWrapper.like("userAccount", userAccount);
        }
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        if (gender!=null) {
            queryWrapper.eq("gender", gender);
        }
        if (StringUtils.isNotBlank(phone)) {
            queryWrapper.like("phone", phone);
        }
        if (StringUtils.isNotBlank(email)) {
            queryWrapper.like("email", email);
        }
        if (userStatus!=null) {
            queryWrapper.eq("userStatus", userStatus);
        }
        if (userRole!=null) {
            queryWrapper.eq("userRole", userRole);
        }
        if (StringUtils.isNotBlank((CharSequence) createTime)) {
            queryWrapper.like("createTime", createTime);
        }

        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 删除用户
     * @param deleteUser    User类
     * @param request   http
     * @return  boolean
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody User deleteUser, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (deleteUser.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"id不能小于0");
        }
        boolean b = userService.removeById(deleteUser.getId());
        return ResultUtils.success(b);
    }

    /**
     * 添加、修改 用户
     * @param changeUser    User类
     * @param request   http
     * @return  boolean
     */
    @PostMapping("/save")
    public BaseResponse<Boolean> saveUser(@RequestBody User changeUser, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean b = userService.saveOrUpdate(changeUser);
        return ResultUtils.success(b);
    }

    /**
     * 是否为管理员
     * @param request   http
     * @return  boolean
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }



}
