package com.kim.controller;

import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.kim.common.lang.BaseResponse;
import com.kim.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

/**
 * @author www
 * @title: AuthController
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/26 0:34
 */
@RestController
public class AuthController extends BaseController{
    @Autowired
    Producer producer;

    @GetMapping("/captcha")
    public BaseResponse captcha() throws IOException {
        // 使用UUID生成验证码的 key
        String key = UUID.randomUUID().toString();
        // 验证码的 values
        String code = producer.createText();

        // 测试
        key ="aaaaa";
        code="11111";

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();
        // 转为 stream 流
        ImageIO.write(image,"jpg",outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        //转为base64
        String str = "data:image/jpeg;base64,";
        //
        String base64Img = str + encoder.encode(outputStream.toByteArray());

        redisString.set(key,code,120);
        // 返回验证码 图片 和key
        return  BaseResponse.succ(MapUtil.builder()
                .put("token", key)
                .put("captchaImg", base64Img)
                .build());
    }

    /**
     * 获取用户信息
     *
     * @param principal 主要
     * @return {@link BaseResponse}
     */
    @GetMapping("/sys/userInfo")
    public BaseResponse userInfo(Principal principal){

        SysUser sysUser = sysUserService.getByUsername(principal.getName());
        return    BaseResponse.succ(MapUtil.builder()
        .put("id",sysUser.getId())
        .put("username",sysUser.getUsername())
        .put("avatar",sysUser.getAvatar())
        .put("created",sysUser.getCreated())
        .map());
    }
}
