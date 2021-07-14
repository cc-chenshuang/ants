package com.ants.modules.log.service.impl;

import com.ants.modules.log.entity.HttpTraceLog;
import com.ants.modules.log.mapper.HttpTraceLogMapper;
import com.ants.modules.log.service.HttpTraceLogService;
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
