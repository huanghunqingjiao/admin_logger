package com.logger.demo264.logrecord.expression.exception;


/**
 * 解析异常
 *
 * @author 风尘
 */
public class ParseException extends ExpressionException {

    public ParseException(String originString, String expressionString, int position, String message) {
        super(originString, expressionString, position, message);
    }

    public ParseException(String originString, String expressionString, Throwable cause) {
        super(originString, expressionString, cause);
    }

    public ParseException(int position, String message, Throwable cause) {
        super(position, message, cause);
    }

    public ParseException(int position, String message) {
        super(position, message);
    }

}