package com.kim.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kim.common.dto.SysMenuDto;
import com.kim.common.lang.BaseResponse;
import com.kim.entity.SysMenu;
import com.kim.entity.SysRoleMenu;
import com.kim.entity.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统菜单控制器
 *
 * @author www
 * @title: SysMenuController
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/25 2:20
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController{

    /**
     * 用户当前用户的菜单和权限信息
     *
     * @param principal 主要
     * @return {@link BaseResponse}
     */
    @GetMapping("/nav")
    public BaseResponse nav(Principal principal){
        // 先获取根据用户名获取 用户
        SysUser sysUser = sysUserService.getByUsername(principal.getName());
        // 获取权限信息  根据用户id 获取权限信息
        String authorityInfo = sysUserService.getUserAuthorityInfo(sysUser.getId());
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");
        // 获取导航栏信息
        List<SysMenuDto> navs = sysMenuService.getCurrentUserNav();
        return BaseResponse.succ(
                MapUtil.builder()
                        .put("authoritys",authorityInfoArray)
                        .put("nav",navs)
                .map());
    }

    /**
     * 信息
     *
     * @param id id
     * @return {@link BaseResponse}
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public BaseResponse info(@PathVariable("id") long id) {
        return BaseResponse.succ(sysMenuService.getById(id));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public BaseResponse list(){
        List<SysMenu> menus = sysMenuService.tree();
        return BaseResponse.succ(menus);
    }
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public BaseResponse save(@Validated @RequestBody SysMenu sysMenu){
        sysMenuService.save(sysMenu);
        return BaseResponse.succ(sysMenu);
    }
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public BaseResponse update(@Validated @RequestBody SysMenu sysMenu){
        sysMenuService.updateById(sysMenu);
        // 清除所有与菜单相关的权限缓存
        sysUserService.clearUserAuthorityInfoByMenuId(sysMenu.getId());
        return BaseResponse.succ(sysMenu);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public BaseResponse delete(@PathVariable("id")Long id){
        int count = sysMenuService.count(new QueryWrapper<SysMenu>().eq("parent_id", id));
        if (count > 0){
            return BaseResponse.fail("请先删除子菜单");
        }
        // 清除所有与该菜单相关的权限画出缓存
        sysUserService.clearUserAuthorityInfoByMenuId(id);
        sysMenuService.removeById(id);
        // 同步删除中间的关联表
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id",id));
        return BaseResponse.succ("");
    }
}
