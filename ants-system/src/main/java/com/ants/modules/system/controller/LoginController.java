package com.ants.modules.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.ants.common.system.result.Result;
import com.ants.common.utils.MD5Util;
import com.ants.common.utils.PasswordUtil;
import com.ants.common.utils.RandImageUtil;
import com.ants.common.utils.RedisUtil;
import com.ants.modules.system.entity.SysUser;
import com.ants.modules.system.model.SysLoginModel;
import com.ants.modules.system.service.SysDictService;
import com.ants.modules.system.service.SysLogService;
import com.ants.modules.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/sys1")
@Api(tags="用户登录")
@Slf4j
public class LoginController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysLogService logService;
	@Autowired
    private RedisUtil redisUtil;

    private SysDictService sysDictService;

	private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

	@ApiOperation("登录接口")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel){
		Result<JSONObject> result = new Result<JSONObject>();
		String username = sysLoginModel.getUsername();
		String password = sysLoginModel.getPassword();
		//update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题
		//前端密码加密，后端进行密码解密
		//password = AesEncryptUtil.desEncrypt(sysLoginModel.getPassword().replaceAll("%2B", "\\+")).trim();//密码解密
		//update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题

		//update-begin-author:taoyan date:20190828 for:校验验证码
        String captcha = sysLoginModel.getCaptcha();
        if(captcha==null){
            result.error500("验证码无效");
            return result;
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
		String realKey = MD5Util.MD5Encode(lowerCaseCaptcha+sysLoginModel.getCheckKey(), "utf-8");
		Object checkCode = redisUtil.get(realKey);
		if(checkCode==null || !checkCode.equals(lowerCaseCaptcha)) {
			result.error500("验证码错误");
			return result;
		}
		//update-end-author:taoyan date:20190828 for:校验验证码

		//1. 校验用户是否有效
		//update-begin-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
		LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SysUser::getUsername,username);
		SysUser sysUser = sysUserService.getOne(queryWrapper);
		//update-end-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
		result = sysUserService.checkUserIsEffective(sysUser);
		if(!result.isSuccess()) {
			return result;
		}

		//2. 校验用户名或密码是否正确
		String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
		String syspassword = sysUser.getPassword();
		if (!syspassword.equals(userpassword)) {
			result.error500("用户名或密码错误");
			return result;
		}


		return result;
	}

	@GetMapping("/info")
	@ApiOperation("/info")
	public Result info(HttpServletRequest request) {
		String token = request.getHeader("Ants-Token");
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

	/**
	 * 退出登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public Result<Object> logout(HttpServletRequest request,HttpServletResponse response) {
		//用户退出逻辑
//	    String token = request.getHeader(DefContants.X_ACCESS_TOKEN);
//	    if(oConvertUtils.isEmpty(token)) {
//	    	return Result.error("退出登录失败！");
//	    }
//	    String username = JwtUtil.getUsername(token);
//		LoginUser sysUser = sysBaseAPI.getUserByName(username);
//	    if(sysUser!=null) {
//	    	sysBaseAPI.addLog("用户名: "+sysUser.getRealname()+",退出成功！", CommonConstant.LOG_TYPE_1, null, sysUser.getUsername(), sysUser.getRealname(), sysUser.getOrgCode());
//	    	log.info(" 用户名:  "+sysUser.getRealname()+",退出成功！ ");
//	    	//清空用户登录Token缓存
//	    	redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
//	    	//清空用户登录Shiro权限缓存
//			redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId());
//			//清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
//			redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
//			//调用shiro的logout
//			SecurityUtils.getSubject().logout();
	    	return Result.ok("退出登录成功！");
//	    }else {
//	    	return Result.error("Token无效!");
//	    }
	}



	/**
	 * 后台生成图形验证码 ：有效
	 * @param response
	 * @param key
	 */
	@ApiOperation("获取验证码")
	@GetMapping(value = "/randomImage/{key}")
	public Result<String> randomImage(HttpServletResponse response,@PathVariable String key){
		Result<String> res = new Result<String>();
		try {
			String code = RandomUtil.randomString(BASE_CHECK_CODES,4);
			String lowerCaseCode = code.toLowerCase();
			String realKey = MD5Util.MD5Encode(lowerCaseCode+key, "utf-8");
			redisUtil.set(realKey, lowerCaseCode, 60);
			String base64 = RandImageUtil.generate(code);
			res.setSuccess(true);
			res.setResult(base64);
		} catch (Exception e) {
			res.error500("获取验证码出错"+e.getMessage());
			e.printStackTrace();
		}
		return res;
	}


	/**
	 * 图形验证码
	 * @param sysLoginModel
	 * @return
	 */
	@RequestMapping(value = "/checkCaptcha", method = RequestMethod.POST)
	public Result<?> checkCaptcha(@RequestBody SysLoginModel sysLoginModel){
		String captcha = sysLoginModel.getCaptcha();
		String checkKey = sysLoginModel.getCheckKey();
		if(captcha==null){
			return Result.error("验证码无效");
		}
		String lowerCaseCaptcha = captcha.toLowerCase();
		String realKey = MD5Util.MD5Encode(lowerCaseCaptcha+checkKey, "utf-8");
		Object checkCode = redisUtil.get(realKey);
		if(checkCode==null || !checkCode.equals(lowerCaseCaptcha)) {
			return Result.error("验证码错误");
		}
		return Result.ok();
	}

}
