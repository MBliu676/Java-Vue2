package com.kim.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**  
    * @title: SysRole
    * @projectName VueAdmin-java
    * @description: 
    * @author www
    * @date 2022/12/25 1:49
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRole  implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @NotBlank(message = "角色名称不能为空")
    private String name;
    @NotBlank(message = "角色编码不能为空")
    private String code;

    /**
    * 备注
    */
    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime created;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updated;

    private Integer statu;

    @TableField(exist = false)
    private List<Long> menuIds  = new ArrayList<>();
}