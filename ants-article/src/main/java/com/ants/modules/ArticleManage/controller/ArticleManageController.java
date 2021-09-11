package com.ants.modules.ArticleManage.controller;

import com.ants.common.annotation.AutoLog;
import com.ants.common.constant.CacheConstant;
import com.ants.common.constant.CommonConstant;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.utils.oConvertUtils;
import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.service.ArticleManageService;
import com.ants.common.system.result.Result;
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
import java.util.Arrays;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */

@Slf4j
@Api(tags = "文章管理")
@RestController
@RequestMapping("/article")
public class ArticleManageController {

    @Autowired
    ArticleManageService articleManageService;

    @AutoLog(value = "文章管理-列表")
    @ApiOperation(value = "文章管理-列表", notes = "文章管理-列表")
    @GetMapping("/list")
    public Result<?> queryPageList(ArticleManage articleManage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ArticleManage> queryWrapper = QueryGenerator.initQueryWrapper(articleManage, req.getParameterMap());
        Page<ArticleManage> page = new Page<ArticleManage>(pageNo, pageSize);
        IPage<ArticleManage> pageList = articleManageService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param articleManage
     * @return
     */
    @AutoLog(value = "文章管理-添加")
    @ApiOperation(value = "文章管理-添加", notes = "文章管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ArticleManage articleManage) {
        articleManage.setDelFlag(CommonConstant.DEL_FLAG_0);
        articleManageService.save(articleManage);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param articleManage
     * @return
     */
    @AutoLog(value = "文章管理-编辑")
    @ApiOperation(value = "文章管理-编辑", notes = "文章管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ArticleManage articleManage) {
        articleManageService.updateById(articleManage);
        return Result.ok("编辑成功!");
    }

    /**
     * @param id
     * @return
     * @功能：删除
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        boolean ok = articleManageService.removeById(id);
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
        boolean ok = articleManageService.removeByIds(Arrays.asList(ids.split(",")));
        if (ok) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

}
