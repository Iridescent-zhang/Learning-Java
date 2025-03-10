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
 * @createTime : 2025/2/15 23:17
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class MinMaxDifferencePairs  {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextInt()) {
            int num = scanner.nextInt();
            List<Integer> data = new ArrayList<>();

            for (int i = 0; i < num; ++i) {
                int temp = scanner.nextInt();
                data.add(temp);
            }

            result(data, num);
        }
    }

    public static void result(List<Integer> a, int n) {
        Collections.sort(a);
        int m1 = 1;
        int m2 = 1;

        for (int i = 0; i < n - 1; ++i) {
            if (!a.get(i).equals(a.get(i + 1))) {
                break;
            }
            ++m1;
        }

        for (int i = n - 1; i > 0; --i) {
            if (!a.get(i).equals(a.get(i - 1))) {
                break;
            }
            ++m2;
        }

        int max = m1 * m2;
        int min_temp = a.get(1) - a.get(0);
        int min = 0;

        for (int i = 2; i < n; ++i) {
            if (a.get(i) - a.get(i - 1) < min_temp) {
                min_temp = a.get(i) - a.get(i - 1);
            }
        }

        if (min_temp == 0) {
            for (int i = 1; i < n; ++i) {
                int j = i - 1;
                while (j >= 0 && a.get(j).equals(a.get(i))) {
                    ++min;
                    --j;
                }
            }
        } else {
            for (int i = 1; i < n; ++i) {
                if (a.get(i) - a.get(i - 1) == min_temp) {
                    ++min;
                }
            }
        }

        System.out.println(min + " " + max);
    }
}
