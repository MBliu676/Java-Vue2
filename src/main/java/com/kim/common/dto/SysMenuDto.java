package com.kim.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author www
 * @title: SysMenuDto
 * @projectName VueAdmin-java
 * @description:
 * @date 2022/12/28 22:30
 */
@Data
public class SysMenuDto implements Serializable {

    private Long id;
    private String name;
    private String title;
    private String icon;
    private String path;
    private String component;
    private List<SysMenuDto> children = new ArrayList<>();
}
