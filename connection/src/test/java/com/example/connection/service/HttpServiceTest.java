package com.example.connection.service;

import com.example.connection.bean.DevMessage;
import com.example.connection.bean.Message;
import com.example.connection.bean.Resource;
import com.example.connection.bean.po.DevicePO;
import com.example.connection.exception.AuthenticateException;
import com.example.connection.exception.FormatException;
import com.example.connection.exception.MethodException;
import com.example.connection.mapper.HttpMapper;
import com.example.connection.mapper.SearchMapper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class HttpServiceTest {

    DevicePO devicePO63 = new DevicePO(63, "mamahaha", 1, 2, "HTTP", new Date(100000));
    DevicePO devicePO66 = new DevicePO(66, "dev123", 1, 1, "HTTP", new Date(100000));
    DevicePO devicePO62 = new DevicePO(62, "dev", 1, 1, "COAP", new Date(100000));

    List<Resource> resourceList = Arrays.asList(
        new Resource(1, "/abc", "ROLE_ADMIN,ROLE_USER"),
        new Resource(2, "/httpUpload(/)?.*", "ROLE_USER")
    );

    List<DevMessage> devMessageList = Arrays.asList(
      new DevMessage(1, "light", "name", "{A:1}", "HTTP", "/abcd", new Date(10000)),
      new DevMessage(2, "television", "name2", "{A:1}", "HTTP", "/abcd", new Date(10000))
    );

    List<Message> messageList = Arrays.asList(
      new Message("payload1", new Date(1000)),
      new Message("payload1", new Date(1000))
    );

    @Test
    void process1() {
        HttpMapper mockMapper = mock(HttpMapper.class);
        SearchMapper mockMapper2 = mock(SearchMapper.class);
        HttpService httpService = new HttpService();
        httpService.setHttpMapper(mockMapper);
        httpService.setSearchMapper(mockMapper2);
        when(mockMapper2.getDeviceById(63)).thenReturn(devicePO63);
        when(mockMapper2.getPrivateKey()).thenReturn("MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI7cf7Pz4yBgkftLxLRawuhfJbbd9xMFY+tkJ0GJwfmlHSUHE8Jn8huXhjeVEYtPQCCu2HUsTPv8aIEo1CM+0HrwIgfEgAmvcozlF4iyrbOR64goDfK9THa9M7O+D2aIUuA2On65fYwzmwXaQGicl478uEinYfyKfjUksIL1EUQFAgMBAAECgYAffyvEkAPGUqQ8xEK1BMGuG7Qj8iIUFn8mGamyAF483RNk43+Ov/4X6TtVK893aFyaGBJvGvpKICcmOssyjhnGfFAcVTf0DGVxg5orfbR1xh2Ncoe1hi8sONAMWFmj60e/1Ka7ftobIKOZeUCYCe0e7AhR1OaKRyf8LchO7t0PQQJBANJE6JmQGDAwi78G07uksBSabnNlLmSi2X4LhbX72dMESLv8Bj84nbV2U3OxVcwBpH1daALjxer0+0c+ZdM2NhECQQCt7otYSfqZAqX5Q2mmcHCZ8UY159KTS4t+eXcAuU9IeW495++QcN8QuZe4gEY0OddFczA1OHljWFZpeeWVumq1AkAaFzrVcy/NKvjsJyi2q+S9abwyzWdITXy3Sy64Ohv5NxrfWJJd3eST067fOC3xNnL2q1Rwp1qzoNpdKLzxzFRBAkArTqAHhbQN8SjeXbiqpoiC7B5tQaGe50p+XUQSPBHPm9ylMWDm+BOymGN8nwPb8SL2ue2g8sTWxaIOdTmDBH2ZAkAnZypU8RtRXkF7/ZhR2KL7LhdtcyGlPOOObTTmUoE09eRy6WKxqrvzWPKf6Iq9Zg30Zr4/a6D/3jBvmipSCbBB");
        when(mockMapper2.getAllResource()).thenReturn(resourceList);
        int clientId = 63;
        String message = "\"POST /httpUpload?temperature=45 HTTP/1.1\\r\\nHOST:127.0.0.1:8090\\r\\nAccept:application/json\\r\\nAccept-Charset:utf-8\\r\\nAccept-Language:zh-CN\\r\\nContent-Type:application/json\\r\\nCookie:MessageID=dFjyjzy5JrDfabUu\\r\\nUser-Agent:Mozilla/5.0 Chrome/73.0.3683.103 Safari/537.36\\r\\n\\r\\n{\\\"A\\\":\\\"120\\\"}\"";
        //admin权限
        String roles = "M6lr7duTLW/IH6+A9tvfYOyTzG5oHPrAmfRtZ5MpHxdjmdYupczbhhvneqa7sDPGyhLspMifcJ8I/bXdzbfzICi2CieTq3vYp9UPxOLXTh80IGhaQow1jBarknpuC4AY3tJM11VTmcEzTFpNkhi0WSDM0WYPRpmaD2XEryE5DX4=";
        assertThrows(AuthenticateException.class, () -> {
            httpService.process(clientId, message, roles);
        });
    }

    @Test
    void process2() {
        HttpMapper mockMapper = mock(HttpMapper.class);
        SearchMapper mockMapper2 = mock(SearchMapper.class);
        HttpService httpService = new HttpService();
        httpService.setHttpMapper(mockMapper);
        httpService.setSearchMapper(mockMapper2);
        when(mockMapper2.getDeviceById(66)).thenReturn(devicePO66);
        when(mockMapper2.getPrivateKey()).thenReturn("MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI7cf7Pz4yBgkftLxLRawuhfJbbd9xMFY+tkJ0GJwfmlHSUHE8Jn8huXhjeVEYtPQCCu2HUsTPv8aIEo1CM+0HrwIgfEgAmvcozlF4iyrbOR64goDfK9THa9M7O+D2aIUuA2On65fYwzmwXaQGicl478uEinYfyKfjUksIL1EUQFAgMBAAECgYAffyvEkAPGUqQ8xEK1BMGuG7Qj8iIUFn8mGamyAF483RNk43+Ov/4X6TtVK893aFyaGBJvGvpKICcmOssyjhnGfFAcVTf0DGVxg5orfbR1xh2Ncoe1hi8sONAMWFmj60e/1Ka7ftobIKOZeUCYCe0e7AhR1OaKRyf8LchO7t0PQQJBANJE6JmQGDAwi78G07uksBSabnNlLmSi2X4LhbX72dMESLv8Bj84nbV2U3OxVcwBpH1daALjxer0+0c+ZdM2NhECQQCt7otYSfqZAqX5Q2mmcHCZ8UY159KTS4t+eXcAuU9IeW495++QcN8QuZe4gEY0OddFczA1OHljWFZpeeWVumq1AkAaFzrVcy/NKvjsJyi2q+S9abwyzWdITXy3Sy64Ohv5NxrfWJJd3eST067fOC3xNnL2q1Rwp1qzoNpdKLzxzFRBAkArTqAHhbQN8SjeXbiqpoiC7B5tQaGe50p+XUQSPBHPm9ylMWDm+BOymGN8nwPb8SL2ue2g8sTWxaIOdTmDBH2ZAkAnZypU8RtRXkF7/ZhR2KL7LhdtcyGlPOOObTTmUoE09eRy6WKxqrvzWPKf6Iq9Zg30Zr4/a6D/3jBvmipSCbBB");
        when(mockMapper2.getAllResource()).thenReturn(resourceList);
        int clientId = 66;
        String message = "\"POST /httpUpload?temperature=-200 HTTP/1.1\\r\\nHOST:127.0.0.1:8090\\r\\nAccept:application/json\\r\\nAccept-Charset:utf-8\\r\\nAccept-Language:zh-CN\\r\\nContent-Type:application/json\\r\\nCookie:MessageID=dFjyjzy5JrDfabUu\\r\\nUser-Agent:Mozilla/5.0 Chrome/73.0.3683.103 Safari/537.36\\r\\n\\r\\n{\\\"temperature\\\":\\\"-200\\\"}\"";
        //user权限
        String roles = "d04BZ/MaFKWtq0F2cwGL/i53L3v0d8bB8v2LI8Agb45QDixI/0S96lSSePI9R27UWoEkv68FbuOmEVukwhpQBKV+ZjSrT86rpcPsDBUBOv0IAS3k0h88qnIgmdpXPVkp70m74Wd+dtC1snF6+RbTaUCj2QrDyJKl68PAWKDegus=";
        assertThrows(FormatException.class, () -> {
            httpService.process(clientId, message, roles);
        });
    }

    @Test
    void process3() {
        HttpMapper mockMapper = mock(HttpMapper.class);
        SearchMapper mockMapper2 = mock(SearchMapper.class);
        HttpService httpService = new HttpService();
        httpService.setHttpMapper(mockMapper);
        httpService.setSearchMapper(mockMapper2);
        when(mockMapper2.getDeviceById(62)).thenReturn(devicePO62);
        when(mockMapper2.getPrivateKey()).thenReturn("MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI7cf7Pz4yBgkftLxLRawuhfJbbd9xMFY+tkJ0GJwfmlHSUHE8Jn8huXhjeVEYtPQCCu2HUsTPv8aIEo1CM+0HrwIgfEgAmvcozlF4iyrbOR64goDfK9THa9M7O+D2aIUuA2On65fYwzmwXaQGicl478uEinYfyKfjUksIL1EUQFAgMBAAECgYAffyvEkAPGUqQ8xEK1BMGuG7Qj8iIUFn8mGamyAF483RNk43+Ov/4X6TtVK893aFyaGBJvGvpKICcmOssyjhnGfFAcVTf0DGVxg5orfbR1xh2Ncoe1hi8sONAMWFmj60e/1Ka7ftobIKOZeUCYCe0e7AhR1OaKRyf8LchO7t0PQQJBANJE6JmQGDAwi78G07uksBSabnNlLmSi2X4LhbX72dMESLv8Bj84nbV2U3OxVcwBpH1daALjxer0+0c+ZdM2NhECQQCt7otYSfqZAqX5Q2mmcHCZ8UY159KTS4t+eXcAuU9IeW495++QcN8QuZe4gEY0OddFczA1OHljWFZpeeWVumq1AkAaFzrVcy/NKvjsJyi2q+S9abwyzWdITXy3Sy64Ohv5NxrfWJJd3eST067fOC3xNnL2q1Rwp1qzoNpdKLzxzFRBAkArTqAHhbQN8SjeXbiqpoiC7B5tQaGe50p+XUQSPBHPm9ylMWDm+BOymGN8nwPb8SL2ue2g8sTWxaIOdTmDBH2ZAkAnZypU8RtRXkF7/ZhR2KL7LhdtcyGlPOOObTTmUoE09eRy6WKxqrvzWPKf6Iq9Zg30Zr4/a6D/3jBvmipSCbBB");
        when(mockMapper2.getAllResource()).thenReturn(resourceList);
        int clientId = 62;
        String message = "\"01000000000000110101101001101100101111100110011101100101011101000100000101101100011011000101011101100101011000010111010001101000011001010111001001110011010100100011010100110000\"";
        String roles = "cD1QkyBltY2cmbvIl6PdeSYrLmt/fM+ncd7m4Avu98q4M186KfXn9aHwkpaOamIT8ddjcG9o9FmyXfHmKpa7WR+K0m3RlGm3AkXm2W3WPROpP8pyorVwOoH9cIDIrRDyqSQVPWo7Ff8JYHvrgoOpNU0fmQmEFlobBcSEyYh9Dq0=";
        assertThrows(MethodException.class, () -> {
            httpService.process(clientId, message, roles);
        });

    }

    @Test
    void getAllMessage() {
        HttpMapper mockMapper = mock(HttpMapper.class);
        SearchMapper mockMapper2 = mock(SearchMapper.class);
        HttpService httpService = new HttpService();
        httpService.setHttpMapper(mockMapper);
        httpService.setSearchMapper(mockMapper2);
        when(mockMapper.getAllMessage()).thenReturn(devMessageList);
        assertEquals(true, httpService.getAllMessage().isSuccess());
    }

    @Test
    void getMessagesByClientId1() {
        HttpMapper mockMapper = mock(HttpMapper.class);
        SearchMapper mockMapper2 = mock(SearchMapper.class);
        HttpService httpService = new HttpService();
        httpService.setHttpMapper(mockMapper);
        httpService.setSearchMapper(mockMapper2);
        when(mockMapper.getMessagesById(62)).thenReturn(messageList);
        assertEquals(true, httpService.getMessagesByClientId(62).isSuccess());
    }

    @Test
    void getMessagesByClientId2() {
        HttpMapper mockMapper = mock(HttpMapper.class);
        SearchMapper mockMapper2 = mock(SearchMapper.class);
        HttpService httpService = new HttpService();
        httpService.setHttpMapper(mockMapper);
        httpService.setSearchMapper(mockMapper2);
        when(mockMapper.getMessagesById(100)).thenReturn(null);
        assertNull(httpService.getMessagesByClientId(100).getDetail());
    }
}