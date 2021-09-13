package com.ants.modules.quartz.threadpool;

import cn.hutool.core.thread.NamedThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
@Configuration
public class RiskQuotaJobThreadPool {

    @Bean("riskQuotaJobThreadPoolExecutor")
    public ThreadPoolExecutor riskQuotaJobThreadPoolExecutor() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() * 2,
                Runtime.getRuntime().availableProcessors() * 4,
                30000,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(50),
                new NamedThreadFactory("RiskQuotaJobThreadPool_", true),
                new ThreadPoolExecutor.CallerRunsPolicy());

        return executor;
    }
}
