package com.ants.modules.articleFollow.vo;

import com.ants.common.annotation.Dict;
import lombok.Data;

/**
 * TODO
 * Author Chen
 * Date   2021/10/28 16:23
 */
@Data
public class ArticleFollowVo {
    /**
     * 用户账号
     */
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String username;
    /**
     * 点赞数
     */
    private Integer likesNum;
    /**
     * 收藏数
     */
    private Integer collectNum;

    /**
     * 浏览数
     */
    private Integer viewsNum;
    /**
     * 文章数
     */
    private Integer articleNum;
    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 加入时间
     */
    private String joinTime;

}
