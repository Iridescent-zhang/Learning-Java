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
import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        // 读取字符串长度n和约束g
//        int n = scanner.nextInt();
//        int g = scanner.nextInt();
//
//        // 读取字符串s
//        String s = scanner.next();
//
//        // 按奇偶性分组
//        int[] oddCount = new int[26]; // 奇数位置的字符频率
//        int[] evenCount = new int[26]; // 偶数位置的字符频率
//
//        for (int i = 0; i < n; i++) {
//            char c = s.charAt(i);
//            if (i % 2 == 0) {
//                evenCount[c - 'a']++; // 偶数位置
//            } else {
//                oddCount[c - 'a']++; // 奇数位置
//            }
//        }
//
//        // 计算有缘分的位置对数
//        long result = 0;
//
//        // 处理偶数位置
//        result += countPairs(evenCount, g);
//
//        // 处理奇数位置
//        result += countPairs(oddCount, g);
//
//        // 输出结果
//        System.out.println(result);
//
//        // 关闭Scanner
//        scanner.close();

        int[] nums = {2,2,2,2,5,5,5,6,6,9,9,10};
        int n = nums.length;
        System.out.println("n = " + n);
        int l = 0;
        int r = n-1;
        int left = 100;
        // 大于等于 left 的最小数
        while (l<r) {
            int mid = (r-l)/2+l;
            if(nums[mid]>=left) {
                r = mid;
            }else {
                l = mid+1;
            }
        }
        int newl = l;
        System.out.println("newl = " + newl);

        l = 0;
        r = n-1;
        int right = -1;
        // 小于等于 right 的最大数
        while (l<r) {
            int mid = (r-l+1)/2+l;
            if(nums[mid] <= right) {
                l = mid;
            }else {
                r = mid-1;
            }
        }
        System.out.println("r = " + r);

        ArrayList<Integer> list = new ArrayList<>();
        for(int i=newl; i<=r; i++) {
            list.add(nums[i]);
        }
        System.out.println("list = " + list);
    }

    // 统计一组字符中满足条件的位置对数
    private static long countPairs(int[] count, int g) {
        long pairs = 0;
        for (int i = 0; i < 26; i++) {
            if (count[i] == 0) continue; // 跳过没有的字符
            for (int j = i; j < 26; j++) {
                if (count[j] == 0) continue; // 跳过没有的字符
                if (Math.abs(i - j) <= g) {
                    if (i == j) {
                        // 同一个字符，计算组合数C(count[i], 2)
                        pairs += (long) count[i] * (count[i] - 1) / 2;
                    } else {
                        // 不同字符，计算乘积
                        pairs += (long) count[i] * count[j];
                    }
                }
            }
        }
        return pairs;
    }
}
