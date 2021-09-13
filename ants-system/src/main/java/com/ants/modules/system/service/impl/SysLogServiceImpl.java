package com.ants.modules.system.service.impl;

import com.ants.modules.system.entity.SysLog;
import com.ants.modules.system.mapper.SysLogMapper;
import com.ants.modules.system.service.SysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TODO
 * Author Chen
 * Date   2021/2/2 9:59
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    /**
     * @功能：清空所有日志记录
     */
    @Override
    public void removeAll() {
        sysLogMapper.removeAll();
    }
}
