package com.logger.demo264.logrecord.expression.handle;

import cn.hutool.core.util.StrUtil;
import com.logger.demo264.logrecord.expression.parse.FunctionMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 函数表达式解析器
 *
 * @author 风尘
 * @date 2022-07-15
 */
@RequiredArgsConstructor
public class StandardFunctionExpressionHandler implements FunctionExpressionHandler {
    private final ConcurrentHashMap<String, FunctionHandler> FUNCTION_EXPRESSION_CACHE = new ConcurrentHashMap<>();
    private final ParseFunctionFactory parseFunctionFactory;
    private ExpressionParser parser;

    @Override
    public void precompile(FunctionMetadata functionMetadata) {
        // 找到是哪个解析器解析并预先解析
        FunctionHandler functionHandler = buildFunctionHandle(functionMetadata);
        FUNCTION_EXPRESSION_CACHE.putIfAbsent(functionMetadata.getFunctionExpression(), functionHandler);
    }

    @Override
    public boolean executeBefore(FunctionMetadata functionMetadata) {
        FunctionHandler functionHandler = FUNCTION_EXPRESSION_CACHE.computeIfAbsent(functionMetadata.getFunctionExpression(),
                key -> buildFunctionHandle(functionMetadata));
        return functionHandler.getIFunctionService().executeBefore();
    }

    @Override
    public Object getValue(FunctionMetadata functionMetadata, EvaluationContext context) {
        FunctionHandler functionHandler = FUNCTION_EXPRESSION_CACHE.computeIfAbsent(functionMetadata.getFunctionExpression(),
                key -> buildFunctionHandle(functionMetadata));
        IFunctionService iFunctionService = functionHandler.getIFunctionService();
        return iFunctionService.getValue(context, functionMetadata, functionHandler.getFunctionParamExpressions());
    }

    @Override
    public void setExpressionParser(ExpressionParser parser) {
        this.parser = parser;
    }

    private FunctionHandler buildFunctionHandle(FunctionMetadata functionMetadata) {
        IFunctionService functionService = parseFunctionFactory.findFunctionService(functionMetadata);
        if (Objects.isNull(functionService)) {
            throw new IllegalArgumentException(StrUtil.format("can't find IFunctionService to deal with {}", functionMetadata.getFunctionExpression()));
        }
        List<String> spelParams = functionMetadata.getSpelParams();
        List<Expression> functionParamExpressions = Optional.ofNullable(spelParams).orElse(Collections.emptyList()).stream()
                .map(parser::parseExpression)
                .collect(Collectors.toList());
        return new FunctionHandler()
                .setFunctionParamExpressions(functionParamExpressions)
                .setIFunctionService(functionService);
    }


    @Getter
    @Setter
    @Accessors(chain = true)
    static class FunctionHandler {
        /**
         * spel参数解析成的expression
         */
        private List<Expression> functionParamExpressions;
        /**
         * 对应的方法执行器
         */
        private IFunctionService IFunctionService;
    }
}
