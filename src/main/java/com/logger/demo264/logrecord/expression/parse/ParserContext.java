package com.logger.demo264.logrecord.expression.parse;


/**
 * 模板前后缀
 *
 * @author 风尘
 */
public interface ParserContext {

    /**
     * 被解析的表达式是否是模板，例如表达式可以是
     * <pre class="code">
     * 	   Some literal text
     *     Hello {name.firstName}!
     *     {3 + 4}
     * </pre>
     *
     * @return 是否是模板
     */
    boolean isTemplate();

    /**
     * 获取模板表达式的前缀
     *
     * @return 例如 '{'
     */
    String getExpressionPrefix();

    /**
     * 获取模板的后缀
     *
     * @return 例如 '}'
     */
    String getExpressionSuffix();


    /**
     * 模板模板 ，前缀是'{',后缀是'}'
     *
     * @see #isTemplate()
     */
    ParserContext TEMPLATE_EXPRESSION = new ParserContext() {

        @Override
        public boolean isTemplate() {
            return true;
        }

        @Override
        public String getExpressionPrefix() {
            return "{";
        }

        @Override
        public String getExpressionSuffix() {
            return "}";
        }
    };

}