package com.ants.modules.system.controller;

import com.ants.common.annotation.AutoLog;
import com.ants.common.query.QueryGenerator;
import com.ants.common.result.Result;
import com.ants.modules.system.entity.SysDict;
import com.ants.modules.system.entity.SysDictItem;
import com.ants.modules.system.service.SysDictItemService;
import com.ants.modules.system.service.SysDictService;
import com.ants.modules.system.vo.DictModel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * TODO
 * Author Chen
 * Date   2021/3/1 9:54
 */
@Api(tags = "字典管理-父表")
@RestController
@RequestMapping("/sys/dict")
public class SysDictController {
    @Autowired
    SysDictService sysDictService;
    @Autowired
    SysDictItemService sysDictItemService;

    @ApiOperation("获取所有父级字典项")
    @GetMapping("/")
    public Result<?> getSysDictAll(SysDict sysDict,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SysDict> qw = QueryGenerator.initQueryWrapper(sysDict, req.getParameterMap());
        qw.orderByDesc("create_time");
        Page<SysDict> page = new Page<SysDict>(pageNo, pageSize);
        IPage<SysDict> pageList = sysDictService.page(page, qw);
        return Result.ok(pageList);
    }

    @ApiOperation("根据ID查询父级字典项")
    @GetMapping("/{id}")
    public Result<?> getSysDictById(@PathVariable("id") String id) {
        QueryWrapper<SysDict> qw = new QueryWrapper<>();
        qw.eq("id", id);
        SysDict one = sysDictService.getOne(qw);
        return Result.ok(one);
    }

    @ApiOperation("根据ID删除父级字典项级联删除子级字典")
    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> deleteSysDictById(@PathVariable("id") String id) {
        sysDictService.removeById(id);
        QueryWrapper<SysDictItem> qw = new QueryWrapper<>();
        qw.eq("dict_id", id);
        sysDictItemService.remove(qw);
        return Result.ok("删除成功！");
    }

    @ApiOperation("保存并更新父级字典项")
    @PostMapping("/")
    @AutoLog("保存并更新父级字典项")
    public Result<?> sysDictAddOrPut(@RequestBody SysDict sysDict) {
        sysDict.setDelFlag(0);
        boolean b = sysDictService.saveOrUpdate(sysDict);
        if (b) {
            return Result.ok("成功！");
        }
        return Result.error("失败！");
    }

    @ApiOperation("判断dictCode是否唯一")
    @GetMapping("/judgeDictCodeRepeat")
    public Result<?> judgeDictCodeRepeat(@RequestParam String dictCode) {
        QueryWrapper<SysDict> qw = new QueryWrapper<>();
        qw.eq("dict_code", dictCode);
        List<SysDict> list = sysDictService.list(qw);
        if (list.isEmpty()){
            return Result.ok(true);
        }
        return Result.ok(false);
    }

    @ApiOperation("根据字典code获取字典值")
    @GetMapping("/getDictItemByCode")
    public Result<?> getDictItemByCode(@RequestParam String dictCode) {
        List<DictModel> list = sysDictService.getDictItemByCode(dictCode);
        return Result.ok(list);
    }

}
