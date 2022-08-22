package com.logger.demo264.logrecord;

import com.logger.demo264.logrecord.context.LogRecordContext;
import com.logger.demo264.logrecord.annotation.LogRecord;
import com.logger.demo264.logrecord.event.LogRecordEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 代理
 *
 * @author zhongjiahua
 * @date 2022-07-19
 */
@Component
@Slf4j
public class LogRecordProxy {

    @LogRecord(bizNo = "anyWay", content = "当前参数为 {#username}，当前返回值是 {#_ret} ,自定义函数 {" + CustomFunctionService.FUNCTION_NAME + "{#username}}，自定义 { {#a + #b} }")
    public String log(String username) {
        // 在实际方法中新增变量
        LogRecordContext.addVariable("a", 1);
        LogRecordContext.addVariable("b", 3);
        log.info("已经调用代理方法");
        return "计算" + username;
    }

    /**
     * 日志监听器
     *
     * @param logRecordEvent {@link LogRecordEvent}
     */
    @EventListener(LogRecordEvent.class)
    public void consumerLogRecordEvent(LogRecordEvent logRecordEvent) {
        String content = logRecordEvent.getContent();
        String ip = logRecordEvent.getIp();
        Integer operatorId = logRecordEvent.getOperatorId();
        String bizNo = logRecordEvent.getBizNo();
        log.info("【{}】【{}：{}】{}", bizNo, operatorId, ip, content);
    }
}
