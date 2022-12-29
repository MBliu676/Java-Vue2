package com.kim.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kim.common.lang.BaseResponse;
import com.kim.common.lang.Const;
import com.kim.entity.SysRole;
import com.kim.entity.SysRoleMenu;
import com.kim.entity.SysUserRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author www
 * @title: SysRoleController
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/25 2:21
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController  extends BaseController{

    /**
     * 根据id查询
     *
     * @param id id
     * @return {@link BaseResponse}
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public BaseResponse info(@PathVariable("id")Long id){
        //查询角色表
        SysRole sysRole = sysRoleService.getById(id);
        // 查询角色与菜单的关联表
        List<SysRoleMenu> roleMenus = sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        // 获取角色相关联的菜单id
        List<Long> menuIds = roleMenus.stream().map(p -> p.getMenuId()).collect(Collectors.toList());

        sysRole.setMenuIds(menuIds);
        return BaseResponse.succ(sysRole);
    }

    /**
     * 根据名称查询
     *
     * @param name 名字
     * @return {@link BaseResponse}
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public BaseResponse list(String name){

        Page<SysRole> pageData = sysRoleService.page(getPage(),
                new QueryWrapper<SysRole>().like(StrUtil.isNotBlank(name), "name", name));
        return BaseResponse.succ(pageData);
    }

    /**
     * 添加
     *
     * @param sysRole 系统作用
     * @return {@link BaseResponse}
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public BaseResponse save(@Validated @RequestBody SysRole sysRole){
        sysRole.setStatu(Const.STATUS_OFF);
        sysRoleService.save(sysRole);
        return BaseResponse.succ(sysRole);
    }

    /**
     * 修改
     *
     * @param sysRole 系统作用
     * @return {@link BaseResponse}
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public BaseResponse update(@Validated @RequestBody SysRole sysRole){
        sysRoleService.updateById(sysRole);
        // 更新缓存
        sysUserService.clearUserAuthorityInfoByRoleId(sysRole.getId());
        return BaseResponse.succ(sysRole);
    }

    /**
     * 删除 或 批量删除
     *
     * @param ids 菜单id
     * @return {@link BaseResponse}
     */
    @Transactional
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public BaseResponse delete(@RequestBody Long [] ids){
        sysRoleService.removeByIds(Arrays.asList(ids));
        //删除中间关联表
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id",ids));
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id",ids));
        // 缓存同步删除
        Arrays.stream(ids).forEach(id -> {
            // 跟新缓存
            sysUserService.clearUserAuthorityInfoByRoleId(id);
        });
        return BaseResponse.succ("删除成功");
    }

    /**
     * 分配权限
     *
     * @param roleId  角色id
     * @param menuIds 菜单id
     * @return {@link BaseResponse}
     */
    @Transactional
    @PostMapping("/perm/{roleId}")
    @PreAuthorize("hasAuthority('sys:role:perm')")
    public BaseResponse perm(@PathVariable("roleId") Long roleId,@RequestBody Long [] menuIds){
        ArrayList<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId -> {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(roleId);

            sysRoleMenus.add(sysRoleMenu);
        });

        // 先删除原来的记录 ，在重新保存新的记录
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id",roleId));
        sysRoleMenuService.saveBatch(sysRoleMenus);
        // 删除缓存
        sysUserService.clearUserAuthorityInfoByRoleId(roleId);

        return BaseResponse.succ(menuIds);
    }
}
