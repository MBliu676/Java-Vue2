package com.kim.common.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * @author www
 * @title: BaseRequest
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/25 23:16
 */
@Data
public class BaseResponse implements Serializable {

    private Integer code; // 编码
    private String message; // 消息
    private Object data;   // 数据


    public static BaseResponse succ(Object data){
        return succ(200,"成功",data);
    }

    public static BaseResponse succ(int code, String msg, Object data) {
        BaseResponse r = new BaseResponse();
        r.setCode(code);
        r.setMessage(msg);
        r.setData(data);
        return r;
    }

    public static BaseResponse fail(String msg) {
        return fail(400, msg, null);
    }

    public static BaseResponse fail(int code, String msg, Object data) {
        BaseResponse r = new BaseResponse();
        r.setCode(code);
        r.setMessage(msg);
        r.setData(data);
        return r;
    }
}
