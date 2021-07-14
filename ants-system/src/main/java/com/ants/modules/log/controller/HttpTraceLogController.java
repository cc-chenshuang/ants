package com.ants.modules.log.controller;

import cn.hutool.core.date.DateUtil;
import com.ants.common.query.QueryGenerator;
import com.ants.common.result.Result;
import com.ants.modules.log.entity.HttpTraceLog;
import com.ants.modules.log.service.HttpTraceLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * TODO
 * Author Chen
 * Date   2021/3/31 17:25
 */
@Api(tags = "请求追踪-记录")
@RestController
@RequestMapping("/httpTraceLog")
public class HttpTraceLogController {
    @Autowired
    HttpTraceLogService httpTraceLogService;

    @GetMapping("/")
    public Result<?> get(HttpTraceLog httpTraceLog,
                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                         HttpServletRequest req){
        QueryWrapper<HttpTraceLog> qw = QueryGenerator.initQueryWrapper(httpTraceLog, req.getParameterMap());
        qw.orderByDesc("time");
        Page<HttpTraceLog> page = new Page<HttpTraceLog>(pageNo, pageSize);
        IPage<HttpTraceLog> pageList = httpTraceLogService.page(page, qw);
        return Result.ok(pageList);
    }
}
