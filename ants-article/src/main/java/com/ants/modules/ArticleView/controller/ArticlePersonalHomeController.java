package com.ants.modules.ArticleView.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ants.common.system.api.ISysBaseAPI;
import com.ants.common.system.result.Result;
import com.ants.common.system.vo.LoginUser;
import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.service.ArticleLikeCollectionService;
import com.ants.modules.ArticleManage.service.ArticleManageService;
import com.ants.modules.ArticleView.vo.PersonalHomeInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * TODO   博客个人中心
 * Author Chen
 * Date   2021/10/15 14:56
 */
@Slf4j
@Api(tags = "博客个人中心")
@RestController
@RequestMapping("/articlePersonalHome")
public class ArticlePersonalHomeController {

    @Autowired
    ArticleLikeCollectionService articleLikeCollectionService;
    @Autowired
    ArticleManageService articleManageService;
    @Autowired
    ISysBaseAPI sysBaseAPI;

    /**
     * 收藏夹
     *
     * @return
     */
    @GetMapping("/myFavorites")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> myFavorites() {
        String username = StpUtil.getLoginIdAsString();// 获取当前会话账号id, 并转化为`String`类型

        List<Map<String, String>> list = articleLikeCollectionService.myFavorites(username);


        return Result.ok(list);
    }






}
