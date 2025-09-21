package com.example.intesting.dewubishi;

import java.util.*;
import java.io.*;

/**
 * 得物笔试第三题
 * https://mp.weixin.qq.com/s/9ULsuAzvhwZGyU42Rbapdg
 * 滑动窗口不行 子序列是有序的
 */
public class MinDeletions {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        List<String> results = new ArrayList<>();
        
        for (int t = 0; t < T; t++) {
            String[] nm = br.readLine().split(" ");
            int n = Integer.parseInt(nm[0]);
            int m = Integer.parseInt(nm[1]);
            
            String s = br.readLine().trim();
            String tStr = br.readLine().trim();
            
            // 存储每个字符在s中的位置列表
            List<List<Integer>> pos = new ArrayList<>();
            for (int i = 0; i < 26; i++) {
                pos.add(new ArrayList<>());
            }
            
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                int idx = c - 'a';
                pos.get(idx).add(i);
            }
            
            int ans = Integer.MAX_VALUE;
            char firstChar = tStr.charAt(0);
            int firstCharIdx = firstChar - 'a';
            
            // 遍历第一个字符在s中的所有可能位置作为起点
            for (int start : pos.get(firstCharIdx)) {
                int current = start;
                boolean valid = true;
                
                // 检查后续字符是否能在s中按顺序找到
                for (int i = 1; i < m; i++) {
                    char c = tStr.charAt(i);
                    int charIdx = c - 'a';
                    List<Integer> charPositions = pos.get(charIdx);
                    
                    // 查找大于current的最小位置（类似bisect_left）
                    int idx = Collections.binarySearch(charPositions, current + 1);
                    if (idx < 0) {
                        idx = -idx - 1; // 计算插入点
                    }
                    
                    if (idx >= charPositions.size()) {
                        valid = false;
                        break;
                    }
                    
                    current = charPositions.get(idx);
                }
                
                if (valid) {
                    int length = current - start + 1;
                    int deletions = length - m;
                    if (deletions < ans) {
                        ans = deletions;
                    }
                }
            }
            
            if (ans == Integer.MAX_VALUE) {
                results.add("-1");
            } else {
                results.add(String.valueOf(ans));
            }
        }
        
        // 输出所有结果
        for (String res : results) {
            System.out.println(res);
        }
    }
}
