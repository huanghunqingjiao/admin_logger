package com.logger.demo264.logrecord.expression.parse;

import org.springframework.expression.EvaluationContext;

/**
 * 表示字符串的文本表达式接口
 *
 * @author 风尘
 * @date 2022-07-13
 */
public class LiteralExpression implements Expression {

    /**
     * 原字符串
     */
    private final String originString;
    /**
     * 剪切后的字符串
     */
    private final String literalValue;

    public LiteralExpression(String originString, String literalValue) {
        this.originString = originString;
        this.literalValue = literalValue;
    }

    @Override
    public String getOriginString() {
        return this.originString;
    }

    @Override
    public String getExpressionString() {
        return this.literalValue;
    }

    @Override
    public void compile() {

    }

    @Override
    public Object getValue(EvaluationContext context) {
        return getExpressionString();
    }
}
