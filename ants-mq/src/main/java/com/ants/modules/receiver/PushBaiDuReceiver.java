package com.ants.modules.receiver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * TODO   主动推送文章链接至百度搜索资源平台 普通收录
 * Author Chen
 * Date   2021/10/13 10:28
 */
@RabbitListener(queues = "pushBaiDuQueue")
@Component
@Slf4j
public class PushBaiDuReceiver {

    @RabbitHandler
    public void process(String url) {
        String PostUrl = "http://data.zz.baidu.com/urls?site=www.wxmin.cn&token=WaSUZJVLgyKxfZIU";
        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            //建立URL之间的连接
            URLConnection conn = new URL(PostUrl).openConnection();
            //HttpURLConnection conn = (HttpURLConnection)new URL(PostUrl).openConnection();
            //if(conn.getResponseCode()!=200){
            //设置通用的请求属性
            conn.setRequestProperty("User-Agent", "curl/7.12.1");
            conn.setRequestProperty("Host", "data.zz.baidu.com");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Content-Length", "83");
            //发送POST请求必须设置如下两行
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //获取conn对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            //发送请求参数
            String param = url;
            out.print(param.trim());
            //进行输出流的缓冲
            out.flush();
            //通过BufferedReader输入流来读取Url的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            //}
        } catch (Exception e) {
            System.out.println("post推送出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        log.info(result);
    }
}
