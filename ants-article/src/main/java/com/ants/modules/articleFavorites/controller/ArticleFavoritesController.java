package com.ants.modules.articleFavorites.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ants.common.annotation.AutoLog;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.modules.articleFavorites.entity.ArticleFavorites;
import com.ants.modules.articleFavorites.entity.ArticleFavoritesSub;
import com.ants.modules.articleFavorites.service.ArticleFavoritesService;
import com.ants.modules.articleFavorites.service.ArticleFavoritesSubService;
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
import java.util.Arrays;
import java.util.List;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */

@Slf4j
@Api(tags = "收藏夹")
@RestController
@RequestMapping("/articleFavorites")
public class ArticleFavoritesController {

    @Autowired
    ArticleFavoritesService articleFavoritesService;
    @Autowired
    ArticleFavoritesSubService articleFavoritesSubService;

    @AutoLog(value = "文章管理-列表")
    @ApiOperation(value = "文章管理-列表", notes = "文章管理-列表")
    @GetMapping("/list")
    public Result<?> queryPageList(ArticleFavorites articleFavorites,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ArticleFavorites> queryWrapper = QueryGenerator.initQueryWrapper(articleFavorites, req.getParameterMap());
        String username = StpUtil.getLoginIdAsString();
        queryWrapper.eq("create_by", username);
        queryWrapper.orderByDesc("create_time");
        Page<ArticleFavorites> page = new Page<ArticleFavorites>(pageNo, pageSize);
        IPage<ArticleFavorites> pageList = articleFavoritesService.page(page, queryWrapper);
        pageList.getRecords().forEach(e -> {
            LambdaQueryWrapper<ArticleFavoritesSub> lqw = new LambdaQueryWrapper<>();
            lqw.eq(ArticleFavoritesSub::getMainId,e.getId());
            int count = articleFavoritesSubService.count(lqw);
            e.setFavoritesSum(count);
        });
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param articleFavorites
     * @return
     */
    @AutoLog(value = "文章管理-添加")
    @ApiOperation(value = "文章管理-添加", notes = "文章管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ArticleFavorites articleFavorites) {
        articleFavoritesService.save(articleFavorites);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param articleFavorites
     * @return
     */
    @AutoLog(value = "文章管理-编辑")
    @ApiOperation(value = "文章管理-编辑", notes = "文章管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ArticleFavorites articleFavorites) {
        boolean ok = articleFavoritesService.updateById(articleFavorites);
        if (ok) {
            return Result.ok("操作成功!");
        }
        return Result.error("操作失败!");
    }

    /**
     * @param id
     * @return
     * @功能：删除
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        boolean ok = articleFavoritesService.removeById(id);
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
        boolean ok = articleFavoritesService.removeByIds(Arrays.asList(ids.split(",")));
        if (ok) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }


    @GetMapping("/getByUser")
    public Result<?> getByUser(){
        String username = StpUtil.getLoginIdAsString();
        QueryWrapper<ArticleFavorites> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_by", username);
        queryWrapper.orderByDesc("create_time");
        List<ArticleFavorites> list = articleFavoritesService.list(queryWrapper);
        return Result.ok(list);
    }

}
