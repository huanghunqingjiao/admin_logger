package com.logger.demo264.logrecord.expression.handle;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Spel表达式处理器
 *
 * @author 风尘
 * @date 2022-07-14
 */
@RequiredArgsConstructor
@Slf4j
public class StandardSpelExpressionHandler implements SpelExpressionHandler {
    private final ConcurrentHashMap<String, Expression> expressionCache = new ConcurrentHashMap<>();

    private ExpressionParser parser;

    @Override
    public void precompile(String expression) {
        expressionCache.computeIfAbsent(expression, this::compileExpression);
    }

    @Override
    public Object getValue(String expression, EvaluationContext context) {
        Expression spelExpression = expressionCache.computeIfAbsent(expression, this::compileExpression);
        return spelExpression.getValue(context);
    }

    @Override
    public void setExpressionParser(ExpressionParser parser) {
        this.parser = parser;
    }

    private Expression compileExpression(String key) {
        return parser.parseExpression(key);
    }
}
