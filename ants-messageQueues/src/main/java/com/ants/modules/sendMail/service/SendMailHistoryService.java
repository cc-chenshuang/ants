package com.ants.modules.sendMail.service;

import com.ants.common.system.result.Result;
import com.ants.modules.sendMail.entity.SendMailHistory;
import com.ants.modules.sendMail.vo.SendMailVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * TODO
 *
 * @author Chen
 * @date 2021/3/5 16:40
 */
public interface SendMailHistoryService extends IService<SendMailHistory> {

    boolean sendMail(SendMailVo sendMailVo);

    Result<?> sendCaptcha(String email);
}
