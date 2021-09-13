package com.ants.common.system.vo;


/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public class SysCategoryModel {
    /**主键*/
    private String id;
    /**父级节点*/
    private String pid;
    /**类型名称*/
    private String name;
    /**类型编码*/
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
