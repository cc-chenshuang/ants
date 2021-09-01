package com.ants.modules.system.service.impl;

import cn.hutool.http.HtmlUtil;
import com.ants.common.utils.MinioUtil;
import com.ants.modules.system.entity.MailConfig;
import com.ants.modules.system.entity.SendMailHistory;
import com.ants.modules.system.mapper.SendMailHistoryMapper;
import com.ants.modules.system.service.MailConfigService;
import com.ants.modules.system.service.SendMailHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * TODO
 * Author Chen
 * Date   2021/3/5 16:40
 */
@Service
@Slf4j
public class SendMailHistoryServiceImpl extends ServiceImpl<SendMailHistoryMapper, SendMailHistory> implements SendMailHistoryService {

    @Autowired
    MailConfigService mailConfigService;
    @Value(value = "${ants.minio.bucketName}")
    private String bucketName;
    @Value(value = "${ants.minio.minio_url}")
    private String minioUrl;

    @Override
    public boolean sendMail(Map<String, Object> map) {
        List<String> list = (List<String>) map.get("addressees");
        boolean b = false;
        SendMailHistory sendMailHistory = new SendMailHistory();
        sendMailHistory.setTitle(String.valueOf(map.get("title")));
        sendMailHistory.setContent(String.valueOf(map.get("content")));
        if (!list.isEmpty()) {
            StringBuffer stringBuffer = new StringBuffer();
            List<Map<String, String>> lists = (List<Map<String, String>>) map.get("addressees");
            for (Map<String, String> map1 : lists) {
                stringBuffer.append(map1.get("email") + ",");
            }
            String addresses = stringBuffer.toString();
            sendMailHistory.setAddressee(addresses.substring(0, addresses.length() - 1));
        } else {
            return false;
        }
        b = this.sendMailUtil(sendMailHistory, (List<Map<String, String>>) map.get("fileIds"));
        return b;
    }

    /**
     * 发送邮件
     *
     * @param mailInfo 待发送的邮件信息
     * @param fileIds
     */
    public boolean sendMailUtil(SendMailHistory mailInfo, List<Map<String, String>> fileIds) {
        MailConfig mailConfig = mailConfigService.list().get(0);

        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtps");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", mailConfig.getSmtpHost());   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.port", mailConfig.getSmtpPort());   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        Session session = Session.getInstance(props, null);
        String ids = "";
        try {
            MimeMessage msg = new MimeMessage(session);
            //设置发件人邮箱、发件人名称
            msg.setFrom(new InternetAddress(mailConfig.getUserName(), mailConfig.getNickName(), "UTF-8"));
            //设置收件人邮箱
            msg.setRecipients(Message.RecipientType.TO, mailInfo.getAddressee());
            //设置主题/标题
            msg.setSubject(mailInfo.getTitle());
            //设置日期
            msg.setSentDate(new Date());
            //设置正文内容
            msg.setText(mailInfo.getContent());

            //向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            MimeMultipart multipart = new MimeMultipart();
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(mailInfo.getContent(), "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);

            multipart.setSubType("mixed");
            //添加附件
            MimeBodyPart filePart = null;
            boolean flag = true;
            for (Map<String, String> map : fileIds) {
                filePart = new MimeBodyPart();
                String id = map.get("id");
                if (StringUtils.isNotBlank(id)) {
                    ids = ids + id + ",";
//                    AntsFile antsFile = antsFileService.getById(id);
//                    InputStream minioFile = MinioUtil.getMinioFile(bucketName, antsFile.getFileName());
//                    byte[] bytes = IOUtils.toByteArray(minioFile);
                    //添加附件的内容
//                    filePart.setDataHandler(new DataHandler(new ByteArrayDataSource(bytes, "application/octet-stream")));
                    //添加附件的标题
//                    filePart.setFileName(MimeUtility.encodeWord(antsFile.getOldFileName()));
                    multipart.addBodyPart(filePart);
                } else {
                    flag = false;
                }
            }
            if (flag) {
                //将multipart对象放到message中
                msg.setContent(multipart);
                ids = ids.substring(0, ids.length() - 1);
                mailInfo.setFileId(ids);
            }
            //发送邮件，参数为邮件信息，发件人邮箱和发件人邮箱密码
            Transport.send(msg, mailConfig.getUserName(), mailConfig.getPassword());
        } catch (MessagingException mex) {
            log.info("Send failed! Exception: " + mex);
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        mailInfo.setAddresser(mailConfig.getUserName());
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        mailInfo.setIp(ip);
        String s = HtmlUtil.cleanHtmlTag(mailInfo.getContent());
        mailInfo.setContent(s);

        try {
            this.saveOrUpdate(mailInfo);
        } catch (Exception e) {
            log.error("保存邮件发送记录失败！" + e);
        }
        return true;
    }
}
