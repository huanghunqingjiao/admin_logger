package com.logger.demo264.logrecord;

import cn.hutool.core.util.StrUtil;
import com.logger.demo264.logrecord.expression.handle.IFunctionService;
import com.logger.demo264.logrecord.expression.parse.FunctionMetadata;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 自定义函数
 *
 * @author zhongjiahua
 * @date 2022-07-19
 */
@Component
public class CustomFunctionService implements IFunctionService {
    /**
     * 函数名
     */
    public final static String FUNCTION_NAME = "customFunctionName";

    @Override
    public boolean executeBefore() {
        return false;
    }

    @Override
    public boolean executeFunctionEnable(FunctionMetadata functionMetadata) {
        String functionName = functionMetadata.getFunctionName();
        List<String> spelParams = functionMetadata.getSpelParams();
        return StrUtil.equals(functionName, FUNCTION_NAME) && Objects.equals(1, spelParams.size());
    }

    @Override
    public Object getValue(EvaluationContext context, FunctionMetadata functionMetadata, List<Expression> functionParamExpressions) {
        Expression expression = functionParamExpressions.get(0);
        String value = String.valueOf(expression.getValue(context));

        return "计算后的结果" + value;
    }

    @Override
    public void setParser(ExpressionParser parser) {
    }
}
