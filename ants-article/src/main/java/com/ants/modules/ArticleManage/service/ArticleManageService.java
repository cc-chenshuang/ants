package com.ants.modules.ArticleManage.service;

import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.vo.ArticleManageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public interface ArticleManageService extends IService<ArticleManage> {

    List<ArticleManageVo> initArticleSort();

    List<ArticleManageVo> initArticleLable();

    List<Map<String, Object>> articleGroupByCreateTime();

    List<ArticleManage> getArticleByTime(String time);

    List<ArticleManage> searchAllActive(String value);
}
