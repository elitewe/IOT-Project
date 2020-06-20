package com.example.connection.util;

import com.example.connection.util.JsonParse.JSON;

public class HttpParser {
    //获取controller的string
    //[0]：请求第一行
    //[1]：余下的内容转换成json
    public static String[] getJsonify(String s) {
        s = s.substring(1, s.length() - 1);
        String s2 = s.replaceAll("\\\\r\\\\n","\n"); //注意是4个反斜杠
        String firstLine = s2.split("\n")[0];
        int length = s2.length();
        String s3 = s2.substring(firstLine.length() + 1, length);
        String s4 = s3.replaceAll("\n", ",\n\t");
        s4 = replacePort(s4);
        String s5 = "{\n\t" + s4 + "\n}\n";

        String[] result = {firstLine, s5};
        return result;
    }

    //[0]:请求第一行
    //[1]:请求头json格式
    //[2]:请求体json格式
    public static String[] postJsonify(String s){
        String[] arr = s.split("\\\\r\\\\n");
        String temp = arr[arr.length - 1];
        String lastLine = temp.substring(0, temp.length() - 1);
        int lLength = lastLine.length();
        String s2 = s.substring(0, s.length() - lLength - 9) + '"'; //post报文剔除请求体的部分
        String s3 = lastLine.replaceAll("\\\\", ""); //json格式的字符串
        String[] arr1 = getJsonify(s2);
        String[] result = {arr1[0], arr1[1], s3};

        return result;
    }

    private static String replacePort(String s) {
        String[] arr1 = s.split(",");
        String[] arr2 = arr1[0].split(":");
        String port = arr2[1] + ':' + arr2[2];
        return s.replaceAll(port, "\"" + port + "\"");
    }

    public static void main(String[] args) {
        String s = "\"POST /httpUpload?temperature=45 HTTP/1.1\\r\\nHOST:127.0.0.1:8090\\r\\nAccept:text/pain\\r\\nAccept-Charset:utf-8\\r\\nAccept-Language:zh-CN\\r\\nContent-Type:application/json\\r\\nCookie:MessageID=JrZ9q9vXhJK2gZu\\r\\nUser-Agent:Mozilla/5.0 Chrome/73.0.3683.103 Safari/537.36\\r\\n\\r\\n{\\\"A\\\":\\\"120\\\"}\"";
        String[] arr = postJsonify(s);
        Object o = JSON.parse(arr[1]);
        System.out.println();
    }
}
