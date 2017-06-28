package com.lei.baselib_java.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rymyz on 2017/5/16.
 */

public class PatternUtil {
    public static final String PHONE = "^1[34578]\\d{9}$";
    public static final String TELPHONE = "\\d{3,4}-\\d{7,8}";
    public static final String CODE = "(?<![0-9])([0-9]{1,6})(?![0-9])";
    public static final String EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    public static final String NICKNAME = "[A-Za-z0-9_\\-\u4e00-\u9fa5]+";
    public static final String PASSWORD = "\\w{6,20}$";
    public static final String IDENTIFY = "^\\d{15}|\\d{18}$";

    public static boolean pattern(String form, String content) {
        Pattern p = Pattern.compile(form);
        Matcher m = p.matcher(content);

        return m.matches();
    }
}
