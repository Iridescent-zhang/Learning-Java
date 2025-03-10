package com.example.intesting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.test
 * @ClassName : .java
 * @createTime : 2024/12/26 22:21
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class FindPrefixString {

    public static void main(String[] args) {
        String[] strings = new String[]{"money", "monday", "monkey", "mother", "map", "monkeys", "mobile", "monetary"};
        String searchWord = "money";
        List<List<String>> ans = findPrefixString(strings, searchWord);
        System.out.println("ans = " + ans);
    }

    public static List<List<String>> findPrefixString(String[] strings, String searchWord) {
        Arrays.sort(strings);
        List<List<String>> ans = new ArrayList<>();
        int length = searchWord.length();
        for (int i = 1; i <= length; i++) {
            String prefix = searchWord.substring(0,i);
            ans.add(binarySearch(strings, prefix));
        }
        return ans;
    }

    public static List<String> binarySearch(String[] strings, String prefix) {
        ArrayList<String> list = new ArrayList<>();
        int left = 0;
        int right = strings.length-1;
        while (left<right) {
            int mid = ((right - left)/2)+left;
            if (strings[mid].startsWith(prefix)) {
                right = mid;
            }else {
                if (prefix.compareTo(strings[mid])>0) {
                    left = mid+1;
                }else {
                    right = mid-1;
                }
            }
        }
        int count = 0;
        int idx = left;
        while (count<3 && idx<strings.length && strings[idx].startsWith(prefix)) {
            list.add(strings[idx++]);
            count++;
        }
        return list;
    }
}
