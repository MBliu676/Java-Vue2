package com.kim.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kim.common.dto.PassDto;
import com.kim.common.lang.BaseResponse;
import com.kim.common.lang.Const;
import com.kim.entity.SysRole;
import com.kim.entity.SysUser;
import com.kim.entity.SysUserRole;
import com.kim.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author www
 * @title: SysUserController
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/25 2:22
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController{
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 信息
     *
     * @param id id
     * @return {@link BaseResponse}
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public BaseResponse info(@PathVariable("id") Long id){
        SysUser sysUser = sysUserService.getById(id);

        Assert.notNull(sysUser,"找不到该管理员");
        List<SysRole> roles  = sysRoleService.listRolesByUserId(id);
        sysUser.setSysRoles(roles);

        return BaseResponse.succ(sysUser);
    }

    /**
     * 查询 分页查询 及 根据名称查询
     *
     * @param username 用户名
     * @return {@link BaseResponse}
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public BaseResponse list(String username){
        Page<SysUser> pageData = sysUserService.page(getPage(), new QueryWrapper<SysUser>()
                .like(StrUtil.isNotBlank(username), "username", username));

        pageData.getRecords().forEach(p -> {
            p.setSysRoles(sysRoleService.listRolesByUserId(p.getId()));
        });
        return BaseResponse.succ(pageData);
    }

    /**
     * 添加
     *
     * @param sysUser 系统用户
     * @return {@link BaseResponse}
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public BaseResponse save(@Validated @RequestBody SysUser sysUser){
        sysUser.setStatu(Const.STATUS_OFF);
        // 默认密码
        String password = bCryptPasswordEncoder.encode(Const.DEFULT_PASSWORD);
        sysUser.setPassword(password);
        // 默认头像
        sysUser.setAvatar(Const.DEFULT_AVATAR);
        sysUserService.save(sysUser);
        return BaseResponse.succ(sysUser);
    }

    /**
     * 更新
     *
     * @param sysUser 系统用户
     * @return {@link BaseResponse}
     */
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public BaseResponse update(@Validated @RequestBody SysUser sysUser){
        sysUserService.updateById(sysUser);
        return BaseResponse.succ(sysUser);
    }

    /**
     * 删除 或批量删除
     *
     * @param ids id
     * @return {@link BaseResponse}
     */
    @Transactional
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public BaseResponse delete(@RequestBody Long [] ids){
        sysUserService.removeByIds(Arrays.asList(ids));
        //删除中间关联表
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id",ids));
        return BaseResponse.succ("删除成功");
    }

    /**
     * 角色权限
     *
     * @param userId  用户id
     * @param roleIds 角色id
     * @return {@link BaseResponse}
     */
    @Transactional
    @PostMapping("role/{userId}")
    @PreAuthorize("hasAuthority('sys:user:role')")
    public BaseResponse rolePerm(@PathVariable("userId")Long userId,@RequestBody Long [] roleIds){
        ArrayList<SysUserRole> sysUserRoles = new ArrayList<>();
        Arrays.stream(roleIds).forEach(r -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(r);
            sysUserRole.setUserId(userId);
            sysUserRoles.add(sysUserRole);
        });
        // 先删除原来的记录 ，在重新保存新的记录
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id",userId));
        sysUserRoleService.saveBatch(sysUserRoles);
        //删除缓存
        SysUser sysUser = sysUserService.getById(userId);
        sysUserService.clearUserAuthorityInfo(sysUser.getUsername());

        return BaseResponse.succ("分配成功");
    }

    /**
     * 重置密码
     *
     * @param userId 用户id
     * @return {@link BaseResponse}
     */
    @PostMapping("/repass")
    @PreAuthorize("hasAuthority('sys:user:repass')")
    public BaseResponse repass(@RequestBody Long userId){
        SysUser sysUser = sysUserService.getById(userId);
        sysUser.setPassword(bCryptPasswordEncoder.encode(Const.DEFULT_PASSWORD));
        sysUserService.updateById(sysUser);
        return BaseResponse.succ("重置密码成功");
    }


    @PostMapping("/updatePass")
    public BaseResponse updatePass(@Validated @RequestBody PassDto passDto, Principal principal){
        SysUser sysUser = sysUserService.getByUsername(principal.getName());
        //判断 旧密码 是否正确
        boolean matches = bCryptPasswordEncoder.matches(passDto.getCurrentPass(), sysUser.getPassword());
        if (!matches) {
            return BaseResponse.fail("旧密码错误");
        }
        //设置新密码
        sysUser.setPassword(bCryptPasswordEncoder.encode(passDto.getPassword()));
        sysUserService.updateById(sysUser);

        return BaseResponse.succ("修改密码成功");
    }
}
