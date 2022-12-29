package com.kim.handle;

import com.kim.entity.SysUser;
import com.kim.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实现用户详情服务
 *
 * @author www
 * @title: UserDetailServiceImpl
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/28 0:28
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    SysUserService sysUserService;

    /**
     * 加载用户用户名
     *
     * @param username 用户名
     * @return {@link UserDetails}
     * @throws UsernameNotFoundException 用户名没有发现异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);
        if (sysUser == null) {
            throw  new UsernameNotFoundException("用户名或密码错误");
        }
        return new AccountUser(sysUser.getId(),sysUser.getUsername(),sysUser.getPassword(),getUserAuthority(sysUser.getId()));
    }

    /**
     * 获取用户权限(角色 和菜单权限)
     *
     * @param userId 用户id
     * @return {@link List}<{@link GrantedAuthority}>
     */
    public List<GrantedAuthority> getUserAuthority(Long userId){
        // 角色(ROLE_admin)、菜单操作权限 sys:user:list
        String authority  = sysUserService.getUserAuthorityInfo(userId);
        //逗号分隔的字符串到权限列表
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
