package com.example.test;

import java.util.Arrays;
import java.util.Scanner;

public class Main1 {
    public static void main(String[] args) {
        // 交换四个位置 字符     找字典序最小的
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        String s = sc.nextLine();
        String ans = s;
        char[] chs = s.toCharArray();
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        String tt = new String(chars);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k<i && k<=n-1-j; k++) {
                    char tmp = chs[i];
                    chs[i] = chs[i-k];
                    chs[i-k] = tmp;
                    tmp = chs[j];
                    chs[j] = chs[j+k];
                    chs[j+k] = tmp;
                    String s1 = new String(chs);
                    if (s1.compareTo(ans) < 0) {
                        ans = s1;
                        if (ans.equals(tt)) {
                            System.out.println(ans);
                            return;
                        }
                    }
                    tmp = chs[j];
                    chs[j] = chs[j+k];
                    chs[j+k] = tmp;
                    tmp = chs[i];
                    chs[i] = chs[i-k];
                    chs[i-k] = tmp;
                }
            }
        }
        System.out.println(ans);
    }
}