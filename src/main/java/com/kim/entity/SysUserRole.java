package com.kim.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**  
    * @title: SysUserRole
    * @projectName VueAdmin-java
    * @description: 
    * @author www
    * @date 2022/12/25 1:49
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRole  implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long roleId;
}