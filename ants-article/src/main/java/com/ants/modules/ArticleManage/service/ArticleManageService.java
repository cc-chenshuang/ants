package com.ants.modules.ArticleManage.service;

import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.vo.ArticleManageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public interface ArticleManageService extends IService<ArticleManage> {

    List<ArticleManageVo> initArticleSort();

    List<ArticleManageVo> initArticleLable();

}
