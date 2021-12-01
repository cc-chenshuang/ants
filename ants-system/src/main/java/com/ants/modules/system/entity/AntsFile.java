package com.ants.modules.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * TODO
 * Author Chen
 * Date   2021/2/2 9:59
 */
@Data
@TableName("ants_file")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "file对象", description = "统一文件表")
public class AntsFile {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    /**
     * 原始文件名称
     */
    @ApiModelProperty(value = "原始文件名称")
    private String oldFileName;
    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径")
    private String url;

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
