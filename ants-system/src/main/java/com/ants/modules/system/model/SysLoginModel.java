package com.ants.modules.system.model;

/**
 * @author ChenShuang
 * @date: 2021/1/31 14:14
 * @Description: TODO
 */


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 * Author Chen
 * Date   2021/2/2 9:59
 */
@ApiModel(value="登录对象", description="登录对象")
@Data
public class SysLoginModel {
    @ApiModelProperty(value = "账号")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "验证码")
    private String captcha;
    @ApiModelProperty(value = "验证码key")
    private String checkKey;
    private String token;
    private String[] roles = {"admin"};
    private String introduction = "ants";
    private String avatar = "http://39.107.111.110:9000/ants-file/414ea39a-216a-4322-9fa5-83d81388d4c1.jpg";
    private String name = "Ants";

}
