package com.ants.modules.ArticleManage.service.impl;

import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.service.ArticleManageService;
import com.ants.modules.ArticleManage.mapper.ArticleManageMapper;
import com.ants.modules.ArticleManage.vo.ArticleManageVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
@Service
public class ArticleManageServiceImpl extends ServiceImpl<ArticleManageMapper, ArticleManage> implements ArticleManageService {

    @Autowired
    ArticleManageMapper articleManageMapper;

    @Override
    public List<ArticleManageVo> initArticleSort() {
        return articleManageMapper.initArticleSort();
    }

    @Override
    public List<ArticleManageVo> initArticleLable() {
        return articleManageMapper.initArticleLable();
    }

    @Override
    public List<Map<String, Object>> articleGroupByCreateTime() {
        return articleManageMapper.articleGroupByCreateTime();
    }

    @Override
    public List<ArticleManage> getArticleByTime(String time) {
        return articleManageMapper.getArticleByTime(time);
    }

    @Override
    public List<ArticleManage> searchAllActive(String value) {
        return articleManageMapper.searchAllActive(value);
    }
}
