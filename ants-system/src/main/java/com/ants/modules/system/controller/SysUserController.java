package com.ants.modules.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ants.common.constant.CommonConstant;
import com.ants.common.query.QueryGenerator;
import com.ants.common.result.Result;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        result.setData(pageList);
        log.info(pageList.toString());
        return result;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<SysUser> add(@RequestBody JSONObject jsonObject) {
        Result<SysUser> result = new Result<SysUser>();
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
//            sysUserService.addUserWithRole(user, selectedRoles);
//            sysUserService.addUserWithDepart(user, selectedDeparts);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }
}
