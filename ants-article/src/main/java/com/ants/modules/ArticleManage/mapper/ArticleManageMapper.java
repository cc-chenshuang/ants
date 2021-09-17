package com.ants.modules.ArticleManage.mapper;

import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.vo.ArticleManageVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */

public interface ArticleManageMapper extends BaseMapper<ArticleManage> {


    List<ArticleManageVo> initArticleSort();

    List<ArticleManageVo> initArticleLable();
}
