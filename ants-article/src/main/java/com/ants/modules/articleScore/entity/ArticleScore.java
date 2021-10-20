package com.ants.modules.articleScore.entity;

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
@TableName(value ="article_score")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "系统评价", description = "系统评价")
public class ArticleScore implements Serializable {
    /**
     * ID
     */
    @TableId
    private String id;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 建议
     */
    private String proposal;

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
}
