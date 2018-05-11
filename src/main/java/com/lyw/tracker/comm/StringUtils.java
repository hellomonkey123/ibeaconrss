package com.lyw.tracker.comm;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .
 * 字符串工具类
 */
public class StringUtils {
    /**
     * 是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || TextUtils.isEmpty(str);
    }

    /**
     * 将规定值，替换成要替换滴值
     * */
    public static String format(String needReplace, String res) {
        if (res != null && !TextUtils.isEmpty(res)) {
            return MessageFormat.format(res, needReplace);
        } else {
            return null;
        }
    }
    public static SpannableStringBuilder setTextColor(String content, int start, int end){
        SpannableStringBuilder style=new SpannableStringBuilder(content);
        style.setSpan(new ForegroundColorSpan(Color.RED),start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        style.setSpan(new BackgroundColorSpan(Color.RED),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);设置字体背景颜色值
//        style.setSpan(new ForegroundColorSpan(Color.RED),start1,end2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//设置字体颜色值
        return style;
    }
    public static SpannableStringBuilder setTextColor(String content, int start, int end, int start1, int end2){
        SpannableStringBuilder style=new SpannableStringBuilder(content);
        style.setSpan(new ForegroundColorSpan(Color.RED),start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        style.setSpan(new BackgroundColorSpan(Color.RED),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);设置字体背景颜色值
        style.setSpan(new ForegroundColorSpan(Color.RED),start1,end2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//设置字体颜色值
        return style;
    }


    public boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    //根据传入的字符串
    public static String getStr(String str) {
        return isEmpty(str) ? "" : str;
    }

    //根据得到的edit获取对应的值
    public static String getEditTextContent(EditText edt) {
        return edt.getText().toString().trim();
    }

    //判断et为空判断
    public static boolean isEditTextEmpty(EditText edt) {
        return isEmpty(getEditTextContent(edt));
    }

    //手机号码的判断
    public static boolean isPhoneEditTextEmpty(EditText edt) {
        return isEmpty(getEditTextContent(edt)) || getEditTextContent(edt).length() != 11;
    }

}
