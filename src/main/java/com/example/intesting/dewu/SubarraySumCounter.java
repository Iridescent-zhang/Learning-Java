package com.example.intesting.dewu;

import java.util.*;
import java.io.*;

/**
 * 得物笔试第三题
 * https://mp.weixin.qq.com/s/9ULsuAzvhwZGyU42Rbapdg
 * 前缀和要用 set 啊  写了个n2的真丢人
 */
public class SubarraySumCounter {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = br.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        
        int[] aList = new int[n];
        int[] bList = new int[n];
        
        String[] aStr = br.readLine().split(" ");
        String[] bStr = br.readLine().split(" ");
        
        for (int i = 0; i < n; i++) {
            aList[i] = Integer.parseInt(aStr[i]);
            bList[i] = Integer.parseInt(bStr[i]);
        }
        
        // 前缀和数组，存储a与b对应元素差值的累积和
        long[] pre = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            pre[i] = pre[i - 1] + aList[i - 1] - bList[i - 1];
        }
        
        // 哈希表记录前缀和出现的次数
        Map<Long, Integer> map = new HashMap<>();
        map.put(0L, 1);
        long res = 0;
        
        for (int i = 1; i <= n; i++) {
            // 计算目标前缀和
            long target = pre[i] - m;
            // 累加出现过的目标前缀和的次数
            res += map.getOrDefault(target, 0);
            // 记录当前前缀和
            map.put(pre[i], map.getOrDefault(pre[i], 0) + 1);
        }
        
        System.out.println(res);
    }
}
