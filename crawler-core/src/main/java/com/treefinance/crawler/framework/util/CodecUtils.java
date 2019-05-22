/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES codec util
 * 
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年9月6日 下午3:10:07
 */
public class CodecUtils {

    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static final SecretKey DEFAULT_SecretKey = new SecretKeySpec("DATATREEDATATREE".getBytes(), "AES");

    public static byte[] decrypt(byte[] data) throws Exception {
        return decrypt(data, DEFAULT_SecretKey);
    }

    public static byte[] encrypt(byte[] data) throws Exception {
        return encrypt(data, DEFAULT_SecretKey);
    }

    public static byte[] decrypt(byte[] data, String keyString) throws Exception {
        return decrypt(data, new SecretKeySpec(keyString.getBytes(), "AES"));
    }

    public static byte[] encrypt(byte[] data, String keyString) throws Exception {
        return encrypt(data, new SecretKeySpec(keyString.getBytes(), "AES"));
    }

    private static byte[] encrypt(byte[] data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.encodeBase64(cipher.doFinal(data));
    }

    private static byte[] decrypt(byte[] data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(Base64.decodeBase64(data));
    }

}
