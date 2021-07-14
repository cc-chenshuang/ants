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
    private String avatar = "http://39.105.0.208:9000/ants-file/d1aba94b-f14c-4a53-999a-e258829700c1.jpg";
    private String name = "Ants";

}
