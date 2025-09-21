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

class Solution {
    public String decodeString(String s) {
        int len = s.length();
        Deque<String> stk = new ArrayDeque<>();
        Deque<String> tmpStk = new ArrayDeque<>();
        int i = 0;
        while(i < len) {
            char c = s.charAt(i);
            if(Character.isLetterOrDigit(c)) {
                int tmp = i;
                while(tmp < len && Character.isLetterOrDigit(s.charAt(tmp))) {
                    tmp++;
                }
                stk.push(s.substring(i, tmp));
                i = tmp;
                continue;
            }else if(c == ']') {
                StringBuilder sb = new StringBuilder();
                while(!Character.isDigit(stk.peek().charAt(0))) {
                    tmpStk.push(stk.pop());
                }
                while(!tmpStk.isEmpty()) {
                    sb.append(tmpStk.pop());
                }
                String str = sb.toString();
                int cnt = Integer.parseInt(stk.pop());
                for(int k=1; k<cnt; k++) {
                    sb.append(str);
                }
                stk.push(sb.toString());
            }
            i++;
        }
        StringBuilder sb = new StringBuilder();
        while(!stk.isEmpty()) {
            sb.append(stk.removeLast());
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        int x = (-3) % 10;
        System.out.println(x);
    }
}
