package com.ants.modules.file.service;

import com.ants.modules.file.entity.AntsFile;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * TODO
 * Author Chen
 * Date   2021/2/2 9:59
 */
public interface AntsFileService extends IService<AntsFile> {

    String uploadLocal(MultipartFile file, String uploadpath, String uploadType, String fileType);


    String upload(MultipartFile file, String uploadpath, String uploadType, String fileType);

    boolean thoroughDelete(String id);
}
