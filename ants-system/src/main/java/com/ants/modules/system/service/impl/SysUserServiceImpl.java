package com.ants.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ants.common.constant.CacheConstant;
import com.ants.common.constant.CommonConstant;
import com.ants.common.system.result.Result;
import com.ants.common.utils.oConvertUtils;
import com.ants.modules.system.entity.SysUser;
import com.ants.modules.system.entity.SysUserDepart;
import com.ants.modules.system.entity.SysUserRole;
import com.ants.modules.system.mapper.*;
import com.ants.modules.system.model.SysLoginModel;
import com.ants.modules.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO
 * Author Chen
 * Date   2021/7/15 18:31
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserDepartMapper sysUserDepartMapper;

    /**
     * 校验用户是否有效
     *
     * @param sysUser
     * @return
     */
    @Override
    public Result<SysLoginModel> checkUserIsEffective(SysUser sysUser) {
        Result<SysLoginModel> result = new Result<SysLoginModel>();
        //情况1：根据用户信息查询，该用户不存在
        if (sysUser == null) {
            result.error500("该用户不存在，请注册");
//            sysBaseAPI.addLog("用户登录失败，用户不存在！", CommonConstant.LOG_TYPE_1, null, null, null, null);
            return result;
        }
        //情况2：根据用户信息查询，该用户已注销
        //update-begin---author:王帅   Date:20200601  for：if条件永远为falsebug------------
        if (CommonConstant.DEL_FLAG_1 == sysUser.getDelFlag()) {
            //update-end---author:王帅   Date:20200601  for：if条件永远为falsebug------------
//            sysBaseAPI.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已注销！", CommonConstant.LOG_TYPE_1, null, sysUser.getUsername(), sysUser.getRealname(), sysUser.getOrgCode());
            result.error500("该用户已注销");
            return result;
        }
        //情况3：根据用户信息查询，该用户已冻结
        if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
//            sysBaseAPI.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_1, null, sysUser.getUsername(), sysUser.getRealname(), sysUser.getOrgCode());
            result.error500("该用户已冻结");
            return result;
        }
        return result;
    }

    @Override
    @Transactional
    public void addUserWithRole(SysUser user, String roles) {
        this.save(user);
        if (oConvertUtils.isNotEmpty(roles)) {
            String[] arr = roles.split(",");
            for (String roleId : arr) {
                SysUserRole userRole = new SysUserRole(user.getId(), roleId);
                sysUserRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    @Transactional
    public void editUserWithRole(SysUser user, String roles) {
        this.updateById(user);
        //先删后加
//        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
//        if (oConvertUtils.isNotEmpty(roles)) {
//            String[] arr = roles.split(",");
//            for (String roleId : arr) {
//                SysUserRole userRole = new SysUserRole(user.getId(), roleId);
//                sysUserRoleMapper.insert(userRole);
//            }
//        }
    }

    @Override
    @Transactional
    public void addUserWithDepart(SysUser user, String selectedParts) {
//		this.save(user);  //保存角色的时候已经添加过一次了
        if (oConvertUtils.isNotEmpty(selectedParts)) {
            String[] arr = selectedParts.split(",");
            for (String deaprtId : arr) {
                SysUserDepart userDeaprt = new SysUserDepart(user.getId(), deaprtId);
                sysUserDepartMapper.insert(userDeaprt);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {CacheConstant.SYS_USERS_CACHE}, allEntries = true)
    public void editUserWithDepart(SysUser user, String departs) {
        this.updateById(user);  //更新角色的时候已经更新了一次了，可以再跟新一次
        String[] arr = {};
        if (oConvertUtils.isNotEmpty(departs)) {
            arr = departs.split(",");
        }
        //查询已关联部门
        List<SysUserDepart> userDepartList = sysUserDepartMapper.selectList(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
        //先删后加
        sysUserDepartMapper.delete(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
        if (oConvertUtils.isNotEmpty(departs)) {
            for (String departId : arr) {
                SysUserDepart userDepart = new SysUserDepart(user.getId(), departId);
                sysUserDepartMapper.insert(userDepart);
            }
        }
    }

}
