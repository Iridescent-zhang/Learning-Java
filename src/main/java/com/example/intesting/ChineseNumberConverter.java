package com.example.intesting;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.intesting
 * @ClassName : .java
 * @createTime : 2025/2/15 20:42
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.util.HashMap;
import java.util.Map;


// 将一个数字字符串（如 "三千一十万五千零一十"）转换成对应的阿拉伯数字
// 根据中文单位的不同处理方式，如果是 “万” 或 “亿”，要特殊处理
public class ChineseNumberConverter {

    private static final Map<Character, Integer> cnNumMap = new HashMap<>();
    private static final Map<Character, Integer> cnUnitMap = new HashMap<>();

    static {
        cnNumMap.put('零', 0);
        cnNumMap.put('一', 1);
        cnNumMap.put('二', 2);
        cnNumMap.put('两', 2);
        cnNumMap.put('三', 3);
        cnNumMap.put('四', 4);
        cnNumMap.put('五', 5);
        cnNumMap.put('六', 6);
        cnNumMap.put('七', 7);
        cnNumMap.put('八', 8);
        cnNumMap.put('九', 9);

        cnUnitMap.put('十', 10);
        cnUnitMap.put('百', 100);
        cnUnitMap.put('千', 1000);
        cnUnitMap.put('万', 10000);
        cnUnitMap.put('亿', 100000000);
    }

    public static int chineseToNumber(String chinese) {
        int len = chinese.length();
        int block = 0;
        int unit = 1;
        int tempUnit = 1;
        int ans = 0;
        for(int i = len-1; i>=0; i--) {
            if(chinese.charAt(i)=='万' || chinese.charAt(i)=='亿') {
                ans += block*unit;
                unit = chinese.charAt(i)=='万'? 10000 : 100000000;
                tempUnit = 1;
                block = 0;
                continue;
            }
            if(cnNumMap.containsKey(chinese.charAt(i))) {
                block += cnNumMap.get(chinese.charAt(i))*tempUnit;
            } else if (cnUnitMap.containsKey(chinese.charAt(i))) {
                tempUnit = cnUnitMap.get(chinese.charAt(i));
            }
        }
        ans += block*unit;
        return ans;
    }

    public static void main(String[] args) {
        String[] chineseNumbers = new String[]{"三千一十万五千零一十", "三千一十六万五千零一十", "一十六亿三千五百一十万五千零一十四", "五千零一十", "一十六亿五千零一十四"};
        for (String chineseNumber : chineseNumbers) {
            int number = chineseToNumber(chineseNumber);
            System.out.println(number); // 输出：30105010
        }
    }
}
