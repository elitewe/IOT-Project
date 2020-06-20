package com.example.connection.exception;

public class AuthenticateException extends RuntimeException{
    /**
     * 无参数自定义业务异常构造方法.
     */
    public AuthenticateException() {
    }

    /**
     *
     * @param message
     *            异常消息.
     */
    public AuthenticateException(String message) {
        super(message);
    }

    /**
     *
     * @param cause
     *            根异常.
     */
    public AuthenticateException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message
     *            异常消息.
     * @param cause
     *            根异常.
     */
    public AuthenticateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     *            异常消息.
     * @param code
     *            根异常.
     */
    public AuthenticateException(String message, String code) {
        super(message);
    }
}
