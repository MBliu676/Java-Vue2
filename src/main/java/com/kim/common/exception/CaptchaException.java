package com.kim.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常
 *
 * @author www
 * @title: CaptchaException
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/27 2:50
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg) {
        super(msg);
    }
}
