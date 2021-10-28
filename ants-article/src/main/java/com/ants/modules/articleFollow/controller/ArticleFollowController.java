package com.ants.modules.articleFollow.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ants.common.annotation.AutoLog;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.modules.articleFavorites.entity.ArticleFavorites;
import com.ants.modules.articleFavorites.entity.ArticleFavoritesSub;
import com.ants.modules.articleFavorites.service.ArticleFavoritesService;
import com.ants.modules.articleFavorites.service.ArticleFavoritesSubService;
import com.ants.modules.articleFollow.entity.ArticleFollow;
import com.ants.modules.articleFollow.service.ArticleFollowService;
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
@RequestMapping("/articleFollow")
public class ArticleFollowController {

    @Autowired
    ArticleFollowService articleFollowService;

    @AutoLog(value = "文章管理-列表")
    @ApiOperation(value = "文章管理-列表", notes = "文章管理-列表")
    @GetMapping("/list")
    public Result<?> queryPageList(ArticleFollow articleFollow,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ArticleFollow> queryWrapper = QueryGenerator.initQueryWrapper(articleFollow, req.getParameterMap());
        String username = StpUtil.getLoginIdAsString();
        queryWrapper.eq("create_by", username);
        queryWrapper.orderByDesc("create_time");
        Page<ArticleFollow> page = new Page<ArticleFollow>(pageNo, pageSize);
        IPage<ArticleFollow> pageList = articleFollowService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param articleFollow
     * @return
     */
    @AutoLog(value = "文章管理-添加")
    @ApiOperation(value = "文章管理-添加", notes = "文章管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ArticleFollow articleFollow) {
        articleFollowService.save(articleFollow);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param articleFollow
     * @return
     */
    @AutoLog(value = "文章管理-编辑")
    @ApiOperation(value = "文章管理-编辑", notes = "文章管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ArticleFollow articleFollow) {
        boolean ok = articleFollowService.updateById(articleFollow);
        if (ok) {
            return Result.ok("操作成功!");
        }
        return Result.error("操作失败!");
    }

    /**
     * @param username
     * @return
     * @功能：删除
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "username", required = true) String username) {
        String createBy = StpUtil.getLoginIdAsString();// 获取当前会话账号id, 并转化为`String`类型
        LambdaQueryWrapper<ArticleFollow> lqw3 = new LambdaQueryWrapper<>();
        lqw3.eq(ArticleFollow::getUsername, username)
                .eq(ArticleFollow::getCreateBy, createBy);
        boolean ok = articleFollowService.remove(lqw3);
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
        boolean ok = articleFollowService.removeByIds(Arrays.asList(ids.split(",")));
        if (ok) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }


}
