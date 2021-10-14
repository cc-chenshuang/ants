package com.ants.modules.sendMail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

import java.util.Date;

/**
 * TODO
 * Author Chen
 * Date   2021/3/5 13:33
 */
@Data
@TableName("config_mail")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "config_mail对象", description = "邮件配置表")
public class MailConfig {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 发送邮件的邮箱地址
     */
    @ApiModelProperty(value = "发送邮件的邮箱地址")
    private String userName;
    /**
     * 客户端授权码，不是邮箱密码
     */
    @ApiModelProperty(value = "客户端授权码，不是邮箱密码")
    private String password;
    /**
     * 发送邮件服务器地址
     */
    @ApiModelProperty(value = "发送邮件服务器地址")
    private String smtpHost;
    /**
     * 发送邮件服务器端口
     */
    @ApiModelProperty(value = "发送邮件服务器端口")
    private String smtpPort;
    /**
     * 自定义发送用户名称
     */
    @ApiModelProperty(value = "自定义发送用户名称")
    private String nickName;
    /**
     * IP
     */
    @ApiModelProperty(value = "IP")
    private String ip;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;

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


    // 是否需要身份验证
    @TableField(exist = false)
    private boolean validate = false;
}
