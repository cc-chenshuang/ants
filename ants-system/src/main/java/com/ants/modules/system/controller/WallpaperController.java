package com.ants.modules.system.controller;

import com.ants.common.annotation.AutoLog;
import com.ants.common.constant.CommonConstant;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.common.utils.oConvertUtils;
import com.ants.modules.system.entity.AntsFile;
import com.ants.modules.system.entity.SysDict;
import com.ants.modules.system.entity.Wallpaper;
import com.ants.modules.system.service.AntsFileService;
import com.ants.modules.system.service.WallpaperService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * TODO
 * Author Chen
 * Date   2021/2/2 9:59
 */
@Slf4j
@Api(tags = "壁纸")
@RestController
@RequestMapping("/wallpaper")
public class WallpaperController {

    @Value(value = "${ants.path.upload}")
    private String uploadpath;

    @Autowired
    WallpaperService wallpaperService;

    @ApiOperation("获取列表")
    @GetMapping("/list")
    public Result list(Wallpaper wallpaper,
                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                       HttpServletRequest req) {
        QueryWrapper<Wallpaper> qw = QueryGenerator.initQueryWrapper(wallpaper, req.getParameterMap());
        qw.orderByDesc("create_time");
        Page<Wallpaper> page = new Page<Wallpaper>(pageNo, pageSize);
        IPage<Wallpaper> pageList = wallpaperService.page(page, qw);
        return Result.ok(pageList);
    }

    /**
     * @param wallpaper
     * @return
     * @功能：新增
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<?> add(@RequestBody Wallpaper wallpaper) {
        boolean ok = wallpaperService.save(wallpaper);
        if (ok) {
            return Result.ok("操作成功!");
        }
        return Result.error("操作失败!");
    }

    @ApiOperation("删除图片")
    @AutoLog("删除图片")
    @DeleteMapping("/delete")
    public Result delete(@RequestParam String id) {
        Wallpaper wallpaper = wallpaperService.getById(id);
        boolean flag = deleteFile(wallpaper.getFileName());
        if (flag) {
            boolean ok = wallpaperService.removeById(id);
            if (ok) {
                return Result.ok("操作成功！");
            }
        }
        return Result.error("操作失败！");
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String fileName) {
        boolean flag = false;
        String path = uploadpath + File.separator + fileName;
        File file = new File(path);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

}
