package com.example.intesting;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.intesting
 * @ClassName : .java
 * @createTime : 2025/2/16 12:49
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 大小为400的数组，元素大小为1e9，将数组分割成k段，使得每段内部按位异或后全部求和，求和的最大值
 * 区间 dp 太牛了
 *
 * dp[i][j] 表示将前 i 个元素分成 j 段后的最大值。
 */
public class maxKSegmentXorSum {
    public static void main(String[] args) {
        int n = 10;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (Math.random() * n);
        }
        int k = 3; // 例如分成5段
        System.out.println(maxXorSum(arr, k));
    }

    public static int maxXorSum(int[] arr, int k) {
        int n = arr.length;
        int[][] dp = new int[n + 1][k + 1];
        int[] prefixXor = new int[n + 1];

        // 计算前缀异或
        for (int i = 1; i <= n; i++) {
            prefixXor[i] = prefixXor[i - 1] ^ arr[i - 1];
        }

        // 初始化dp数组
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= k; j++) {
                dp[i][j] = Integer.MIN_VALUE;
            }
        }
        dp[0][0] = 0;

        // 动态规划计算每个分割点值
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= k; j++) {
                for (int l = 0; l < i; l++) {
                    if(l >= j - 1) { // 只考虑合法的分割
                        dp[i][j] = Math.max(dp[i][j], dp[l][j - 1] + (prefixXor[i] ^ prefixXor[l]));
                    }
                }
            }
        }

        return dp[n][k];
    }
}
