package com.ants.modules.system.service;

import com.alibaba.fastjson.JSONObject;
import com.ants.common.system.result.Result;
import com.ants.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * TODO
 * Author Chen
 * Date   2021/7/15 18:30
 */
public interface SysUserService extends IService<SysUser> {
    Result<JSONObject> checkUserIsEffective(SysUser sysUser);
}
