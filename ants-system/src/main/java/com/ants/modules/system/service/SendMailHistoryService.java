package com.ants.modules.system.service;

import com.ants.modules.system.entity.SendMailHistory;
import com.ants.modules.system.vo.SendMailVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * TODO
 *
 * @author Chen
 * @date 2021/3/5 16:40
 */
public interface SendMailHistoryService extends IService<SendMailHistory> {

    boolean sendMail(SendMailVo sendMailVo);
}
