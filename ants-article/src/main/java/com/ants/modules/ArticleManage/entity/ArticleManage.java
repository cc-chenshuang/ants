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
@TableName(value ="article_manage")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "文章管理对象", description = "文章管理对象")
public class ArticleManage implements Serializable {
    /**
     * ID
     */
    @TableId
    private String id;

    /**
     * 标签
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 发布状态
     */
    private String publishState;

    /**
     * 分类
     */
    private String articleSort;

    /**
     * 标签
     */
    private String articleLable;

    /**
     * 点赞数
     */
    private Integer likesNum;

    /**
     * 封面
     */
    private String cover;
    /**
     * 文章类型
     */
    @Dict(dicCode = "article_type")
    private String articleType;
    /**
     * 发布形式
     */
    @Dict(dicCode = "publish_form")
    private String publishForm;
    /**
     * 原文链接
     */
    private String originalTextLink;
    /**
     * 原文是否允许转载，或者是否获得作者授权
     */
    @Dict(dicCode = "yn")
    private String isAuthorAuthorization;
    /**
     * 删除状态 0未删除 1删除
     */
    @ApiModelProperty(value = "删除状态 0未删除 1删除")
    @TableLogic
    @Dict(dicCode = "del_flag")
    private Integer delFlag;

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
