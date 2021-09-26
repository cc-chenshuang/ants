package com.ants.modules.ArticleManage.mapper;

import com.ants.modules.ArticleManage.entity.ArticleLikeCollection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */

public interface ArticleLikeCollectionMapper extends BaseMapper<ArticleLikeCollection> {


    List<Map<String, String>> myFavorites(@Param("username") String username);
}
