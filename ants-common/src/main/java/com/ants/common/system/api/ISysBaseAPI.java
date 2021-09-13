package com.ants.common.system.api;

import com.alibaba.fastjson.JSONObject;
import com.ants.common.system.vo.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public interface ISysBaseAPI {

    /**
     * 日志添加
     *
     * @param LogContent  内容
     * @param logType     日志类型(0:操作日志;1:登录日志;2:定时任务)
     * @param operatetype 操作类型(1:添加;2:修改;3:删除;)
     * @param username
     * @param realname
     * @param orgCode
     */
    void addLog(String LogContent, Integer logType, Integer operatetype, String username, String realname, String orgCode);

    /**
     * 根据用户账号查询用户信息
     *
     * @param username
     * @return
     */
    public LoginUser getUserByName(String username);

    /**
     * 根据用户账号查询用户真实姓名
     *
     * @param username
     * @return
     */
    public String getRealNameByUserName(String username);

    /**
     * 根据用户id查询用户信息
     *
     * @param id
     * @return
     */
    public LoginUser getUserById(String id);

    /**
     * 通过用户账号查询角色集合
     *
     * @param username
     * @return
     */
    public List<String> getRolesByUsername(String username);

    /**
     * 通过用户账号查询部门集合
     *
     * @param username
     * @return 部门 id
     */
    List<String> getDepartIdsByUsername(String username);


    /**
     * 通过用户账号查询部门 name
     *
     * @param username
     * @return 部门 name
     */
    List<String> getDepartNamesByUsername(String username);

    /**
     * 获取当前数据库类型
     *
     * @return
     * @throws Exception
     */
    public String getDatabaseType() throws SQLException;

    /**
     * 获取数据字典
     *
     * @param code
     * @return
     */
    public List<DictModel> queryDictItemsByCode(String code);

    /**
     * 查询所有的父级字典，按照create_time排序
     */
    public List<DictModel> queryAllDict();


    /**
     * 获取表数据字典
     *
     * @param table
     * @param text
     * @param code
     * @return
     */
    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

    /**
     * 查询所有部门 作为字典信息 id -->value,departName -->text
     *
     * @return
     */
    public List<DictModel> queryAllDepartBackDictModel();

    /**
     * 查询所有部门，拼接查询条件
     *
     * @return
     */
    List<JSONObject> queryAllDepart(Wrapper wrapper);

    /**
     * 查询表字典 支持过滤数据
     *
     * @param table
     * @param text
     * @param code
     * @param filterSql
     * @return
     */
    public List<DictModel> queryFilterTableDictInfo(String table, String text, String code, String filterSql);

    /**
     * 查询指定table的 text code 获取字典，包含text和value
     *
     * @param table
     * @param text
     * @param code
     * @param keyArray
     * @return
     */
    @Deprecated
    public List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray);

    /**
     * 获取所有有效用户
     *
     * @return
     */
    public List<ComboModel> queryAllUser();

    /**
     * 获取所有有效用户 带参
     * userIds 默认选中用户
     *
     * @return
     */
    public JSONObject queryAllUser(String[] userIds, int pageNo, int pageSize);

    /**
     * 获取所有有效用户 拼接查询条件
     *
     * @return
     */
    List<JSONObject> queryAllUser(Wrapper wrapper);

    /**
     * 获取所有角色
     *
     * @return
     */
    public List<ComboModel> queryAllRole();

    /**
     * 获取所有角色 带参
     * roleIds 默认选中角色
     *
     * @return
     */
    public List<ComboModel> queryAllRole(String[] roleIds);

    /**
     * 通过用户账号查询角色Id集合
     *
     * @param username
     * @return
     */
    public List<String> getRoleIdsByUsername(String username);

    /**
     * 通过部门编号查询部门id
     *
     * @param orgCode
     * @return
     */
    public String getDepartIdsByOrgCode(String orgCode);

    /**
     * 通过部门id查询部门名称
     *
     * @param deptId
     * @return
     */
    public String getDepartNameByDeptId(String deptId);

    /**
     * 通过部门id查询部门编码
     *
     * @param deptIds
     * @return
     */
    public List<String> getDepartCodesByDeptIds(String[] deptIds);

    /**
     * 通过用户名查询部门编码
     *
     * @param userNames
     * @return
     */
    public List<String> getDepartCodesByUserNames(String[] userNames);

    /**
     * 查询上一级部门
     *
     * @param departId
     * @return
     */
    public DictModel getParentDepartId(String departId);

    /**
     * 查询所有部门
     *
     * @return
     */
    public List<SysDepartModel> getAllSysDepart();

    /**
     * 根据部门Id获取部门负责人
     *
     * @param deptId
     * @return
     */
    public List<String> getDeptHeadByDepId(String deptId);

    /**
     * 根据id获取所有参与用户
     * userIds
     *
     * @return
     */
    public List<LoginUser> queryAllUserByIds(String[] userIds);

    /**
     * 根据name获取所有参与用户
     * userNames
     *
     * @return
     */
    List<LoginUser> queryUserByNames(String[] userNames);

}
