package com.kim.handle;

import cn.hutool.json.JSONUtil;
import com.kim.common.lang.BaseResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt拒绝访问处理程序
 *
 * @author www
 * @title: JwtAccessDeniedHandler
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/27 23:42
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        BaseResponse baseResponse = BaseResponse.fail(e.getMessage());
        outputStream.write(JSONUtil.toJsonStr(baseResponse).getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
    }
}
