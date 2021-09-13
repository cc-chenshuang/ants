package com.ants.common.annotation;

import java.lang.annotation.*;

/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoQuartzLog {
    /**
     * 任务内容
     *
     */
    String value() default "";

    /**
     * 任务名称
     *
     */
    String quartzName() default "";

}
