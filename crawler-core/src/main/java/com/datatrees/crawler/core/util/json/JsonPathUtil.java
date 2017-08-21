package com.datatrees.crawler.core.util.json;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Jerry
 * @datetime 2015-07-18 11:18
 */
public class JsonPathUtil {

//    private static String filter(String json){
//        return json.replaceAll("\\s+", "");
//    }

    private static Object read(String json, String jsonPath) {
        if (json == null)return null;

        return JsonPath.read(json, jsonPath);
    }

    private static String parse(Object obj) {
        return JsonPath.parse(obj).jsonString();
    }

    private static String parseAsString(Object jsonObj) {
        String result = null;

        if (jsonObj == null) {
            result = "";
        } else if (jsonObj instanceof String) {
            result = String.valueOf(jsonObj);
        } else if (jsonObj instanceof Number){
        	result = String.valueOf(jsonObj);
        } else if (jsonObj instanceof Boolean){
        	result = String.valueOf(jsonObj);
        } else if(jsonObj instanceof JSONArray) {
            JSONArray array = (JSONArray) jsonObj;
            if (array.size() == 1) {
                result = array.get(0).toString();
            } else {
                result = array.toJSONString();
            }
        } else {
            result = parse(jsonObj);
        }

        return result;
    }

    private static List<String> parseAsList(Object jsonObj) {
        List<String> result = new ArrayList<String>();

        if (jsonObj == null) {
            result.add("");
        } else if (jsonObj instanceof String) {
            result.add(String.valueOf(jsonObj));
        } else if (jsonObj instanceof JSONArray) {
            JSONArray array = (JSONArray) jsonObj;

            Iterator iterator = array.iterator();

            Object item = null;
            while (iterator.hasNext()) {
                item = iterator.next();

                if (item instanceof String) {
                    result.add(String.valueOf(item));
                } else {
                    result.add(parse(item));
                }
            }
        } else {
            result.add(parse(jsonObj));
        }

        return result;
    }

    /**
     * 对JSON进行jsonpath解析，返回结果json字符串
     * <p>
     *     例如：
     *     { "store": {
     *          "book": [
     *              { "category": "reference",
     *                "author": "Nigel Rees",
     *                "title": "Sayings of the Century",
     *                "price": 8.95
     *              },
     *              { "category": "fiction",
     *                "author": "Evelyn Waugh",
     *                "title": "Sword of Honour",
     *                "price": 12.99,
     *                "isbn": "0-553-21311-3"
     *              }
     *              ],
     *          "bicycle": {
     *              "color": "red",
     *              "price": 19.95
     *          }
     *      }
     *   }
     *   jsonpath:$.store.book.category
     *   result:"[\"reference\", \"fiction\"]"
     * </p>
     * @param json
     * @param jsonPath
     * @return
     */
    public static String readAsString(String json, String jsonPath) {
        return parseAsString(read(json, jsonPath));
    }

    /**
     *      * 对JSON进行jsonpath解析，返回结果json字符串列表
     * <p>
     *     例如：
     *     { "store": {
     *          "book": [
     *              { "category": "reference",
     *                "author": "Nigel Rees",
     *                "title": "Sayings of the Century",
     *                "price": 8.95
     *              },
     *              { "category": "fiction",
     *                "author": "Evelyn Waugh",
     *                "title": "Sword of Honour",
     *                "price": 12.99,
     *                "isbn": "0-553-21311-3"
     *              }
     *              ],
     *          "bicycle": {
     *              "color": "red",
     *              "price": 19.95
     *          }
     *      }
     *   }
     *   jsonpath:$.store.book.category
     *   result: List["reference", "fiction"]
     * </p>
     * @param json
     * @param jsonPath
     * @param json
     * @param jsonPath
     * @return
     */
    public static List<String> readAsList(String json, String jsonPath) {
        return parseAsList(read(json, jsonPath));
    }

}
