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

package com.datatrees.spider.share.service.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class StreamUtils {

    public static final int BUFFER_SIZE = 1024;

    /**
     * tansfer the stream's data to byte[]
     * 
     * @param inputStream
     * @return
     */
    public static byte[] read(InputStream inputStream) {
        byte[] result;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[BUFFER_SIZE];

        int count = 0;
        try {
            while ((count = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
            }

            outputStream.flush();

            result = outputStream.toByteArray();

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            result = new byte[0];
        }

        return result;
    }

    /**
     * convert Object to byte[]
     * 
     * @param object
     * @return
     * @exception IOException
     */
    public static byte[] read(Object object) throws IOException {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);

        objectOutput.writeObject(object);
        objectOutput.flush();
        objectOutput.close();

        try {
            byteOutput.flush();
            return byteOutput.toByteArray();
        } finally {
            byteOutput.close();
        }
    }

    /**
     * convert the byte[] to Object
     * 
     * @param data
     * @return
     * @exception IOException
     * @exception ClassNotFoundException
     */
    public static Object write(byte[] data) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        try {
            return inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw e;
        } finally {
            inputStream.close();
        }
    }

}
