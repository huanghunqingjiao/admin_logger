package com.logger.demo264.logrecord.expression.parse;

import cn.hutool.core.util.StrUtil;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义函数包裹的数据
 *
 * @author 风尘
 * @date 2022-07-14
 */
public class StandardFunctionMetadata implements FunctionMetadata {
    /**
     * 待解析的功能实体
     */
    private final String functionExpressionString;

    /**
     * 函数名称
     */
    private String functionName;

    /**
     * 函数中的Spel中的个数
     */
    private List<String> spelStringParams;


    public StandardFunctionMetadata(String functionExpressionString) {
        this.functionExpressionString = functionExpressionString;
        parseFunctionExpression(functionExpressionString);
    }

    private void parseFunctionExpression(String functionExpressionString) {
        if (!StringUtils.hasText(functionExpressionString)) {
            throw new IllegalArgumentException("functionExpressionString must not be blank!");
        }
        functionExpressionString = StrUtil.trim(functionExpressionString);
        if (!ParseUtils.isFunctionExpression(functionExpressionString)) {
            throw new IllegalArgumentException("functionExpressionString [{}] is not function!");
        }
        this.functionName = StrUtil.subBefore(functionExpressionString, '{', false);

        String subString = StrUtil.subAfter(functionExpressionString, '{', false);
        subString = StrUtil.subBefore(subString, '}', true);
        if (StringUtils.hasText(subString)) {
            this.spelStringParams = Arrays.stream(subString.split(","))
                    .filter(StringUtils::hasText)
                    .map(StringUtils::trimAllWhitespace)
                    .collect(Collectors.toList());
        }


    }

    @Override
    public String getFunctionExpression() {
        return this.functionExpressionString;
    }

    @Override
    public String getFunctionName() {
        return this.functionName;
    }

    @Override
    public List<String> getSpelParams() {
        return this.spelStringParams;
    }
}
