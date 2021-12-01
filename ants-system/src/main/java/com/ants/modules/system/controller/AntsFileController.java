package com.ants.modules.system.controller;

import com.ants.common.annotation.AutoLog;
import com.ants.common.constant.CommonConstant;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.common.utils.oConvertUtils;
import com.ants.modules.system.entity.AntsFile;
import com.ants.modules.system.service.AntsFileService;
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

/**
 * TODO
 * Author Chen
 * Date   2021/2/2 9:59
 */
@Slf4j
@Api(tags = "统一文件上传")
@RestController
@RequestMapping("/antsFile")
public class AntsFileController {

    @Value(value = "${ants.path.upload}")
    private String uploadpath;    @Value(value = "${ants.uploadType}")
    private String uploadType;
    @Value(value = "${ants.minio.bucketName}")
    private String bucketName;

    @Autowired
    AntsFileService fileService;

    @ApiOperation("获取文件列表")
    @GetMapping("/list")
    public Result list(AntsFile antsFile,
                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                         HttpServletRequest req) {
        QueryWrapper<AntsFile> qw = QueryGenerator.initQueryWrapper(antsFile, req.getParameterMap());
        qw.orderByDesc("create_time");
        Page<AntsFile> page = new Page<AntsFile>(pageNo, pageSize);
        IPage<AntsFile> pageList = fileService.page(page, qw);
        return Result.ok(pageList);
    }

    @ApiOperation("删除图片")
    @AutoLog("删除图片")
    @DeleteMapping("/delete")
    public Result delete(@RequestParam String id) {
        AntsFile antsFile = fileService.getById(id);
        boolean b = fileService.updateById(antsFile);
        return Result.ok(b);
    }

    /**
     * @param request
     * @return 文件ID
     */
    @ApiOperation("上传图片")
    @AutoLog("上传图片")
    @PostMapping("/upload")
    public Result<?> upload(HttpServletRequest request) {
        Result<?> result = new Result<>();
        String savePath = "";
        String bizPath = request.getParameter("biz");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象
        if (CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)) {
            savePath = fileService.uploadLocal(file, uploadpath, bizPath);
        }
        if (oConvertUtils.isNotEmpty(savePath)) {
            result.setMessage(savePath);
            result.setSuccess(true);
            result.setCode(200);
        } else {
            result.setMessage("上传失败！");
            result.setSuccess(false);
            result.setCode(500);
        }
        return result;
    }


    /**
     * http://localhost:8080/ants/1.jpg
     * 预览图片&下载文件
     * 请求地址：http://localhost:8080/common/static/{user/20190119/e1fe9925bc315c60addea1b98eb1cb1349547719_1547866868179.jpg}
     *
     * @param request
     * @param response
     */
    @GetMapping(value = "/static/**")
    public void view(HttpServletRequest request, HttpServletResponse response) {
        // ISO-8859-1 ==> UTF-8 进行编码转换
        String fileId = extractPathFromPattern(request);

        AntsFile antsFile = fileService.getById(fileId);
        String imgPath = antsFile.getFileName();
        if (oConvertUtils.isEmpty(imgPath) || imgPath == "null") {
            return;
        }
        // 其余处理略
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            imgPath = imgPath.replace("..", "");
            if (imgPath.endsWith(",")) {
                imgPath = imgPath.substring(0, imgPath.length() - 1);
            }
            String filePath = uploadpath + File.separator + imgPath;
            File file = new File(filePath);
            if (!file.exists()) {
                response.setStatus(404);

                //TODO 临时处理，以后需要在打开
                log.error("文件不存在..");
//				throw new RuntimeException("文件不存在..");
            }
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(antsFile.getOldFileName().getBytes("UTF-8"), "iso-8859-1"));
            inputStream = new BufferedInputStream(new FileInputStream(filePath));
            outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (IOException e) {
            log.error("预览文件失败" + e.getMessage());
            response.setStatus(404);
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 把指定URL后的字符串全部截断当成参数
     * 这么做是为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
     *
     * @param request
     * @return
     */
    private static String extractPathFromPattern(final HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }
}
