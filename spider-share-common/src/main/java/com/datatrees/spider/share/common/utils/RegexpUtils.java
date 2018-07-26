package com.datatrees.spider.share.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpUtils {

    public static String select(String source, String regex, int groupIndex) {
        return selectFirst(source, regex, groupIndex);
    }

    public static String selectFirst(String source, String regex, int groupIndex) {
        Matcher matcher = Pattern.compile(regex).matcher(source);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return null;
    }

    public static List<String> selectList(String source, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(source);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public static String selectLast(String source, String regex, int groupIndex) {
        Matcher matcher = Pattern.compile(regex).matcher(source);
        String selectText = null;
        while (matcher.find()) {
            selectText = matcher.group();
        }
        matcher = Pattern.compile(regex).matcher(selectText);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return null;
    }
}