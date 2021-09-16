package com.ants.modules.articleLable.service;

import com.ants.modules.articleLable.entity.ArticleLable;
import com.ants.modules.articleLable.vo.ArticleLableVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public interface ArticleLableService extends IService<ArticleLable> {

    List<ArticleLableVo> genArticleLableList(String name);
}
