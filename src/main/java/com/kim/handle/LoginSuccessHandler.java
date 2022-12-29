package com.kim.handle;

import cn.hutool.json.JSONUtil;
import com.kim.common.lang.BaseResponse;
import com.kim.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author www
 * @title: LoginSuccessHandler
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/27 2:25
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        //生成jwt ，并将放置到请求头中
        String jwt = jwtUtils.generateToken(authentication.getName());
        httpServletResponse.setHeader(jwtUtils.getHeader(), jwt);

        BaseResponse baseResponse = BaseResponse.succ("");
        outputStream.write(JSONUtil.toJsonStr(baseResponse).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
