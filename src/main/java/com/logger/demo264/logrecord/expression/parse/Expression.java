package com.logger.demo264.logrecord.expression.parse;

import org.springframework.expression.EvaluationContext;

/**
 * 表达式
 *
 * @author 风尘
 */
public interface Expression {

    /**
     * 获得原本的字符串
     *
     * @return 原本字符串
     */
    String getOriginString();

    /**
     * 获取当前表达式中需解析的字符串
     *
     * @return 需解析的字符串
     */
    String getExpressionString();

    /**
     * 编译
     */
    void compile();

    /**
     * 获取值
     *
     * @return 获取值
     */
    Object getValue(EvaluationContext context);
}