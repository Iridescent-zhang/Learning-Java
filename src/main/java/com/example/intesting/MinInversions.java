package com.example.intesting;

import java.util.Scanner;

/**
 * 携程秋招笔试 第四题
 * https://mp.weixin.qq.com/s/8xuJAKrofTYdhxCwh_D4gA
 * 动态规划，思路大致正确知道用增量，但没用dp想用滑动窗口，结果有问题
 */
public class MinInversions {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        
        if (n == 0) {
            System.out.println(0);
            return;
        }
        
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        
        // 计算初始逆序对数量
        int initialInversions = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (a[i] > a[j]) {
                    initialInversions++;
                }
            }
        }
        
        if (n < 2) {
            System.out.println(initialInversions);
            return;
        }
        
        int maxDiff = 0;   // 逆序对变化量，我们目标是取减少量，所以是负数
        int[] dp = new int[n];
        
        for (int r = 1; r < n; r++) {
            int diff = 0;
            for (int l = r - 1; l >= 0; l--) {
                if (a[l] > a[r]) {
                    diff--;
                    /**
                     * 应该就是因为 我用的是注释的这种写法，而不是 dp[l] += diff; 这种有累计效果的写法导致出了问题
                     */
//                    dp[l]--;
                } else if (a[l] < a[r]) {
                    diff++;
//                    dp[l]++;
                }

                /**
                 * dp 表示反转当前 l r 区间带来的 逆序对 的增量变化
                 * 假设三个数 3 1 4，通过 diff 累加 r = 2 时 l 从 1 -> 0 的过程中形成的影响，比如 4 1 反转的影响是 逆序对 +1
                 * l 到 0时 3 4 反转的影响是  逆序对 再加 1
                 * 所以在 3 这个位置，整个区间反转的增量起码是 2
                 * 但是，目前这个只考虑了 新来的 r 对 l 的影响，我们不能忽略之前的 r 对 l 的影响，即3 1 反转的影响也要考虑上，所以应该在之前的 dp[l] 的基础上加上这个增量
                 */
                dp[l] += diff;
                if (dp[l] < maxDiff) {
                    maxDiff = dp[l];
                }
            }
        }
        
        int minInversions = initialInversions + maxDiff;
        System.out.println(minInversions);
    }
}
