package com.example.intesting;

import java.util.PriorityQueue;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.intesting
 * @ClassName : .java
 * @createTime : 2025/2/19 0:08
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class LC786kthSmallestPrimeFraction {
    public static int[] kthSmallestPrimeFraction(int[] arr, int k) {
//        PriorityQueue<int[]> pque = new PriorityQueue<>(k, (o1, o2)->{
//            double d2 = (double)o2[0]/(double)o2[1];
//            double d1 = (double)o1[0]/(double)o1[1];
//            return d2>d1? 1 : (d2==d1? 0 : -1);
//        });
//        for(int i=0; i<arr.length; i++) {
//            for(int j=i+1; j<arr.length; j++) {
//                pque.offer(new int[]{arr[i], arr[j]});
//                if(pque.size()>k) {
//                    pque.poll();
//                }
//            }
//        }
//        return pque.poll();

        // 二分
        int n = arr.length;
        double l = 0.0, r = 1.0;
        while (l < r) {
            double mid = (r-l)/2+l;
            int cnt = 0;
            int row = 0;
            int x=0, y=1;
            for(int j=1; j<n; j++) {
                while ((double)arr[row]/arr[j]<mid && row<j) {
                    if ( arr[row]*y > arr[j]*x ) {
                        x = arr[row];
                        y = arr[j];
                    }
                    row++;
                }
                cnt += row;
            }
            if (cnt > k) {
                r = mid;
            }else if (cnt < k){
                l = mid;
            }else {
                return new int[]{x, y};
            }
        }
        return new int[]{};
    }

    public static void main(String[] args) {
        int k = 3;
        int[] arr = new int[]{1,2,3,5};
        int[] ints = kthSmallestPrimeFraction(arr, k);
        System.out.println(ints[0] + " " + ints[1]);
    }
}
