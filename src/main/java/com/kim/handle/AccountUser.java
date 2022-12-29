package com.kim.handle;

import cn.hutool.core.lang.Assert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 账户用户
 *
 * @author www
 * @title: AccountUser
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/28 0:47
 */

public class AccountUser implements UserDetails {

    private Long userId;

    private String password;

    private final String username;

    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean accountNonExpired;

    private final boolean accountNonLocked;

    private final boolean credentialsNonExpired;

    private final boolean enabled;

    /**
     * 账户用户
     *
     * @param userId      用户id
     * @param username    用户名
     * @param password    密码
     * @param authorities 当局
     */
    public AccountUser(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(userId, username, password, true, true, true, true, authorities);
    }


    /**
     * 账户用户
     *
     * @param userId                用户id
     * @param username              用户名
     * @param password              密码
     * @param enabled               启用
     * @param accountNonExpired     账户不过期
     * @param credentialsNonExpired 凭证不过期
     * @param accountNonLocked      非锁定账户
     * @param authorities           当局
     */
    public AccountUser(Long userId, String username, String password, boolean enabled, boolean accountNonExpired,
                       boolean credentialsNonExpired, boolean accountNonLocked,
                       Collection<? extends GrantedAuthority> authorities) {
        Assert.isTrue(username != null && !"".equals(username) && password != null,
                "Cannot pass null or empty values to constructor");
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
    }
    /**
     * 获取权限
     *
     * @return {@link Collection}<{@link ?} {@link extends} {@link GrantedAuthority}>
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    /**
     * 得到密码
     *
     * @return {@link String}
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * 获得用户名
     *
     * @return {@link String}
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * 是账户非过期
     *
     * @return boolean
     */
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    /**
     * 是账户非锁定
     *
     * @return boolean
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    /**
     * 是凭证不过期
     *
     * @return boolean
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    /**
     * 启用了
     *
     * @return boolean
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
