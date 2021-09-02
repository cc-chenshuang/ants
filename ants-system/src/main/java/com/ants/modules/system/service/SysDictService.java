package com.ants.modules.system.service;

import com.ants.modules.system.entity.SysDict;
import com.ants.modules.system.vo.DictModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * TODO
 *
 * @author Chen
 * @date 2021/3/1 9:53
 */
public interface SysDictService extends IService<SysDict> {
    List<DictModel> getDictItemByCode(String dictCode);

    String queryTableDictTextByKey(String table, String text, String code, String trim);

    String queryDictTextByKey(String code, String trim);

    @Deprecated
    List<DictModel> queryTableDictItemsByCodeAndFilter(String param, String param1, String param2, String param3);

    @Deprecated
    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

    public List<DictModel> queryDictItemsByCode(String code);
}
