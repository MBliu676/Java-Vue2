package com.kim.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kim.entity.SysRole;

import java.util.List;

/**
 * @author www
 * @title: SysRoleService
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/25 2:02
 */
public interface SysRoleService extends IService<SysRole> {
    /**
     * 角色用户id列表
     *
     * @param userId id
     * @return {@link List}<{@link SysRole}>
     */
    List<SysRole> listRolesByUserId(Long userId);
}
