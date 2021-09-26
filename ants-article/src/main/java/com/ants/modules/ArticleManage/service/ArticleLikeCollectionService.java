package com.ants.modules.ArticleManage.service;

import com.ants.modules.ArticleManage.entity.ArticleLikeCollection;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public interface ArticleLikeCollectionService extends IService<ArticleLikeCollection> {

    List<Map<String, String>> myFavorites(String username);

}
