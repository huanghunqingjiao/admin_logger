package com.logger.demo264.logrecord.expression.handle;

import com.logger.demo264.logrecord.expression.parse.FunctionMetadata;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;

/**
 * 自定义函数表达式解析器
 * 解析自定义函数表达式
 *
 * @author 风尘
 * @date 2022-07-14
 */
public interface FunctionExpressionHandler {
    /**
     * 预编译表达式，提前校验错误
     *
     * @param functionMetadata 自定义函数包装实体
     */
    void precompile(FunctionMetadata functionMetadata);

    /**
     * 是否在实际方法运行之前执行
     *
     * @return true：在实际方法运行之前执行，false：将在方法运行之后执行
     */
    boolean executeBefore(FunctionMetadata functionMetadata);

    /**
     * 通过表达式获取值
     *
     * @param functionMetadata 函数表达式
     * @param context          内容
     * @return 结果
     */
    Object getValue(FunctionMetadata functionMetadata, EvaluationContext context);

    /**
     * 设置Spel解析器
     *
     * @param parser 解析器
     */
    void setExpressionParser(ExpressionParser parser);
}
