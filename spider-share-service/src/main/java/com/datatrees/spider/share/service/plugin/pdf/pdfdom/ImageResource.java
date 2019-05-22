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

package com.datatrees.spider.share.service.plugin.pdf.pdfdom;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageResource extends HtmlResource {

    private final BufferedImage image;

    private double x = 0;

    private double y = 0;

    public ImageResource(String name, BufferedImage image) {
        super(name);

        this.image = image;
    }

    public byte[] getData() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", buffer);
        return buffer.toByteArray();
    }

    public String getFileEnding() {
        return "png";
    }

    public String getMimeType() {
        return "image/png";
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public float getHeight() {
        return image.getHeight();
    }

    public float getWidth() {
        return image.getWidth();
    }
}
