package com.ants.modules.config;

import com.ants.modules.sendMail.entity.MailConfig;
import org.springframework.stereotype.Component;

import javax.mail.Session;
import java.util.Properties;

/**
 * TODO
 * Author Chen
 * Date   2021/10/15 9:47
 */
@Component
public class MailProperties {

    public Session initProperties(MailConfig mailConfig) {
        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtps");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", mailConfig.getSmtpHost());   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.port", mailConfig.getSmtpPort());   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        Session session = Session.getInstance(props, null);
        return session;
    }
}
