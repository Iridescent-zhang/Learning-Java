package com.example.test;


import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.test
 * @ClassName : .java
 * @createTime : 2025/1/16 16:30
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
class Solution {
    final int a = 5;
    static  long[][] arr;
    public static void main(String[] args) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, ExecutionException, InterruptedException {

        String replace = new String(new char[10]).replace("\0", "_");
        System.out.println("replace = " + replace);

        String str = "|sdhwjk|sdjhwa|";
        String[] split = str.split("\\|");
        String s = Arrays.toString(split);
        System.out.println("s = " + s);


        String s1 = str.replaceAll("\\|", "*");
        System.out.println("s1 = " + s1);

        List<String> list = new ArrayList<>();
        list.add("-1");
        list.add("800");
//        list.add("ii");
        Object[] array = list.toArray();
        String[] array1 = list.toArray(new String[list.size()]);
        String s2 = Arrays.deepToString(array1);
        System.out.println("s2 = " + s2);

        System.out.println(null != null);

    }
}
