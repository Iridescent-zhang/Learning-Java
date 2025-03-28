package com.example.intesting;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.intesting
 * @ClassName : .java
 * @createTime : 2025/3/26 23:54
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * https://mp.weixin.qq.com/s/EmW-GgsR6oud9zEopl4F0w
 * 太难
 */
public class MayiBishi3 {

    static final int MAXN = 200010;
    static int[][] tree = new int[MAXN][2];

    static int lowbit(int x) {
        return x & (-x);
    }

    static void add(int x, int k, int d) {
        while (x < MAXN) {
            tree[x][d] += k;
            x += lowbit(x);
        }
    }

    static int query(int x, int d) {
        int ans = 0;
        while (x > 0) {
            ans += tree[x][d];
            x -= lowbit(x);
        }
        return ans;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] arr = new int[n];
        Set<Integer> nums = new HashSet<>();
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
            if (arr[i] != 0) {
                nums.add(Math.abs(arr[i]));
            }
        }
        scanner.close();

        Map<Integer, Integer> mp = new HashMap<>();
        int id = 1;
        for (int x : nums) {
            mp.put(x, id++);
        }

        long sum = 0;  // 用于累计满足条件的组合数

        for (int i = 0; i < n; i++) {
            if (arr[i] == 0) {
                sum += i;  // y=0时，x可以取数组中的任意值
                continue;
            }
            int idx = mp.get(Math.abs(arr[i]));
            int which = arr[i] > 0 ? 0 : 1;

            // Queries
            sum += query(id - 1, which) - query(idx - 1, which);

            // Updates
            add(idx, 1, which);
        }

        System.out.println(sum);
    }
}
