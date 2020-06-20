package com.example.connection.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSACoder {
    private static final int DEFAULT_RSA_KEY_SIZE = 1024;

    private static final String KEY_ALGORITHM = "RSA";

    public static void main(String [] args){
//        Map<String,String> result = generateRsaKey(DEFAULT_RSA_KEY_SIZE);
//        System.out.println("公钥为：" + result.get("publicKey") );
//        System.out.println("私钥为：" + result.get("privateKey"));

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCO3H+z8+MgYJH7S8S0WsLoXyW23fcTBWPrZCdBicH5pR0lBxPCZ/Ibl4Y3lRGLT0Agrth1LEz7/GiBKNQjPtB68CIHxIAJr3KM5ReIsq2zkeuIKA3yvUx2vTOzvg9miFLgNjp+uX2MM5sF2kBonJeO/LhIp2H8in41JLCC9RFEBQIDAQAB";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI7cf7Pz4yBgkftLxLRawuhfJbbd9xMFY+tkJ0GJwfmlHSUHE8Jn8huXhjeVEYtPQCCu2HUsTPv8aIEo1CM+0HrwIgfEgAmvcozlF4iyrbOR64goDfK9THa9M7O+D2aIUuA2On65fYwzmwXaQGicl478uEinYfyKfjUksIL1EUQFAgMBAAECgYAffyvEkAPGUqQ8xEK1BMGuG7Qj8iIUFn8mGamyAF483RNk43+Ov/4X6TtVK893aFyaGBJvGvpKICcmOssyjhnGfFAcVTf0DGVxg5orfbR1xh2Ncoe1hi8sONAMWFmj60e/1Ka7ftobIKOZeUCYCe0e7AhR1OaKRyf8LchO7t0PQQJBANJE6JmQGDAwi78G07uksBSabnNlLmSi2X4LhbX72dMESLv8Bj84nbV2U3OxVcwBpH1daALjxer0+0c+ZdM2NhECQQCt7otYSfqZAqX5Q2mmcHCZ8UY159KTS4t+eXcAuU9IeW495++QcN8QuZe4gEY0OddFczA1OHljWFZpeeWVumq1AkAaFzrVcy/NKvjsJyi2q+S9abwyzWdITXy3Sy64Ohv5NxrfWJJd3eST067fOC3xNnL2q1Rwp1qzoNpdKLzxzFRBAkArTqAHhbQN8SjeXbiqpoiC7B5tQaGe50p+XUQSPBHPm9ylMWDm+BOymGN8nwPb8SL2ue2g8sTWxaIOdTmDBH2ZAkAnZypU8RtRXkF7/ZhR2KL7LhdtcyGlPOOObTTmUoE09eRy6WKxqrvzWPKf6Iq9Zg30Zr4/a6D/3jBvmipSCbBB";
        String encoded = "C43MIqIBOQHaT2OOsGWUqo2aytUKQVk3w6juL20Zt9ZwJONmbOXMNL6DMyE+fxymXI9TgxREdp/kNxtIybbjujdN0o5JDrkjbOfTOKmUqeL+uZ/ji6G09tG9a5efqnpzg5G4sWeKv8/c+CepaBzQqHunPA+Yy/10p5fhURManX8=";
        String decoded = decrypt(encoded, privateKey);
        System.out.println(decoded);
        System.out.println(publicKey.length());
        System.out.println(privateKey.length());
    }

    /**
     * 生成RSA 公私钥,可选长度为1024,2048位.
     */
    public static Map<String,String> generateRsaKey(int keySize) {
        Map<String,String> result = new HashMap<>(2);
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);

            // 初始化密钥对生成器，密钥大小为1024 2048位
            keyPairGen.initialize(keySize, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 得到公钥字符串
            result.put("publicKey", new String(Base64.encodeBase64(keyPair.getPublic().getEncoded())));
            // 得到私钥字符串
            result.put("privateKey", new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded())));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) {
        //64位解码加密后的字符串
        byte[] inputByte;
        String outStr = "";
        try {
            inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            outStr = new String(cipher.doFinal(inputByte));
        } catch (UnsupportedEncodingException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return outStr;
    }
}
