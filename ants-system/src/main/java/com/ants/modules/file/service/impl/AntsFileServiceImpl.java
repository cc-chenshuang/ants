package com.ants.modules.file.service.impl;

import cn.hutool.core.lang.UUID;
import com.ants.common.constant.CommonConstant;
import com.ants.common.utils.CommonUtils;
import com.ants.modules.file.entity.AntsFile;
import com.ants.modules.file.mapper.AntsFileMapper;
import com.ants.modules.file.service.AntsFileService;
import com.ants.modules.file.util.MinioUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.ants.common.utils.FileUtils.FormetFileSize;
import static com.ants.common.utils.FileUtils.flagFileType;

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
    public String uploadLocal(MultipartFile mf, String uploadpath, String uploadType, String fileType) {
        try {
            String ctxPath = uploadpath;
            String fileName = null;
            File file = new File(ctxPath + File.separator + File.separator);
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
            dbpath = fileName;
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            long size = mf.getSize();
            String fileSuffix = orgName.substring(orgName.indexOf("."));
            String fileSize = FormetFileSize(size);
            String FileId = saveFile(fileName, orgName, savePath, fileSize, fileSuffix, uploadType, fileType);
            return FileId;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    @Override
    public String upload(MultipartFile file, String uploadpath, String uploadType, String fileType) {
        String id = "";
        AntsFile antsFile = null;

        if (CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)) {
            antsFile = MinioUtil.upload(file, uploadType, fileType);
        } else {
        }
        if (antsFile != null) {
            id = saveFile(antsFile.getFileName(), antsFile.getOldFileName(), antsFile.getUrl(), antsFile.getFileSize(), antsFile.getFileSuffix(), antsFile.getUploadType(), fileType);
        }
        return id;
    }


    @Override
    public boolean thoroughDelete(String id) {
        AntsFile antsFile = this.getById(id);
        boolean b = this.removeById(id);
        if (b) {
            MinioUtil.removeObject(bucketName, antsFile.getFileName());
        }
        return b;
    }

    /**
     * 本地文件上传
     *
     * @param fileName
     * @param orgName
     * @param filePath
     * @param fileSize
     * @param fileType
     * @return
     */
    private String saveFile(String fileName, String orgName, String filePath, String fileSize, String fileSuffix, String uploadType, String fileType) {
        AntsFile antsFile = new AntsFile();
        antsFile.setFileName(fileName);
        antsFile.setOldFileName(orgName);
        antsFile.setUrl(filePath);
        antsFile.setFileSize(fileSize);
        antsFile.setFileSuffix(fileSuffix);
        antsFile.setDelFlag(0);
        antsFile.setUploadType(flagFileType(uploadType));
        antsFile.setFileType(fileType);
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        antsFile.setIp(ip);
        this.save(antsFile);
        String id = antsFile.getId();
        log.info("文件ID:  " + id);
        return id;
    }

}
