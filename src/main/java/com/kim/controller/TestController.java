package com.kim.controller;

import com.kim.common.lang.BaseResponse;
import com.kim.entity.SysUser;
import com.kim.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author www
 * @title: TestController
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/28 1:08
 */
@RestController
public class TestController {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 测试数据
     *
     * @return {@link Object}
     */
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/findAll")
    public BaseResponse test(){
        List<SysUser> list = sysUserService.list();
        return BaseResponse.succ(200,"查询成功",list);
    }

    /**
     * 生成加密后的密码
     *
     * @return {@link BaseResponse}
     */
    @GetMapping("/test/pass")
    public BaseResponse pass(){
        // 加密后的密码
        String password = bCryptPasswordEncoder.encode("123456");
        System.out.println("加密的密码"+password);
        boolean matches = bCryptPasswordEncoder.matches("123456", password);
        System.out.println("匹配结果" + matches);
        return BaseResponse.succ(200,"成功",password);
    }
}
