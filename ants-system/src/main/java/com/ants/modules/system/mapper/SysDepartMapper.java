package com.ants.modules.system.mapper;

import com.ants.modules.system.entity.SysDepart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 *
 * @Author: Steve
 * @Since：   2019-01-22
 */
public interface SysDepartMapper extends BaseMapper<SysDepart> {

    /**
     * 根据用户名查询部门
     *
     * @param username
     * @return
     */
    public List<SysDepart> queryDepartsByUsername(@Param("username") String username);

    @Select("select id from sys_depart where org_code=#{orgCode}")
    public String queryDepartIdByOrgCode(@Param("orgCode") String orgCode);

    @Select("select depart_name from sys_depart where id=#{deptId}")
    public String getDepartNameByDeptId(@Param("deptId") String deptId);

    @Select("select * from sys_depart where ori_org_code=#{oriOrgCode}")
    public SysDepart queryDepartByOriOrgCode(@Param("oriOrgCode") String oriOrgCode);

    @Select("select id,parent_id from sys_depart where id=#{departId}")
    public SysDepart getParentDepartId(@Param("departId") String departId);

    /**
     * 通过部门id查询部门编码
     * @param deptIds
     * @return
     */
    List<String> getDepartCodesByDeptIds(@org.apache.ibatis.annotations.Param("deptIds") String[] deptIds);
    /**
     * 通过用户名查询部门编码
     * @param userNames
     * @return
     */
    List<String> getDepartCodesByUserNames(@org.apache.ibatis.annotations.Param("userNames") String[] userNames);

}
