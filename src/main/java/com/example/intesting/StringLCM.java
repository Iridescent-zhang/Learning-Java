package com.example.intesting;

import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.intesting
 * @ClassName : .java
 * @createTime : 2025/2/14 16:03
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 两个字符串的最小公倍数字符串
 * 规定字符串乘法如下，ab*3 = ababab
 * 给定两个字符串，求他们的最小公倍字符串
 * 比如baba，ba  就是baba
 * aba ab就是没有
 */
public class StringLCM {

    public static void main(String[] args) {
        String[][] words = {{"baba", "ba"}, {"aba", "ab"}, {"aaa", "aaaa"}, {"abababab", "abab"}, {"abababab", "ababab"}};
        for(int i = 0; i< words.length; i++) {
            String stringLcm = lcmOfStrings(words[i][0], words[i][1]);
            System.out.println("stringLcm = " + stringLcm);
        }
    }

    // 求两个字符串的最小公倍字符串
    public static String lcmOfStrings(String str1, String str2) {
        String gcdStr = gcdOfStrings(str1, str2);
        if (gcdStr.isEmpty()) {
            return "没有";
        }

        // 这里其实是考虑到了 假如两个串各自分解成多个公约串，这两个乘数实际上不会有公因子了，比如一个是6x, 一个是4x，这并不可能，这种情况公约串应该是 2x
        int lcmLength = (str1.length() * str2.length()) / gcdStr.length();
        StringBuilder lcmStrBuilder = new StringBuilder();
        while (lcmStrBuilder.length() < lcmLength) {
            lcmStrBuilder.append(gcdStr);
        }
        return lcmStrBuilder.toString();
    }

    // TODO 求两个字符串的最大公约字符串，太牛了
    public static String gcdOfStrings(String str1, String str2) {
        if (str2.length() > str1.length()) {
            return gcdOfStrings(str2, str1);
        }
        if (str1.equals(str2)) return str1;
        if(str2.isEmpty()) {
            return "";
        }
        // Note also that true will be returned if the argument is an empty string
        if(!str1.startsWith(str2)) {
            return "";
        }
        return gcdOfStrings(str1.substring(str2.length()), str2);
    }

    /**
     * 你这写的好唐啊
     */
    public static String stringLcm(String str1, String str2) {
        // 先找两个字符串的最大公约字符串。找一个字符串的重复子串是一道 lc
        HashSet<String> hset = new HashSet<>();
        int len1 = str1.length();
        String repl = str1 + str1;
        // 重复子串可能是自身，所以是 l<=len1
        for(int l = 1; l<=len1; l++) {
            if(len1%l==0) {
                if (repl.substring(l, l+len1).equals(str1)) {
                    hset.add(repl.substring(0,l));
                }
            }
        }

        String gcdString = "";
        int maxLen = 0;
        int len2 = str2.length();
        repl = str2 + str2;
        for(int l = 1; l<=len2; l++) {
            if(len2%l==0) {
                if (repl.substring(l, l+len2).equals(str2)) {
                    String substring = repl.substring(0, l);
                    if(hset.contains(substring) && substring.length()>maxLen) {
                        maxLen = substring.length();
                        gcdString = substring;
                    }
                }
            }
        }

        if(gcdString.equals("")) return "";

        int cnt1 = len1/maxLen;
        int cnt2 = len2/maxLen;
        // 找二者的最小公倍数（二者相乘除以他们的最大公约数）
        int gcd = gcd(cnt1, cnt2);
        int lcm = cnt1*cnt2/gcd;

        return gcdString.repeat(lcm);
    }

    // a 表面上要大于 b，实际不用管
    public static int gcd(int a, int b) {
        if(a%b == 0) {
            return b;
        }
        return gcd(b, (a%b));
    }
}
