package com.kim.handle;

import cn.hutool.json.JSONUtil;
import com.kim.common.lang.BaseResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理器
 *
 * @author www
 * @title: LoginFailureHandler
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/27 2:17
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    /**
     * 在身份验证失败
     *
     * @param httpServletRequest  http servlet请求
     * @param httpServletResponse http servlet响应
     * @param e                   e
     * @throws IOException      ioexception
     * @throws ServletException servlet异常
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        BaseResponse baseResponse  = BaseResponse.fail("用户名或密码错误");
        outputStream.write(JSONUtil.toJsonStr(baseResponse).getBytes("UTF-8"));

        outputStream.flush();
        //关闭 流
        outputStream.close();
    }
}
