package com.ants.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="测试DEMO对象", description="测试DEMO")
@TableName("demo")
public class Demo {
    /** id */
    @ApiModelProperty(value = "id")
    private String id;
    /** 姓名 */
    @ApiModelProperty(value = "姓名")
    private String name;

    /**
     *  @TableField(exist = false)   不会去关联数据库字段
     */
    @TableField(exist = false)
    private String chen;
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String password;
    @TableField(exist = false)
    private String client_id;
    @TableField(exist = false)
    private String client_secret;
    @TableField(exist = false)
    private String grant_type;
}
