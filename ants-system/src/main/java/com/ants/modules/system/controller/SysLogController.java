package com.ants.modules.system.controller;

import cn.hutool.core.date.DateUtil;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.modules.system.entity.SysLog;
import com.ants.modules.system.service.SysLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO
 * Author Chen
 * Date   2021/2/9 13:47
 */
@Slf4j
@Api(tags = "系统日志")
@RestController
@RequestMapping("/sysLog")
public class SysLogController {
    @Autowired
    SysLogService sysLogService;

    @ApiOperation("获取日志列表")
    @GetMapping("/get")
    public Result get(SysLog sysLog,
                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                      @RequestParam(name = "endTime") String endTime,
                      @RequestParam(name = "startTime") String startTime,
                      HttpServletRequest req) {
        log.info(endTime);
        log.info(startTime);
        QueryWrapper<SysLog> qw = QueryGenerator.initQueryWrapper(sysLog, req.getParameterMap());
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
            qw.between("create_time", startTime, DateUtil.formatDate(DateUtil.offsetDay(DateUtil.parseDate(endTime), 1)));
        }
        Page<SysLog> page = new Page<SysLog>(pageNo, pageSize);
        IPage<SysLog> pageList = sysLogService.page(page, qw);
        return Result.ok(pageList);
    }

}
