package com.ants.modules.sendMail.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HtmlUtil;
import com.ants.common.system.result.Result;
import com.ants.common.utils.RedisUtil;
import com.ants.common.utils.oConvertUtils;
import com.ants.modules.sendMail.entity.MailConfig;
import com.ants.modules.sendMail.entity.SendMailHistory;
import com.ants.modules.sendMail.mapper.SendMailHistoryMapper;
import com.ants.modules.sendMail.service.MailConfigService;
import com.ants.modules.sendMail.service.SendMailHistoryService;
import com.ants.modules.sendMail.vo.SendMailVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * TODO
 * Author Chen
 * Date   2021/3/5 16:40
 */
@Service
@Slf4j
public class SendMailHistoryServiceImpl extends ServiceImpl<SendMailHistoryMapper, SendMailHistory> implements SendMailHistoryService {

    @Value(value = "${ants.minio.bucketName}")
    private String bucketName;
    @Value(value = "${ants.minio.minio_url}")
    private String minioUrl;
    @Value(value = "${ants.path.upload}")
    private String uploadpath;

    @Autowired
    MailConfigService mailConfigService;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public boolean sendMail(SendMailVo sendMailVo) {
        String addressees = sendMailVo.getAddressees();

        SendMailHistory sendMailHistory = new SendMailHistory();
        sendMailHistory.setTitle(sendMailVo.getTitle());
        sendMailHistory.setContent(sendMailVo.getContent());
        sendMailHistory.setAddressee(addressees);
        boolean flag = this.sendMailUtil(sendMailHistory, sendMailVo.getUploadFile());
        return flag;
    }

    @Override
    public Result<?> sendCaptcha(String email) {
        String code = RandomUtil.randomNumbers(4);
        boolean captchaCode = redisUtil.set("captchaCode:" + email, code, 60);
        if (captchaCode) {
            SendMailHistory sendMailHistory = new SendMailHistory();
            sendMailHistory.setTitle("Mr.Chen Blog");
            sendMailHistory.setContent("此信息由Mr.Chen Blog网站发出；您的验证码为：" + code);
            sendMailHistory.setAddressee(email);
            boolean flag = this.sendMailUtil(sendMailHistory, null);
            if (flag) {
                return Result.ok(code);
            }
        }
        return Result.error(code);
    }

    /**
     * 发送邮件
     *
     * @param mailInfo  待发送的邮件信息
     * @param filePaths
     */
    public boolean sendMailUtil(SendMailHistory mailInfo, String filePaths) {
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
        InputStream inputStream = null;
        String content = mailInfo.getContent();
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
            msg.setText(content);

            //向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            MimeMultipart multipart = new MimeMultipart();
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(content, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);

            multipart.setSubType("mixed");
            //添加附件
            MimeBodyPart filePart = null;
            if (filePaths != null && filePaths.split(",").length > 0) {
                for (String filePath : filePaths.split(",")) {
                    inputStream = null;
                    filePart = new MimeBodyPart();
                    ids = ids + filePath + ",";
                    inputStream = readLocalFile(filePath);
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    //添加附件的内容
                    filePart.setDataHandler(new DataHandler(new ByteArrayDataSource(bytes, "application/octet-stream")));
                    //添加附件的标题
                    filePart.setFileName(MimeUtility.encodeWord(filePath.replace("mailFile/", "")));
                    multipart.addBodyPart(filePart);
                }
                //将multipart对象放到message中
                msg.setContent(multipart);
                mailInfo.setFileId(filePaths);
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
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        mailInfo.setSender(mailConfig.getUserName());
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

    public InputStream readLocalFile(String filePath) throws FileNotFoundException {
        if (oConvertUtils.isEmpty(filePath) || filePath == "null") {
            return null;
        }
        // 其余处理略
        InputStream inputStream = null;
        filePath = filePath.replace("..", "");
        if (filePath.endsWith(",")) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }
        filePath = uploadpath + File.separator + filePath;
        inputStream = new BufferedInputStream(new FileInputStream(filePath));
        return inputStream;
    }
}
