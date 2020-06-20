package com.example.connection.util;

public class CoapGenerator {

    //data是对象toString后的结果
    public static String generate(String data, String messageId) {
        String coapResponse = "";
        String Ver = "01";
        String T = "10"; //ACK
        String TKL = "0000"; //no-token
        String Code = "01000101"; //正确响应
        String MessageID = messageId; //和request保持一致
        String TOKEN = TKL == "0000" ? "" : generateRandomBinary(Transfer.transferBToI(TKL) * 8);
        String Options = "";
        String Delta = "";
        String Length = "";
        String Value = "";
        Delta = "1100"; // content-format
        Length = "0010";
        Value = Transfer.transferSToB("50"); //value = 50
        Options = Options + Delta + Length + Value;
        String Split = "11111111"; //分隔符
        String Payload = Transfer.transferSToB(data);
        coapResponse = Ver + T + TKL + Code + MessageID + TOKEN + Options + Split + Payload;
        return coapResponse;
    }

    private static String generateRandomBinary(int size) {
        String s = "";
        for (int i = 0; i < size; i++) {
            s += (int)(Math.random() * 2);
        }
        return s;
    }

    public static void main(String[] args) {
//        CoapGenerator coapGenerator = new CoapGenerator();
        String coap = CoapGenerator.generate("ABC", "0101010101010101");
        CoapParser coapParser = new CoapParser();
        coapParser.parse(coap);
        System.out.println();
    }
}
