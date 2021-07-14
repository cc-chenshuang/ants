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
}
