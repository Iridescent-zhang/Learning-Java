package com.example.debug;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.debug
 * @ClassName : .java
 * @createTime : 2024/12/23 17:59
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class MultiplyStrings{
    public static void main(String[] args) {
        Solution solution = new MultiplyStrings().new Solution();
        solution.multiply("123", "456");
    }
    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public String multiply(String num1, String num2) {
            char[] num1Char = num1.toCharArray();
            char[] num2Char = num2.toCharArray();
            int n1 = num1Char.length;
            int n2 = num2Char.length;
            int[][] ints = new int[n2][n1 + n2];
            for (int i = n2-1; i >= 0; i--) {
                int adv = 0;
                int sum = 0;
                int idx = n1 + i;
                for (int j = n1-1; j >= 0; j--) {
                    sum = adv + (num2Char[i]-'0')*(num1Char[j]-'0');
                    ints[i][idx] = sum%10;
                    adv = sum/10;
                    idx--;
                }
                ints[i][idx] = adv;
            }
            int[] ans = new int[n1 + n2];
            int sum = 0;
            int adv = 0;
            for (int j = n1 + n2 - 1; j >=0; j--) {
                sum = adv;
                for (int i = 0; i < ints.length; i++) {
                    sum += ints[i][j];
                }
                adv = sum/10;
                ans[j] = sum%10;
            }
            int idx = 0;
            while (idx<(n1+n2) && ans[idx]==0) {
                idx++;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = idx; i < (n1+n2); i++) {
                sb.append(ans[i]);
            }
            return sb.toString().equals("")? "0" : sb.toString();
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}