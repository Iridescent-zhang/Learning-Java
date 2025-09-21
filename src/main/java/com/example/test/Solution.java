package com.example.test;


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.test
 * @ClassName : .java
 * @createTime : 2025/1/16 16:30
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.util.*;

class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int l1 = 0, r1 = m-1;
        int l2 = 0, r2 = n-1;
        while(l1<=r1 && l2<=r2) {
            if(l1 == r1 && l2 == r2)
                return ((double)(nums1[l1]+nums2[l2]))/2;
            int mid1 = (r1-l1)/2+l1;
            int mid2 = (r2-l2)/2+l2;
            int c = mid1-l1, d = r1-mid1;
            int a = mid2-l2, b = r2-mid2;
            int cnt = Math.max(1, Math.max(Math.min(a+c+1, b), Math.min(a, b+d+1)));
            while(cnt-->0) {
                if(l2 > r2 || (l1 <= r1 && nums1[l1] <= nums2[l2])) {
                    l1++;
                }else if(l1 > r1 || (l2 <= r2 && nums2[l2] <= nums1[l1])){
                    l2++;
                }

                if(l2 > r2 || (l1 <= r1 && nums1[r1] >= nums2[r2])) {
                    r1--;
                }else if(l1 > r1 || (l2 <= r2 && nums2[r2] >= nums1[r1])) {
                    r2--;
                }
            }
        }
        while(r1-l1 > 1) {
            r1--;
            l1++;
        }
        while(r2-l2 > 1) {
            r2--;
            l2++;
        }
        int cnt = 0;
        double sum = 0;
        while(l1<=r1) {
            sum += nums1[l1++];
            cnt++;
        }
        while(l2<=r2) {
            sum += nums2[l2++];
            cnt++;
        }
        return sum/cnt;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums1 = {1,3};
        int[] nums2 = {2};
        solution.findMedianSortedArrays(nums1, nums2);
    }
}
