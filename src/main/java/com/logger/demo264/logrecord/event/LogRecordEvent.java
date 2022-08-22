package com.logger.demo264.logrecord.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 日志事件
 *
 * @author 风尘
 * @date 2022-07-18
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class LogRecordEvent {
    /**
     * 操作人id
     */
    private Integer operatorId;

    /**
     * ip
     */
    private String ip;

    /**
     * 日志详情
     */
    private String content;

    /**
     * 操作日志绑定的业务对象标识
     */
    private String bizNo;

    /**
     * 方法是否执行成功
     */
    private boolean executeSuccess;
}
