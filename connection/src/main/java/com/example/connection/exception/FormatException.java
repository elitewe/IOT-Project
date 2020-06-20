package com.example.connection.exception;

public class FormatException extends RuntimeException {
    /**
     * 无参数自定义业务异常构造方法.
     */
    public FormatException() {
    }

    /**
     *
     * @param message
     *            异常消息.
     */
    public FormatException(String message) {
        super(message);
    }

    /**
     *
     * @param cause
     *            根异常.
     */
    public FormatException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message
     *            异常消息.
     * @param cause
     *            根异常.
     */
    public FormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     *            异常消息.
     * @param code
     *            根异常.
     */
    public FormatException(String message, String code) {
        super(message);
    }
}
