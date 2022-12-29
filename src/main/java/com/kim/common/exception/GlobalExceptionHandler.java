package com.kim.common.exception;

import com.kim.common.lang.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理程序
 *
 * @author www
 * @title: GlobalExceptionHandler
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/25 23:28
 */
@Slf4j
@RestControllerAdvice  /*  @ControllerAdvice  进行统一异常处理*/
public class GlobalExceptionHandler {

    /**
     * 处理实体校验异常
     *
     * @param e e
     * @return {@link BaseResponse}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)  // 异常处理器
    public BaseResponse handle(MethodArgumentNotValidException e) {

        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        log.error("实体校验异常：----------{}",objectError.getDefaultMessage());
        return  BaseResponse.fail(objectError.getDefaultMessage());
    }
    /**
     * 运行时异常
     *
     * @param e e
     * @return {@link BaseResponse}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value =RuntimeException.class)  // 异常处理器
    public BaseResponse handle(RuntimeException e) {
        log.error("运行时异常：----------{}",e.getMessage());
        return  BaseResponse.fail(e.getMessage());
    }


    /**
     * 不合法 异常
     *
     * @param e e
     * @return {@link BaseResponse}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public BaseResponse handle(IllegalArgumentException e){
        log.error("Assert异常：----------{}",e.getMessage());
        return BaseResponse.fail(e.getMessage());
    }
}
