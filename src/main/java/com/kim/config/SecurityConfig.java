package com.kim.config;

import com.kim.filter.CaptchaFilter;
import com.kim.filter.JwtAuthenticationFilter;
import com.kim.handle.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author www
 * @title: SecurityConfig
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/26 1:15
 */
@Configuration
@EnableWebSecurity  //开启 安全策略
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    LoginFailureHandler loginFailureHandler; //登陆失败处理器
    @Autowired
    LoginSuccessHandler loginSuccessHandler; //登录成功处理器
    @Autowired
    CaptchaFilter captchaFilter; // 验证码 过滤器
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; //jwt认证入口
    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;  // jwt 异常处理
    @Autowired
    UserDetailServiceImpl userDetailService;  // 账号验证
    @Autowired
    JwtLogoutSuccessHandler jwtLogoutSuccessHandler;  // 退出成功处理器
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }


    /**
     * 密码编码器
     *
     * @return {@link BCryptPasswordEncoder}
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    private static final String [] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/favicon.io"
    };

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors()  //允许跨域
                .and().csrf().disable()      // 关闭 预防攻击
        // 登录配置
        .formLogin()
                .successHandler(loginSuccessHandler) // 成功处理器
                .failureHandler(loginFailureHandler)  // 失败处理器
                .and()
                .logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler) // 退出处理器

        // 禁用session
        .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 配置session生成规则
        // 配置拦截规则
        .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()  // 放行白名单
        .anyRequest().authenticated()   //其余都需要 登录
        // 异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)  //入口
                .accessDeniedHandler(jwtAccessDeniedHandler)

        // 配置自定义过滤器
        .and()
                .addFilter(jwtAuthenticationFilter())
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class); //配置 验证码验证 在用户名密码之前
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }
}
