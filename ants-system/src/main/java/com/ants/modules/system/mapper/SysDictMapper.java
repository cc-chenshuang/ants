package com.ants.modules.system.mapper;

import com.ants.modules.system.entity.SysDict;
import com.ants.modules.system.model.DuplicateCheckVo;
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

    Long duplicateCheckCountSql(DuplicateCheckVo duplicateCheckVo);

    Long duplicateCheckCountSqlNoDataId(DuplicateCheckVo duplicateCheckVo);

    @Deprecated
    public String queryTableDictTextByKey(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("key") String key);

    public String queryDictTextByKey(@Param("code") String code, @Param("key") String key);

    @Deprecated
    public List<DictModel> queryTableDictItemsByCodeAndFilter(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("filterSql") String filterSql);

    @Deprecated
    public List<DictModel> queryTableDictItemsByCode(@Param("table") String table, @Param("text") String text, @Param("code") String code);

    List<DictModel> queryDictItemsByCode(@Param("code") String code);
}
