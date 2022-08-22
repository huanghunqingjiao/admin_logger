package com.logger.demo264.logrecord.expression.handle;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

/**
 * 解析spel表达式
 *
 * @author 风尘
 * @date 2022-07-15
 */
public interface SpelExpressionHandler {

    /**
     * 预编译表达式，提前校验错误
     *
     * @param expression 表达式
     */
    void precompile(String expression);

    /**
     * 通过表达式，获取值
     *
     * @param expression 表达式
     * @param context    内容
     * @return result
     */
    Object getValue(String expression, EvaluationContext context);

    /**
     * 设置解析器
     *
     * @param parser 设置解析器
     */
    void setExpressionParser(ExpressionParser parser);
}
