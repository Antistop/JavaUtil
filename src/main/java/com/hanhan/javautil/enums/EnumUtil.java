package com.hanhan.javautil.enums;

import java.util.HashMap;
import java.util.Map;

public class EnumUtil {
    private static final Map<Class<? extends IntegerEnum>, Map<Integer, ? extends IntegerEnum>> integerEnumCache = new HashMap<>();
    private static final Map<Class<? extends CodeEnum>, Map<String, ? extends CodeEnum>> codeEnumCache = new HashMap<>();

    public static <T extends Enum> boolean in(T t, T... in) {
        if (isEmpty(in)) {
            return false;
        }
        if (t == null) {
            return false;
        }
        for (int i = 0; i < in.length; i++) {
            if (in[i] == t) {
                return true;
            }
        }
        return false;
    }

    private static <T> boolean isEmpty(T[] in) {
        return in == null || in.length == 0;
    }

    public static <T extends IntegerEnum> boolean in(Integer t, T... in) {
        if (isEmpty(in)) {
            return false;
        }
        if (t == null) {
            return false;
        }
        for (int i = 0; i < in.length; i++) {
            if (in[i].getCode() == t) {
                return true;
            }
        }
        return false;
    }

    public static <T extends CodeEnum> boolean in(String t, T... in) {
        if (isEmpty(in)) {
            return false;
        }
        if (t == null) {
            return false;
        }
        for (int i = 0; i < in.length; i++) {
            if (in[i].getCode().equals(t)) {
                return true;
            }
        }
        return false;
    }

    public static <T extends IntegerEnum> boolean notIn(Integer t, T... in) {
        if (isEmpty(in)) {
            return true;
        }
        if (t == null) {
            return true;
        }
        for (int i = 0; i < in.length; i++) {
            if (t.equals(in[i].getCode())) {
                return false;
            }
        }
        return true;
    }


    public static <T extends CodeEnum> boolean notIn(String t, T... in) {
        if (isEmpty(in)) {
            return true;
        }
        if (t == null) {
            return true;
        }
        for (int i = 0; i < in.length; i++) {
            if (in[i].getCode().equals(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean notIn(T t, T... in) {
        if (isEmpty(in)) {
            return true;
        }
        if (t == null) {
            return true;
        }
        for (int i = 0; i < in.length; i++) {
            if (in[i] == t) {
                return false;
            }
        }
        return true;
    }

    public static <T extends CodeEnum> T getByCode(String code, Class<T> clazz) {
        if (!clazz.isEnum() || code == null) {
            return null;
        }
        Map<String, T> cache = (Map<String, T>) codeEnumCache.get(clazz);
        if (cache == null) {
            synchronized (integerEnumCache) {
                cache = (Map<String, T>) codeEnumCache.get(clazz);
                if (cache == null) {
                    cache = new HashMap<>();
                    T[] enumConstants = clazz.getEnumConstants();
                    for (T enumConstant : enumConstants) {
                        cache.put(enumConstant.getCode(), enumConstant);
                    }
                    codeEnumCache.put(clazz, cache);
                }
            }
        }
        return cache.get(code);
    }

    public static <T extends IntegerEnum> T getByCode(int code, Class<T> clazz) {
        if (!clazz.isEnum()) {
            return null;
        }
        Map<Integer, T> cache = (Map<Integer, T>) integerEnumCache.get(clazz);
        if (cache == null) {
            synchronized (integerEnumCache) {
                cache = (Map<Integer, T>) integerEnumCache.get(clazz);
                if (cache == null) {
                    cache = new HashMap<>();
                    T[] enumConstants = clazz.getEnumConstants();
                    for (T enumConstant : enumConstants) {
                        cache.put(enumConstant.getCode(), enumConstant);
                    }
                    integerEnumCache.put(clazz, cache);
                }
            }
        }
        return cache.get(code);
    }

    public static <T extends IntegerEnum, Enum> T getByCode(Integer code, Class<T> clazz) {
        if (code == null) {
            code = 0;
        }
        return getByCode(code.intValue(), clazz);
    }
}
