package com.kim.handle;

import cn.hutool.json.JSONUtil;
import com.kim.common.lang.BaseResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 认jwt证入口点
 *
 * @author www
 * @title: AuthenticationEntryPoint
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/27 23:40
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        BaseResponse baseResponse = BaseResponse.fail("请先登录");

        outputStream.write(JSONUtil.toJsonStr(baseResponse).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
