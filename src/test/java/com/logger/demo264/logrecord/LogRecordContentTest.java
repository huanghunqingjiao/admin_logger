package com.logger.demo264.logrecord;

import com.logger.demo264.AdminLoggerApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 日志测试
 *
 * @author zhongjiahua
 * @date 2022-07-19
 */
@Slf4j
public class LogRecordContentTest extends AdminLoggerApplicationTests {
    @Autowired
    private LogRecordProxy logRecordProxy;

    /**
     * 测试日志
     */
    @Test
    public void testLogRecord() {
        logRecordProxy.log("Where there is a will, things come true");
    }


}
