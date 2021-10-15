package com.ants.modules.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ants.common.constant.CommonConstant;
import com.ants.common.system.result.Result;
import com.ants.common.utils.*;
import com.ants.modules.system.entity.SysUser;
import com.ants.modules.system.model.SysLoginModel;
import com.ants.modules.system.service.ISysDictService;
import com.ants.modules.system.service.SysLogService;
import com.ants.modules.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "用户登录")
@Slf4j
public class LoginController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysLogService logService;
    @Autowired
    private RedisUtil redisUtil;

    private ISysDictService sysDictService;

    private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    @ApiOperation("登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<SysLoginModel> login(@RequestBody SysLoginModel sysLoginModel) {
        Result<SysLoginModel> result = new Result<SysLoginModel>();
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        //前端密码加密，后端进行密码解密
        //password = AesEncryptUtil.desEncrypt(sysLoginModel.getPassword().replaceAll("%2B", "\\+")).trim();//密码解密

//        String captcha = sysLoginModel.getCaptcha();
//        if(captcha==null){
//            result.error500("验证码无效");
//            return result;
//        }
//        String lowerCaseCaptcha = captcha.toLowerCase();
//		String realKey = MD5Util.MD5Encode(lowerCaseCaptcha+sysLoginModel.getCheckKey(), "utf-8");
//		Object checkCode = redisUtil.get(realKey);
//		if(checkCode==null || !checkCode.equals(lowerCaseCaptcha)) {
//			result.error500("验证码错误");
//			return result;
//		}

        //1. 校验用户是否有效
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (!result.isSuccess()) {
            return result;
        }

        //2. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
            return result;
        }
        StpUtil.login(sysLoginModel.getUsername());
        String token = StpUtil.getTokenValue();
        sysLoginModel.setToken(token);
        result.setCode(200);
        result.setResult(sysLoginModel);
        return result;
    }

    @GetMapping("/info")
    @ApiOperation("/info")
    public Result info(HttpServletRequest request) {
        SysLoginModel sysLoginModel = new SysLoginModel();
        // 查询所有账号Session会话
        List<String> list = StpUtil.searchSessionId("", 0, 10);
        // 查询所有token
        List<String> list1 = StpUtil.searchTokenValue("", 0, 10);

        // 查询所有令牌Session会话
        List<String> list2 = StpUtil.searchTokenSessionId("", 0, 10);
        Object loginId = StpUtil.getLoginId();
        LambdaQueryWrapper<SysUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SysUser::getUsername, loginId);
        SysUser sysUser = sysUserService.getOne(lqw);
        BeanUtils.copyProperties(sysUser, sysLoginModel);
        return Result.ok(sysLoginModel);
    }

    /**
     * 退出登录
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/logout")
    public Result<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        StpUtil.logout();
        return Result.ok("退出登录成功！");
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
            res.setResult(base64);
        } catch (Exception e) {
            res.error500("获取验证码出错" + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 图形验证码
     *
     * @param sysLoginModel
     * @return
     */
    @RequestMapping(value = "/checkCaptcha", method = RequestMethod.POST)
    public Result<?> checkCaptcha(@RequestBody SysLoginModel sysLoginModel) {
        String captcha = sysLoginModel.getCaptcha();
        String checkKey = sysLoginModel.getCheckKey();
        if (captcha == null) {
            return Result.error("验证码无效");
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
        String realKey = MD5Util.MD5Encode(lowerCaseCaptcha + checkKey, "utf-8");
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.equals(lowerCaseCaptcha)) {
            return Result.error("验证码错误");
        }
        return Result.ok();
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody JSONObject jsonObject) {
        SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
        String captchaCode = (String) redisUtil.get("captchaCode:" + user.getEmail());
        if (StrUtil.isNotBlank(captchaCode)) {
            String captcha = (String) jsonObject.get("captcha");
            if (captchaCode.equals(captcha)) {
                user.setCreateTime(new Date()); //设置创建时间
                String salt = oConvertUtils.randomGen(8);
                user.setSalt(salt);
                String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
                user.setPassword(passwordEncode);
                user.setStatus(1);
                user.setDelFlag(CommonConstant.DEL_FLAG_0);
                user.setCreateBy("用户注册");
                user.setCreateTime(new Date());
                sysUserService.addUserWithRole(user, "1446724729018728450");
            } else {
                return Result.error("验证码错误！");
            }
        } else {
            return Result.error("验证码已过期，请重新发送！");
        }
        return Result.ok("添加成功！");
    }
}
