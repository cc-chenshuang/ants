package com.ants.modules.ArticleView.controller;

import com.ants.common.annotation.AutoLog;
import com.ants.common.constant.CommonConstant;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.service.ArticleManageService;
import com.ants.modules.ArticleManage.vo.ArticleManageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */

@Slf4j
@Api(tags = "文章管理")
@RestController
@RequestMapping("/articleView")
public class ArticleViewController {

    @Autowired
    ArticleManageService articleManageService;

    /**
     * @return
     * @功能：根据分类统计
     */
    @GetMapping("/initArticleSort")
    public Result<?> initArticleSort() {
        List<ArticleManageVo> articleManageVoList = articleManageService.initArticleSort();
        return Result.ok(articleManageVoList);
    }

    /**
     * @return
     * @功能：根据分类统计
     */
    @GetMapping("/initArticleLable")
    public Result<?> initArticleLable() {
        List<ArticleManageVo> articleManageVoList = articleManageService.initArticleLable();
        return Result.ok(articleManageVoList);
    }

    /**
     * @param type 1：最新        根据创建时间排序
     *             2：最热        根据点赞数和浏览数综合排序
     * @return
     * @功能：查询文章列表
     */
    @GetMapping("/getArticleList")
    public Result<?> getArticleList(@RequestParam String type) {
        List<ArticleManage> list = null;
        LambdaQueryWrapper<ArticleManage> lqw = new LambdaQueryWrapper<>();
        if ("1".equals(type)) {
            lqw.clear();
            lqw.orderByDesc(ArticleManage::getCreateTime);
            list = articleManageService.list(lqw);
        } else {
            lqw.clear();
            lqw.orderByDesc(ArticleManage::getViewsNum).orderByDesc(ArticleManage::getLikesNum);
            list = articleManageService.list(lqw);
        }
        return Result.ok(list);
    }

    /**
     * @param id
     * @return
     * @功能：查询文章  根据id
     */
    @GetMapping("/getArticleById")
    public Result<?> getArticleById(@RequestParam String id) {
        List<ArticleManage> list = new ArrayList<>();
        ArticleManage byId = articleManageService.getById(id);
        IPage<ArticleManage> page = new Page<>();
        list.add(byId);
        page.setRecords(list);
        return Result.ok(page);
    }

}
