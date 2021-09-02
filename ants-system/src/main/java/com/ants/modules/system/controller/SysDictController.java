package com.ants.modules.system.controller;

import com.ants.common.annotation.AutoLog;
import com.ants.common.constant.CommonConstant;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.common.utils.SqlInjectionUtil;
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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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


    /**
     * 获取字典数据
     * @param dictCode 字典code
     * @param dictCode 表名,文本字段,code字段  | 举例：sys_user,realname,id
     * @return
     */
    @RequestMapping(value = "/getDictItems/{dictCode}", method = RequestMethod.GET)
    public Result<List<DictModel>> getDictItems(@PathVariable String dictCode, @RequestParam(value = "sign",required = false) String sign,HttpServletRequest request) {
        log.info(" dictCode : "+ dictCode);
        Result<List<DictModel>> result = new Result<List<DictModel>>();
        List<DictModel> ls = null;
        try {
            if(dictCode.indexOf(",")!=-1) {
                //关联表字典（举例：sys_user,realname,id）
                String[] params = dictCode.split(",");

                if(params.length<3) {
                    result.error500("字典Code格式不正确！");
                    return result;
                }
                //SQL注入校验（只限制非法串改数据库）
                final String[] sqlInjCheck = {params[0],params[1],params[2]};
                SqlInjectionUtil.filterContent(sqlInjCheck);

                if(params.length==4) {
                    //SQL注入校验（查询条件SQL 特殊check，此方法仅供此处使用）
                    SqlInjectionUtil.specialFilterContent(params[3]);
                    ls = sysDictService.queryTableDictItemsByCodeAndFilter(params[0],params[1],params[2],params[3]);
                }else if (params.length==3) {
                    ls = sysDictService.queryTableDictItemsByCode(params[0],params[1],params[2]);
                }else{
                    result.error500("字典Code格式不正确！");
                    return result;
                }
            }else {
                //字典表
                ls = sysDictService.queryDictItemsByCode(dictCode);
            }

            result.setSuccess(true);
            result.setResult(ls);
            result.setCode(CommonConstant.SC_OK_200);
            log.info(result.toString());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            result.error500("操作失败");
            return result;
        }

        return result;
    }

}
