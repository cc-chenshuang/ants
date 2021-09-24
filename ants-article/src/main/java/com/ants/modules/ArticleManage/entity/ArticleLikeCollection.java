package com.ants.modules.ArticleManage.entity;

import com.ants.common.annotation.Dict;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
@Data
@TableName(value ="article_like_collection")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "点赞/收藏", description = "点赞/收藏")
public class ArticleLikeCollection implements Serializable {
    /**
     * ID
     */
    @TableId
    private String id;

    /**
     * 点赞/收藏   1：收藏；2：点赞
     */
    private String type;

    /**
     * 用户名
     */
    private String username;

    /**
     * 文章id
     */
    private String articleId;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "创建人")
    private String updateBy;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date updateTime;
}
