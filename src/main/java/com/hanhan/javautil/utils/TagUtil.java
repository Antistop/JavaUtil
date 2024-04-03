package com.hanhan.javautil.utils;

import java.util.*;

/**
 * @author pl
 */
public class TagUtil {
    private static final String KP = ",";

    public static String addStringSet(String tagStr, String append) {
        Set<String> tag = toStringSet(tagStr);
        tag.add(append);
        return fromSet(tag);
    }

    public static Set<String> toStringSet(String tagStr) {
        Set<String> tag = new HashSet<>();
        if (isEmpty(tagStr)) {
            return tag;
        }
        String[] split = tagStr.split(KP);
        if (isEmpty(split)) {
            return tag;
        }
        for (String s : split) {
            if (!isEmpty(s)) {
                tag.add(s);
            }
        }
        return tag;
    }

    public static Set<Integer> toIntSet(String tagStr) {
        Set<Integer> tag = new HashSet<>();
        if (isEmpty(tagStr)) {
            return tag;
        }
        String[] split = tagStr.split(KP);
        if (isEmpty(split)) {
            return tag;
        }
        for (String s : split) {
            if (!isEmpty(s)) {
                try {
                    tag.add(Integer.parseInt(s));
                } catch (NumberFormatException ignore) {
                }
            }
        }
        return tag;
    }

    public static List<String> toStringList(String tagStr) {
        return toStringList(tagStr, KP);
    }

    public static List<String> toStringList(String tagStr, String sp) {
        if (isEmpty(tagStr)) {
            return new ArrayList<>();
        }
        String[] split = tagStr.split(sp);
        if (isEmpty(split)) {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<>(split.length);
        for (String s : split) {
            if (!isEmpty(s)) {
                list.add(s);
            }
        }
        return list;
    }

    private static <T> boolean isEmpty(T[] tag) {
        return tag == null || tag.length == 0;
    }

    private static boolean isEmpty(String tag) {
        return tag == null || "".equals(tag);
    }

    private static boolean isEmpty(Collection<?> tag) {
        return tag == null || tag.isEmpty();
    }

    /**
     * 前后分隔符便于like或者contains查询时（查询信息拼上前后分隔符）不要考虑开头和结尾的数据
     */
    public static String fromSet(Collection<?> tag) {
        if (isEmpty(tag)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(KP);
        for (Object str : tag) {
            if (str == null) {
                continue;
            }
            sb.append(str).append(KP);
        }
        return sb.toString();
    }

    public static String strFromSet(Set<String> tag) {
        if (isEmpty(tag)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : tag) {
            if (str == null) {
                continue;
            }
            sb.append(str).append(KP);
        }
        sb = new StringBuilder(sb.substring(0,sb.length()-1));
        return sb.toString();
    }

    public static boolean check(Set<Integer> tags, int tag) {
        if (tags == null) {
            return false;
        }
        return tags.contains(tag);
    }

    public static boolean checkConfig(Set<String> tags, String tag) {
        if (tags == null || tags.isEmpty()) {
            return true;
        }
        return tags.contains(tag);
    }

    public static void main(String[] args) {
        Set<String> strings = new HashSet<>();
        strings.add("2222");
        strings.add("1111");
        System.out.println(strFromSet(strings));
        System.out.println(fromSet(strings));
    }
}
