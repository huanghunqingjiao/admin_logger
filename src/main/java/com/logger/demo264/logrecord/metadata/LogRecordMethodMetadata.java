package com.logger.demo264.logrecord.metadata;

import com.logger.demo264.logrecord.annotation.LogRecord;
import com.logger.demo264.logrecord.util.MethodUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * 日志方法信息
 *
 * @author 风尘
 * @date 2022-07-18
 */
public class LogRecordMethodMetadata implements MethodMetaData<LogRecord> {
    /**
     * 方法
     */
    private final Method method;
    /**
     * 当前运行时方法中的参数
     */
    private final Supplier<Object[]> argsSupplier;
    /**
     * 当前方法的全称
     * 使用{@link MethodUtils#getClassMethodName(Method)}方法获得
     */
    private final String classMethodName;
    /**
     * 分布式锁所用到的注解
     */
    private final LogRecord logRecord;

    public LogRecordMethodMetadata(Method method, Supplier<Object[]> argsSupplier, LogRecord logRecord) {
        Assert.notNull(method, "'method' cannot be null");
        Assert.notNull(argsSupplier, "'argsSupplier' cannot be null");
        Assert.notNull(logRecord, "'logRecord' cannot be null");

        this.method = method;
        this.argsSupplier = argsSupplier;
        this.logRecord = logRecord;
        this.classMethodName = MethodUtils.getClassMethodName(method);
    }

    @Override
    public String getClassMethodName() {
        return this.classMethodName;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArgs() {
        return argsSupplier.get();
    }

    @Override
    public LogRecord getAnnotation() {
        return this.logRecord;
    }
}
