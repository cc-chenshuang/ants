package com.ants.config;

import com.ants.common.utils.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
@Slf4j
@Configuration
public class MinioConfig {
    @Value(value = "${ants.minio.minio_url}")
    private String minioUrl;
    @Value(value = "${ants.minio.minio_name}")
    private String minioName;
    @Value(value = "${ants.minio.minio_pass}")
    private String minioPass;
    @Value(value = "${ants.minio.bucketName}")
    private String bucketName;

    @Bean
    public void initMinio(){
        if(!minioUrl.startsWith("http")){
            minioUrl = "http://" + minioUrl;
        }
        if(!minioUrl.endsWith("/")){
            minioUrl = minioUrl.concat("/");
        }
        MinioUtil.setMinioUrl(minioUrl);
        MinioUtil.setMinioName(minioName);
        MinioUtil.setMinioPass(minioPass);
        MinioUtil.setBucketName(bucketName);
    }

}
