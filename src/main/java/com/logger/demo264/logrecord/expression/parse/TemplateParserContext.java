package com.logger.demo264.logrecord.expression.parse;

/**
 * 模板解析{@link ParserContext}可配置的实现
 *
 * @author 风尘
 * @date 2022-07-13
 */
public class TemplateParserContext implements ParserContext {
    private final String expressionPrefix;

    private final String expressionSuffix;

    public TemplateParserContext() {
        this("{", "}");
    }

    public TemplateParserContext(String expressionPrefix, String expressionSuffix) {
        this.expressionPrefix = expressionPrefix;
        this.expressionSuffix = expressionSuffix;
    }

    @Override
    public boolean isTemplate() {
        return true;
    }

    @Override
    public String getExpressionPrefix() {
        return this.expressionPrefix;
    }

    @Override
    public String getExpressionSuffix() {
        return this.expressionSuffix;
    }
}
