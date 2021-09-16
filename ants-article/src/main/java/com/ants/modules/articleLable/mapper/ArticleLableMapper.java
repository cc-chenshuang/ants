package com.ants.modules.articleLable.mapper;

import com.ants.modules.articleLable.entity.ArticleLable;
import com.ants.modules.articleLable.vo.ArticleLableVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */

public interface ArticleLableMapper extends BaseMapper<ArticleLable> {


    List<ArticleLableVo> genArticleLableList(@Param("name") String name);
}
