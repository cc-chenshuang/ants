package com.ants.modules.system.service;

import com.ants.modules.system.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;


/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 删除角色
     * @param roleid
     * @return
     */
    public boolean deleteRole(String roleid);

    /**
     * 批量删除角色
     * @param roleids
     * @return
     */
    public boolean deleteBatchRole(String[] roleids);

}
