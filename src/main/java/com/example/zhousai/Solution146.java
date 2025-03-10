package com.example.zhousai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.zhousai
 * @ClassName : .java
 * @createTime : 2024/12/21 22:31
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class Solution146 {
    /**
     * 146
     */
    public int countSubarrays(int[] nums) {
        int ans = 0;
        for (int i = 1; i < nums.length-1 ; i++) {
            if (nums[i]==((nums[i-1]+nums[i+1])*2))
                ans++;
        }
        return ans;
    }

    public int countPathsWithXorValue(int[][] grid, int k) {
        int base = 1000000007;
        int m = grid.length;
        int n = grid[0].length;
        List<Integer>[] dp = new List[n];
        for (int i = 0; i < dp.length; i++) {
            dp[i] = new ArrayList<Integer>();
        }
        dp[0].add(0);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ArrayList<Integer> list = new ArrayList<>();
                if (j>0) {
                    for (Integer item : dp[j - 1]) {
                        list.add(item^grid[i][j]);
                    }
                }
                for (Integer item : dp[j]) {
                    list.add(item^grid[i][j]);
                }
                dp[j] = list;
            }
        }
        int ans = 0;
        for (Integer item : dp[n - 1]) {
            if (item==k) {
                ans++;
                ans %= base;
            }
        }
        return ans;
    }

    public boolean checkValidCuts(int n, int[][] rectangles) {
        Arrays.sort(rectangles, (o1,o2)->o1[0]-o2[0]);
//        int start = rectangles[0][0];
        int end = rectangles[0][2];
        int count = 0;
        for (int i = 1; i < rectangles.length; i++) {
            if (rectangles[i][0]<end) {
                end = Math.max(end, rectangles[i][2]);
            }else {
//                start = rectangles[i][0];
                end = rectangles[i][2];
                count++;
                if (count==2)
                    return true;
            }
        }
        count = 0;
        Arrays.sort(rectangles, (o1,o2)->o1[1]-o2[1]);
        end = rectangles[0][3];
        for (int i = 1; i < rectangles.length; i++) {
            if (rectangles[i][1]<end) {
                end = Math.max(end, rectangles[i][3]);
            }else {
//                start = rectangles[i][0];
                end = rectangles[i][3];
                count++;
                if (count==2)
                    return true;
            }
        }
        return false;
    }

}
