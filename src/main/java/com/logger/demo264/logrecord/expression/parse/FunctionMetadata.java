package com.logger.demo264.logrecord.expression.parse;

import java.util.List;

/**
 * 自定义函数包装实体
 *
 * @author 风尘
 * @date 2022-07-15
 */
public interface FunctionMetadata {

    /**
     * 获得函数表达式
     *
     * @return 表达式
     */
    String getFunctionExpression();

    /**
     * 获得函数名称
     *
     * @return 自定义函数名称
     */
    String getFunctionName();

    /**
     * 函数中的参数名称
     * 也就是spel表达式
     *
     * @return 参数表达式
     */
    List<String> getSpelParams();
}
