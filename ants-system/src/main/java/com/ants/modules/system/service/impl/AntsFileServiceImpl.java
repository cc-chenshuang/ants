package com.ants.modules.system.service.impl;

import cn.hutool.core.lang.UUID;
import com.ants.common.utils.CommonUtils;
import com.ants.common.utils.oConvertUtils;
import com.ants.modules.system.entity.AntsFile;
import com.ants.modules.system.mapper.AntsFileMapper;
import com.ants.modules.system.service.AntsFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * TODO
 * Author Chen
 * Date   2021/2/2 9:59
 */
@Slf4j
@Service
public class AntsFileServiceImpl extends ServiceImpl<AntsFileMapper, AntsFile> implements AntsFileService {

    @Autowired
    AntsFileMapper antsFileMapper;

    @Value("${ants.minio.bucketName}")
    private String bucketName;

    @Override
    public String uploadLocal(MultipartFile mf, String uploadpath, String bizPath) {
        try {
            String ctxPath = uploadpath;
            String fileName = null;
            File file = new File(ctxPath + File.separator + bizPath + File.separator);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            String orgName = mf.getOriginalFilename();// 获取文件名
            orgName = CommonUtils.getFileName(orgName);
            if (orgName.indexOf(".") != -1) {
                fileName = UUID.randomUUID() + orgName.substring(orgName.indexOf("."));
            } else {
                fileName = orgName + "_" + System.currentTimeMillis();
            }
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbpath = null;
            if (oConvertUtils.isNotEmpty(bizPath)) {
                dbpath = bizPath + File.separator + fileName;
            } else {
                dbpath = fileName;
            }
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            saveFile(bizPath + File.separator + fileName, orgName, savePath);
            return dbpath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }


    /**
     * 本地文件上传
     *
     * @param fileName
     * @param orgName
     * @param filePath
     * @return
     */
    private void saveFile(String fileName, String orgName, String filePath) {
        AntsFile antsFile = new AntsFile();
        antsFile.setFileName(fileName);
        antsFile.setOldFileName(orgName);
        antsFile.setUrl(filePath);
        this.save(antsFile);
    }

}
