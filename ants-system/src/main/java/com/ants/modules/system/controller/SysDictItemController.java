package com.ants.modules.system.controller;

import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.modules.system.entity.SysDictItem;
import com.ants.modules.system.service.SysDictItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * TODO
 * Author Chen
 * Date   2021/3/1 9:58
 */
@Api(tags = "字典管理-子表")
@RestController
@RequestMapping("/sys/dictItem")
public class SysDictItemController {
    @Autowired
    SysDictItemService sysDictItemService;

    @ApiOperation("获取所有父级字典项")
    @GetMapping("/")
    public Result<?> getSysDictAll(SysDictItem sysDictItem,
                                   HttpServletRequest req) {
        QueryWrapper<SysDictItem> qw = QueryGenerator.initQueryWrapper(sysDictItem, req.getParameterMap());
        qw.orderByAsc("sort_order");
//        Page<SysDictItem> page = new Page<SysDictItem>(pageNo, pageSize);
//        IPage<SysDictItem> pageList = sysDictItemService.page(page, qw);
        List<SysDictItem> list = sysDictItemService.list(qw);
        return Result.ok(list);
    }

    @ApiOperation("根据ID查询父级字典项")
    @GetMapping("/{id}")
    public Result<?> getSysDictById(@PathVariable("id") String id) {
        QueryWrapper<SysDictItem> qw = new QueryWrapper<>();
        qw.eq("dict_id", id);
        qw.orderByAsc("sort_order");
        List<SysDictItem> list = sysDictItemService.list(qw);
        return Result.ok(list);
    }

    @ApiOperation("根据ID删除字典项")
    @DeleteMapping("/{id}")
    public Result<?> deleteSysDictById(@PathVariable("id") String id) {
        boolean b = sysDictItemService.removeById(id);
        if (b) {
            return Result.ok("删除成功！");
        }
        return Result.error("删除失败！");
    }

    @ApiOperation("保存或更新")
    @PostMapping("/")
    public Result<?> sysDictAddOrPut(@RequestBody SysDictItem sysDictItem) {
        boolean b = sysDictItemService.saveOrUpdate(sysDictItem);
        if (b) {
            return Result.ok("成功！");
        }
        return Result.error("失败！");
    }
}
