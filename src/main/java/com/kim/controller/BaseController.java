package com.kim.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kim.service.*;
import com.kim.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author www
 * @title: BaseController
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/26 0:37
 */
public class BaseController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    RedisUtil.redisString redisString;

    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysMenuService sysMenuService;
    @Autowired
    SysRoleMenuService sysRoleMenuService;
    @Autowired
    SysUserRoleService sysUserRoleService;


    /**
     * 获取页码
     *
     * @return {@link Page}
     */
    public Page getPage(){
        int current = ServletRequestUtils.getIntParameter(request,"current",1);
        int size = ServletRequestUtils.getIntParameter(request,"size",10);
        return new Page(current,size);
    }
}
