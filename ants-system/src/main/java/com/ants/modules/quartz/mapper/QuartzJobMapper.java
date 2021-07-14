package com.ants.modules.quartz.mapper;

import java.util.List;

import com.ants.modules.quartz.entity.QuartzJob;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 定时任务在线管理
 * @Author: chen
 * @Date:  2021-03-11
 */
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

	public List<QuartzJob> findByJobClassName(@Param("jobClassName") String jobClassName);

}
