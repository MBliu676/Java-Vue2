package com.kim.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kim.common.dto.SysMenuDto;
import com.kim.entity.SysMenu;

import java.util.List;

/**
 * @author www
 * @title: SysMenuService
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/25 2:01
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获得当前用户导航
     *
     * @return {@link List}<{@link SysMenuDto}>
     */
    List<SysMenuDto> getCurrentUserNav();

    List<SysMenu> tree();

}
