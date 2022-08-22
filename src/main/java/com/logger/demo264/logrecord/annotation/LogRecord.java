package com.logger.demo264.logrecord.annotation;

import java.lang.annotation.*;

/**
 * 日志操作记录
 * @author 风尘
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecord {
    /**
     * 记录操作日志的修改详情
     *
     * @return 修改详情
     */
    String content();

    /**
     * 操作日志失败的文本版本
     *
     * @return 失败文本
     */
    String fail() default "";

    /**
     * 操作日志的执行人
     *
     * @return 执行人，可以自动搜寻
     */
    String operator() default "";

    /**
     * 操作日志绑定的业务对象标识
     *
     * @return 业务标识对象
     */
    String bizNo();

    /**
     * 操作日志的种类
     *
     * @return 种类
     */
    String category() default "";


}