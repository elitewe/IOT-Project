package com.example.connection.service;

import com.example.connection.bean.ProcessContent;
import com.example.connection.bean.Result;
import com.example.connection.bean.Weather;
import com.example.connection.exception.FormatException;
import com.example.connection.exception.MethodException;
import com.example.connection.util.CoapGenerator;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

@Service
public class CommonService {

    public static <T> Result doProcess(T type, ProcessContent processContent) {
        Object details = null;
        Result result = new Result();
        result.setSuccess(true);

        int clientId = processContent.getClientId();
        String payload = processContent.getPayload();
        String protocol = processContent.getProtocol();
        String url = processContent.getUrl();
        String messageId = processContent.getMessageId();
        String methodName = "";
        try {
            if (url.contains("get")) {

                String[] arr = url.split("/");
                methodName = arr[1].indexOf("&") == -1 ? arr[1] : arr[1].split("&")[0];
                switch (methodName) {
                    case "getDeviceTypeBy": //url长度不够
                        Method method = type.getClass().getMethod(methodName + "Id", Integer.class);
                        String devType = (String)method.invoke(type, 1);
                        details = getDetails(devType, protocol, messageId);
                    case "getAllWeathers":
                        Method method1 = type.getClass().getMethod(methodName);
                        List<Weather> weatherList = (List<Weather>)method1.invoke(type);
                        details = getDetails(weatherList, protocol, messageId);
                    default:
                        throw new MethodException("被调方法" + methodName + "不存在！");

                }
            } else if (url.contains("upload") || url.contains("Upload")) {
                Method method = type.getClass().getMethod("insertContent", Integer.class, String.class, String.class, String.class, String.class);
                method.invoke(type, clientId, payload, protocol, url, messageId);
                details = (String)CoapGenerator.generate("upload success!", messageId);
            }
        } catch (Exception e) {
            throw new MethodException("被调方法" + methodName + "不存在！");
        }

        result.setDetail(details);
        return result;
    }

    //根据协议类型决定返回类型
    public static <T> Object getDetails(T data, String url, String messageId) {
        if (url.contains("COAP")) {
            return CoapGenerator.generate(data.toString(), messageId);
        } else if (url.contains("HTTP")) {
            return data;
        }
        return null;
    }

}