package com.ants.modules.articleSort.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.ants.common.annotation.AutoLog;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.modules.articleLable.entity.ArticleLable;
import com.ants.modules.articleLable.vo.ArticleLableVo;
import com.ants.modules.articleSort.entity.ArticleSort;
import com.ants.modules.articleSort.service.ArticleSortService;
import com.ants.modules.articleSort.vo.ArticleSortVo;
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
@Api(tags = "分类管理")
@RestController
@RequestMapping("/articleSort")
public class ArticleSortController {

    @Autowired
    ArticleSortService articleSortService;

    @AutoLog(value = "分类管理-列表")
    @ApiOperation(value = "分类管理-列表", notes = "分类管理-列表")
    @GetMapping("/list")
    public Result<?> queryPageList(ArticleSort articleSort,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        String username = StpUtil.getLoginIdAsString();
        QueryWrapper<ArticleSort> queryWrapper = QueryGenerator.initQueryWrapper(articleSort, req.getParameterMap());
        queryWrapper.eq("create_by", username);
        queryWrapper.orderByAsc("sort_no");
        Page<ArticleSort> page = new Page<ArticleSort>(pageNo, pageSize);
        IPage<ArticleSort> pageList = articleSortService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param articleSort
     * @return
     */
    @AutoLog(value = "分类管理-添加")
    @ApiOperation(value = "分类管理-添加", notes = "分类管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ArticleSort articleSort) {
        articleSortService.save(articleSort);
        return Result.ok("添加成功！");
    }

    @AutoLog(value = "分类管理-添加")
    @ApiOperation(value = "分类管理-添加", notes = "分类管理-添加")
    @PostMapping(value = "/addSort")
    public Result<?> addSort(@RequestBody ArticleSort articleSort) {
        String username = StpUtil.getLoginIdAsString();
        LambdaQueryWrapper<ArticleSort> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleSort::getName, articleSort.getName()).eq(ArticleSort::getCreateBy, username);
        ArticleSort entity = articleSortService.getOne(lqw);
        if (entity != null) {
            return Result.error("分类已存在，不可继续添加！");
        }
        QueryWrapper<ArticleSort> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.select("max(sort_no) sortNo");
        ArticleSort one = articleSortService.getOne(queryWrapper2);
        articleSort.setSortNo(one.getSortNo() + 1);
        articleSortService.save(articleSort);
        ArticleSortVo articleSortVo = new ArticleSortVo();
        articleSortVo.setKey(articleSort.getId());
        articleSortVo.setLabel(articleSort.getName());
        return Result.ok(articleSortVo);
    }

    /**
     * 编辑
     *
     * @param articleSort
     * @return
     */
    @AutoLog(value = "分类管理-编辑")
    @ApiOperation(value = "分类管理-编辑", notes = "分类管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ArticleSort articleSort) {
        articleSortService.updateById(articleSort);
        return Result.ok("编辑成功!");
    }

    /**
     * @param id
     * @return
     * @功能：删除
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        boolean ok = articleSortService.removeById(id);
        if (ok) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * @param ids
     * @return
     * @功能：批量删除
     */
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        boolean ok = articleSortService.removeByIds(Arrays.asList(ids.split(",")));
        if (ok) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    @GetMapping("/genArticSortList")
    public Result<?> genArticSortList(@RequestParam(required = false) String name) {
        LambdaQueryWrapper<ArticleSort> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(ArticleSort::getSortNo);
        List<ArticleSort> list = articleSortService.list(lqw);

        List<ArticleSortVo> articleSortVoList = new ArrayList<>();
        ArticleSortVo articleSortVo = null;
        for (ArticleSort articleSort : list) {
            articleSortVo = new ArticleSortVo();
            articleSortVo.setKey(articleSort.getId());
            articleSortVo.setLabel(articleSort.getName());
            articleSortVoList.add(articleSortVo);
        }
        return Result.ok(articleSortVoList);
    }

    @GetMapping("/getSortByIds")
    public Result<?> getSortByIds(@RequestParam String ids) {
        LambdaQueryWrapper<ArticleSort> lqw = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(ids)) {
            lqw.in(ArticleSort::getId, ids.split(","));
        }
        List<ArticleSort> list = articleSortService.list(lqw);

        List<ArticleSortVo> articleSortVoList = new ArrayList<>();
        ArticleSortVo articleSortVo = null;
        for (ArticleSort articleSort : list) {
            articleSortVo = new ArticleSortVo();
            articleSortVo.setKey(articleSort.getId());
            articleSortVo.setLabel(articleSort.getName());
            articleSortVoList.add(articleSortVo);
        }
        return Result.ok(articleSortVoList);
    }
}
