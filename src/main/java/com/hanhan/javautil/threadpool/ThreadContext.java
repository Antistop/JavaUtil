package com.hanhan.javautil.threadpool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ThreadContext {

    private static final ThreadLocal<Map<String, Object>> context = ThreadLocal.withInitial(HashMap::new);

    public static void put(String key, Object value) {
        Map<String, Object> contextMap = context.get();
        contextMap.put(key, value);
    }


    public static Object get(String key) {
        Map<String, Object> contextMap = context.get();
        return contextMap.get(key);
    }

    public static void clear() {
        context.get().clear();
    }

    /**
     * threadLocal线程复制
     */
    public static Map<String, Object> copy() {
        return Collections.unmodifiableMap(context.get());
    }

    public static void restore(Map<String, Object> contextMap) {
        context.get().clear();
        context.get().putAll(contextMap);
    }


}
