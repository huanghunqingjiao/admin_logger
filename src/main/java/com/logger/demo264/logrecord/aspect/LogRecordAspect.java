package com.logger.demo264.logrecord.aspect;

import com.logger.demo264.logrecord.annotation.LogRecord;
import com.logger.demo264.logrecord.handle.ILogRecordHandler;
import com.logger.demo264.logrecord.metadata.LogRecordMethodMetadata;
import com.logger.demo264.logrecord.metadata.MethodMetaData;
import com.logger.demo264.logrecord.util.MethodUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志处理类解析处理器
 *
 * @author 风尘
 * @date 2022-07-18
 */
@Aspect
@RequiredArgsConstructor
public class LogRecordAspect {
    private final Map<String, LogRecord> LOG_RECORD_CACHE = new ConcurrentHashMap<>();
    private final ILogRecordHandler iLogRecordHandler;

    @Pointcut("@annotation(com.logger.demo264.logrecord.annotation.LogRecord)")
    private void packageMethod() {
    }


    @Around("packageMethod()")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodMetaData<LogRecord> logRecordMethodMetaData = this.buildMethodMetadata(pjp);
        return iLogRecordHandler.proceed(pjp, logRecordMethodMetaData);
    }

    private MethodMetaData<LogRecord> buildMethodMetadata(JoinPoint jp) {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        String classMethodName = MethodUtils.getClassMethodName(method);
        LogRecord logRecord = LOG_RECORD_CACHE.computeIfAbsent(classMethodName, key -> method.getAnnotation(LogRecord.class));

        return new LogRecordMethodMetadata(method, jp::getArgs, logRecord);
    }

}
