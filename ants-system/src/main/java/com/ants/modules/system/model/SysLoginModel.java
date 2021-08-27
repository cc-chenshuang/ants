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
    private String avatar = "http%3A%2F%2Fgss0.baidu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1632395777&t=831d383e9027dcfa7ad15e13d322c0ee";
    private String name = "Ants";

}
