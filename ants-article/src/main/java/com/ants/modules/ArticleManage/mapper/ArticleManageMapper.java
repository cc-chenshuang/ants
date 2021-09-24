package com.ants.modules.ArticleManage.mapper;

import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.vo.ArticleManageVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */

public interface ArticleManageMapper extends BaseMapper<ArticleManage> {


    List<ArticleManageVo> initArticleSort();

    List<ArticleManageVo> initArticleLable();

    List<Map<String, Object>> articleGroupByCreateTime();

    List<ArticleManage> getArticleByTime(@Param("time") String time);
    @SqlParser(filter = true)
    List<ArticleManage> searchAllActive(@Param("value") String value);
}
