package com.ants.modules.system.service;

import com.alibaba.fastjson.JSONObject;
import com.ants.common.system.result.Result;
import com.ants.modules.system.entity.SysUser;
import com.ants.modules.system.model.SysLoginModel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * TODO
 * Author Chen
 * Date   2021/7/15 18:30
 */
public interface SysUserService extends IService<SysUser> {
    Result<SysLoginModel> checkUserIsEffective(SysUser sysUser);

    void addUserWithRole(SysUser user, String selectedRoles);

    void addUserWithDepart(SysUser user, String selectedDeparts);

    void editUserWithRole(SysUser user, String roles);

    void editUserWithDepart(SysUser user, String departs);

    /**
     * 根据角色Id查询
     * @param
     * @return
     */
    public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username);

}
