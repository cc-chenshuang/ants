package com.ants.modules.system.service.impl;

import cn.hutool.core.lang.UUID;
import com.ants.common.utils.CommonUtils;
import com.ants.common.utils.oConvertUtils;
import com.ants.modules.system.entity.AntsFile;
import com.ants.modules.system.entity.Wallpaper;
import com.ants.modules.system.mapper.AntsFileMapper;
import com.ants.modules.system.mapper.WallpaperMapper;
import com.ants.modules.system.service.AntsFileService;
import com.ants.modules.system.service.WallpaperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * TODO
 * Author Chen
 * Date   2021/2/2 9:59
 */
@Slf4j
@Service
public class WallpaperServiceImpl extends ServiceImpl<WallpaperMapper, Wallpaper> implements WallpaperService {

    @Autowired
    WallpaperMapper wallpaperMapper;

}
