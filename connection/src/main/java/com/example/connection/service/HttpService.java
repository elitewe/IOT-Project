package com.example.connection.service;

import com.example.connection.bean.ProcessContent;
import com.example.connection.bean.Resource;
import com.example.connection.bean.Result;
import com.example.connection.bean.po.DevicePO;
import com.example.connection.bean.vo.DeviceVO;
import com.example.connection.exception.AuthenticateException;
import com.example.connection.exception.FormatException;
import com.example.connection.exception.ServiceException;
import com.example.connection.mapper.HttpMapper;
import com.example.connection.mapper.SearchMapper;
import com.example.connection.util.CoapGenerator;
import com.example.connection.util.CoapParser;
import com.example.connection.util.HttpParser;
import com.example.connection.util.JsonParse.JSON;
import com.example.connection.util.RSACoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class HttpService {

    @Autowired
    HttpMapper httpMapper;

    @Autowired
    SearchMapper searchMapper;

    public void setHttpMapper(HttpMapper httpMapper) {
        this.httpMapper = httpMapper;
    }

    public void setSearchMapper(SearchMapper searchMapper) {
        this.searchMapper = searchMapper;
    }

    public Result process(int clientId, String message, String encRole) {
        String[] arr;
        String payload = "";
        String protocol = "";
        String url = ""; //主要是根据url和payload决定是上传至数据库还是返回数据
        String messageId = "";

        HashMap<String, String> payloadMap = new HashMap<>();
        HashMap<String, String> headerMap = new HashMap<>();
        HashMap<String, String> optionsMap = new HashMap<>();

        DevicePO device = searchMapper.getDeviceById(clientId); //获取设备详细信息
        String devProtocol = device.getProtocol();

        if (devProtocol.equals("HTTP")) {
            if (message.contains("GET")) {
                arr = HttpParser.getJsonify(message);
                protocol = "HTTP(GET)";
            } else {
                arr = HttpParser.postJsonify(message);
                protocol = "HTTP(POST)";
                payload = arr[2];
                payloadMap = (HashMap<String, String>)JSON.parse(payload);
                checkPayload(payloadMap);
            }
            url = arr[0].split(" ")[1];
            headerMap = (HashMap<String, String>)JSON.parse(arr[1]);
            checkHeader(headerMap);

        } else if (devProtocol.equals("COAP")) {
            CoapParser coapParser = new CoapParser();
            coapParser.parse(message.substring(1, message.length() - 1)); //去掉两边的冒号
            payload = coapParser.getPayload();
            protocol = "COAP(" + coapParser.getCode() + ")";
            url = coapParser.getUrl();
            messageId =coapParser.getMessageId();

            checkOptions(optionsMap);
            checkPayload(payloadMap);
        }

        String roles = decRole(encRole); //私钥解密

        ProcessContent processContent = new ProcessContent(clientId, payload, url, messageId, protocol);

        //添加鉴权
        if (isAuthenticated(url, roles)) {
            //反射调用方法，模拟根据url处理请求
            if (url.contains("get")) {
                return CommonService.doProcess(searchMapper, processContent);
            } else {
                return CommonService.doProcess(httpMapper, processContent);
            }
        } else {
            throw new AuthenticateException("无访问权限！");
        }
    }

    public Result getAllMessage() {
        Result result = new Result();
        result.setSuccess(true);
        result.setDetail(httpMapper.getAllMessage());
        return result;
    }

    public Result getMessagesByClientId(int id) {
        Result result = new Result();
        result.setDetail(httpMapper.getMessagesById(id));
        result.setSuccess(true);
        return result;
    }

    private boolean isAuthenticated(String url, String roles) {
        List<Resource> resourceList = searchMapper.getAllResource();
        for (Resource resource : resourceList) {
            if (url.matches(resource.getUrl())) {
               return realContains(resource.getRoles(), roles);
            }
        }
        return true; //未匹配，说明该url无权限即可访问
    }

    // str类型为role1,role2,...,roleN
    // 判断shortStr的角色至少有一个属于longStr
    private boolean realContains(String longStr, String shortStr) {
        String[] arr1 = getStringArr(longStr);
        String[] arr2 = getStringArr(shortStr);
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr2.length; j++) {
                if (arr1[i].equals(arr2[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    private String[] getStringArr(String s) {
        String[] arr = null;
        if (s.indexOf(",") == -1) {
            arr = new String[1];
            arr[0] = s;
        } else {
            arr = s.split(",");
        }
        return arr.clone();
    }

    private String decRole(String encRole) {
        String privateKey = searchMapper.getPrivateKey();
        String role = RSACoder.decrypt(encRole, privateKey);
        return role;
    }

    //检查json串的合法性
    private void checkHeader(HashMap<String, String> map) {
        if (map.containsKey("HOST")) {
            if (!map.get("HOST").equals("127.0.0.1:8090")) {
                throw new FormatException("ip或端口号解析出错!");
            }
        }
        if (map.containsKey("Accept")) {
            if (!map.get("Accept").contains("application/json")) {
                throw new FormatException("未支持json格式数据传输!");
            }
        }
    }

    //检查携带数据的合法性
    private void checkPayload(HashMap<String, String> map) {
        if (map.containsKey("temperature") && Integer.parseInt(map.get("temperature")) < -1) {
            throw new FormatException("温度上报异常!");
        }
    }

    //检查COAP报文的OPTION部分
    private void checkOptions(HashMap<String, String> map) {

    }
}
