package com.spy.devApplication.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用工具类
 */
public class BasicUtil {
    /**
     * 加密混淆数值
     */
    private static final String SALT = "12*&BHih878HJB^&(hbxiua";

    /**
     * 生成随机用户名
     * @return
     */
    public static String randomName() {
        String randomUserName = "用户" + UUID.randomUUID().toString().replace("-", "").trim().substring(0, 6);
        return randomUserName;
    }

    /**
     * 校验密码
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        // 长度在6-16位，只能包含数字、大写字母、小写字母
        String pattern = "^[a-zA-Z0-9]{6,16}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(password);
        return m.matches();
    }

    /**
     * 校验电话号码
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        String pattern = "^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(phone);
        return m.matches();
    }

    public static boolean checkUserName(String userName) {
        if(StringUtils.isBlank(userName)) {
            return false;
        }

        if(userName.length() >= 10) {
            return false;
        }

        return true;
    }
    /**
     * 校验邮箱
     */
    public static boolean checkEmail(String email) {
        String pattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(email);
        return m.matches();
    }

    /**
     * 加密密码
     * @param password
     * @return
     */
    public static String md5Password(String password) {
        String md5Password = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        return md5Password;
    }
}
