package com.kim.handle;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.kim.common.lang.BaseResponse;
import com.kim.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt注销成功处理程序
 *
 * @author www
 * @title: JwtLogoutSuccessHandle
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/28 3:13
 */
@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    JwtUtils jwtUtils;
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(httpServletRequest,httpServletResponse,authentication);
        }

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        httpServletResponse.setHeader(jwtUtils.getHeader(),"");
        BaseResponse baseResponse = BaseResponse.succ("登出成功");
        outputStream.write(JSONUtil.toJsonStr(baseResponse).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();

    }
}
