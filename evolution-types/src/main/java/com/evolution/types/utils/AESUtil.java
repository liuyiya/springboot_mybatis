package com.evolution.types.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.evolution.types.enums.ErrorCodeEnum;
import com.evolution.types.exception.AppException;

public class AESUtil {

    /**
     * 加密
     */
    public static String encryptHex(String code, String encodedKey) {
        try {
            // 构建
            byte[] decode = Base64.decode(encodedKey);
            AES aes = SecureUtil.aes(decode);
            return aes.encryptHex(code);
        } catch (Exception e) {
            throw new AppException(ErrorCodeEnum.unknown_error);
        }
    }

    /**
     * 解密
     */
    public static String decryptStr(String code, String encodedKey) {
        try {
            // 构建
            byte[] decode = Base64.decode(encodedKey);
            AES aes = SecureUtil.aes(decode);
            return aes.decryptStr(code);
        } catch (Exception e) {
            throw new AppException(ErrorCodeEnum.unknown_error);
        }
    }

    public static void main(String[] args) {
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        // 使用Base64编码将字节数组转换为字符串
        String encode = Base64.encode(key);
        System.out.println("秘钥：" + encode);
        byte[] decode = Base64.decode(encode);
        // 构建
        AES aes = SecureUtil.aes(decode);
        String encryptHex = aes.encryptHex("123456");
        System.out.println("加密：" + encryptHex);
        String decryptStr = aes.decryptStr(encryptHex);
        System.out.println("解密：" + decryptStr);
    }

}
