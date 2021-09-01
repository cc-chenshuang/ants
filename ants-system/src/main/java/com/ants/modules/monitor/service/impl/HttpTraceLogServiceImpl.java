package com.ants.modules.monitor.service.impl;

import com.ants.modules.monitor.entity.HttpTraceLog;
import com.ants.modules.monitor.mapper.HttpTraceLogMapper;
import com.ants.modules.monitor.service.HttpTraceLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * TODO
 * Author Chen
 * Date   2021/3/31 17:25
 */
@Service
public class HttpTraceLogServiceImpl extends ServiceImpl<HttpTraceLogMapper, HttpTraceLog> implements HttpTraceLogService {
}
