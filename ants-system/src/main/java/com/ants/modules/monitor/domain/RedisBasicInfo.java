package com.ants.modules.monitor.domain;

import lombok.Data;

/**
 * TODO
 * Author Chen
 * Date   2021/3/26 18:05
 */
@Data
public class RedisBasicInfo {
    private String version;
    private String runMode;
    private String port;
    private String clientNum;
    private String runTime;
    private String useMemory;
    private String useCpu;
    private String memoryConfig;
    private String aof;
    private String rdb;
    private String keyNum;
    private String networkPortal;
    private String networkExport;
}
