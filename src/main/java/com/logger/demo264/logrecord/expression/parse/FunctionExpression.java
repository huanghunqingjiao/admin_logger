package com.logger.demo264.logrecord.expression.parse;

import com.logger.demo264.logrecord.expression.exception.ParseException;
import com.logger.demo264.logrecord.expression.handle.FunctionExpressionHandler;
import org.springframework.expression.EvaluationContext;

/**
 * 自定义函数文本表达式
 * 函数有固定格式
 * <pre class="code">
 *      functionName{#userId,#userName}
 * </pre>
 * 函数名{}的形式，参数使用英文逗号分隔
 *
 * @author 风尘
 * @date 2022-07-14
 */
public class FunctionExpression implements Expression {
    /**
     * 原字符串
     */
    private final String originString;
    /**
     * 剪切后的字符串
     */
    private final String functionExpressionString;

    private final FunctionMetadata functionMetadata;

    /**
     * 函数处理类
     */
    private final FunctionExpressionHandler functionExpressionHandler;

    public FunctionExpression(String originString, String functionExpressionString, FunctionExpressionHandler functionExpressionHandler) {
        this.originString = originString;
        this.functionExpressionString = functionExpressionString;
        try {
            this.functionMetadata = new StandardFunctionMetadata(functionExpressionString);
        } catch (Exception e) {
            throw new ParseException(originString, functionExpressionString, e);
        }
        this.functionExpressionHandler = functionExpressionHandler;
    }

    @Override
    public String getOriginString() {
        return this.originString;
    }

    @Override
    public String getExpressionString() {
        return this.functionExpressionString;
    }

    @Override
    public void compile() {
        functionExpressionHandler.precompile(functionMetadata);
    }

    /**
     * 是否需要提前执行，比如有些方法需要在修改前查询
     *
     * @return true
     */
    public boolean executeBefore() {
        return functionExpressionHandler.executeBefore(functionMetadata);
    }

    @Override
    public Object getValue(EvaluationContext context) {
        return functionExpressionHandler.getValue(functionMetadata, context);
    }

}
