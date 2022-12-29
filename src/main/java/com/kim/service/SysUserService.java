package com.kim.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kim.entity.SysUser;

/**
 * @author www
 * @title: SysUserService
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/25 2:02
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 得到用户名
     *
     * @param username 用户名
     * @return {@link SysUser}
     */
    SysUser getByUsername(String username);

    String getUserAuthorityInfo(Long userId);

    /**
     * 清除 与用户相关的权限信息
     *
     * @param username 用户名
     */
    void clearUserAuthorityInfo(String username);

    void clearUserAuthorityInfoByRoleId(Long roleId);

    void clearUserAuthorityInfoByMenuId(Long menuId);
}
