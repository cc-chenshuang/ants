package com.ants.modules.system.controller;

import com.ants.common.system.result.Result;
import com.ants.modules.system.entity.MailConfig;
import com.ants.modules.system.service.MailConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * TODO
 * Author Chen
 * Date   2021/3/5 16:39
 */
@Api(tags = "邮件工具-配置")
@RestController
@RequestMapping("/mail/config")
public class MailConfigController {

    @Autowired
    MailConfigService mailConfigService;

    @ApiOperation("邮件配置")
    @PostMapping("/send")
    public Result sendMail(@RequestBody MailConfig mailConfig) {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        mailConfig.setIp(ip);
        boolean save = mailConfigService.saveOrUpdate(mailConfig);
        if (save) {
            return Result.ok(mailConfig);
        }
        return Result.error("配置失败！");
    }

    @ApiOperation("获取邮件配置")
    @GetMapping("/getInfo")
    public Result<?> getConfigMail() {
        MailConfig mailConfig = mailConfigService.list().get(0);
        return Result.ok(mailConfig);
    }
}
