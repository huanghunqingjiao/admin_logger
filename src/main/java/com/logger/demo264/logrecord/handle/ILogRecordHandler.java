package com.logger.demo264.logrecord.handle;

import com.logger.demo264.logrecord.metadata.MethodMetaData;
import com.logger.demo264.logrecord.annotation.LogRecord;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 日志处理器
 *
 * @author 风尘
 * @date 2022-07-18
 */
public interface ILogRecordHandler {


    /**
     * 继续执行
     *
     * @param pjp            {@link ProceedingJoinPoint}
     * @param methodMetadata {@link LogRecord}
     * @return Object
     */
    Object proceed(ProceedingJoinPoint pjp, MethodMetaData<LogRecord> methodMetadata) throws Throwable;
}
