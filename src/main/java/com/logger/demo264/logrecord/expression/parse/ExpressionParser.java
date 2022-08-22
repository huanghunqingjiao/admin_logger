package com.logger.demo264.logrecord.expression.parse;

import com.logger.demo264.logrecord.expression.exception.ParseException;

import java.util.List;

/**
 * 解析表达式
 *
 * @author 风尘
 */
public interface ExpressionParser {


    /**
     * 解析表达式，默认使用{@link ParserContext#TEMPLATE_EXPRESSION}模板，返回解析的表达式
     *
     * @param expressionString 表达式文字
     * @return 解析后的表达式构成列表
     * @throws ParseException 解析时出现的异常
     */
    default List<Expression> parseExpressions(String expressionString) throws ParseException {
        return parseExpressions(expressionString, ParserContext.TEMPLATE_EXPRESSION);
    }

    /**
     * 解析表达式
     *
     * @param expressionString 表达式文字
     * @param context          解析模板
     * @return 解析后的表达式列表
     * @throws ParseException 解析时出现的异常
     */
    List<Expression> parseExpressions(String expressionString, ParserContext context) throws ParseException;

}