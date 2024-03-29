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

import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;

public class TextMetrics {

    private float x, baseline, width, height, pointSize, descent, ascent, fontSize;

    private PDFont font;

    public TextMetrics(TextPosition tp) {
        x = tp.getX();
        baseline = tp.getY();
        font = tp.getFont();
        width = tp.getWidth();
        height = tp.getHeight();
        pointSize = tp.getFontSizeInPt();
        fontSize = tp.getYScale();
        ascent = getAscent();
        descent = getDescent();
    }

    public static float getBoundingBoxDescent(PDFont font, float fontSize) {
        try {
            BoundingBox bBox = font.getBoundingBox();
            float boxDescent = bBox.getLowerLeftY();
            return (boxDescent / 1000) * fontSize;
        } catch (IOException e) {
        }
        return 0.0f;
    }

    public static float getBoundingBoxAscent(PDFont font, float fontSize) {
        try {
            BoundingBox bBox = font.getBoundingBox();
            float boxAscent = bBox.getUpperRightY();
            return (boxAscent / 1000) * fontSize;
        } catch (IOException e) {
        }
        return 0.0f;
    }

    private static float getAscent(PDFont font, float fontSize) {
        try {
            return (font.getFontDescriptor().getAscent() / 1000) * fontSize;
        } catch (Exception e) {
        }
        return 0.0f;
    }

    private static float getDescent(PDFont font, float fontSize) {
        try {
            return (font.getFontDescriptor().getDescent() / 1000) * fontSize;
        } catch (Exception e) {
        }
        return 0.0f;
    }

    public void append(TextPosition tp) {
        width += tp.getX() - (x + width) + tp.getWidth();
        height = Math.max(height, tp.getHeight());
        ascent = Math.max(ascent, getAscent(tp.getFont(), tp.getYScale()));
        descent = Math.min(descent, getDescent(tp.getFont(), tp.getYScale()));
    }

    public float getX() {
        return x;
    }

    public float getTop() {
        if (ascent != 0) {
            return baseline - ascent;
        } else {
            return baseline - getBoundingBoxAscent();
        }
    }

    public float getBottom() {
        if (descent != 0) {
            return baseline - descent;
        } else {
            return baseline - getBoundingBoxDescent();
        }
    }

    public float getBaseline() {
        return baseline;
    }

    public float getAscent() {
        return getAscent(font, fontSize);
    }

    public float getDescent() {
        final float descent = getDescent(font, fontSize);
        return descent > 0 ? -descent : descent; // positive descent is not allowed
    }

    public float getBoundingBoxDescent() {
        return getBoundingBoxDescent(font, fontSize);
    }

    public float getBoundingBoxAscent() {
        return getBoundingBoxAscent(font, fontSize);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return getBottom() - getTop();
    }

    public float getPointSize() {
        return pointSize;
    }
}
