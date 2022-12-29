package com.kim.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kim.common.dto.SysMenuDto;
import com.kim.entity.SysMenu;
import com.kim.entity.SysUser;
import com.kim.mapper.SysMenuMapper;
import com.kim.mapper.SysUserMapper;
import com.kim.service.SysMenuService;
import com.kim.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author www
 * @title: SysMenuServiceImpl
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/25 2:05
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysUserMapper sysUserMapper;
    /**
     * 获得当前用户导航
     *
     * @return {@link List}<{@link SysMenuDto}>
     */
    @Override
    public List<SysMenuDto> getCurrentUserNav() {

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 获取用户信息
        SysUser sysUser = sysUserService.getByUsername(username);
        // 获取菜单信息
        List<Long> menuIds = sysUserMapper.getNavMenuIds(sysUser.getId());
        List<SysMenu> menus = this.listByIds(menuIds);
        // 菜单转而 树状结构
        List<SysMenu> menuTree = buildTreeMenu(menus);
        // 将实体转为Dto

        return convert(menuTree);
    }

    @Override
    public List<SysMenu> tree() {
        // 获取所有菜单信息
        List<SysMenu> sysMenus = this.list(new QueryWrapper<SysMenu>().orderByAsc("orderNum"));
        //转为树状结构
        return buildTreeMenu(sysMenus);
    }

    /**
     * 转换
     *
     * @param menuTree 菜单树
     * @return {@link List}<{@link SysMenuDto}>
     */
    private List<SysMenuDto> convert(List<SysMenu> menuTree) {
        ArrayList<SysMenuDto> menuDtos  = new ArrayList<>();
        menuTree.forEach( m -> {
            SysMenuDto sysMenuDto = new SysMenuDto();
            sysMenuDto.setId(m.getId());
            sysMenuDto.setName(m.getPerms());
            sysMenuDto.setTitle(m.getName());
            sysMenuDto.setComponent(m.getComponent());
            sysMenuDto.setPath(m.getPath());

            if (m.getChildren().size() > 0){
                // 子节点调用当前方法 进行再次转换   递归
                sysMenuDto.setChildren(convert(m.getChildren()));
            }
            menuDtos.add(sysMenuDto);
        });
        return menuDtos;
    }

    /**
     * 构建树菜单
     *
     * @param menus 菜单
     * @return {@link List}<{@link SysMenu}>
     */
    private List<SysMenu> buildTreeMenu(List<SysMenu> menus) {

        ArrayList<SysMenu> finalMenus = new ArrayList<>();
        // 先各自找各自的 子类
        for (SysMenu menu : menus){
            for (SysMenu e : menus) {
                // 找到他们的 父id
                if (menu.getId() == e.getParentId()){
                    menu.getChildren().add(e);
                }
            }
            // 判断是否为 顶级 菜单   提取父节点
            if (menu.getParentId() == 0L){
                finalMenus.add(menu);
            }
        }
        System.out.println(JSONUtil.toJsonStr(finalMenus));
        // 提取出父节点
        return finalMenus;
    }
}
