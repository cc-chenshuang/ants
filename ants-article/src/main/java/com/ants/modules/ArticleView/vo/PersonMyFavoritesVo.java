package com.ants.modules.ArticleView.vo;

import com.ants.common.annotation.Dict;
import lombok.Data;

import java.util.Date;

/**
 * TODO
 * Author Chen
 * Date   2021/10/28 14:16
 */
@Data
public class PersonMyFavoritesVo {

    private String id;

    // 文章名称
    private String title;

    // 作者名称
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String createBy;

    private String html;

    private Date createTime;

    // 收藏时间
    private Date dateTime;
}
