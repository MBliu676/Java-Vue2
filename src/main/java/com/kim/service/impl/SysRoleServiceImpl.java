package com.kim.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kim.entity.SysRole;
import com.kim.mapper.SysRoleMapper;
import com.kim.service.SysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author www
 * @title: SysRoleServiceImpl
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/25 2:07
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Override
    public List<SysRole> listRolesByUserId(Long userId) {

        List<SysRole> sysRoles = this.list(new QueryWrapper<SysRole>()
                .inSql("id", "select role_id from sys_user_role where user_id =" + userId));
        return sysRoles;
    }
}
