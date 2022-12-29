package com.kim.filter;

import cn.hutool.core.util.StrUtil;
import com.kim.entity.SysUser;
import com.kim.handle.UserDetailServiceImpl;
import com.kim.service.SysUserService;
import com.kim.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt身份验证过滤器
 *
 * @author www
 * @title: JwtAuthenticationFilter
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/27 21:57
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserDetailServiceImpl userDetailService;
    @Autowired
    SysUserService sysUserService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 做过滤器内部
     *
     * @param request  请求
     * @param response 响应
     * @param chain    链
     * @throws IOException      ioexception
     * @throws ServletException servlet异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwt = request.getHeader(jwtUtils.getHeader());
        // 没有jwt 返回
        if (StrUtil.isBlankOrUndefined(jwt)){
            chain.doFilter(request,response);
            return;
        }
        Claims claim = jwtUtils.getClaimByToken(jwt);
        if (chain == null){
            throw  new JwtException("token 异常");
        }
        if(jwtUtils.isTokenExpired(claim)){
            throw  new JwtException("token 已过期");
        }

        String username = claim.getSubject();


        SysUser sysUser = sysUserService.getByUsername(username);
        // 获取用户的权限信息
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username,null, userDetailService.getUserAuthority(sysUser.getId()));

        SecurityContextHolder.getContext().setAuthentication(token);

        chain.doFilter(request,response);
    }
}
