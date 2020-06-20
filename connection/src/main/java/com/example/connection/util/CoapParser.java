package com.example.connection.util;

import java.util.HashMap;
import java.util.Map;

public class CoapParser {

    private String T; //报文类型

    private int TKL; //token_length

    private String Code;

    private String MessageId;

    private String Token;

    private Map<String, String> Options = new HashMap<>();

    private String Payload;

    public void parse(String s) {
        String temp = s.substring(2, 4);
        parseT(temp);
        temp = s.substring(4, 8);
        parseTKL(temp);
        temp = s.substring(8, 16);
        parseCode(temp);
        temp = s.substring(16, 32);
        parseMessageId(temp);
        temp = s.substring(32, 32 + 8 * TKL);
        parseToken(temp);
        int index = getSplitIndex(s);
        if (index == -1) { //没有分隔符
            temp = s.substring(32 + 8 * TKL);
            parseOptions(temp);
            Payload = "";
        } else {
            temp = s.substring(32 + 8 * TKL, index);
            parseOptions(temp);
            temp = s.substring(index + 8);
            parsePayload(temp);
        }
    }

    private void parseT(String s) {
        switch (s) {
            case "00":
                T = "CON"; break;
            case "01":
                T = "NON"; break;
            case "10":
                T = "ACK"; break;
            case "11":
                T = "Reset"; break;
            default:
                T = "Error";
        }
    }

    private void parseTKL(String s) {
        TKL = parseBinary(s);
    }

    private void parseCode(String s) {
        int num1 = parseBinary(s.substring(0, 3));
        int num2 = parseBinary(s.substring(3, 8));
        int sum = num1 * 100 + num2;
        switch (sum) {
            case 1: Code = "GET"; break;
            case 2: Code = "POST"; break;
            case 3: Code = "PUT"; break;
            case 4: Code = "DELETE"; break;
            case 201: Code = "Created"; break;
            case 202: Code = "Deleted"; break;
            case 203: Code = "Valid"; break;
            case 204: Code = "Changed"; break;
            case 205: Code = "Content"; break;
            case 400: Code = "Bad Request"; break;
            case 401: Code = "Unauthorized"; break;
            case 402: Code = "Bad Option"; break;
            case 403: Code = "Forbidden"; break;
            case 404: Code = "Not Found"; break;
            case 405: Code = "Method Not Allowed"; break;
            case 406: Code = "Not Acceptable"; break;
            case 412: Code = "Precondition Failed"; break;
            case 415: Code = "Unsuppor Conten-Type"; break;
            case 500: Code = "Internal Server Error"; break;
            case 501: Code = "Not Implemented"; break;
            case 502: Code = "Bad Gateway"; break;
            case 503: Code = "Service Unavailable"; break;
            case 504: Code = "Gateway Timeout"; break;
            case 505: Code = "Proxying Not Supported"; break;
        }
    }

    private void parseMessageId(String s) {
        MessageId = s;
    }

    private void parseToken(String s) {
        Token = s;
    }

    private void parseOptions(String s) {
        int delta = 0;
        int length = 0;
        int count = 0;
        String key = "";
        String value = "";
        while (count < s.length()) {
            delta += parseBinary(s.substring(count, count + 4));
            length = parseBinary(s.substring(count + 4, count + 8));
            key = getKey(delta);
            String temp = s.substring(count + 8, count + 8 + length * 8);//ascii字符串
            if (!"If-Match, E-Tag".contains(key)) { //Opaque类型无法解释为ascii
                value = parseAscii(temp);
            } else {
                value = temp;
            }
            if (key.equals("Uri-Path")) { //多个url需要拼接
                value = (Options.containsKey("Uri-Path") ? Options.get("Uri-Path") : "") + "/" + value;
            } else if (key.equals("Uri-Query")) { //多个Query需要拼接
                value = (Options.containsKey("Uri-Query") ? Options.get("Uri-Query") + "&" : "") + value;
            }
            Options.put(key, value);
            count = count + 8 + length * 8; //下一个key-value的开始
        }
    }

    private void parsePayload(String s) {
        Payload = parseAscii(s);
    }

    private int getSplitIndex(String s) {
        return s.indexOf("11111111"); //分隔符
    }

    private int parseBinary(String s) {
        int sum = 0;
        for (char c : s.toCharArray()) {
            sum = sum * 2 + (c - '0');
        }
        return sum;
    }

    private String parseAscii(String s) {
        String result = "";
        int t = s.length() / 8;
        for (int i = 0; i < t; i++) {
            String temp = s.substring(i * 8, i * 8 + 8);
            char c = (char)parseBinary(temp);
            result += c;
        }
        return result;
    }

    private String getKey(int v) {
        String result = "";
        switch (v) {
            case 1: result = "If-Match"; break;
            case 2: result = "Uri-Host"; break;
            case 4: result = "E-Tag"; break;
            case 5: result = "If-None-Match"; break;
            case 7: result = "Uri-Port"; break;
            case 8: result = "Location-Path"; break;
            case 11: result = "Uri-Path"; break;
            case 12: result = "Content-Format"; break;
            case 14: result = "Max-Age"; break;
            case 15: result = "Uri-Query"; break;
            case 16: result = "Accept"; break;
            case 17: result = "Location-Query"; break;
        }
        return result;
    }

    public String getT() {
        return T;
    }

    public int getTKL() {
        return TKL;
    }

    public String getCode() {
        return Code;
    }

    public String getMessageId() {
        return MessageId;
    }

    public String getToken() {
        return Token;
    }

    public Map<String, String> getOptions() {
        return Options;
    }

    public String getPayload() {
        return Payload;
    }

    public String getUrl() {
        return Options.get("Uri-Path") + (Options.containsKey("Uri-Query") ? "&" + Options.get("Uri-Query") : "");
    }



    public static void main(String[] args) {
        CoapParser coapParser = new CoapParser();
        String s = "62451234567848CBB0EF056311E38480FF54445F434F52455F434F41505F30392073756231";
        String s1 = "";
        for (char c : s.toCharArray()) {
//            s1 += coapParser.f(c);
        }
        coapParser.parse(s1);
        System.out.println();
    }
}
