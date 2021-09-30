package com.ants.modules.ArticleView.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.ants.common.annotation.AutoLog;
import com.ants.common.constant.CommonConstant;
import com.ants.common.system.api.ISysBaseAPI;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.common.system.vo.LoginUser;
import com.ants.modules.ArticleManage.entity.ArticleLikeCollection;
import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.service.ArticleLikeCollectionService;
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
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    ArticleLikeCollectionService articleLikeCollectionService;
    @Autowired
    ISysBaseAPI sysBaseAPI;

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
            qw.eq("del_flag", 0);
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
            qw.eq("del_flag", 0);
            if (StrUtil.isNotBlank(sortId)) {
                QueryGenerator.assM2MQueryWrapper(qw, "article_sort", sortId);
            }
            if (StrUtil.isNotBlank(lableId)) {
                QueryGenerator.assM2MQueryWrapper(qw, "article_lable", lableId);
            }
            qw.orderByDesc("views_num").orderByDesc("likes_num");
            list = articleManageService.list(qw);
        }
        for (ArticleManage articleManage : list) {
            LoginUser loginUser = sysBaseAPI.getUserByName(articleManage.getCreateBy());
            articleManage.setUserAvatar(loginUser.getAvatar());
        }
        IPage<ArticleManage> page = new Page<>();
        page.setRecords(list);
        return Result.ok(page);
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
        boolean login = StpUtil.isLogin();
        if (login) {
            String username = StpUtil.getLoginIdAsString();// 获取当前会话账号id, 并转化为`String`类型
            LambdaQueryWrapper<ArticleLikeCollection> lqw = new LambdaQueryWrapper<>();
            lqw.eq(ArticleLikeCollection::getUsername, username)
                    .eq(ArticleLikeCollection::getArticleId, id);
            List<ArticleLikeCollection> list1 = articleLikeCollectionService.list(lqw);
            for (ArticleLikeCollection articleLikeCollection : list1) {
                if ("1".equals(articleLikeCollection.getType())) {
                    byId.setIsCollect("1");
                } else {
                    byId.setIsLikes("1");
                }
            }
        }
        IPage<ArticleManage> page = new Page<>();
        list.add(byId);
        page.setRecords(list);
        return Result.ok(page);
    }

    /**
     * @return
     * @功能：归档 根据id
     */
    @GetMapping("/articleGroupByCreateTime")
    public Result<?> articleGroupByCreateTime() {
        List<Map<String, Object>> listMaps = articleManageService.articleGroupByCreateTime();
        listMaps.forEach(e -> {
            String createMonth = String.valueOf(e.get("createMonth"));
            List<ArticleManage> list = articleManageService.getArticleByTime(createMonth);
            e.put("data", list);
        });
        List<List<Map<String, Object>>> list = new LinkedList<>();
        Map<String, List<Map<String, Object>>> map = listMaps.stream().collect(Collectors.groupingBy(e -> String.valueOf(e.get("createMonth")).substring(0, 4)));
        for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        return Result.ok(list);
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
     * @功能：全文检索 根据标题、内容检索文章
     */
    @GetMapping("/searchAllActive")
    public Result<?> searchAllActive(@RequestParam String value) {

        List<ArticleManage> list = articleManageService.searchAllActive(value);
        for (ArticleManage articleManage : list) {
            LoginUser loginUser = sysBaseAPI.getUserByName(articleManage.getCreateBy());
            articleManage.setUserAvatar(loginUser.getAvatar());
        }
        IPage<ArticleManage> page = new Page<>();
        page.setRecords(list);
        return Result.ok(page);
    }

    /**
     * 收藏/点赞
     * 1：收藏；2：点赞
     *
     * @param articleLikeCollection
     * @return
     */
    @PostMapping("/likeCollection")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> likeCollection(@RequestBody ArticleLikeCollection articleLikeCollection) {
        String username = StpUtil.getLoginIdAsString();// 获取当前会话账号id, 并转化为`String`类型
        articleLikeCollection.setUsername(username);
        articleLikeCollectionService.save(articleLikeCollection);
        ArticleManage byId = articleManageService.getById(articleLikeCollection.getArticleId());
        if ("1".equals(articleLikeCollection.getType())) {
            byId.setCollectNum(byId.getCollectNum() + 1);
        } else {
            byId.setLikesNum(byId.getLikesNum() + 1);
        }
        articleManageService.updateById(byId);
        return Result.ok();
    }

    /**
     * 取消收藏/取消点赞
     *
     * @param type
     * @param articleId
     * @return
     */
    @DeleteMapping("/cancelLikeCollection")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> cancelLikeCollection(@RequestParam String type,
                                          @RequestParam String articleId) {
        String username = StpUtil.getLoginIdAsString();// 获取当前会话账号id, 并转化为`String`类型
        LambdaQueryWrapper<ArticleLikeCollection> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleLikeCollection::getType, type)
                .eq(ArticleLikeCollection::getUsername, username)
                .eq(ArticleLikeCollection::getArticleId, articleId);
        articleLikeCollectionService.remove(lqw);
        ArticleManage byId = articleManageService.getById(articleId);
        if ("1".equals(type)) {
            byId.setCollectNum(byId.getCollectNum() - 1);
        } else {
            byId.setLikesNum(byId.getLikesNum() - 1);
        }
        articleManageService.updateById(byId);
        return Result.ok();
    }


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
