package com.carlos.utils;

import java.util.List;
import java.util.Map;

/**
 * Created by Carlos on 2017/3/23.
 */
public class CheckUtil {
    /**
     * 对象是否为空判断
     *      如果只是判断是否是null，适配所有类型
     *      如果要判断是否是空值，某种类型不在考虑之内，抛出异常
     * @param obj
     * @return
     * @throws Exception
     */
    public static boolean isEmpty(Object obj) throws Exception{
        if (null == obj) {
            return true;
        }
        if (obj instanceof String) {
            return isEmptyStr((String)obj);
        }
        if (obj instanceof List) {
            return isEmptyList((List)obj);
        }
        if (obj instanceof Map) {
            return isEmptyMap((Map)obj);
        }
        throw new Exception("对象是否为空判断，类型不支持");
    }

    // 是否为空字符串
    public  static boolean isEmptyStr(String obj) {
        if (null == obj || "".equals(obj.toString())) {
            return true;
        }
        return false;
    }

    // 是否为空队列
    public static boolean isEmptyList(List obj) {
        if (null == obj || 0 == obj.size()) {
            return true;
        }
        return false;
    }


    // 是否为空map
    public static boolean isEmptyMap(Map obj) {
        if (null == obj || 0 == obj.size()) {
            return true;
        }
        return false;
    }

    public  static boolean isContainsSpecialchar(String str){

        return str.contains("\\")|str.contains("/");
    }
}
