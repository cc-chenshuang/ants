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
     * ????????????
     *
     * @param mailInfo ????????????????????????
     * @param fileIds
     */
    public boolean sendMailUtil(SendMailHistory mailInfo, List<Map<String, String>> fileIds) {
        MailConfig mailConfig = mailConfigService.list().get(0);

        // 1. ??????????????????, ??????????????????????????????????????????
        Properties props = new Properties();                    // ????????????
        props.setProperty("mail.transport.protocol", "smtps");   // ??????????????????JavaMail???????????????
        props.setProperty("mail.smtp.host", mailConfig.getSmtpHost());   // ????????????????????? SMTP ???????????????
        props.setProperty("mail.smtp.port", mailConfig.getSmtpPort());   // ????????????????????? SMTP ???????????????
        props.setProperty("mail.smtp.auth", "true");            // ??????????????????
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        Session session = Session.getInstance(props, null);
        String ids = "";
        try {
            MimeMessage msg = new MimeMessage(session);
            //???????????????????????????????????????
            msg.setFrom(new InternetAddress(mailConfig.getUserName(), mailConfig.getNickName(), "UTF-8"));
            //?????????????????????
            msg.setRecipients(Message.RecipientType.TO, mailInfo.getAddressee());
            //????????????/??????
            msg.setSubject(mailInfo.getTitle());
            //????????????
            msg.setSentDate(new Date());
            //??????????????????
            msg.setText(mailInfo.getContent());

            //???multipart????????????????????????????????????????????????????????????????????????
            MimeMultipart multipart = new MimeMultipart();
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(mailInfo.getContent(), "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);

            multipart.setSubType("mixed");
            //????????????
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
                    //?????????????????????
//                    filePart.setDataHandler(new DataHandler(new ByteArrayDataSource(bytes, "application/octet-stream")));
                    //?????????????????????
//                    filePart.setFileName(MimeUtility.encodeWord(antsFile.getOldFileName()));
                    multipart.addBodyPart(filePart);
                } else {
                    flag = false;
                }
            }
            if (flag) {
                //???multipart????????????message???
                msg.setContent(multipart);
                ids = ids.substring(0, ids.length() - 1);
                mailInfo.setFileId(ids);
            }
            //??????????????????????????????????????????????????????????????????????????????
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
            log.error("?????????????????????????????????" + e);
        }
        return true;
    }
}
