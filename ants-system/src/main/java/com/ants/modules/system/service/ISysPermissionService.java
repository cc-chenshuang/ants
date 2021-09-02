package com.ants.modules.system.service;

import com.ants.common.exception.AntsException;
import com.ants.modules.system.entity.SysPermission;
import com.ants.modules.system.model.TreeModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysPermissionService extends IService<SysPermission> {

    public List<TreeModel> queryListByParentId(String parentId);

    public List<SysPermission> queryByUser(String username);

    /**真实删除*/
    public void deletePermission(String id) throws AntsException;

    public void addPermission(SysPermission sysPermission) throws AntsException;

    public void editPermission(SysPermission sysPermission) throws AntsException;

}
