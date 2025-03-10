package com.example.debug;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.debug
 * @ClassName : .java
 * @createTime : 2025/2/19 22:43
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class LC378kthSmallest {

    public static int kthSmallest(int[][] matrix, int k) {
        // 二分是真难
        int n = matrix.length;
        int l = matrix[0][0], r = matrix[n-1][n-1];
        while(l<r) {
            int mid = (r-l)/2+l;
            int cnt = count(matrix, mid);
            if(cnt >= k) {
                r = mid;
            }else {
                l = mid+1;
            }
        }
        return l;
    }

    public static int count(int[][] matrix, int bound) {
        int ret = 0;
        int col = 0;
        for(int i = matrix.length-1; i>=0; i--) {
            while(col<matrix[0].length && matrix[i][col]<=bound) {
                col++;
            }
            ret += col;
        }
        return ret;
    }

    public static void main(String[] args) {
        int[][] matrix = {{1,2},{1,3}};
//        int[][] matrix = {{1,5,9},{10,11,13},{12,13,15}};
        int i = LC378kthSmallest.kthSmallest(matrix, 3);
        System.out.println("i = " + i);
    }
}
