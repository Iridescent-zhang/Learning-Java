package com.example.intesting.pdd;

import java.util.Arrays;
import java.util.Scanner;

/**
 * https://mp.weixin.qq.com/s/zid_rWzxI3g1wtxjZljGnQ
 * 大鱼吃小鱼
 * 游戏规则
 * 初始状态: 池塘中有 n 条鱼，按照从 1 到 n 的顺序排成一排。第 i 条鱼的初始血量为 a_i。（a_i>=0）
 *
 * 捕食条件: 一条鱼只能吃掉与它 相邻 的鱼。要成功捕食，捕食者的血量必须 严格大于 (不包含等于) 被捕食者的血量。
 * 捕食后果:
 * 被吃掉的鱼会从队列中消失。
 * 捕食者的血量会增加，增加量等于被吃掉的鱼的血量。
 * 原来与被吃掉的鱼相邻的另外一条鱼，会成为捕食者的新邻居。
 * 游戏结束: 当没有任何一条鱼能够吃掉它的邻居时，游戏结束。
 *
 * 问题目标
 * 对于每一条初始的鱼，请计算在所有可能的捕食顺序中，要使它被其他鱼吃掉，整个游戏至少需要发生多少次捕食事件？
 * 这个“最少次数”是指，为了达成“某条鱼被吃掉”这个目标，全局需要发生的最少捕食次数。
 * 如果某条鱼在任何可能的捕食顺序下都无法被吃掉，则该条鱼的结果输出 -1。
 *
 * ---
 * 我们得出解决这个问题的两个充要条件。一个连续的鱼群 B = a[j...k] 要想吃掉它旁边的鱼 a[m]，必须满足：
 * 实力条件: 鱼群 B 的总血量必须严格大于 a[m] 的血量。即 sum(a[j]...a[k]) > a[m]。
 * 启动条件 (Mergeability): 鱼群 B 自身必须能够启动连锁反应并合并成一条鱼。这个过程唯一的阻碍是：鱼群 B 中所有鱼的血量完全相同。例如 [3, 3, 3]，谁都无法吃掉谁，无法启动合并。
 *      只要区间内至少存在两个血量不同的相邻鱼，合并过程就可以启动。更简单地说，只要这个区间不是所有元素都相等，它就总能合并。
 *      注意，一个区间如果开始的时候就存在血量不同的鱼，那就一定可以合并成一条鱼，不会出现合并到一半卡住的情况（卡住唯一的发生情况是剩余鱼的血量一样）
 *      这个很关键，所以一个区间能不能用来把旁边的吃掉，首先得区间和大于目标值，其次是得能启动，也就是这个区间的值不是全相同的。
 *
 * 我们的目标是：对于每条鱼 i，找到能吃掉它的、满足上述两个条件的、最短的连续鱼群。最短的鱼群意味着最少的合并步数。
 * 借助前缀和快速计算区间和，使用辅助数组 last_diff[i] 表示在 i 左侧第一个与 a[i] 值不同的元素的索引。比如 a[i-1] != a[i]，则 last_diff[i] = i-1；否则 last_diff[i] = last_diff[i-1]。这个数组可以 O(N) 预处理。
 * 借助辅助数组判断当前区间能否合并
 *
 * 一条鱼可能被左边的吃也可能被右边的吃，所以要分两种情况考虑，各算一遍
 *
 * 主逻辑 (以左侧为例):
 * 遍历每条鱼 i (从 1 到 n-1)。
 * 我们要为 a[i] 寻找左侧最短的捕食链。最短的链有1步，最长有 i 步。我们可以在 [1, i] 这个步数范围上进行二分查找。
 * 对于二分查找的某个步数 k：
 * 它对应的捕食链区间是 [i-k, i-1]。
 * 检查实力条件: 利用前缀和 P，判断 sum(a[i-k]...a[i-1]) > a[i] 是否成立。
 * 检查启动条件: 利用辅助数组 last_diff，判断区间 [i-k, i-1] 是否所有元素都相等。这等价于判断 last_diff[i-1] 是否小于 i-k。如果小于，说明从 i-k 到 i-1 都是同一个值，无法启动；否则就可以启动。
 * 如果两个条件都满足，说明 k 步是可行的，我们尝试更少的步数（high = k-1）。
 * 如果至少一个条件不满足，说明需要更多步数（low = k+1）。
 *
 * 双向扫描:
 * 执行一次左到右的扫描，计算每个 result[i]。
 * 再执行一次右到左的扫描（或者在反转数组上执行相同逻辑），用更小的步数更新 result[i]。
 */
public class Main4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextLong();
        }
        scanner.close();

        // 预处理前缀和
        long[] prefixSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + a[i];
        }

        int[] result = new int[n];
        Arrays.fill(result, -1);

        // 1. 左到右扫描
        int[] lastDiffLeft = new int[n];
        for (int i = 1; i < n; i++) {
            if (a[i - 1] != a[i]) {
                lastDiffLeft[i] = i - 1;
            } else {
                lastDiffLeft[i] = (i > 1) ? lastDiffLeft[i - 1] : -1;
            }
        }

        /**
         * i: 代表当前正在被分析的目标鱼的索引。
         * k: 代表捕食者鱼群的长度，也就是捕食所需要的步数。
         * j: 代表捕食者鱼群的起始索引。
         */
        for (int i = 1; i < n; i++) {
            // 对步数 k in [1, i] 进行二分查找
            int low = 1, high = i, bestSteps = -1;
            while (low <= high) {
                int k = low + (high - low) / 2;
                int j = i - k; // 区间起始点等于 i - 步数

                // 检查实力条件
                boolean strongEnough = (prefixSum[i] - prefixSum[j]) > a[i];
                // 检查启动条件
                boolean canMerge = lastDiffLeft[i - 1] >= j;

                if (strongEnough && canMerge) {
                    bestSteps = k;
                    high = k - 1; // 尝试更少的步数
                } else {
                    low = k + 1; // 需要更多步数
                }
            }
            result[i] = bestSteps;
        }

        // 2. 右到左扫描
        int[] lastDiffRight = new int[n];
        for (int i = n - 2; i >= 0; i--) {
            if (a[i + 1] != a[i]) {
                lastDiffRight[i] = i + 1;
            } else {
                lastDiffRight[i] = (i < n - 2) ? lastDiffRight[i + 1] : n;
            }
        }
        
        for (int i = n - 2; i >= 0; i--) {
            int low = 1, high = n - 1 - i, bestSteps = -1;
            while (low <= high) {
                int k = low + (high - low) / 2;
                int j = i + k; // 区间结束点
                
                // 检查实力条件 (注意前缀和用法)
                boolean strongEnough = (prefixSum[j + 1] - prefixSum[i + 1]) > a[i];
                // 检查启动条件
                boolean canMerge = lastDiffRight[i + 1] <= j;

                if (strongEnough && canMerge) {
                    bestSteps = k;
                    high = k - 1;
                } else {
                    low = k + 1;
                }
            }
            
            if (bestSteps != -1) {
                if (result[i] == -1 || bestSteps < result[i]) {
                    result[i] = bestSteps;
                }
            }
        }
        
        // 3. 输出结果
        StringBuilder sb = new StringBuilder();
        for (int res : result) {
            sb.append(res).append(" ");
        }
        System.out.println(sb.toString().trim());
    }
}