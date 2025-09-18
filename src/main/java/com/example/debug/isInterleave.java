package com.example.debug;

import java.util.Arrays;

public class isInterleave {
    public  static void main(String[] args) {
        boolean interleave = new Solution().isInterleave("aabcc", "dbbca", "aadbbcbcac");
        System.out.println("interleave = " + interleave);
    }
}

class Solution {
    public boolean isInterleave(String s1, String s2, String s3) {
        if(s1.length()+s2.length() != s3.length()) 
            return false;
        return isInterleave(s1, s2, s3, 0, 0, 0);
    }

    public boolean isInterleave(String s1, String s2, String s3, int idx1, int idx2, int idx3) {
        if(idx1 == s1.length()) 
            return s2.substring(idx2, s2.length()).equals(s3.substring(idx3, s3.length()));
        if(idx2 == s2.length()) 
            return s1.substring(idx1, s1.length()).equals(s3.substring(idx3, s3.length()));

        if(s3.charAt(idx3)==s1.charAt(idx1))
            return isInterleave(s1, s2, s3, idx1+1, idx2, idx3+1);
        return s3.charAt(idx3)==s2.charAt(idx2) && isInterleave(s1, s2, s3, idx1, idx2+1, idx3+1);
    }
}