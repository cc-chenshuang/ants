package com.ants.modules.system.service;

import com.ants.modules.system.entity.SysDepart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public interface ISysDepartService extends IService<SysDepart>{

    /**
     * 根据用户名查询部门
     *
     * @param username
     * @return
     */
    List<SysDepart> queryDepartsByUsername(String username);
}
