package com.ants.modules.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ants.common.annotation.AutoLog;
import com.ants.modules.log.entity.SysLog;
import com.ants.modules.log.service.SysLogService;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import com.alibaba.fastjson.JSONObject;
import com.ants.common.constant.CommonConstant;
import com.ants.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;


/**
 * 系统日志，切面处理类
 *
 * @Author chenshuang
 * @Date 2020
 */
@Slf4j
@Aspect
@Component
public class AutoLogAspect {
	@Autowired
	private SysLogService sysLogService;

	@Pointcut("@annotation(com.ants.common.annotation.AutoLog)")
	public void logPointCut() {

	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);

		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLog sysLog = new SysLog();
		AutoLog syslog = method.getAnnotation(AutoLog.class);
		if(syslog != null){
			//注解上的描述,操作日志内容
			sysLog.setLogContent(syslog.value());
			sysLog.setLogType(syslog.logType());

		}

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");


		//设置操作类型
		sysLog.setLogType(2);
		if (sysLog.getLogType() == CommonConstant.LOG_TYPE_2) {
			sysLog.setOperateType(getOperateType(methodName, syslog.operateType()));
		}

		//获取request
		HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
		//请求的参数
		sysLog.setRequestParam(getReqestParams(request,joinPoint));
		String requestURI = request.getRequestURI();
		String remoteAddr = request.getRemoteAddr();
		sysLog.setRequestUrl(requestURI);
		//设置IP地址
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			String address = getAddress(ip);
			log.info(address);
			sysLog.setIpAddress(address);
			sysLog.setIp(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		getBrowser(request,sysLog);

		//获取登录用户信息
//		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
//		if(sysUser!=null){
//			sysLog.setUserid(sysUser.getUsername());
//			sysLog.setUsername(sysUser.getRealname());
//
//		}
		//耗时
		sysLog.setCostTime(time);
		sysLog.setCreateTime(new Date());
		//保存系统日志
		sysLogService.save(sysLog);
	}
	/**
	 * 获取操作类型
	 */
	private int getOperateType(String methodName,int operateType) {
		if (operateType > 0) {
			return operateType;
		}
        if (methodName.startsWith("list")) {
        	return CommonConstant.OPERATE_TYPE_1;
		}
        if (methodName.startsWith("add")) {
        	return CommonConstant.OPERATE_TYPE_2;
		}
        if (methodName.startsWith("edit")) {
        	return CommonConstant.OPERATE_TYPE_3;
		}
        if (methodName.startsWith("delete")) {
        	return CommonConstant.OPERATE_TYPE_4;
		}
        if (methodName.startsWith("import")) {
        	return CommonConstant.OPERATE_TYPE_5;
		}
        if (methodName.startsWith("export")) {
        	return CommonConstant.OPERATE_TYPE_6;
		}
		return CommonConstant.OPERATE_TYPE_1;
	}

	/**
	* @Description: 获取请求参数
	* @author: chen
	* @date: 2020/4/16 0:10
	* @param request:  request
	* @param joinPoint:  joinPoint
	* @Return: java.lang.String
	*/
	private String getReqestParams(HttpServletRequest request, JoinPoint joinPoint) {
		String httpMethod = request.getMethod();
		String params = "";
		if ("POST".equals(httpMethod) || "PUT".equals(httpMethod) || "PATCH".equals(httpMethod)) {
			Object[] paramsArray = joinPoint.getArgs();
			// java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
             //  https://my.oschina.net/mengzhang6/blog/2395893
			 Object[] arguments  = new Object[paramsArray.length];
			for (int i = 0; i < paramsArray.length; i++) {
				if (paramsArray[i] instanceof ServletRequest || paramsArray[i] instanceof ServletResponse || paramsArray[i] instanceof MultipartFile) {
					//ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
					//ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
					continue;
				}
				arguments[i] = paramsArray[i];
			}

			params = JSONObject.toJSONString(arguments);
		} else {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			Method method = signature.getMethod();
			// 请求的方法参数值
			Object[] args = joinPoint.getArgs();
			// 请求的方法参数名称
			LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
			String[] paramNames = u.getParameterNames(method);
			if (args != null && paramNames != null) {
				for (int i = 0; i < args.length; i++) {
					params += "  " + paramNames[i] + ": " + args[i];
				}
			}
		}
		return params;
	}


	public void getBrowser(HttpServletRequest request, SysLog syslog){
		//获取浏览器信息
		String ua = request.getHeader("User-Agent");
		//转成UserAgent对象
		UserAgent userAgent = UserAgent.parseUserAgentString(ua);
		//获取浏览器信息
		Browser browser = userAgent.getBrowser();
		//获取系统信息
		OperatingSystem os = userAgent.getOperatingSystem();
		//系统名称
		String system = os.getName();
		//浏览器名称
		String browserName = browser.getName();
		syslog.setBrowser(browserName);
	}

	/**
	 * 根据ip获取地址
	 * @param ip
	 * @return
	 */
	public static String getAddress(String ip) {
		String url = "http://ip.ws.126.net/ipquery?ip=" + ip;
		String str = HttpUtil.get(url);
		if(!StrUtil.hasBlank(str)){
			String substring = str.substring(str.indexOf("{"), str.indexOf("}")+1);
			System.out.println(substring);
			cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(substring);
			String province = jsonObject.getStr("province");
			String city = jsonObject.getStr("city");
			return province + " " + city;
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			InetAddress ip4 = Inet4Address.getLocalHost();
			System.out.println(ip4.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
