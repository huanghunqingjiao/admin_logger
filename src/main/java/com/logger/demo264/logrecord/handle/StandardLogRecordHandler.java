package com.logger.demo264.logrecord.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.logger.demo264.logrecord.context.LogRecordContext;
import com.logger.demo264.logrecord.context.LogRecordEvaluationContext;
import com.logger.demo264.logrecord.event.LogRecordEvent;
import com.logger.demo264.logrecord.expression.parse.*;
import com.logger.demo264.logrecord.metadata.MethodMetaData;
import com.logger.demo264.module.SecurityUser;
import com.logger.demo264.logrecord.annotation.LogRecord;
import com.logger.demo264.logrecord.expression.exception.ParseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * 标准的日志处理器
 * {@link org.springframework.context.event.EventListener}
 *
 * @author 风尘
 * @date 2022-07-18
 */
@RequiredArgsConstructor
public class StandardLogRecordHandler implements ILogRecordHandler {
    private final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    private final InheritableThreadLocal<Stack<LogContext>> LOG_CONTEXT_THREAD_LOCAL = new InheritableThreadLocal<>();
    private final ExpressionParser expressionParser;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    public Object proceed(ProceedingJoinPoint joinPoint, MethodMetaData<LogRecord> methodMetadata) throws Throwable {
        LogRecordEvent logRecordEvent = new LogRecordEvent()
                .setBizNo(methodMetadata.getAnnotation().bizNo())
                // todo：到时候设置实际的
                .setIp("127.0.0.1")
                .setOperatorId(1);
        try {
            // 在方法运行之前执行
            beforeProceed(methodMetadata);

            Object result = joinPoint.proceed();

            LogContext logContext = LOG_CONTEXT_THREAD_LOCAL.get().peek();
            LogRecordEvaluationContext logRecordEvaluationContext = logContext.getLogRecordEvaluationContext();
            logRecordEvaluationContext.setRet(result);
            // 加上用户自定义参数
            logRecordEvaluationContext.addLogRecordContextVariables();

            List<Expression> expressions = logContext.getExpressions();
            String logContent = expressions.stream()
                    .map(expression -> {
                        Object value;
                        try {
                            value = expression.getValue(logRecordEvaluationContext);
                        } catch (Exception e) {
                            throw new ParseException(expression.getOriginString(), expression.getExpressionString(), e);
                        }
                        return value;
                    }).map(String::valueOf)
                    .filter(StrUtil::isNotBlank)
                    .collect(Collectors.joining());
            logRecordEvent.setContent(logContent);

            return result;
        } finally {
            // 发送日志通知
            applicationEventPublisher.publishEvent(logRecordEvent);
            removeThreadLocal();
        }
    }

    /**
     * 在实际方法运行前执行
     *
     * @param methodMetadata 方法参数
     */
    private void beforeProceed(MethodMetaData<LogRecord> methodMetadata) {
        // 初始化thread_local
        initThreadLocal();
        LogContext logContext = new LogContext();
        LOG_CONTEXT_THREAD_LOCAL.get().push(logContext);

        LogRecord logRecord = methodMetadata.getAnnotation();
        List<Expression> expressions = expressionParser.parseExpressions(logRecord.content(), new TemplateParserContext("{", "}"));
        LogRecordEvaluationContext logRecordEvaluationContext = new LogRecordEvaluationContext(null,
                methodMetadata.getMethod(), methodMetadata.getArgs(), PARAMETER_NAME_DISCOVERER);
        // 假装设置当前操作的用户
        setCurrentUser(logRecordEvaluationContext);
        if (CollUtil.isNotEmpty(expressions)) {
            ListIterator<Expression> iterator = expressions.listIterator();
            while (iterator.hasNext()) {
                Expression expression = iterator.next();
                try {
                    // 提前运行
                    expression.compile();
                    // 运行之前的方法
                    executeBefore(logRecordEvaluationContext, iterator, expression);
                } catch (Exception e) {
                    throw new ParseException(expression.getOriginString(), expression.getExpressionString(), e);
                }
            }
        }

        logContext.setExpressions(expressions)
                .setLogRecordEvaluationContext(logRecordEvaluationContext);
    }

    private void setCurrentUser(LogRecordEvaluationContext logRecordEvaluationContext) {
        SecurityUser securityUser = new SecurityUser()
                .setId(1)
                .setUsername("张三");
        logRecordEvaluationContext.setVariable("securityUser", securityUser);
    }

    private void executeBefore(LogRecordEvaluationContext logRecordEvaluationContext, ListIterator<Expression> iterator, Expression expression) {
        if (expression instanceof FunctionExpression) {
            FunctionExpression functionExpression = (FunctionExpression) expression;
            if (functionExpression.executeBefore()) {
                Object value;
                try {
                    value = functionExpression.getValue(logRecordEvaluationContext);
                } catch (Exception e) {
                    throw new ParseException(functionExpression.getOriginString(), functionExpression.getExpressionString(), e);
                }
                // 替换成文本形式的表达式
                iterator.set(new LiteralExpression(functionExpression.getOriginString(), String.valueOf(value)));
            }
        }
    }

    private void initThreadLocal() {
        if (Objects.isNull(LOG_CONTEXT_THREAD_LOCAL.get())) {
            LOG_CONTEXT_THREAD_LOCAL.set(new Stack<>());
        }

        LogRecordContext.init();
    }

    private void removeThreadLocal() {
        LogRecordContext.remove();

        Stack<LogContext> logContextStack = LOG_CONTEXT_THREAD_LOCAL.get();
        if (!logContextStack.isEmpty()) {
            logContextStack.pop();
        }
        if (logContextStack.isEmpty()) {
            LOG_CONTEXT_THREAD_LOCAL.remove();
        }
    }

    @Setter
    @Getter
    @ToString
    @Accessors(chain = true)
    static class LogContext {
        List<Expression> expressions;
        private LogRecordEvaluationContext logRecordEvaluationContext;
    }
}
