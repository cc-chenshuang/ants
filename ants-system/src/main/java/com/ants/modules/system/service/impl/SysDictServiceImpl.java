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
    @Cacheable(value = CacheConstant.SYS_DICT_CACHE, key = "#code")
    public List<DictModel> getDictItemByCode(String dictCode) {
        log.info("无缓存dictCache的时候调用这里！");
        List<DictModel> list = sysDictMapper.getDictItemByCode(dictCode);
        return list;
    }

    /**
     * 通过查询指定table的 text code 获取字典值text
     * dictTableCache采用redis缓存有效期10分钟
     * @param table
     * @param text
     * @param code
     * @param key
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_TABLE_CACHE)
    public String queryTableDictTextByKey(String table,String text,String code, String key) {
        log.info("无缓存dictTable的时候调用这里！");
        return sysDictMapper.queryTableDictTextByKey(table,text,code,key);
    }

    /**
     * 通过查询指定code 获取字典值text
     * @param code
     * @param key
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_CACHE,key = "#code+':'+#key")
    public String queryDictTextByKey(String code, String key) {
        log.info("无缓存dictText的时候调用这里！");
        return sysDictMapper.queryDictTextByKey(code, key);
    }

    @Override
    public List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql) {
        log.info("无缓存dictTableList的时候调用这里！");
        return sysDictMapper.queryTableDictItemsByCodeAndFilter(table,text,code,filterSql);
    }

    /**
     * 通过查询指定table的 text code 获取字典
     * dictTableCache采用redis缓存有效期10分钟
     * @param table
     * @param text
     * @param code
     * @return
     */
    @Override
    //@Cacheable(value = CacheConstant.SYS_DICT_TABLE_CACHE)
    public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
        log.info("无缓存dictTableList的时候调用这里！");
        return sysDictMapper.queryTableDictItemsByCode(table,text,code);
    }

    /**
     * 通过查询指定code 获取字典
     * @param code
     * @return
     */
    @Override
    @Cacheable(value = CacheConstant.SYS_DICT_CACHE,key = "#code")
    public List<DictModel> queryDictItemsByCode(String code) {
        log.info("无缓存dictCache的时候调用这里！");
        return sysDictMapper.queryDictItemsByCode(code);
    }
}
