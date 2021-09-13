package com.ants.common.utils;

import lombok.extern.slf4j.Slf4j;


/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
@Slf4j
public class TenantContext {

    private static ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setTenant(String tenant) {
        log.debug(" setting tenant to " + tenant);
        currentTenant.set(tenant);
    }

    public static String getTenant() {
        return currentTenant.get();
    }

    public static void clear(){
        currentTenant.remove();
    }
}
