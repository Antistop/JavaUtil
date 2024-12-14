package com.hanhan.api.spilt;

/**
 * @author pl
 */
public class SplitTest {

    /**
     * 记录split函数的一些坑
     *
     */
    //TODO split的坑
    //NULL的值用于填补那些从 objectValue 中获取的空值 和 split的坑
    //处理数据未空的情况，如果最后一个字符
    //String[] dataLine = StringUtils.split(data, SPLIT); 忽略会""
    public static final String SPLIT_STR = "\u2000";
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("a");
        sb.append(SPLIT_STR);
        sb.append(SPLIT_STR);
        sb.append(SPLIT_STR);
        sb.append("b");
        String[] split = sb.toString().split(SPLIT_STR);
        System.out.println(split.length);
    }

}
