package com.ants.modules.system.service.impl;

import com.ants.modules.system.entity.SysUserDepart;
import com.ants.modules.system.mapper.SysUserDepartMapper;
import com.ants.modules.system.model.DepartIdModel;
import com.ants.modules.system.service.ISysDepartService;
import com.ants.modules.system.service.ISysUserDepartService;
import com.ants.modules.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <P>
 * 用户部门表实现类
 * <p/>
 * @Author ZhiLin
 *@since 2019-02-22
 */
@Service
public class SysUserDepartServiceImpl extends ServiceImpl<SysUserDepartMapper, SysUserDepart> implements ISysUserDepartService {
	@Autowired
	private ISysDepartService sysDepartService;
	@Autowired
	private SysUserService sysUserService;

}
