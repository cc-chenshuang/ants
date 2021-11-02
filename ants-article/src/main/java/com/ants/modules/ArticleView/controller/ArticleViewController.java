package com.ants.modules.ArticleView.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.ants.common.system.api.ISysBaseAPI;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.common.system.vo.LoginUser;
import com.ants.common.utils.ComputeDate;
import com.ants.modules.ArticleManage.entity.ArticleLikeCollection;
import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.service.ArticleLikeCollectionService;
import com.ants.modules.ArticleManage.service.ArticleManageService;
import com.ants.modules.ArticleManage.vo.ArticleManageVo;
import com.ants.modules.ArticleView.vo.PersonMyFavoritesVo;
import com.ants.modules.ArticleView.vo.PersonalHomeInfoVo;
import com.ants.modules.articleFavorites.entity.ArticleFavoritesSub;
import com.ants.modules.articleFavorites.service.ArticleFavoritesSubService;
import com.ants.modules.articleFollow.entity.ArticleFollow;
import com.ants.modules.articleFollow.service.ArticleFollowService;
import com.ants.modules.articleFollow.vo.ArticleFollowVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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
    @Autowired
    ArticleFavoritesSubService articleFavoritesSubService;
    @Autowired
    ArticleFollowService articleFollowService;

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
            ArticleLikeCollection articleLikeCollection = articleLikeCollectionService.getOne(lqw);
            if (articleLikeCollection != null) {
                byId.setIsLikes("1");
            }
            LambdaQueryWrapper<ArticleFavoritesSub> lqw2 = new LambdaQueryWrapper<>();
            lqw2.eq(ArticleFavoritesSub::getCreateBy, username)
                    .eq(ArticleFavoritesSub::getArticleId, id);
            ArticleFavoritesSub articleFavoritesSub = articleFavoritesSubService.getOne(lqw2);
            if (articleFavoritesSub != null) {
                byId.setIsCollect("1");
            }
            LambdaQueryWrapper<ArticleFollow> lqw3 = new LambdaQueryWrapper<>();
            lqw3.eq(ArticleFollow::getUsername, byId.getCreateBy())
                    .eq(ArticleFollow::getCreateBy, username);
            ArticleFollow articleFollow = articleFollowService.getOne(lqw3);
            if (articleFollow != null) {
                byId.setIsFollow(false);
            } else {
                byId.setIsFollow(true);
            }
        } else {
            byId.setIsFollow(true);
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
     * 点赞
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
        byId.setLikesNum(byId.getLikesNum() + 1);
        articleManageService.updateById(byId);
        return Result.ok();
    }

    /**
     * 取消点赞
     *
     * @param articleId
     * @return
     */
    @DeleteMapping("/cancelLikeCollection")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> cancelLikeCollection(@RequestParam String articleId) {
        String username = StpUtil.getLoginIdAsString();// 获取当前会话账号id, 并转化为`String`类型
        LambdaQueryWrapper<ArticleLikeCollection> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleLikeCollection::getUsername, username)
                .eq(ArticleLikeCollection::getArticleId, articleId);
        articleLikeCollectionService.remove(lqw);
        ArticleManage byId = articleManageService.getById(articleId);
        byId.setLikesNum(byId.getLikesNum() - 1);
        articleManageService.updateById(byId);
        return Result.ok();
    }


    @GetMapping("/recordViewsNum")
    public Result recordViewsNum(@RequestParam String id) {
        ArticleManage articleManage = articleManageService.getById(id);
        articleManage.setViewsNum(articleManage.getViewsNum() + 1);
        articleManageService.updateById(articleManage);
        return Result.ok();
    }


    /**
     * 个人主页-个人信息+文章信息
     *
     * @return
     */
    @GetMapping("/getPersonalHomeInfo")
    public Result<?> getPersonalHomeInfo(@RequestParam String username) {

        LoginUser loginUser = sysBaseAPI.getUserByName(username);

        PersonalHomeInfoVo personalHomeInfoVo = new PersonalHomeInfoVo();
        personalHomeInfoVo.setUserAvatar(loginUser.getAvatar());
        personalHomeInfoVo.setUsername(loginUser.getUsername());
        personalHomeInfoVo.setRealname(loginUser.getRealname());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String createTime = sdf.format(loginUser.getCreateTime());
        String currentTime = sdf.format(new Date());
        personalHomeInfoVo.setJoinTime(ComputeDate.remainDateToString(createTime, currentTime));
        LambdaQueryWrapper<ArticleManage> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleManage::getCreateBy, username);
        List<ArticleManage> list = articleManageService.list(lqw);
        personalHomeInfoVo.setCreateTime(loginUser.getCreateTime());
        Integer collectNum = list.stream().collect(Collectors.summingInt(ArticleManage::getCollectNum));
        Integer viewNum = list.stream().collect(Collectors.summingInt(ArticleManage::getViewsNum));
        Integer likeNum = list.stream().collect(Collectors.summingInt(ArticleManage::getLikesNum));
        personalHomeInfoVo.setLikesNum(likeNum);
        personalHomeInfoVo.setViewsNum(viewNum);
        personalHomeInfoVo.setCollectNum(collectNum);
        personalHomeInfoVo.setArticleNum(list.size());
        return Result.ok(personalHomeInfoVo);
    }

    /**
     * 个人主页-获取文章
     *
     * @return
     */
    @GetMapping("/getPersonalHomelist")
    public Result<?> getPersonalHomelist(@RequestParam String sortType,
                                         @RequestParam String username) {
        LambdaQueryWrapper<ArticleManage> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleManage::getCreateBy, username);
        if ("1".equals(sortType)) {
            lqw.orderByDesc(ArticleManage::getCreateTime);
        } else if ("2".equals(sortType)) {
            lqw.orderByDesc(ArticleManage::getViewsNum);
        } else if ("3".equals(sortType)) {
            lqw.orderByDesc(ArticleManage::getLikesNum);
        } else {
            lqw.orderByDesc(ArticleManage::getCollectNum);
        }
        List<ArticleManage> list = articleManageService.list(lqw);
        IPage<ArticleManage> page = new Page<>();
        page.setRecords(list);

        return Result.ok(page);
    }

    /**
     * 个人主页-收藏-展示个人收藏的文章
     *
     * @param username
     * @return
     */
    @GetMapping("/getPersonMyFavoritesList")
    public Result<?> getPersonMyFavoritesList(@RequestParam String username) {
        LambdaQueryWrapper<ArticleFavoritesSub> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleFavoritesSub::getCreateBy, username);
        List<ArticleFavoritesSub> list = articleFavoritesSubService.list(lqw);
        List<PersonMyFavoritesVo> results = new ArrayList<>();
        PersonMyFavoritesVo personMyFavoritesVo = null;
        for (ArticleFavoritesSub articleFavoritesSub : list) {
            personMyFavoritesVo = new PersonMyFavoritesVo();
            personMyFavoritesVo.setDateTime(articleFavoritesSub.getCreateTime());
            ArticleManage articleManage = articleManageService.getById(articleFavoritesSub.getArticleId());
            BeanUtils.copyProperties(articleManage, personMyFavoritesVo);
            results.add(personMyFavoritesVo);
        }
        IPage<PersonMyFavoritesVo> page = new Page<>();
        page.setRecords(results);
        return Result.ok(page);
    }

    /**
     * 个人主页-点赞-展示个人点赞的文章
     *
     * @param username
     * @return
     */
    @GetMapping("/getPersonLikeList")
    public Result<?> getPersonLikeList(@RequestParam String username) {
        LambdaQueryWrapper<ArticleLikeCollection> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleLikeCollection::getUsername, username);
        List<ArticleLikeCollection> list = articleLikeCollectionService.list(lqw);
        List<PersonMyFavoritesVo> results = new ArrayList<>();
        PersonMyFavoritesVo personMyFavoritesVo = null;
        for (ArticleLikeCollection articleLikeCollection : list) {
            personMyFavoritesVo = new PersonMyFavoritesVo();
            personMyFavoritesVo.setDateTime(articleLikeCollection.getCreateTime());
            ArticleManage articleManage = articleManageService.getById(articleLikeCollection.getArticleId());
            BeanUtils.copyProperties(articleManage, personMyFavoritesVo);
            results.add(personMyFavoritesVo);
        }
        IPage<PersonMyFavoritesVo> page = new Page<>();
        page.setRecords(results);
        return Result.ok(page);
    }

    /**
     * 个人主页-关注
     *
     * @param username
     * @return
     */
    @GetMapping("/getPersonFollowList")
    public Result<?> getPersonFollowList(@RequestParam String username) {
        LambdaQueryWrapper<ArticleFollow> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleFollow::getCreateBy, username);
        List<ArticleFollow> list = articleFollowService.list(lqw);
        List<ArticleFollowVo> results = new ArrayList<>();
        ArticleFollowVo articleFollowVo = null;
        for (ArticleFollow articleFollow : list) {
            articleFollowVo = new ArticleFollowVo();
            LambdaQueryWrapper<ArticleManage> lqw2 = new LambdaQueryWrapper<>();
            lqw2.eq(ArticleManage::getCreateBy, articleFollow.getUsername());
            List<ArticleManage> articleManageList = articleManageService.list(lqw2);
            Integer collectNum = articleManageList.stream().collect(Collectors.summingInt(ArticleManage::getCollectNum));
            Integer viewNum = articleManageList.stream().collect(Collectors.summingInt(ArticleManage::getViewsNum));
            Integer likeNum = articleManageList.stream().collect(Collectors.summingInt(ArticleManage::getLikesNum));
            LoginUser loginUser = sysBaseAPI.getUserByName(articleFollow.getUsername());
            articleFollowVo.setUsername(articleFollow.getUsername());
            articleFollowVo.setUserAvatar(loginUser.getAvatar());
            articleFollowVo.setCollectNum(collectNum);
            articleFollowVo.setViewsNum(viewNum);
            articleFollowVo.setLikesNum(likeNum);
            articleFollowVo.setArticleNum(articleManageList.size());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String createTime = sdf.format(loginUser.getCreateTime());
            String currentTime = sdf.format(new Date());
            articleFollowVo.setJoinTime(ComputeDate.remainDateToString(createTime, currentTime));

            results.add(articleFollowVo);
        }
        IPage<ArticleFollowVo> page = new Page<>();
        page.setRecords(results);
        return Result.ok(page);
    }
}
