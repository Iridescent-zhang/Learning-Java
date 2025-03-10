package com.example.debug;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.debug
 * @ClassName : .java
 * @createTime : 2025/1/3 19:23
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class PowxN{
    public static void main(String[] args) {
        Solution solution = new PowxN().new Solution();
        solution.myPow(2,-2147483648);
    }
    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public double myPow(double x, int nn) {
            if (x == 0)
                return 0;
            if (nn ==0)
                return 1;

            double ans = x;
            double tmp = x;
            int pow = 1;
            boolean flag = nn<0;
            long n = flag? -(long)nn : (long)nn;
            while (pow<(n/2)) {
                ans *= ans;
                pow *= 2;
                tmp *= tmp;
            }
            n -= pow;
            while (n>0) {
                while (pow>n) {
                    pow /= 2;
                    tmp = Math.sqrt(tmp);
                    if (x<0 && pow%2==1) {
                        tmp *= -1;
                    }
                }
                ans *= tmp;
                n -= pow;
            }
            return flag? 1/ans : ans;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}
