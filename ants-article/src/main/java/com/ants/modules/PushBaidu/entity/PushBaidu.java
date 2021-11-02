package com.ants.modules.PushBaidu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @TableName push_baidu
 */
@TableName(value ="push_baidu")
@Data
public class PushBaidu implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 文章id
     */
    private String param;

    /**
     * 推送时间
     */
    private Date pushTime;

    /**
     * 1：成功；
     * 2：失败；
     */
    private String state;
    /**
     * 错误信息
     */
    private String errMsg;

}
