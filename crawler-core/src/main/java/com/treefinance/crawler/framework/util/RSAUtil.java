/*
 * Copyright © 2015 - 2019 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.treefinance.crawler.framework.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年11月11日 下午1:47:04
 */
@Deprecated
public class RSAUtil {

    private static Logger logger = LoggerFactory.getLogger(RSAUtil.class);

    /**
     * 生成密钥对
     * 
     * @param filePath 生成密钥的路径
     * @return
     */
    public static Map<String, String> generateKeyPair(String filePath) {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            // 密钥位数
            keyPairGen.initialize(1024);
            // 密钥对
            KeyPair keyPair = keyPairGen.generateKeyPair();

            // 公钥
            PublicKey publicKey = keyPair.getPublic();
            // 得到公钥字符串
            String publicKeyString = getKeyString(publicKey);

            // 私钥
            PrivateKey privateKey = keyPair.getPrivate();
            // 得到私钥字符串
            String privateKeyString = getKeyString(privateKey);

            try (FileWriter out = new FileWriter("publicKey.keystore"); BufferedWriter pubbw = new BufferedWriter(out)) {
                pubbw.write(publicKeyString);
                pubbw.flush();
            }

            try (FileWriter out = new FileWriter("privateKey.keystore"); BufferedWriter pribw = new BufferedWriter(out)) {
                pribw.write(privateKeyString);
                pribw.flush();
            }

            // 将生成的密钥对返回
            Map<String, String> map = new HashMap<>();
            map.put("publicKey", publicKeyString);
            map.put("privateKey", privateKeyString);
            return map;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 得到公钥
     * 
     * @param key 密钥字符串（经过base64编码）
     * @exception Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 得到私钥
     * 
     * @param key 密钥字符串（经过base64编码）
     * @exception Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 得到密钥字符串（经过base64编码）
     * 
     * @return
     */
    public static String getKeyString(Key key) throws Exception {
        byte[] keyBytes = key.getEncoded();
        String s = new String(Base64.encodeBase64(keyBytes));
        return s;
    }

    /**
     * 使用公钥对明文进行加密，返回BASE64编码的字符串
     * 
     * @param publicKey
     * @param plainText
     * @return
     */
    public static String encrypt(Key publicKey, String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] enBytes = cipher.doFinal(plainText.getBytes());
            return new String(Base64.encodeBase64(enBytes));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static String encryptWithKeystore(String publicKeystore, String plainText) {
        try {
            FileReader fr = new FileReader(publicKeystore);
            BufferedReader br = new BufferedReader(fr);
            String publicKeyString = "";
            String str;
            while ((str = br.readLine()) != null) {
                publicKeyString += str;
            }
            br.close();
            fr.close();
            return encrypt(publicKeyString, plainText, false);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static String encrypt(String keyString, String plainText, boolean isPrivateKey) {
        try {
            Key key = null;
            if (isPrivateKey) {
                key = getPrivateKey(keyString);
            } else {
                key = getPublicKey(keyString);
            }
            return encrypt(key, plainText);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static String encrypt(String publicKeyString, String plainText) {
        return encrypt(publicKeyString, plainText, false);
    }

    /**
     * 使用私钥对明文密文进行解密
     * 
     * @param privateKey
     * @param enStr
     * @return
     */
    public static String decrypt(Key privateKey, String enStr) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] deBytes = cipher.doFinal(Base64.decodeBase64(enStr));
            return new String(deBytes);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 使用keystore对密文进行解密
     * 
     * @param privateKeystore 私钥路径
     * @param enStr 密文
     * @return
     */
    public static String decryptWithKeystore(String privateKeystore, String enStr) {
        try {
            FileReader fr = new FileReader(privateKeystore);
            BufferedReader br = new BufferedReader(fr);
            String privateKeyString = "";
            String str;
            while ((str = br.readLine()) != null) {
                privateKeyString += str;
            }
            br.close();
            fr.close();
            return decrypt(privateKeyString, enStr, false);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static String decrypt(String keyString, String enStr, boolean isPubliceKey) {
        try {
            Key key = null;
            if (isPubliceKey) {
                key = getPublicKey(keyString);
            } else {
                key = getPrivateKey(keyString);
            }
            return decrypt(key, enStr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static String decrypt(String privateKeyString, String enStr) {
        return decrypt(privateKeyString, enStr, false);
    }
}
