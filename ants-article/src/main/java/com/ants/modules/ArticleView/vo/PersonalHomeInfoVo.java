package com.ants.modules.ArticleView.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * TODO
 * Author Chen
 * Date   2021/10/26 15:36
 */
@Data
public class PersonalHomeInfoVo {

    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 登录人账号
     */
    private String username;

    /**
     * 登录人名字
     */
    private String realname;

    /**
     * 加入时间
     */
    private String joinTime;

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
}
