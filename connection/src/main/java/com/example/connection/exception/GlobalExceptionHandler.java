package com.example.connection.exception;

import com.example.connection.bean.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    // todo 可以考虑添加错误码决定错误类型
    @ResponseBody
    @ExceptionHandler(value = FormatException.class)
    public Result formatExceptionHandler(FormatException e) {
        e.printStackTrace();
        return getResult(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = SQLException.class)
    public Result sqlExceptionHandler(SQLException e) {
        e.printStackTrace();
        return getResult(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = AuthenticateException.class)
    public Result authenticateExceptionHandler(AuthenticateException e) {
        e.printStackTrace();
        return getResult(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = MethodException.class)
    public Result methodExceptionHandler(MethodException e) {
        e.printStackTrace();
        return getResult(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public Result serviceExceptionHandler(ServiceException e) {
        e.printStackTrace();
        return getResult(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e) {
        e.printStackTrace();
        return getResult(e.getMessage());
    }

    private Result getResult(String message) {
        Result result = new Result();
        result.setMsg(message);
        result.setSuccess(false);
        return result;
    }
}
