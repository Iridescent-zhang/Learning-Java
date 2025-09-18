package com.example.intesting;

import java.util.*;
import java.io.*;

/**
 * 携程秋招笔试 第三题
 * https://mp.weixin.qq.com/s/8xuJAKrofTYdhxCwh_D4gA
 * 很恶心的一点在于他用 “以下操作任意次（可以不执行任何操作）” 来唬人，实际上一次的效果就是最好的，下次不要再被吓到了
 * 理论还不太确定
 *
 * 好题啊就是恶心人
 */
public class MaxXorSum {
    private static final int MAX_BITS = 31;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        
        for (int t = 0; t < T; t++) {
            solve(br);
        }
    }

    private static void solve(BufferedReader br) throws IOException {
        int n = Integer.parseInt(br.readLine());
        String[] parts = br.readLine().split(" ");
        int[] a = new int[n];
        
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(parts[i]);
        }

        int[] bitCounts = new int[MAX_BITS];

        // 统计每个比特位上1的数量
        for (int num : a) {
            for (int i = 0; i < MAX_BITS; i++) {
                if ((num >> i & 1) == 1) {
                    bitCounts[i]++;
                }
            }
        }

        // 计算原始数组的和
        long initialSum = 0;
        for (int i = 0; i < MAX_BITS; i++) {
            initialSum += (long) bitCounts[i] * (1 << i);
        }

        long maxSum = initialSum;

        // 使用Set获取数组中的唯一元素作为候选x
        Set<Integer> uniqueCandidates = new HashSet<>();
        for (int num : a) {
            uniqueCandidates.add(num);
        }

        // 尝试每个候选x，计算异或后的最大可能和
        for (int x : uniqueCandidates) {
            long currentSum = 0;
            for (int i = 0; i < MAX_BITS; i++) {
                // 检查x的第i位是否为1
                if ((x >> i & 1) == 1) {
                    // 如果x的第i位为1，异或后该位1的数量为n - bitCounts[i]
                    currentSum += (long) (n - bitCounts[i]) * (1 << i);
                } else {
                    // 如果x的第i位为0，异或后该位1的数量不变
                    currentSum += (long) bitCounts[i] * (1 << i);
                }
            }

            if (currentSum > maxSum) {
                maxSum = currentSum;
            }
        }

        System.out.println(maxSum);
    }
}
