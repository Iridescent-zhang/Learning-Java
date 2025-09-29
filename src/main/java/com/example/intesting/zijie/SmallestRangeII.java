package com.example.intesting.zijie;

import java.util.Arrays;

class SmallestRangeII {
    /**
     * 没写出来
     * 计算数组中每个元素可以加k或减k后，数组最大值与最小值差值的最小值。
     *
     * 解题思路：
     * 排序是关键: 解决这类涉及数组范围、最大/最小差异的问题，一个常见的有效策略是首先对数组进行排序。排序后，我们可以更容易地分析元素之间的关系。假设我们已经将数组 nums 从小到大排好序。
     * 核心思想: 为了让新数组的 (最大值 - 最小值) 尽可能小，我们  直觉上应该让原来较小的数变大（通过 +k），让原来较大的数变小（通过 -k）。  这样是对的，反过来如果让大的去加k，小的去减k，算出来的分数一定是更大的
     * 寻找分割点: 既然我们已经对数组进行了排序，那么这个“较小的数”和“较大的数”的界限就可以看作是数组中的一个分割点。假设我们在索引 i 处分割数组，我们将 nums[0] 到 nums[i] 都加上 k，而将 nums[i+1] 到 nums[n-1] 都减去 k。
     *
     * @param nums 原始数组，元素非负
     * @param k    可以加或减的数值，非负
     * @return 最小的分数（新数组的最大值 - 最小值）
     */
    public int smallestRangeII(int[] nums, int k) {
        // 如果数组只有一个或没有元素，差值为0
        if (nums.length <= 1) {
            return 0;
        }

        // 1. 对数组进行排序
        Arrays.sort(nums);

        int n = nums.length;
        
        // 2. 初始化结果。基准情况是所有元素都 +k 或都 -k，此时差值不变。
        int result = nums[n - 1] - nums[0];

        // 3. 遍历所有可能的分割点 i
        // 分割点在 nums[i] 和 nums[i+1] 之间
        // 我们假设 nums[0...i] 都 +k，而 nums[i+1...n-1] 都 -k
        for (int i = 0; i < n - 1; i++) {
            // 对于当前的分割点 i:
            // 前半部分 [nums[0]...nums[i]] 都 +k
            // 后半部分 [nums[i+1]...nums[n-1]] 都 -k

            // 4. 计算新数组的潜在最大值
            // 最大值可能是前半部分的最大值 (nums[i] + k)
            // 或后半部分的最大值 (nums[n-1] - k)
            int maxVal = Math.max(nums[i] + k, nums[n - 1] - k);

            // 5. 计算新数组的潜在最小值
            // 最小值可能是前半部分的最小值 (nums[0] + k)
            // 或后半部分的最小值 (nums[i+1] - k)
            int minVal = Math.min(nums[0] + k, nums[i + 1] - k);

            // 6. 计算当前分割策略下的分数，并更新全局最小结果
            result = Math.min(result, maxVal - minVal);
        }

        return result;
    }

    public static void main(String[] args) {
        SmallestRangeII sol = new SmallestRangeII();

        // 示例 1
        int[] nums1 = {1, 3, 6};
        int k1 = 3;
        // 排序后 [1, 3, 6]。
        // 可能的一种最优操作是 [1+3, 3+3, 6-3] -> [4, 6, 3]。
        // 新数组为 [3, 4, 6]，最大值6，最小值3，差值为3。
        System.out.println("示例 1: nums = [1, 3, 6], k = 3");
        System.out.println("最小分数: " + sol.smallestRangeII(nums1, k1)); // 预期输出: 3

        // 示例 2
        int[] nums2 = {0, 10};
        int k2 = 2;
        // 操作后可能为 [0+2, 10-2] -> [2, 8]，差值为6。
        // 或 [0-2, 10+2] -> [-2, 12]，差值为14。
        // 或 [0+2, 10+2] -> [2, 12]，差值为10。
        // 或 [0-2, 10-2] -> [-2, 8]，差值为10。
        System.out.println("\n示例 2: nums = [0, 10], k = 2");
        System.out.println("最小分数: " + sol.smallestRangeII(nums2, k2)); // 预期输出: 6

        // 示例 3
        int[] nums3 = {7, 8, 8};
        int k3 = 5;
        // 排序后 [7, 8, 8]。
        // 初始差值 8-7=1。
        // 分割点i=0: [7+5, 8-5, 8-5] -> [12, 3, 3]。新数组为[3, 3, 12]，差值为9。
        // 分割点i=1: [7+5, 8+5, 8-5] -> [12, 13, 3]。新数组为[3, 12, 13]，差值为10。
        // 所以最小值为1。
        System.out.println("\n示例 3: nums = [7, 8, 8], k = 5");
        System.out.println("最小分数: " + sol.smallestRangeII(nums3, k3)); // 预期输出: 1
    }
}