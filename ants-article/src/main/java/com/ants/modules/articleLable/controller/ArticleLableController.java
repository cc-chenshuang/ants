package com.ants.modules.articleLable.controller;

import cn.hutool.core.util.StrUtil;
import com.ants.common.annotation.AutoLog;
import com.ants.common.constant.CacheConstant;
import com.ants.common.constant.CommonConstant;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.utils.oConvertUtils;
import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.service.ArticleManageService;
import com.ants.common.system.result.Result;
import com.ants.modules.articleLable.entity.ArticleLable;
import com.ants.modules.articleLable.service.ArticleLableService;
import com.ants.modules.articleLable.vo.ArticleLableVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
@RequestMapping("/articleLable")
public class ArticleLableController {

    @Autowired
    ArticleLableService articleLableService;

    @AutoLog(value = "文章管理-列表")
    @ApiOperation(value = "文章管理-列表", notes = "文章管理-列表")
    @GetMapping("/list")
    public Result<?> queryPageList(ArticleLable articleLable,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ArticleLable> queryWrapper = QueryGenerator.initQueryWrapper(articleLable, req.getParameterMap());
        Page<ArticleLable> page = new Page<ArticleLable>(pageNo, pageSize);
        IPage<ArticleLable> pageList = articleLableService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param articleLable
     * @return
     */
    @AutoLog(value = "文章管理-添加")
    @ApiOperation(value = "文章管理-添加", notes = "文章管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ArticleLable articleLable) {
        articleLableService.save(articleLable);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param articleLable
     * @return
     */
    @AutoLog(value = "文章管理-编辑")
    @ApiOperation(value = "文章管理-编辑", notes = "文章管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ArticleLable articleLable) {
        articleLableService.updateById(articleLable);
        return Result.ok("编辑成功!");
    }

    /**
     * @param id
     * @return
     * @功能：删除
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        boolean ok = articleLableService.removeById(id);
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
        boolean ok = articleLableService.removeByIds(Arrays.asList(ids.split(",")));
        if (ok) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    @GetMapping("/genArticleLableList")
    public Result<?> genArticleLableList(@RequestParam(required = false) String name) {
        LambdaQueryWrapper<ArticleLable> lqw = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(name)) {
            lqw.like(ArticleLable::getName, name);
        }
        List<ArticleLable> list = articleLableService.list(lqw);

        List<ArticleLableVo> articleLableVoList = new ArrayList<>();
        ArticleLableVo articleLableVo = null;
        for (ArticleLable articleLable : list) {
            articleLableVo = new ArticleLableVo();
            articleLableVo.setKey(articleLable.getId());
            articleLableVo.setLabel(articleLable.getName());
            articleLableVoList.add(articleLableVo);
        }
        return Result.ok(articleLableVoList);
    }


    @GetMapping("/getLableById")
    public Result<?> getLableById(@RequestParam String ids) {
        LambdaQueryWrapper<ArticleLable> lqw = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(ids)) {
            lqw.in(ArticleLable::getId, ids.split(","));
        }
        List<ArticleLable> list = articleLableService.list(lqw);

        List<ArticleLableVo> articleLableVoList = new ArrayList<>();
        ArticleLableVo articleLableVo = null;
        for (ArticleLable articleLable : list) {
            articleLableVo = new ArticleLableVo();
            articleLableVo.setKey(articleLable.getId());
            articleLableVo.setLabel(articleLable.getName());
            articleLableVoList.add(articleLableVo);
        }
        return Result.ok(articleLableVoList);
    }


}
