package com.kim.test;

import com.kim.entity.SysUser;
import com.kim.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

/**
 * @author www
 * @title: SysUserServiceImplTest
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/25 2:12
 */
@SpringBootTest
public class SysUserServiceImplTest {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void pass(){
        String password = bCryptPasswordEncoder.encode("123456");
        System.out.println("加密的密码"+password);
    }
}
