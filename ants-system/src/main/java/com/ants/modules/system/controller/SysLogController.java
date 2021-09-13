package com.ants.modules.system.controller;

import cn.hutool.core.date.DateUtil;
import com.ants.common.constant.CommonConstant;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.common.utils.oConvertUtils;
import com.ants.modules.system.entity.SysLog;
import com.ants.modules.system.service.SysLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * TODO
 * Author Chen
 * Date   2021/2/9 13:47
 */
@Slf4j
@Api(tags = "系统日志")
@RestController
@RequestMapping("/sys/log")
public class SysLogController {
    @Autowired
    SysLogService sysLogService;

    @ApiOperation("获取日志列表")
    /**
     * @param syslog
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     * @功能：查询日志记录
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<?> queryPageList(SysLog syslog, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<SysLog> queryWrapper = QueryGenerator.initQueryWrapper(syslog, req.getParameterMap());
        Page<SysLog> page = new Page<SysLog>(pageNo, pageSize);
        //日志关键词
        String keyWord = req.getParameter("keyWord");
        if (oConvertUtils.isNotEmpty(keyWord)) {
            queryWrapper.like("log_content", keyWord);
        }
        //TODO 过滤逻辑处理
        //TODO begin、end逻辑处理
        //TODO 一个强大的功能，前端传一个字段字符串，后台只返回这些字符串对应的字段
        //创建时间/创建人的赋值
        IPage<SysLog> pageList = sysLogService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * @param id
     * @return
     * @功能：删除单个日志记录
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<SysLog> delete(@RequestParam(name = "id", required = true) String id) {
        Result<SysLog> result = new Result<SysLog>();
        SysLog sysLog = sysLogService.getById(id);
        if (sysLog == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysLogService.removeById(id);
            if (ok) {
                result.success("删除成功!");
                result.setCode(CommonConstant.SC_OK_200);
            }
        }
        return result;
    }

    /**
     * @param ids
     * @return
     * @功能：批量，全部清空日志记录
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<?> result = new Result<>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            if ("allclear".equals(ids)) {
                this.sysLogService.removeAll();
                result.success("清除成功!");
                result.setCode(CommonConstant.SC_OK_200);
            }
            this.sysLogService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
            result.setCode(CommonConstant.SC_OK_200);
        }
        return result;
    }

}
