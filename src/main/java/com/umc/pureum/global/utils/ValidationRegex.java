package com.umc.pureum.global.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* 형식 체크하는 곳 */
public class ValidationRegex {
    /* 이메일 형식 체크 */
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();

    }
}
