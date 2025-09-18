package com.example.test;


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.test
 * @ClassName : .java
 * @createTime : 2025/1/16 16:30
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.util.*;

public class Solution {
    /**
     * Note: 类名、方法名、参数名已经指定，请勿修改
     *
     *
     *
     * @param param string字符串
     * @return string字符串
     */
    public String longestPalindrome(String param) {
        // write code here
        int len = param.length();
        int max = 0;
        int left = 0, right = 0;
        for(int i=0; i<len-1; i++) {
            for (int j=0; j<2; j++) {
                int l = i, r = i+j;
                while(l>=0 && r<len && param.charAt(l)==param.charAt(r)) {
                    if((r-l+1) > max) {
                        max = r-l+1;
                        left = l;
                        right = r;
                    }
                    l--;
                    r++;
                }
            }
        }
        return param.substring(left, right+1);
    }
}
