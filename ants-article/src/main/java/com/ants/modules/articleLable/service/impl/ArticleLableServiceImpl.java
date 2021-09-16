package com.ants.modules.articleLable.service.impl;

import com.ants.modules.articleLable.entity.ArticleLable;
import com.ants.modules.articleLable.mapper.ArticleLableMapper;
import com.ants.modules.articleLable.service.ArticleLableService;
import com.ants.modules.articleLable.vo.ArticleLableVo;
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
public class ArticleLableServiceImpl extends ServiceImpl<ArticleLableMapper, ArticleLable> implements ArticleLableService {

    @Autowired
    ArticleLableMapper articleLableMapper;

    @Override
    public List<ArticleLableVo> genArticleLableList(String name) {
        return articleLableMapper.genArticleLableList(name);
    }
}
