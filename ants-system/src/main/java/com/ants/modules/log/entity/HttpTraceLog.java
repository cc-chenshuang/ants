package com.ants.modules.log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * TODO
 * Author Chen
 * Date   2021/3/31 11:07
 */
@Data
@TableName("http_trace_log")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "http_trace_log对象", description = "请求追踪-记录")
public class HttpTraceLog {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 请求地址
     */
    @ApiModelProperty(value = "请求地址")
    private String path;
    /**
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private String parameterMap;
    /**
     * 请求方法
     */
    @ApiModelProperty(value = "请求方法")
    private String method;
    /**
     * 请求时间
     */
    @ApiModelProperty(value = "请求时间")
    private String time;
    /**
     * 请求耗时
     */
    @ApiModelProperty(value = "请求耗时")
    private String timeTaken;
    /**
     * 请求耗时请求状态
     */
    @ApiModelProperty(value = "请求耗时请求状态")
    private String status;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String requestBody;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String responseBody;
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
     * 创建人
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
