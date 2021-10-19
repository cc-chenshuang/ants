package com.ants.modules.articleFavorites.entity;

import com.ants.common.annotation.Dict;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * TODO   收藏夹文章关联表
 * Author Chen
 * Date   2021/10/18 18:44
 */
@Data
@TableName(value ="article_favorites_sub")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "收藏夹文章关联表", description = "收藏夹文章关联表")
public class ArticleFavoritesSub implements Serializable {
    /**
     * ID
     */
    @TableId
    private String id;

    /**
     * 名称
     */
    private String mainId;

    /**
     * 文章id
     */
    @Dict(dictTable = "article_manage", dicText = "title", dicCode = "id")
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
    @ApiModelProperty(value = "*/")
    private String updateBy;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @TableField(exist = false)
    private String articleName;
}
