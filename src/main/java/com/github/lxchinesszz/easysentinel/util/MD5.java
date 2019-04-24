package com.github.lxchinesszz.easysentinel.util;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author liuxin
 * @version Id: MD5.java, v 0.1 2019-04-23 16:07
 */
public class MD5 {


    public static String encodeMd5(byte[] data) {
        // 初始化MessageDigest
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 执行摘要信息
        byte[] digest = md.digest(data);
        // 将摘要信息转换为32位的十六进制字符串
        return new String(new HexBinaryAdapter().marshal(digest));
    }

    public static String encodeMd5(String dateStr) {

        // 将摘要信息转换为32位的十六进制字符串
        return encodeMd5(dateStr.getBytes());
    }
}
