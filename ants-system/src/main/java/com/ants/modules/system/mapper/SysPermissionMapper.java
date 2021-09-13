package com.ants.modules.system.mapper;

import com.ants.modules.system.entity.SysPermission;
import com.ants.modules.system.model.TreeModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 通过父菜单ID查询子菜单
     * @param parentId
     * @return
     */
    public List<TreeModel> queryListByParentId(@Param("parentId") String parentId);

    /**
     *   根据用户查询用户权限
     */
    public List<SysPermission> queryByUser(@Param("username") String username);

    /**
     *   修改菜单状态字段： 是否子节点
     */
    @Update("update sys_permission set is_leaf=#{leaf} where id = #{id}")
    public int setMenuLeaf(@Param("id") String id, @Param("leaf") int leaf);

}
