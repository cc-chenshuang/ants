package com.ants.modules.sendMail.service.impl;

import com.ants.modules.sendMail.entity.MailConfig;
import com.ants.modules.sendMail.mapper.MailConfigMapper;
import com.ants.modules.sendMail.service.MailConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * TODO
 * Author Chen
 * Date   2021/3/5 16:38
 */
@Service
public class MailConfigServiceImpl extends ServiceImpl<MailConfigMapper, MailConfig> implements MailConfigService {
}
