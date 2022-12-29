package com.kim.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kim.common.exception.CaptchaException;
import com.kim.handle.LoginFailureHandler;
import com.kim.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码过滤器
 *
 * @author www
 * @title: CaptchaFilter
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/27 2:35
 */
@Component
@Slf4j
public class CaptchaFilter extends OncePerRequestFilter {
    @Autowired
    LoginFailureHandler loginFailureHandler;
    @Autowired
    RedisUtil.redisString redisString;
    @Autowired
    RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String url = httpServletRequest.getRequestURI();
        // 验证是否为 登录路径 和 是否为post请求
        if ("/login".equals(url) && httpServletRequest.getMethod().equals("POST")){

            try{
                //校验验证码
                validate(httpServletRequest);
            }catch (CaptchaException e){
                // 认证失败处理器
                loginFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void validate(HttpServletRequest httpServletRequest){
        String code = httpServletRequest.getParameter("code");
        String key = httpServletRequest.getParameter("token");

        log.info("code:{}", code);
        log.info("key:{}", key);
        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)){
            // 自定义 异常
            throw new CaptchaException("验证码异常");
        }
        // 判断 redis中的key 是否相同
        if (!code.equals(redisString.get(key))){
            throw new CaptchaException("验证码错误");
        }
         // 删除redis的 key
        redisUtil.del(key);
    }
}
