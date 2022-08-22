package com.logger.demo264.logrecord.expression.parse;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 解析工具类
 *
 * @author 风尘
 * @date 2022-07-14
 */
public class ParseUtils {

    /**
     * 是否是自定义函数表达式
     *
     * @param expression 表达式
     * @return true or false
     */
    public static boolean isFunctionExpression(String expression) {
        if (StrUtil.isBlank(expression)) {
            return false;
        }
        expression = StrUtil.trim(expression);
        String functionName = StrUtil.subBefore(expression, '{', false);
        if (StrUtil.isBlank(functionName)) {
            return false;
        }
        for (char ch : functionName.toCharArray()) {
            if (!CharUtil.isNumber(ch) && !CharUtil.isLetter(ch)) {
                return false;
            }
        }
        return true;
    }
}
