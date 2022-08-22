package com.logger.demo264.logrecord.expression.handle;

import cn.hutool.core.collection.CollUtil;
import com.logger.demo264.logrecord.expression.parse.FunctionMetadata;
import lombok.Getter;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 自定义解析器的工厂类
 *
 * @author 风尘
 * @date 2022-07-15
 */
public class ParseFunctionFactory {
    @Getter
    private List<IFunctionService> functionServices;

    public ParseFunctionFactory(List<IFunctionService> functionServices) {
        this.functionServices = functionServices;
    }

    /**
     * 找到合适的自定义函数执行方法
     *
     * @param functionMetadata 函数
     * @return 执行类
     */
    public IFunctionService findFunctionService(FunctionMetadata functionMetadata) {
        if (CollUtil.isNotEmpty(functionServices)) {
            return functionServices.stream()
                    .filter(iFunctionService -> iFunctionService.executeFunctionEnable(functionMetadata))
                    .findAny()
                    .orElse(null);
        }
        return null;
    }


    @PostConstruct
    public void afterConstruct() {
        if (Objects.nonNull(functionServices)) {
            this.functionServices = Collections.unmodifiableList(functionServices);
        }
    }
}
