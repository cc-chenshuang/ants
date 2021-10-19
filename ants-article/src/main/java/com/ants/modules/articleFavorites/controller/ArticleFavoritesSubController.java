package com.ants.modules.articleFavorites.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ants.common.annotation.AutoLog;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.service.ArticleManageService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */

@Slf4j
@Api(tags = "收藏夹")
@RestController
@RequestMapping("/articleFavoritesSub")
public class ArticleFavoritesSubController {

    @Autowired
    ArticleFavoritesSubService articleFavoritesSubService;
    @Autowired
    ArticleFavoritesService articleFavoritesService;
    @Autowired
    ArticleManageService articleManageService;

    @AutoLog(value = "收藏夹-列表")
    @ApiOperation(value = "收藏夹-列表", notes = "收藏夹-列表")
    @GetMapping("/list")
    public Result<?> queryPageList(ArticleFavoritesSub articleFavoritesSub,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ArticleFavoritesSub> queryWrapper = QueryGenerator.initQueryWrapper(articleFavoritesSub, req.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<ArticleFavoritesSub> page = new Page<ArticleFavoritesSub>(pageNo, pageSize);
        IPage<ArticleFavoritesSub> pageList = articleFavoritesSubService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param articleFavoritesSub
     * @return
     */
    @AutoLog(value = "收藏夹-添加")
    @ApiOperation(value = "收藏夹-添加", notes = "收藏夹-添加")
    @PostMapping(value = "/add")
    @Transactional
    public Result<?> add(@RequestBody ArticleFavoritesSub articleFavoritesSub) {
        articleFavoritesSubService.save(articleFavoritesSub);
        ArticleManage byId = articleManageService.getById(articleFavoritesSub.getArticleId());
        byId.setCollectNum(byId.getCollectNum() + 1);
        articleManageService.updateById(byId);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param articleFavoritesSub
     * @return
     */
    @AutoLog(value = "收藏夹-编辑")
    @ApiOperation(value = "收藏夹-编辑", notes = "收藏夹-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ArticleFavoritesSub articleFavoritesSub) {
        articleFavoritesSubService.updateById(articleFavoritesSub);
        return Result.ok("编辑成功!");
    }

    /**
     * @param articleId
     * @return
     * @功能：删除
     */
    @DeleteMapping("/delete")
    @Transactional
    public Result<?> delete(@RequestParam(name = "articleId", required = true) String articleId) {
        String username = StpUtil.getLoginIdAsString();
        LambdaQueryWrapper<ArticleFavoritesSub> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleFavoritesSub::getArticleId, articleId).eq(ArticleFavoritesSub::getCreateBy, username);
        boolean ok = articleFavoritesSubService.remove(lqw);
        if (ok) {
            ArticleManage byId = articleManageService.getById(articleId);
            byId.setCollectNum(byId.getCollectNum() - 1);
            articleManageService.updateById(byId);
            return Result.ok("取消收藏！");
        }
        return Result.error("取消收藏失败！");
    }

    /**
     * @param ids
     * @return
     * @功能：批量删除
     */
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        boolean ok = articleFavoritesSubService.removeByIds(Arrays.asList(ids.split(",")));
        if (ok) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

}
