package com.ants.modules.system.vo;

import lombok.Data;

/**
 * TODO
 * Author Chen
 * Date   2021/9/13 18:13
 */
@Data
public class SendMailVo {
    private String title;
    private String addressees;
    private String uploadFile;
    private String content;
}
