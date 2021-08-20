package com.ants.modules.system.mapper;

import com.ants.modules.system.entity.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     *   根据用户查询用户权限
     */
    public List<SysPermission> queryByUser(@Param("username") String username);

}
