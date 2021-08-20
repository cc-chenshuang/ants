package com.ants.modules.system.service.impl;

import com.ants.modules.system.entity.SysPermission;
import com.ants.modules.system.mapper.SysPermissionMapper;
import com.ants.modules.system.service.ISysPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public List<SysPermission> queryByUser(String username) {
        return this.sysPermissionMapper.queryByUser(username);
    }

}
