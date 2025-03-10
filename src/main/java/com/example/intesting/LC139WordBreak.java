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
 * @createTime : 2025/2/18 22:01
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

// 背包动规题，尝试结合使用字典树
public class LC139WordBreak {
    public static boolean wordBreak(String s, List<String> wordDict) {
        int len = s.length();
        Trie root = new Trie();
        for (String str : wordDict) {
            root.add(str);
        }
        boolean[] dp = new boolean[len+1];
        dp[0] = true;
        for(int i=1; i<=len; i++) {
            for(int j = i-1; j>=0; j--) {
                if(dp[j] && root.isExist(s.substring(j,i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[len];
    }

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(new String[]{"a","b","bbb","bbbb"}));
        String s = "bb";
        boolean ans = wordBreak(s, list);
        System.out.println("ans = " + ans);
    }
}

class Trie {
    boolean isEnd;
    Trie[] children;

    Trie() {
        isEnd = false;
        children = new Trie[26];
    }

    public void add(String str) {
        if (str.equals("")) {
            this.isEnd = true;
            return;
        }
        int idx = str.charAt(0) - 'a';
        if (this.children[idx] == null) {
            this.children[idx] = new Trie();
        }
        Trie trie = this.children[idx];
        trie.add(str.substring(1, str.length()));
    }

    public boolean isExist(String str) {
        if (str.equals("")) {
            return this.isEnd;
        }
        int idx = str.charAt(0) - 'a';
        Trie trie = this.children[idx];
        if (trie == null) {
            return false;
        }
        return trie.isExist(str.substring(1, str.length()));
    }
}