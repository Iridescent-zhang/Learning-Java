package com.example.test;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.test
 * @ClassName : .java
 * @createTime : 2025/3/21 20:45
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import java.util.*;


public class Main {
    public static void main(String[] args) {
        // 最长不相邻的子序列，且最大公约数要大于1
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] nums = new int[n];
        for(int i=0; i<n; i++) {
            nums[i] = sc.nextInt();
        }
        int ans = 1;

        // 最大公约数  长度
        int[][] notake = new int[n+1][2];
        int[][] take = new int[n+1][2];
        for (int i = 1; i <= n; i++) {
            if (notake[i-1][0] == 0) {
                take[i][0] = nums[i-1];
                take[i][1] = 1;
            }else {
                take[i][0] = gcd(notake[i-1][0], nums[i-1]);
                if(take[i][0] > 1) {
                    take[i][1] = notake[i-1][1] + 1;
                }else {
                    take[i][0] = nums[i-1];
                    take[i][1] = 1;
                }
            }

            if(take[i-1][1] > notake[i-1][1]) {
                notake[i][1] = take[i-1][1];
                notake[i][0] = take[i-1][0];
            }else {
                notake[i][1] = notake[i-1][1];
                notake[i][0] = notake[i-1][0];
            }
            ans = Math.max(Math.max(ans, notake[i][1]), take[i][1]);
        }
        System.out.println(ans);
    }

    public static int gcd(int a, int b) {
        int n = Math.min(a, b);
        while (n>0) {
            if (a % n ==0 && b%n ==0) {
                return n;
            }
            n--;
        }
        return 1;
    }
}