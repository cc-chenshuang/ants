package com.ants.modules.system.service.impl;

import com.ants.modules.system.entity.Demo;
import com.ants.modules.system.mapper.DemoMapper;
import com.ants.modules.system.service.DemoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, Demo> implements DemoService {

}
