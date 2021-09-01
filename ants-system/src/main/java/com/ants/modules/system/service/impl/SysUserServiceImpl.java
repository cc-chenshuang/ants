package com.ants.modules.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ants.common.constant.CommonConstant;
import com.ants.common.system.result.Result;
import com.ants.modules.system.entity.SysUser;
import com.ants.modules.system.mapper.SysUserMapper;
import com.ants.modules.system.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * TODO
 * Author Chen
 * Date   2021/7/15 18:31
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    /**
     * 校验用户是否有效
     *
     * @param sysUser
     * @return
     */
    @Override
    public Result<JSONObject> checkUserIsEffective(SysUser sysUser) {
        Result<JSONObject> result = new Result<JSONObject>();
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
}
