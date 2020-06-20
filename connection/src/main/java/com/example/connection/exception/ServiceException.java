package com.example.connection.exception;

public class ServiceException extends RuntimeException{
    /**
     * 无参数自定义业务异常构造方法.
     */
    public ServiceException() {
    }

    /**
     *
     * @param message
     *            异常消息.
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     *
     * @param cause
     *            根异常.
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message
     *            异常消息.
     * @param cause
     *            根异常.
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     *            异常消息.
     * @param code
     *            根异常.
     */
    public ServiceException(String message, String code) {
        super(message);
    }
}
