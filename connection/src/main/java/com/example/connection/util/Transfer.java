package com.example.connection.util;

public class Transfer {

    //二进制串转十进制数
    public static int transferBToI(String s) {
        int sum = 0;
        for (char c : s.toCharArray()) {
            sum = sum * 2 + c - '0';
        }
        return sum;
    }

    //十进制数转二进制串
    public static String transferIToB(int v, int size) {
        String res = "";
        while (v != 0) {
            int t = v % 2;
            v /= 2;
            res = t + res;
        }
        while (res.length() < size) {
            res = '0' + res;
        }
        return res;
    }

    public static String transferCToB(char c, int size) {
        return transferIToB((int)c, size);
    }

    public static String transferSToB(String s) {
        String res = "";
        for (char c : s.toCharArray()) {
            res += transferCToB(c, 8);
        }
        return res;
    }

    public static String f(char c) {//十六进制转二进制字符串
        int index = "0123456789ABCDEF".indexOf(c);
        String s = "";
        while (index / 2 != 0) {
            int x = index % 2;
            index = index / 2;
            s = x + s;
        }
        s = index + s;
        while (s.length() < 4) {
            s = "0" + s;
        }
        return s;
    }

    public static void main(String[] args) {
        System.out.println(transferIToB(10,4));
    }
}
