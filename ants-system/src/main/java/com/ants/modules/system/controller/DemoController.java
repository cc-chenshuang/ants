package com.ants.modules.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.ants.common.annotation.AutoLog;
import com.ants.common.result.Result;
import com.ants.common.utils.*;
import com.ants.modules.system.entity.Demo;
import com.ants.modules.system.model.SysLoginModel;
import com.ants.modules.system.service.DemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "集成MyBatisPlus测试接口")
@RestController
@RequestMapping("/sys")
public class DemoController {
    private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";


    @Autowired
    DemoService demoService;
    @Autowired
    RedisUtil redisUtil;

    @ApiOperation("/获取所有数据")
    @AutoLog("获取所有数据")
    @GetMapping("/list")
    public Result getList() {
        redisUtil.set("CE_SHI1", "测试数据");
        Object ce_shi = redisUtil.get("CE_SHI1");
        System.out.println(ce_shi.toString());
        List<Demo> list = demoService.list();
        return Result.ok(list);
    }


    @PostMapping("/login")
    @ApiOperation("/login")
    public Result login(@RequestBody SysLoginModel sysLoginModel) {
        StpUtil.login(sysLoginModel.getUsername());
        String token = StpUtil.getTokenValue();
        sysLoginModel.setToken(token);
        return Result.ok(sysLoginModel);
    }

    @GetMapping("/info")
    @ApiOperation("/info")
    public Result info(HttpServletRequest request) {
        String token = request.getHeader("X-Token");
        SysLoginModel sysLoginModel = new SysLoginModel();
        sysLoginModel.setToken(token);
        // 查询所有账号Session会话
        List<String> list = StpUtil.searchSessionId("", 0, 10);
        // 查询所有token
        List<String> list1 = StpUtil.searchTokenValue("", 0, 10);

        // 查询所有令牌Session会话
        List<String> list2 = StpUtil.searchTokenSessionId("", 0, 10);
        Map<String, Object> map = new HashMap<>();
        map.put("sysLoginModel",sysLoginModel);
        map.put("roles",new String[]{"admin"});
        return Result.ok(sysLoginModel);
    }

    @GetMapping("/getOnLineUser")
    @ApiOperation("/getOnLineUser")
    public Result getOnLineUser() {
        // 查询所有账号Session会话
        List<String> list = StpUtil.searchSessionId("Ants-Token", 1, 10);
        return Result.ok(list);
    }


    @PostMapping("/logout")
    @ApiOperation("/logout")
    public Result logout() {
        StpUtil.logout();
        return Result.ok();
    }

    /**
     * 后台生成图形验证码 ：有效
     *
     * @param response
     * @param key
     */
    @ApiOperation("获取验证码")
    @GetMapping(value = "/randomImage/{key}")
    public Result<String> randomImage(HttpServletResponse response, @PathVariable String key) {
        Result<String> res = new Result<String>();
        try {
            String code = RandomUtil.randomString(BASE_CHECK_CODES, 4);
            String lowerCaseCode = code.toLowerCase();
            String realKey = MD5Util.MD5Encode(lowerCaseCode + key, "utf-8");
            redisUtil.set(realKey, lowerCaseCode, 60);
            String base64 = RandImageUtil.generate(code);
            res.setSuccess(true);
            res.setData(base64);
        } catch (Exception e) {
            res.error500("获取验证码出错" + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }


}
