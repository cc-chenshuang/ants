package com.ants.modules.ArticleView.controller;

import cn.hutool.core.util.StrUtil;
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
import java.util.*;
import java.util.stream.Collectors;

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
    public Result<?> getArticleList(@RequestParam String type,
                                    @RequestParam String sortId,
                                    @RequestParam String lableId) {
        List<ArticleManage> list = null;
        QueryWrapper<ArticleManage> qw = new QueryWrapper<>();

        if ("1".equals(type)) {
            qw.clear();
            if (StrUtil.isNotBlank(sortId)) {
                QueryGenerator.assM2MQueryWrapper(qw, "article_sort", sortId);
            }
            if (StrUtil.isNotBlank(lableId)) {
                QueryGenerator.assM2MQueryWrapper(qw, "article_lable", lableId);
            }
            qw.orderByDesc("create_time");
            list = articleManageService.list(qw);
        } else {
            qw.clear();
            if (StrUtil.isNotBlank(sortId)) {
                QueryGenerator.assM2MQueryWrapper(qw, "article_sort", sortId);
            }
            if (StrUtil.isNotBlank(lableId)) {
                QueryGenerator.assM2MQueryWrapper(qw, "article_lable", lableId);
            }
            qw.orderByDesc("views_num").orderByDesc("likes_num");
            list = articleManageService.list(qw);
        }
        return Result.ok(list);
    }

    /**
     * @param id
     * @return
     * @功能：查询文章 根据id
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

    /**
     * @return
     * @功能：查询文章 根据id
     */
    @GetMapping("/articleGroupByCreateTime")
    public Result<?> articleGroupByCreateTime() {
        List<Map<String, Object>> listMaps = articleManageService.articleGroupByCreateTime();
        listMaps.forEach(e -> {
            String createMonth = String.valueOf(e.get("createMonth"));
            List<ArticleManage> list = articleManageService.getArticleByTime(createMonth);
            e.put("data", list);
        });
        Map<String, List<Map<String, Object>>> map = listMaps.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("createMonth")).substring(0, 4)));
        return Result.ok(map);
    }

    /**
     * @return
     * @功能：查询文章 根据id
     */
    @GetMapping("/getArticleByTime")
    public Result<?> getArticleByTime(@RequestParam String time) {
        List<ArticleManage> list = articleManageService.getArticleByTime(time);
        IPage<ArticleManage> page = new Page<>();
        page.setRecords(list);
        return Result.ok(page);
    }

    /**
     * @return
     * @功能：全文检索    根据标题、内容检索文章
     */
    @GetMapping("/searchAllActive")
    public Result<?> searchAllActive(@RequestParam String value) {
        List<ArticleManage> list = articleManageService.searchAllActive(value);
        return Result.ok(list);
    }

}
