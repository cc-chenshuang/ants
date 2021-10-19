package com.ants.modules.ArticleView.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ants.common.system.result.Result;
import com.ants.modules.ArticleManage.service.ArticleLikeCollectionService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
