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

public class BoxStyle {

    public static final String defaultColor = "#000000";

    public static final String defaultFontWeight = "normal";

    public static final String defaultFontStyle = "normal";

    public static final String defaultPosition = "absolute";

    public static final String transparentColor = "rgba(0,0,0,0)";

    private String units;

    // font
    private String fontFamily;

    private float fontSize;

    private String fontWeight;

    private String fontStyle;

    private float lineHeight;

    private float wordSpacing;

    private float letterSpacing;

    private String color;

    private String strokeColor;

    // position
    private String position;

    private float left;

    private float top;

    /**
     * Creates a new style using the specified units for lengths.
     * 
     * @param units Units used for lengths (e.g. 'pt')
     */
    public BoxStyle(String units) {
        this.units = units;
        this.fontFamily = null;
        this.fontSize = 0;
        this.fontWeight = null;
        this.fontStyle = null;
        this.lineHeight = 0;
        this.wordSpacing = 0;
        this.letterSpacing = 0;
        this.color = null;
        this.position = null;
        this.left = 0;
        this.top = 0;
    }

    public BoxStyle(BoxStyle src) {
        this.units = src.units;
        this.fontFamily = src.fontFamily;
        this.fontSize = src.fontSize;
        this.fontWeight = src.fontWeight;
        this.fontStyle = src.fontStyle;
        this.lineHeight = src.lineHeight;
        this.wordSpacing = src.wordSpacing;
        this.letterSpacing = src.letterSpacing;
        this.color = src.color;
        this.position = src.position;
        this.left = src.left;
        this.top = src.top;
        this.strokeColor = src.strokeColor;
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (position != null && !position.equals(defaultPosition))
            appendString(ret, "position", position);
        appendLength(ret, "top", top);
        appendLength(ret, "left", left);
        appendLength(ret, "line-height", lineHeight);
        if (fontFamily != null)
            appendString(ret, "font-family", fontFamily);
        if (fontSize != 0)
            appendLength(ret, "font-size", fontSize);
        if (fontWeight != null && !defaultFontWeight.equals(fontWeight))
            appendString(ret, "font-weight", fontWeight);
        if (fontStyle != null && !defaultFontStyle.equals(fontStyle))
            appendString(ret, "font-style", fontStyle);
        if (wordSpacing != 0)
            appendLength(ret, "word-spacing", wordSpacing);
        if (letterSpacing != 0)
            appendLength(ret, "letter-spacing", letterSpacing);
        if (color != null && !defaultColor.equals(color))
            appendString(ret, "color", color);
        if (strokeColor != null && !strokeColor.equals(transparentColor))
            ret.append(createTextStrokeCss(strokeColor));

        return ret.toString();
    }

    public String formatLength(float length) {
        // return String.format(Locale.US, "%1.1f%s", length, units); //nice but slow
        return (float)length + units;
    }

    /**
     * @return the units
     */
    public String getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * @return the fontFamily
     */
    public String getFontFamily() {
        return fontFamily;
    }

    // ================================================================

    /**
     * @param fontFamily the fontFamily to set
     */
    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    /**
     * @return the fontSize
     */
    public float getFontSize() {
        return fontSize;
    }

    /**
     * @param fontSize the fontSize to set
     */
    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * @return the fontWeight
     */
    public String getFontWeight() {
        return fontWeight;
    }

    /**
     * @param fontWeight the fontWeight to set
     */
    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    /**
     * @return the fontStyle
     */
    public String getFontStyle() {
        return fontStyle;
    }

    /**
     * @param fontStyle the fontStyle to set
     */
    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    /**
     * @return the lineHeight
     */
    public float getLineHeight() {
        return lineHeight;
    }

    /**
     * @param lineHeight the lineHeight to set
     */
    public void setLineHeight(float lineHeight) {
        this.lineHeight = lineHeight;
    }

    /**
     * @return the wordSpacing
     */
    public float getWordSpacing() {
        return wordSpacing;
    }

    /**
     * @param wordSpacing the wordSpacing to set
     */
    public void setWordSpacing(float wordSpacing) {
        this.wordSpacing = wordSpacing;
    }

    /**
     * @return the letterSpacing
     */
    public float getLetterSpacing() {
        return letterSpacing;
    }

    /**
     * @param letterSpacing the letterSpacing to set
     */
    public void setLetterSpacing(float letterSpacing) {
        this.letterSpacing = letterSpacing;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the strokeColor
     */
    public String getStrokeColor() {
        return strokeColor;
    }

    /**
     * @param strokeColor the strokeColor to set
     */
    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    /**
     * @return the left
     */
    public float getLeft() {
        return left;
    }

    /**
     * @param left the left to set
     */
    public void setLeft(float left) {
        this.left = left;
    }

    /**
     * @return the top
     */
    public float getTop() {
        return top;
    }

    /**
     * @param top the top to set
     */
    public void setTop(float top) {
        this.top = top;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + ((strokeColor == null) ? 0 : strokeColor.hashCode());
        result = prime * result + ((fontFamily == null) ? 0 : fontFamily.hashCode());
        result = prime * result + Float.floatToIntBits(fontSize);
        result = prime * result + ((fontStyle == null) ? 0 : fontStyle.hashCode());
        result = prime * result + ((fontWeight == null) ? 0 : fontWeight.hashCode());
        result = prime * result + Float.floatToIntBits(letterSpacing);
        result = prime * result + Float.floatToIntBits(wordSpacing);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BoxStyle other = (BoxStyle)obj;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (strokeColor == null) {
            if (other.strokeColor != null)
                return false;
        } else if (!strokeColor.equals(other.strokeColor))
            return false;
        if (fontFamily == null) {
            if (other.fontFamily != null)
                return false;
        } else if (!fontFamily.equals(other.fontFamily))
            return false;
        if (Float.floatToIntBits(fontSize) != Float.floatToIntBits(other.fontSize))
            return false;
        if (fontStyle == null) {
            if (other.fontStyle != null)
                return false;
        } else if (!fontStyle.equals(other.fontStyle))
            return false;
        if (fontWeight == null) {
            if (other.fontWeight != null)
                return false;
        } else if (!fontWeight.equals(other.fontWeight))
            return false;
        if (Float.floatToIntBits(letterSpacing) != Float.floatToIntBits(other.letterSpacing))
            return false;
        if (Float.floatToIntBits(wordSpacing) != Float.floatToIntBits(other.wordSpacing))
            return false;
        return true;
    }

    private void appendString(StringBuilder s, String propertyName, String value) {
        s.append(propertyName);
        s.append(':');
        s.append(value);
        s.append(';');
    }

    // ================================================================

    private void appendLength(StringBuilder s, String propertyName, float value) {
        s.append(propertyName);
        s.append(':');
        s.append(formatLength(value));
        s.append(';');
    }

    private String createTextStrokeCss(String color) {
        // text shadow fall back for non webkit, gets disabled in default style sheet
        // since can't use @media in inline styles
        String strokeCss = "-webkit-text-stroke: %color% 1px ;" + "text-shadow:" + "-1px -1px 0 %color%, " + "1px -1px 0 %color%," + "-1px 1px 0 %color%, " + "1px 1px 0 %color%;";

        return strokeCss.replaceAll("%color%", color);
    }
}
