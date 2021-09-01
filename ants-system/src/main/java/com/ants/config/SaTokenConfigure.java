package com.ants.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO   全局拦截器
 * Author Chen
 * Date   2021/7/13 20:05
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    // 注册Sa-Token的拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 拦截所有 URl
        List<String> interceptList = new ArrayList<>();
        interceptList.add("/**");

        // 排除 URL
        List<String> releaseList = new ArrayList<>();
        releaseList.add("/sys/login");
        releaseList.add("/actuator/**");
        releaseList.add("/actuator/health");
        releaseList.add("/doc.html");
        releaseList.add("/webjars/**");
        releaseList.add("/swagger-resources/**");
        releaseList.add("/sys/common/static/**");

        // 注册路由拦截器，自定义验证规则
        registry.addInterceptor(new SaRouteInterceptor((req, res, handler) -> {

            // 登录验证 -- 排除多个路径
            SaRouter.match(interceptList, releaseList, () -> StpUtil.checkLogin());

        })).addPathPatterns("/**");
    }
}
