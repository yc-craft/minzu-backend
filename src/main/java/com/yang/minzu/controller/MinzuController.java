package com.yang.minzu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.minzu.common.BaseResponse;
import com.yang.minzu.common.ErrorCode;
import com.yang.minzu.common.ResultUtils;
import com.yang.minzu.contant.UserConstant;
import com.yang.minzu.exception.BusinessException;
import com.yang.minzu.model.domain.request.MinzuSearchByOneRequest;
import com.yang.minzu.model.domain.request.MinzuSearchByRequest;
import com.yang.minzu.model.domain.Minzu;
import com.yang.minzu.model.domain.User;
import com.yang.minzu.service.MinzuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/minzu")
public class MinzuController {

    @Resource
    private MinzuService minzuService;

    /**
     * 查找所有minzu数据
     * @param request   http
     * @return  list
     */
    @GetMapping("/search")
    public BaseResponse<List<Minzu>> search(HttpServletRequest request) {
        // 检查是否登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        QueryWrapper<Minzu> queryWrapper = new QueryWrapper<>();
        List<Minzu> minzuList = minzuService.list(queryWrapper);
        return ResultUtils.success(minzuList);
    }

    /**
     * 根据minzu资源名字查询
     * @param minzuName 资源name
     * @param request   http
     * @return  list
     */
    @PostMapping("/searchByName")
    public BaseResponse<List<Minzu>> searchMinzuByName(@RequestParam(value = "minzuName", required = false) String minzuName, HttpServletRequest request) {
        // 检查是否登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        QueryWrapper<Minzu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("minzuName",minzuName);
        List<Minzu> minzuList = minzuService.list(queryWrapper);
        return ResultUtils.success(minzuList);
    }

    /**
     * 通过 多个民族类型、资源类型mp3~、民族资源name 查询minzu资源
     * @param minzuSearchByRequest   minzuName、minzuType[]、minzuSource
     * @param request   http
     * @return  list
     */
    @PostMapping("/searchBy")
    public BaseResponse<List<Minzu>> searchMinzuBy(@RequestBody(required = false) MinzuSearchByRequest minzuSearchByRequest, HttpServletRequest request ) {
        // 检查是否登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        QueryWrapper<Minzu> queryWrapper = new QueryWrapper<>();

        if(minzuSearchByRequest!=null) {
            System.out.println("body = " + minzuSearchByRequest);
            String minzuName = minzuSearchByRequest.getMinzuName();
            String[] minzuType = minzuSearchByRequest.getMinzuType();
            String source = minzuSearchByRequest.getMinzuSource();
            //处理动态数据逻辑
            //source  如果有就加入，没有就查询所有的类型
            Integer minzuSource = null;
            if (!StringUtils.isAnyBlank(source)) {
                if (!"3".equals(source)) {
                    minzuSource = Integer.valueOf(source);
                    queryWrapper = queryWrapper.eq("minzuSource", minzuSource);
                }
            }
            //minzuName
            if (!StringUtils.isAnyBlank(minzuName)) {
                queryWrapper = queryWrapper.like("minzuName", minzuName);
            }
            //minzuType
            if (minzuType != null && !CollectionUtils.isEmpty(Arrays.asList(minzuType))) {
                queryWrapper = queryWrapper.in("minzuType", minzuType);
            }
        }
        queryWrapper = queryWrapper.orderByAsc("minzuSource");
        List<Minzu> minzuList = minzuService.list(queryWrapper);
        return ResultUtils.success(minzuList);
    }

    /**
     * 通过 一个民族类型、资源类型mp3~、民族资源name 查询minzu资源
     * @param minzuSearchByOneRequest    minzuName、minzuType、minzuSource
     * @param request   http
     * @return  list
     */
    @PostMapping("/searchByOne")
    public BaseResponse<List<Minzu>> searchMinzuByOne(@RequestBody(required = false) MinzuSearchByOneRequest minzuSearchByOneRequest, HttpServletRequest request ) {
        // 检查是否登录
        System.out.println("body = " + minzuSearchByOneRequest);
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        String minzuName = minzuSearchByOneRequest.getMinzuName();
        String minzuType = minzuSearchByOneRequest.getMinzuType();
        String source = minzuSearchByOneRequest.getMinzuSource();

        QueryWrapper<Minzu> queryWrapper = new QueryWrapper<>();

        //处理动态数据逻辑
        //minzuName
        if (!StringUtils.isAnyBlank(minzuName)) {
            queryWrapper = queryWrapper.like("minzuName",minzuName);
        }
        //minzuType
        if (!StringUtils.isAnyBlank(minzuType)) {
            queryWrapper = queryWrapper.like("minzuType",minzuType);
        }
        //source  如果有就加入，没有就查询所有的类型
        Integer minzuSource = null;
        //3表示 类型不限制
        if (!StringUtils.isAnyBlank(source)){
            if ( ! "3".equals(source) ){
                minzuSource = Integer.valueOf(source);
                queryWrapper = queryWrapper.eq("minzuSource",minzuSource);
            }
        }
        queryWrapper = queryWrapper.orderByAsc("minzuSource");
        List<Minzu> minzuList = minzuService.list(queryWrapper);
        return ResultUtils.success(minzuList);
    }

    /**
     * 根据Minzu类型 delete
     * @param deleteMinzu   Minzu类
     * @param request   http
     * @return  Boolean
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteMinzu(@RequestBody Minzu deleteMinzu, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (deleteMinzu.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"id不能小于0");
        }
        boolean b = minzuService.removeById(deleteMinzu.getId());
        return ResultUtils.success(b);
    }

    /**
     * 根据Minzu类型 修改或新增 到数据库
     * @param changeMinzu   Minzu类
     * @param request   http
     * @return  Boolean
     */
    @PostMapping("/save")
    public BaseResponse<Boolean> saveMinzu(@RequestBody Minzu changeMinzu, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        boolean b = minzuService.saveOrUpdate(changeMinzu);
        return ResultUtils.success(b);
    }

    /**
     * 是否为管理员
     *
     * @param request   http
     * @return  boolean
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }
}
