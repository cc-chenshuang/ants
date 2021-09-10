package com.ants.common.system.vo;

/**
 * @Author oolg
 * @Date 2020/2/19 12:01
 * @Description:
 * @Version 1.0
 */
public class SysUserModel {
    /**主键*/
    private String id;
    /**用户名*/
    private String username;
    /**姓名*/
    private String realname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}
