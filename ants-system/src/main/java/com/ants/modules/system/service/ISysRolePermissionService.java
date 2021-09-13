package com.ants.modules.system.service;

import com.ants.modules.system.entity.SysRolePermission;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {

	/**
	 * 保存授权/先删后增
	 * @param roleId
	 * @param permissionIds
	 */
	public void saveRolePermission(String roleId, String permissionIds);

	/**
	 * 保存授权 将上次的权限和这次作比较 差异处理提高效率
	 * @param roleId
	 * @param permissionIds
	 * @param lastPermissionIds
	 */
	public void saveRolePermission(String roleId, String permissionIds, String lastPermissionIds);

}
