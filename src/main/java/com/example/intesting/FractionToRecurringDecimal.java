package com.example.intesting;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.intesting
 * @ClassName : .java
 * @createTime : 2025/1/16 0:48
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.util.HashMap;
import java.util.Map;

/**
 * 输入int a,int b,计算a/b，如果是无限循环小数需要把循环的情况输出出来，类似4/3=1.3(2),(2)表示从第二位开始循环
 *  这个算法是表示成将循环段用括号括起来，比如20.(3529411764705882)，那一大段都是循环段
 *  关键就是分母是不变的，那出现循环的情况就是我们的分子出现了循环，所以就要用哈希表表示分子的出现索引，而result该索引处的值应该是分子*10/分母
 */
public class FractionToRecurringDecimal {
    public static void main(String[] args) {
        int numerator = 346;
        int denominator = 17;
        System.out.println(fractionToDecimal(numerator, denominator));
    }

    public static String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0) {
            return "0";
        }
        StringBuilder result = new StringBuilder();
        if ((numerator < 0) ^ (denominator < 0)) {
            result.append("-");
        }

        long num = Math.abs((long) numerator);
        long den = Math.abs((long) denominator);

        // 整数部分
        result.append(num / den);
        num %= den;
        if (num == 0) {
            return result.toString();
        }

        result.append(".");
        Map<Long, Integer> map = new HashMap<>();
        while (num != 0) {
            if (map.containsKey(num)) {
                int index = map.get(num);
                result.insert(index, "(");
                result.append(")");
                break;
            }
            map.put(num, result.length());
            num *= 10;
            result.append(num / den);
            num %= den;
        }

        return result.toString();
    }
}
