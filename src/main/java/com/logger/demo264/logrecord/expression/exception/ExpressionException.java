package com.logger.demo264.logrecord.expression.exception;

import lombok.Getter;

/**
 * 解析表达式异常
 *
 * @author 风尘
 */
public class ExpressionException extends RuntimeException {

    @Getter
    protected final String originString;

    @Getter
    protected final String expressionString;

    /**
     * -1 if not known; should be known in all reasonable cases
     */
    @Getter
    protected int position;


    public ExpressionException(String message) {
        super(message);
        this.originString = null;
        this.expressionString = null;
        this.position = 0;
    }


    public ExpressionException(String message, Throwable cause) {
        super(message, cause);
        this.originString = null;
        this.expressionString = null;
        this.position = 0;
    }

    /**
     * @param originString     当前表达式
     * @param expressionString 解析的表达式
     * @param message          描述的错误
     */
    public ExpressionException(String originString, String expressionString, String message) {
        super(message);
        this.originString = originString;
        this.expressionString = expressionString;
        this.position = -1;
    }

    /**
     * @param originString     当前表达式
     * @param expressionString 解析的表达式
     * @param cause            错误
     */
    public ExpressionException(String originString, String expressionString, Throwable cause) {
        super(cause);
        this.originString = originString;
        this.expressionString = expressionString;
        this.position = -1;
    }

    /**
     * @param position 在解析的表达式中的索引
     * @param message  错误消息
     */
    public ExpressionException(String originString, String expressionString, int position, String message) {
        super(message);
        this.originString = null;
        this.expressionString = expressionString;
        this.position = position;
    }

    public ExpressionException(int position, String message) {
        super(message);
        this.originString = null;
        this.expressionString = null;
        this.position = position;
    }

    public ExpressionException(int position, String message, Throwable cause) {
        super(message, cause);
        this.originString = null;
        this.expressionString = null;
        this.position = position;
    }


    @Override
    public String getMessage() {
        return toDetailedString();
    }

    public String toDetailedString() {
        if (this.expressionString != null) {
            StringBuilder output = new StringBuilder();
            output.append("Expression [");
            output.append(this.expressionString);
            output.append(']');
            output.append(" in [");
            output.append(this.originString);
            output.append(']');
            if (this.position >= 0) {
                output.append(" @");
                output.append(this.position);
            }
            output.append(": ");
            output.append(getSimpleMessage());
            return output.toString();
        } else {
            return getSimpleMessage();
        }
    }

    /**
     * 简短错误
     */
    public String getSimpleMessage() {
        return super.getMessage();
    }

}