package com.ants.modules.system.service;

import com.ants.modules.system.entity.SysDictItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public interface ISysDictItemService extends IService<SysDictItem> {
    public List<SysDictItem> selectItemsByMainId(String mainId);
}
