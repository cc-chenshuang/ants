package com.ants.common.annotation;

import java.lang.annotation.*;

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
