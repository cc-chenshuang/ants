package com.ants.modules.system.mapper;

import com.ants.modules.system.entity.SysCategory;
import com.ants.modules.system.model.TreeSelectModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * TODO   分类字典
 * Author Chen
 * Date   2021/9/7 18:44
 */
public interface SysCategoryMapper extends BaseMapper<SysCategory> {

	/**
	  *  根据父级ID查询树节点数据
	 * @param pid
	 * @return
	 */
	public List<TreeSelectModel> queryListByPid(@Param("pid") String pid, @Param("query") Map<String, String> query);

	@Select("SELECT ID FROM sys_category WHERE CODE = #{code,jdbcType=VARCHAR}")
	public String queryIdByCode(@Param("code") String code);


}
