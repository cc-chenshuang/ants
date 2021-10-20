package com.ants.modules.articleScore.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ants.common.annotation.AutoLog;
import com.ants.common.system.result.Result;
import com.ants.modules.articleScore.entity.ArticleScore;
import com.ants.modules.articleScore.service.ArticleScoreService;
import com.ants.modules.articleSort.entity.ArticleSort;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */

@Slf4j
@Api(tags = "系统评价")
@RestController
@RequestMapping("/articleScore")
public class ArticleScoreController {

    @Autowired
    ArticleScoreService articleScoreService;

    @AutoLog(value = "系统评价-查询")
    @ApiOperation(value = "系统评价-查询", notes = "系统评价-查询")
    @GetMapping(value = "/get")
    public Result<?> get() {
        String username = StpUtil.getLoginIdAsString();
        LambdaQueryWrapper<ArticleScore> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleScore::getCreateBy, username);
        ArticleScore articleScore = articleScoreService.getOne(lqw);
        return Result.ok(articleScore);
    }

    /**
     * 添加
     *
     * @param articleScore
     * @return
     */
    @AutoLog(value = "系统评价-添加/编辑")
    @ApiOperation(value = "系统评价-添加/编辑", notes = "系统评价-添加/编辑")
    @PostMapping(value = "/saveOrUpdate")
    public Result<?> saveOrUpdate(@RequestBody ArticleScore articleScore) {
        boolean ok = articleScoreService.saveOrUpdate(articleScore);
        if (ok) {
            return Result.ok("感谢您对本系统的支持！");
        }
        return Result.error("操作失败！");
    }

    /**
     * 编辑
     *
     * @param articleScore
     * @return
     */
    @AutoLog(value = "系统评价-编辑")
    @ApiOperation(value = "系统评价-编辑", notes = "系统评价-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ArticleScore articleScore) {
        articleScoreService.updateById(articleScore);
        return Result.ok("编辑成功!");
    }


}
