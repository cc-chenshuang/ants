package com.ants.modules.system.controller;

import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.modules.system.entity.SendMailHistory;
import com.ants.modules.system.service.SendMailHistoryService;
import com.ants.modules.system.vo.SendMailVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * TODO 邮件工具-发送记录
 * Author Chen
 * Date   2021/3/5 13:34
 */
@Api(tags = "邮件工具-发送记录")
@RestController
@RequestMapping("/mail/tool")
@Slf4j
public class MailToolController {

    @Autowired
    SendMailHistoryService sendMailHistoryService;

    @ApiOperation("发送邮件")
    @PostMapping("/")
    public Result sendMail(@RequestBody SendMailVo sendMailVo) {
        boolean b = sendMailHistoryService.sendMail(sendMailVo);
        if (b){
            log.info("邮件发送成功！");
            return Result.ok("发送成功！");
        }
        log.error("邮件发送失败！");
        return Result.error("发送失败！");
    }

    @ApiOperation("获取邮件发送记录")
    @GetMapping("/")
    public Result<?> get(SendMailHistory sendMailHistory,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SendMailHistory> qw = QueryGenerator.initQueryWrapper(sendMailHistory, req.getParameterMap());
        qw.orderByDesc("create_time");
        Page<SendMailHistory> page = new Page<SendMailHistory>(pageNo, pageSize);
        IPage<SendMailHistory> pageList = sendMailHistoryService.page(page, qw);
        return Result.ok(pageList);
    }

    @ApiOperation("删除邮件发送记录")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable("id") String id) {
        boolean b = sendMailHistoryService.removeById(id);
        if (b){
            return Result.ok("删除成功！");
        }
        return Result.error("删除失败！");
    }
}
