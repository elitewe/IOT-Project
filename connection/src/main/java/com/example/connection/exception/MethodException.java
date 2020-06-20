package com.example.connection.exception;

public class MethodException extends RuntimeException{
    /**
     * 无参数自定义业务异常构造方法.
     */
    public MethodException() {
    }

    /**
     *
     * @param message
     *            异常消息.
     */
    public MethodException(String message) {
        super(message);
    }

    /**
     *
     * @param cause
     *            根异常.
     */
    public MethodException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message
     *            异常消息.
     * @param cause
     *            根异常.
     */
    public MethodException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     *            异常消息.
     * @param code
     *            根异常.
     */
    public MethodException(String message, String code) {
        super(message);
    }
}
