package com.logger.demo264.logrecord.expression.parse;

import com.logger.demo264.logrecord.expression.handle.SpelExpressionHandler;
import com.logger.demo264.logrecord.expression.exception.ParseException;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 表示SPEL的表达式接口
 *
 * @author 风尘
 * @date 2022-07-14
 */
public class SpelExpression implements Expression {
    /**
     * 原字符串
     */
    private final String originString;
    /**
     * 剪切后的字符串
     */
    private final String spelExpressionString;


    /**
     * 解析器
     */
    private final SpelExpressionHandler spelExpressionHandler;

    public SpelExpression(String originString, String spelExpressionString, SpelExpressionHandler spelExpressionHandler) {
        this.originString = originString;
        this.spelExpressionString = spelExpressionString;
        this.spelExpressionHandler = spelExpressionHandler;
    }

    @Override
    public String getOriginString() {
        return this.originString;
    }

    @Override
    public String getExpressionString() {
        return this.spelExpressionString;
    }

    @Override
    public void compile() {
        spelExpressionHandler.precompile(spelExpressionString);
    }

    @Override
    public Object getValue(EvaluationContext context) {
        if (Objects.nonNull(spelExpressionHandler) && StringUtils.hasText(spelExpressionString)) {
            try {
                return spelExpressionHandler.getValue(spelExpressionString, context);
            } catch (Exception e) {
                throw new ParseException(originString, spelExpressionString, e);
            }
        }
        return null;
    }


}
