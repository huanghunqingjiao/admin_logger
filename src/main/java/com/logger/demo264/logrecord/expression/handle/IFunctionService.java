package com.logger.demo264.logrecord.expression.handle;

import com.logger.demo264.logrecord.expression.parse.FunctionMetadata;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;

import java.util.List;

/**
 * 自定义函数执行器
 *
 * @author 风尘
 * @date 2022-07-15
 */
public interface IFunctionService {

    /**
     * 是否在实际方法运行之前执行
     *
     * @return true：在实际方法运行之前执行，false：将在方法运行之后执行
     */
    boolean executeBefore();

    /**
     * 能否执行此自定义方法
     *
     * @param functionMetadata 自定义方法
     * @return true or false
     */
    boolean executeFunctionEnable(FunctionMetadata functionMetadata);


    /**
     * 获取表达式中的值
     *
     * @param context                  表达式
     * @param functionMetadata         基本信息
     * @param functionParamExpressions spel解析的参数表达式
     * @return 值
     */
    Object getValue(EvaluationContext context, FunctionMetadata functionMetadata, List<Expression> functionParamExpressions);

    /**
     * 设置解析器
     *
     * @param parser 解析器
     */
    void setParser(ExpressionParser parser);

}
