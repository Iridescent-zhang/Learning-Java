package com.example.intesting;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.intesting
 * @ClassName : .java
 * @createTime : 2025/3/15 23:52
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * oppo 笔试
 * 一个长达 10^5 的字符串数字，其中包含一些问号（?），每个问号可以替换为 0 到 9 中的任意一个数字。问有多少种替换方式，使得整个数字能被 3 整除。
 * 关键在 一个数能被 3 整除的条件是：其各位数字之和能被 3 整除。
 * 再用 dp 就可以了
 * 下次在做这种3整除的做不出来就去吃屎
 */
public class NumberDivisibleByThree {
    public static int countWays(String num) {
        int sum = 0; // 已知数字之和
        int questionMarks = 0; // 问号的数量
        for (char c : num.toCharArray()) {
            if (c == '?') {
                questionMarks++;
            } else {
                sum += (c - '0');
            }
        }

        // 动态规划数组
        int[][] dp = new int[questionMarks + 1][3];
        dp[0][sum % 3] = 1; // 初始状态

        int idx = sum % 3;
        // 处理每个问号
        if(num.charAt(0)=='?') {
            for (int k = 1; k <= 9; k++) {
                int newMod = (idx + k) % 3;
                dp[1][newMod] += dp[0][idx];
            }
        }else {
            for (int k = 0; k <= 9; k++) {
                int newMod = (idx + k) % 3;
                dp[1][newMod] += dp[0][idx];
            }
        }
        for (int i = 2; i <= questionMarks; i++) {
            for (int j = 0; j < 3; j++) {
                if (dp[i - 1][j] > 0) {
                    for (int k = 0; k <= 9; k++) {
                        int newMod = (j + k) % 3;
                        dp[i][newMod] += dp[i - 1][j];
                        dp[i][newMod] %= 1000000007;
                    }
                }
            }
        }

        return dp[questionMarks][0]; // 返回满足条件的方案数
    }

    public static void main(String[] args) {
        String num = "??????????";
//        String num = "12?3?";
        System.out.println(countWays(num)); // 输出结果
    }
}
