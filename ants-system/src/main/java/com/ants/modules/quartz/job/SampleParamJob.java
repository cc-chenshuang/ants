package com.ants.modules.quartz.job;

import com.ants.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 示例带参定时任务
 *
 * @Author chen
 */
@Slf4j
public class SampleParamJob implements Job {

    /**
     * 若参数变量名修改 QuartzJobController中也需对应修改
     */
    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info(String.format("welcome %s! Ants 带参数定时任务 SampleParamJob !   时间:" + DateUtils.now(), this.parameter));
    }
}
