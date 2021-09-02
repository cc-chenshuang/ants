package com.ants.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ants.common.constant.CommonConstant;
import com.ants.common.system.query.QueryGenerator;
import com.ants.common.system.result.Result;
import com.ants.common.utils.PasswordUtil;
import com.ants.common.utils.oConvertUtils;
import com.ants.modules.system.entity.SysUser;
import com.ants.modules.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * TODO
 * Author Chen
 * Date   2021/7/15 18:27
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    SysUserService sysUserService;

    /**
     * 获取用户列表数据
     *
     * @param user
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<IPage<SysUser>> queryPageList(SysUser user, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
        String realname = user.getRealname();
        user.setRealname("");
        QueryWrapper<SysUser> queryWrapper = QueryGenerator.initQueryWrapper(user, req.getParameterMap());
        //TODO 外部模拟登陆临时账号，列表不显示
//        queryWrapper.ne("username","_reserve_user_external");
        if (!StringUtils.isEmpty(realname)) {
            queryWrapper.like("realname", realname);
        }
        Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
        IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);

        result.setSuccess(true);
        result.setResult(pageList);
        result.setCode(200);
        log.info(pageList.toString());
        return result;
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody JSONObject jsonObject) {
        String selectedRoles = jsonObject.getString("selectedroles");
        String selectedDeparts = jsonObject.getString("selecteddeparts");
        try {
            SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
            user.setCreateTime(new Date());//设置创建时间
            String salt = oConvertUtils.randomGen(8);
            user.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
            user.setPassword(passwordEncode);
            user.setStatus(1);
            user.setDelFlag(CommonConstant.DEL_FLAG_0);
            sysUserService.addUserWithRole(user, selectedRoles);
            sysUserService.addUserWithDepart(user, selectedDeparts);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("操作失败");
        }
        return Result.ok("添加成功！");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public Result<?> edit(@RequestBody JSONObject jsonObject) {
        try {
            SysUser sysUser = sysUserService.getById(jsonObject.getString("id"));
            if (sysUser == null) {
                return Result.error("未找到对应实体");
            } else {
                SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
                user.setUpdateTime(new Date());
                //String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), sysUser.getSalt());
                user.setPassword(sysUser.getPassword());
                String roles = jsonObject.getString("selectedroles");
                String departs = jsonObject.getString("selecteddeparts");
                sysUserService.editUserWithRole(user, roles);
                sysUserService.editUserWithDepart(user, departs);

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error("操作失败");
        }
        return Result.ok("修改成功!");
    }
}
