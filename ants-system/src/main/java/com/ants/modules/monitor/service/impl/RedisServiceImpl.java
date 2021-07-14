package com.ants.modules.monitor.service.impl;

import com.ants.common.utils.oConvertUtils;
import com.ants.modules.monitor.domain.RedisBasicInfo;
import com.ants.modules.monitor.domain.RedisInfo;
import com.ants.modules.monitor.exception.RedisConnectException;
import com.ants.modules.monitor.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Redis 监控信息获取
 *
 * @Author MrBird
 */
@Service("redisService")
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * Redis详细信息
     */
    @Override
    public Map<String, Object> getRedisInfo() throws RedisConnectException {
        Properties info = redisConnectionFactory.getConnection().info();
        Map<String, Object> map = new HashMap<>();
        List<RedisInfo> infoList = new ArrayList<>();
        RedisInfo redisInfo = null;
        RedisBasicInfo redisBasicInfo = new RedisBasicInfo();
        for (Map.Entry<Object, Object> entry : info.entrySet()) {
            redisInfo = new RedisInfo();
            redisInfo.setKey(oConvertUtils.getString(entry.getKey()));
            redisInfo.setValue(oConvertUtils.getString(entry.getValue()));
            infoList.add(redisInfo);
            switch (oConvertUtils.getString(entry.getKey())) {
                case "redis_version":
                    //Redis版本
                    redisBasicInfo.setVersion(oConvertUtils.getString(entry.getValue()));
                    break;
                case "redis_mode":
                    //运行模式
                    redisBasicInfo.setRunMode("standalone".equals(oConvertUtils.getString(entry.getValue())) ? "单机" : "集群");
                    break;
                case "tcp_port":
                    //端口
                    redisBasicInfo.setPort(oConvertUtils.getString(entry.getValue()));
                    break;
                case "connected_clients":
                    //客户端数
                    redisBasicInfo.setClientNum(oConvertUtils.getString(entry.getValue()));
                    break;
                case "uptime_in_days":
                    //运行时间(天)
                    redisBasicInfo.setRunTime(oConvertUtils.getString(entry.getValue()));
                    break;
                case "used_memory_human":
                    //使用内存
                    redisBasicInfo.setUseMemory(oConvertUtils.getString(entry.getValue()));
                    break;
                case "used_cpu_user_children":
                    //使用CPU
                    redisBasicInfo.setUseCpu(oConvertUtils.getString(entry.getValue()));
                    break;
                case "maxmemory_human":
                    //内存配置
                    redisBasicInfo.setMemoryConfig(oConvertUtils.getString(entry.getValue()));
                    break;
                case "aof_enabled":
                    //AOF是否开启
                    redisBasicInfo.setAof(oConvertUtils.getString(entry.getValue()));
                    break;
                case "rdb_last_bgsave_status":
                    //RDB是否成功
                    redisBasicInfo.setRdb(oConvertUtils.getString(entry.getValue()));
                    break;
                case "instantaneous_input_kbps":
                    //网络入口
                    redisBasicInfo.setNetworkPortal(oConvertUtils.getString(entry.getValue()));
                    break;
                case "instantaneous_output_kbps":
                    //网络出口
                    redisBasicInfo.setNetworkExport(oConvertUtils.getString(entry.getValue()));
                    break;
            }
        }
        map.put("table", infoList);
        map.put("redisBasicInfo", redisBasicInfo);
        return map;
    }

    @Override
    public Map<String, Object> getKeysSize() throws RedisConnectException {
        Long dbSize = redisConnectionFactory.getConnection().dbSize();
        Map<String, Object> map = new HashMap<>();
        map.put("create_time", System.currentTimeMillis());
        map.put("dbSize", dbSize);

        log.info("--getKeysSize--: " + map.toString());
        return map;
    }

    @Override
    public Map<String, Object> getMemoryInfo() throws RedisConnectException {
        Map<String, Object> map = null;
        Properties info = redisConnectionFactory.getConnection().info();
        for (Map.Entry<Object, Object> entry : info.entrySet()) {
            String key = oConvertUtils.getString(entry.getKey());
            if ("used_memory".equals(key)) {
                map = new HashMap<>();
                map.put("used_memory", entry.getValue());
                map.put("create_time", System.currentTimeMillis());
            }
        }
        log.info("--getMemoryInfo--: " + map.toString());
        return map;
    }
}
