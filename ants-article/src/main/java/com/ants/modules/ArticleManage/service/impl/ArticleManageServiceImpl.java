package com.ants.modules.ArticleManage.service.impl;

import com.ants.modules.ArticleManage.entity.ArticleManage;
import com.ants.modules.ArticleManage.service.ArticleManageService;
import com.ants.modules.ArticleManage.mapper.ArticleManageMapper;
import com.ants.modules.ArticleManage.vo.ArticleManageVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
