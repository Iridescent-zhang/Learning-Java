package com.example.intesting;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.test
 * @ClassName : .java
 * @createTime : 2024/12/26 23:18
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
//给定n个正整数，求选出两个数拼接在一起能得到的最大整数
public class CombineTwoInteger {
    public static void main(String[] args) {
        int[] nums = {3, 5, 9, 34, 30, 93, 95, 903,8546,35,9,45};
        String[] array = Arrays.stream(nums).mapToObj(String::valueOf).toArray(String[]::new);
        Arrays.sort(array, (o1, o2) -> {
            if (o1.length()!=o2.length()) {
                return o1.length()-o2.length();  // 按长度升序排列
            }
            return o1.compareTo(o2);  // 长度相同按字典序排列
        });
        int len = array.length;
        String s1 = array[len - 1];
        String s2 = array[len - 2];
        int ans;
        if (s1.length()==s2.length()) {
            ans = Integer.parseInt(s1+s2);
        }else {
            ans = s2.compareTo(s1)>0 ? Integer.parseInt(s2+s1) : Integer.parseInt(s1+s2);
        }
        System.out.println("ans = " + ans);

        for (String s : array) {
            System.out.println("s = " + s);
        }
    }
}
