package com.example.intesting.xiaohongshu;

import java.util.*;

/**
 * 理论：最大公约数 大于 1  等价于  公共质因数 大于 1
 * 给一个数组，求最长且元素不相邻的子序列，要求子序列最大公约数大于1
 * 方法：求出每个元素的所有质因数，hmap 的key是质因数，value就是包含这些质因数的数组元素的位置，一个子序列的最大公约数要大于一，实际上要求公共质因数大于1
 * 所以在取出 hmap 中的每一个 value，是一个list，这个list下面取出的不相邻子序列才能满足要求
 * 比如 2 和 3，他们质因数没有重合的，所以公约数只能是1。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // 统计每个质数出现的位置
        Map<Integer, List<Integer>> primePositions = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int num = a[i];
            // 找出num的所有质因数
            Set<Integer> primes = getPrimeFactors(num);
            for (int p : primes) {
                if (!primePositions.containsKey(p)) {
                    primePositions.put(p, new ArrayList<>());
                }
                primePositions.get(p).add(i);
            }
        }

        int maxCount = 0;
        for (Map.Entry<Integer, List<Integer>> entry : primePositions.entrySet()) {
            List<Integer> positions = entry.getValue();
            int count = maxNonAdjacent(positions);
            maxCount = Math.max(maxCount, count);
        }

        System.out.println(maxCount);
        scanner.close();
    }

    // 获取一个数的所有质因数
    private static Set<Integer> getPrimeFactors(int num) {
        Set<Integer> primes = new HashSet<>();
        if (num % 2 == 0) {
            primes.add(2);
            while (num % 2 == 0) {
                num /= 2;
            }
        }
        /**
         * 对于一个数 num，如果它有一个大于 Math.sqrt(num) 的质因数 p，那么必然存在一个小于 Math.sqrt(num) 的质因数 q，使得 num = p * q。
         *
         * 假设 num = 15，Math.sqrt(15) ≈ 3.87。
         * 15 的质因数有 3 和 5，其中 5 > 3.87，但 3 < 3.87，且 15 = 3 * 5。
         * 因此，只要我们在 i ≤ Math.sqrt(num) 的范围内找到了所有质因数，剩下的大于 Math.sqrt(num) 的质因数（如果存在）可以通过 num 除以已找到的质因数得到。
         *
         * 先处理偶数（质因数 2），因为 2 是唯一的偶质数。
         * 然后从 i = 3 开始，遍历到 Math.sqrt(num)，步长为 2（只检查奇数，因为除了 2 外，质数都是奇数）。
         * 若 i 是 num 的因数，则 i 是质因数（因为之前的步骤已经处理了更小的因数，所以此时 i 无法再被更小的质数整除，必然是质数）。
         */
        for (int i = 3; i <= Math.sqrt(num); i += 2) {
            if (num % i == 0) {
                primes.add(i);
                while (num % i == 0) {
                    num /= i;
                }
            }
        }
        if (num > 2) {
            primes.add(num);
        }
        return primes;
    }

    /**
     * 计算下标不相邻的最大数量（动态规划）
     * 这个应该有问题，可以写成贪心的
     */
    private static int maxNonAdjacent(List<Integer> positions) {
        int n = positions.size();
        if (n == 0) {
            return 0;
        }
        int[] dp = new int[n];
        dp[0] = 1;
        for (int i = 1; i < n; i++) {
            if (positions.get(i) - positions.get(i - 1) > 1) {
                dp[i] = dp[i - 1] + 1;
            } else {
                dp[i] = Math.max(dp[i - 1], 1);
            }
        }
        return dp[n - 1];
    }
}