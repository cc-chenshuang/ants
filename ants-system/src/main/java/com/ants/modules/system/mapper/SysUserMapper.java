package com.ants.modules.system.mapper;

import com.ants.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * @Author scott
     * @Date 2019/12/13 16:10
     * @Description: 批量删除角色与用户关系
     */
    void deleteBathRoleUserRelation(@Param("roleIdArray") String[] roleIdArray);

    /**
     * @Author scott
     * @Date 2019/12/13 16:10
     * @Description: 批量删除角色与权限关系
     */
    void deleteBathRolePermissionRelation(@Param("roleIdArray") String[] roleIdArray);

    /**
     * 根据角色Id查询用户信息
     * @param page
     * @param
     * @return
     */
    IPage<SysUser> getUserByRoleId(Page page, @Param("roleId") String roleId, @Param("username") String username);
}
