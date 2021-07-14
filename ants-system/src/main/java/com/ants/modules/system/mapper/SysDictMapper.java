package com.ants.modules.system.mapper;

import com.ants.modules.system.entity.SysDict;
import com.ants.modules.system.vo.DictModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TODO
 * Author Chen
 * Date   2021/3/1 9:52
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

    List<DictModel> getDictItemByCode(@Param("dictCode") String dictCode);
}
