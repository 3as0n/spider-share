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

package com.datatrees.spider.share.service.plugin.pdf.pdfdom;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static BufferedImage rotateImage(BufferedImage image, double theta) {
        int degrees = (int)Math.abs(Math.toDegrees(theta));
        double xCenter = image.getWidth() / 2.0d;
        double yCenter = image.getHeight() / 2.0d;
        AffineTransform rotateTransform = AffineTransform.getRotateInstance(-theta, xCenter, yCenter);
        // Translation adjustments so image still centered after rotate width/height changes
        if (image.getHeight() != image.getWidth() && degrees != 180 && degrees != 0) {
            Point2D origin = new Point2D.Double(0.0, 0.0);
            origin = rotateTransform.transform(origin, null);
            double yTranslate = origin.getY();
            Point2D yMax = new Point2D.Double(0, image.getHeight());
            yMax = rotateTransform.transform(yMax, null);
            double xTranslate = yMax.getX();
            AffineTransform translationAdjustment = AffineTransform.getTranslateInstance(-xTranslate, -yTranslate);
            rotateTransform.preConcatenate(translationAdjustment);
        }
        AffineTransformOp op = new AffineTransformOp(rotateTransform, AffineTransformOp.TYPE_BILINEAR);
        // Have to recopy image because of JDK bug #4723021, AffineTransformationOp throwing
        // exception sometimes
        image = copyImage(image, BufferedImage.TYPE_INT_ARGB);
        // Need to create filter dest image ourselves since AffineTransformOp's own dest image
        // creation
        // throws exceptions in some cases.
        Rectangle bounds = op.getBounds2D(image).getBounds();
        BufferedImage finalImage = new BufferedImage((int)bounds.getWidth(), (int)bounds.getHeight(), BufferedImage.TYPE_INT_ARGB);
        return op.filter(image, finalImage);
    }

    public static BufferedImage copyImage(BufferedImage source, int type) {
        BufferedImage copy = new BufferedImage(source.getWidth(), source.getHeight(), type);
        Graphics gfx = copy.getGraphics();
        gfx.drawImage(source, 0, 0, null);
        gfx.dispose();
        return copy;
    }
}
