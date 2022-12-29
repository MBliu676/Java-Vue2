package com.kim.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kim.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**  
    * @title: SysUserMapper
    * @projectName VueAdmin-java
    * @description: 
    * @author www
    * @date 2022/12/25 1:49
    */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * ids得到导航菜单
     *
     * @param userId 用户id
     * @return {@link List}<{@link Long}>
     */
    List<Long> getNavMenuIds(Long userId);

    List<SysUser> listByMenuId(Long menuId);
}