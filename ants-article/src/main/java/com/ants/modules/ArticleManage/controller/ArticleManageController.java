package com.ants.modules.ArticleManage.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ants.common.annotation.AutoLog;
import com.ants.common.constant.CacheConstant;
import com.ants.common.constant.CommonConstant;
import com.ants.common.system.api.ISysBaseAPI;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.utils.oConvertUtils;
import com.ants.modules.ArticleManage.entity.ArticleLikeCollection;
import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.service.ArticleLikeCollectionService;
import com.ants.modules.ArticleManage.service.ArticleManageService;
import com.ants.common.system.result.Result;
import com.ants.modules.ArticleManage.vo.ArticleManageVo;
import com.ants.modules.articleFavorites.entity.ArticleFavoritesSub;
import com.ants.modules.articleFavorites.service.ArticleFavoritesSubService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
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
@Api(tags = "文章管理")
@RestController
@RequestMapping("/article")
public class ArticleManageController {

    @Autowired
    ArticleManageService articleManageService;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    ArticleLikeCollectionService articleLikeCollectionService;
    @Autowired
    ArticleFavoritesSubService articleFavoritesSubService;

    @AutoLog(value = "文章管理-列表")
    @ApiOperation(value = "文章管理-列表", notes = "文章管理-列表")
    @GetMapping("/list")
    public Result<?> queryPageList(ArticleManage articleManage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        String username = StpUtil.getLoginIdAsString();
        articleManage.setCreateBy(username);
        articleManage.setDelFlag(0);
        QueryWrapper<ArticleManage> queryWrapper = QueryGenerator.initQueryWrapper(articleManage, req.getParameterMap());
        Page<ArticleManage> page = new Page<ArticleManage>(pageNo, pageSize);
        IPage<ArticleManage> pageList = articleManageService.page(page, queryWrapper);
        return Result.ok(pageList);
    }


    @AutoLog(value = "回收站-列表")
    @ApiOperation(value = "回收站-列表", notes = "回收站-列表")
    @GetMapping("/deleteList")
    public Result<?> deleteList(ArticleManage articleManage,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                HttpServletRequest req) {
        articleManage.setDelFlag(1);
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
        // 推送消息队列
        rabbitTemplate.convertAndSend("pushBaiDuQueue", "http://www.wxmin.cn/articleDetails/" + articleManage.getId());
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
        // 推送消息队列
        rabbitTemplate.convertAndSend("pushBaiDuQueue", "http://www.wxmin.cn/articleDetails/" + articleManage.getId());
        return Result.ok("编辑成功!");
    }

    /**
     * 恢复
     *
     * @param articleManage
     * @return
     */
    @AutoLog(value = "文章管理-恢复")
    @ApiOperation(value = "文章管理-恢复", notes = "文章管理-恢复")
    @PutMapping(value = "/recovery")
    public Result<?> recovery(@RequestBody ArticleManage articleManage) {
        articleManage.setDelFlag(0);
        articleManageService.updateById(articleManage);
        return Result.ok("恢复成功!");
    }

    /**
     * @param id
     * @return
     * @功能：删除
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
//        boolean ok = articleManageService.removeById(id);
        ArticleManage byId = articleManageService.getById(id);
        byId.setDelFlag(1);
        boolean ok = articleManageService.updateById(byId);
        if (ok) {
            return Result.ok("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * @param id
     * @return
     * @功能：彻底删除
     */
    @DeleteMapping("/thoroughDelete")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> thoroughDelete(@RequestParam(name = "id", required = true) String id) {
        boolean ok = articleManageService.removeById(id);
        // 同时删除收藏与点赞信息
        LambdaQueryWrapper<ArticleLikeCollection> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleLikeCollection::getArticleId, id);
        articleLikeCollectionService.remove(lqw);
        LambdaQueryWrapper<ArticleFavoritesSub> lqw2 = new LambdaQueryWrapper<>();
        lqw2.eq(ArticleFavoritesSub::getArticleId, id);
        articleFavoritesSubService.remove(lqw2);
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

    /**
     * @param id
     * @return
     * @功能：根据id查询
     */
    @GetMapping("/getById")
    public Result<?> getById(@RequestParam(name = "id", required = true) String id) {
        ArticleManage byId = articleManageService.getById(id);
        return Result.ok(byId);
    }


}
