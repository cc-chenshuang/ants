package com.ants.modules.receiver;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HtmlUtil;
import com.ants.common.utils.RedisUtil;
import com.ants.common.utils.oConvertUtils;
import com.ants.modules.config.MailProperties;
import com.ants.modules.sendMail.entity.MailConfig;
import com.ants.modules.sendMail.entity.SendMailHistory;
import com.ants.modules.sendMail.service.MailConfigService;
import com.ants.modules.sendMail.service.SendMailHistoryService;
import com.ants.modules.sendMail.vo.SendMailVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

/**
 * TODO   发送邮件
 * Author Chen
 * Date   2021/10/15 9:18
 */
@RabbitListener(queues = "sendMailQueue")
@Component
@Slf4j
public class SendMailReceiver {

    @Value(value = "${ants.path.upload}")
    private String uploadpath;

    @Autowired
    MailConfigService mailConfigService;
    @Autowired
    SendMailHistoryService sendMailHistoryService;
    @Autowired
    MailProperties mailProperties;
    @Autowired
    RedisUtil redisUtil;

    @RabbitHandler
    public void sendMail(String addressee) {
        String code = RandomUtil.randomNumbers(4);
        redisUtil.set("captchaCode:" + addressee, code, 60);

        MailConfig mailConfig = mailConfigService.list().get(0);
        Session session = mailProperties.initProperties(mailConfig);
        SendMailHistory mailInfo = new SendMailHistory();
        mailInfo.setTitle("随笔一记");
        mailInfo.setContent("此信息由《随笔一记》发出；您的验证码为：" + code);
        mailInfo.setAddressee(addressee);
        InputStream inputStream = null;
        try {
            MimeMessage msg = new MimeMessage(session);
            //设置发件人邮箱、发件人名称
            msg.setFrom(new InternetAddress(mailConfig.getUserName(), mailConfig.getNickName(), "UTF-8"));
            //设置收件人邮箱
            msg.setRecipients(Message.RecipientType.TO, addressee);
            //设置主题/标题
            msg.setSubject("随笔一记");
            //设置日期
            msg.setSentDate(new Date());
            //设置正文内容
            msg.setText("此信息由《随笔一记》发出；您的验证码为：" + code);

            //发送邮件，参数为邮件信息，发件人邮箱和发件人邮箱密码
            Transport.send(msg, mailConfig.getUserName(), mailConfig.getPassword());
        } catch (MessagingException mex) {
            log.info("Send failed! Exception: " + mex);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
            sendMailHistoryService.saveOrUpdate(mailInfo);
        } catch (Exception e) {
            log.error("保存邮件发送记录失败！" + e);
        }
    }


    @RabbitHandler
    public void sendMailFile(SendMailVo sendMailVo) {
        String addressees = sendMailVo.getAddressees();

        SendMailHistory sendMailHistory = new SendMailHistory();
        sendMailHistory.setTitle(sendMailVo.getTitle());
        sendMailHistory.setContent(sendMailVo.getContent());
        sendMailHistory.setAddressee(addressees);
        sendMailFileUtil(sendMailHistory, sendMailVo.getUploadFile());
    }

    /**
     * 发送邮件-带附件
     *
     * @param mailInfo  待发送的邮件信息
     * @param filePaths
     */
    public boolean sendMailFileUtil(SendMailHistory mailInfo, String filePaths) {
        MailConfig mailConfig = mailConfigService.list().get(0);

        Session session = mailProperties.initProperties(mailConfig);
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
            sendMailHistoryService.saveOrUpdate(mailInfo);
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
