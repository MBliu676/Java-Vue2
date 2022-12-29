package com.kim.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author www
 * @title: PassDto
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/30 4:26
 */
@Data
public class PassDto implements Serializable {

    @NotBlank(message = "新密码不能为空")
    private String password;
    @NotBlank(message = "旧密码不能为空")
    private String currentPass;
}
