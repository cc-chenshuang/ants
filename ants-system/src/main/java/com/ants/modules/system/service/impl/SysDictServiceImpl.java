package com.ants.modules.system.service.impl;

import com.ants.common.constant.CacheConstant;
import com.ants.modules.system.entity.SysDict;
import com.ants.modules.system.mapper.SysDictMapper;
import com.ants.modules.system.service.SysDictService;
import com.ants.modules.system.vo.DictModel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 * Author Chen
 * Date   2021/3/1 9:54
 */
@Slf4j
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Autowired
    SysDictMapper sysDictMapper;

    @Override
//    @Cacheable(value = CacheConstant.SYS_DICT_CACHE, key = "#code")
    public List<DictModel> getDictItemByCode(String dictCode) {
        log.info("无缓存dictCache的时候调用这里！");
        List<DictModel> list = sysDictMapper.getDictItemByCode(dictCode);
        return list;
    }
}
