package com.kuangxf.baseappas.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by kuangxf on 2017/7/31.
 */

public class EncUtil {

    private static final String CHARSET_NAME = "UTF-8";
    private static final String DEFUALT_KEY = "12345678123456781234567812345678";

    public static byte[] desEncrypt(String strKey, String msg) {
        if (msg == null)
            msg = "";
        if (strKey == null) {
            strKey = DEFUALT_KEY;
        }
        byte[] keyBytes = new byte[8];
        int saltLen = strKey.length();
        byte[] saltBytes = strKey.getBytes();
        for (int i = 0; i < 8; i++) {
            keyBytes[i] = saltBytes[i % saltLen];
        }

        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(
                    keySpec);
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] text = msg.getBytes(CHARSET_NAME);
            byte[] ciphertext = desCipher.doFinal(text);

            return ciphertext;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String desDecrypt(String strKey, byte[] msg) {
        if (msg == null)
            return null;
        if (strKey == null) {
            strKey = DEFUALT_KEY;
        }
        byte[] keyBytes = new byte[8];
        int saltLen = strKey.length();
        byte[] saltBytes = strKey.getBytes();
        for (int i = 0; i < 8; i++) {
            keyBytes[i] = saltBytes[i % saltLen];
        }

        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(
                    keySpec);
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] deciphertext = desCipher.doFinal(msg);

            return new String(deciphertext, CHARSET_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dumpBytes(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            /*
			 * if (i%32 == 0 && i!=0) { sb.append("\n"); }
			 */
            String s = Integer.toHexString(bytes[i]);
            if (s.length() < 2) {
                s = "0" + s;
            }
            if (s.length() > 2) {
                s = s.substring(s.length() - 2);
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static byte[] parseBytes(String str) {
        try {
            int len = str.length() / 2;
            if (len <= 2) {
                return new byte[]{Byte.parseByte(str)};
            }
            byte[] arr = new byte[len];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = (byte) Integer.parseInt(
                        str.substring(i * 2, i * 2 + 2), 16);
            }
            return arr;
        } catch (Exception e) {
            return new byte[0];
        }
    }

    /**
     * 加密
     *
     * @param encrypt_value 被加密的字符串
     * @param encrypt_key   加密的密钥
     * @return
     */
    public static String encryptAsString(String encrypt_key, String encrypt_value) {
        return dumpBytes(desEncrypt(encrypt_key, encrypt_value));
    }

    /**
     * 解密
     *
     * @param encrypt_value 要解密的字符串
     * @param encrypt_key   密钥
     * @return
     */
    public static String desEncryptAsString(String encrypt_key, String encrypt_value) {
        return desDecrypt(encrypt_key, parseBytes(encrypt_value));
    }
}
