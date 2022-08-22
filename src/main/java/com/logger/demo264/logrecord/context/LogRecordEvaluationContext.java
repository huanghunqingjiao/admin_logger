package com.logger.demo264.logrecord.context;

import cn.hutool.core.collection.CollUtil;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author 风尘
 * @date 2022-07-18
 */
public class LogRecordEvaluationContext extends MethodBasedEvaluationContext {

    public LogRecordEvaluationContext(Object rootObject, Method method, Object[] arguments, ParameterNameDiscoverer parameterNameDiscoverer) {
        super(rootObject, method, arguments, parameterNameDiscoverer);
    }

    /**
     * 设置返回值
     *
     * @param ret 返回值数据
     */
    public void setRet(Object ret) {
        setVariable("_ret", ret);
    }

    public void addLogRecordContextVariables() {
        Map<String, Object> variables = LogRecordContext.getVariables();
        if (CollUtil.isNotEmpty(variables)) {
            variables.forEach(this::setVariable);
        }
    }
}
