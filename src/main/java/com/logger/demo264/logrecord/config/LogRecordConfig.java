package com.logger.demo264.logrecord.config;

import com.logger.demo264.logrecord.aspect.LogRecordAspect;
import com.logger.demo264.logrecord.expression.handle.*;
import com.logger.demo264.logrecord.expression.parse.ExpressionParser;
import com.logger.demo264.logrecord.expression.parse.TemplateExpressionParser;
import com.logger.demo264.logrecord.handle.ILogRecordHandler;
import com.logger.demo264.logrecord.handle.StandardLogRecordHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 配置类
 *
 * @author 风尘
 * @date 2022-07-19
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class LogRecordConfig {
    SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

    @Bean
    public LogRecordAspect logRecordAspect(ILogRecordHandler logRecordHandler) {
        return new LogRecordAspect(logRecordHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public StandardLogRecordHandler standardLogRecordHandler(ExpressionParser expressionParser, ApplicationEventPublisher applicationEventPublisher) {
        return new StandardLogRecordHandler(expressionParser, applicationEventPublisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExpressionParser templateExpressionParser(SpelExpressionHandler spelExpressionHandler, FunctionExpressionHandler functionExpressionHandler) {
        return new TemplateExpressionParser(spelExpressionHandler, functionExpressionHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpelExpressionHandler spelExpressionHandler() {
        StandardSpelExpressionHandler standardSpelExpressionHandler = new StandardSpelExpressionHandler();
        standardSpelExpressionHandler.setExpressionParser(spelExpressionParser);
        return standardSpelExpressionHandler;
    }

    @Bean
    @ConditionalOnMissingBean
    public FunctionExpressionHandler functionExpressionHandler(ParseFunctionFactory parseFunctionFactory) {
        StandardFunctionExpressionHandler standardFunctionExpressionHandler = new StandardFunctionExpressionHandler(parseFunctionFactory);
        standardFunctionExpressionHandler.setExpressionParser(spelExpressionParser);
        return standardFunctionExpressionHandler;
    }

    @Bean
    @ConditionalOnMissingBean
    public ParseFunctionFactory parseFunctionFactory(Optional<List<IFunctionService>> functionServicesOptional) {
        List<IFunctionService> functionServices = functionServicesOptional.orElse(Collections.emptyList());
        functionServices.forEach(functionService -> functionService.setParser(spelExpressionParser));
        return new ParseFunctionFactory(functionServices);
    }

}
