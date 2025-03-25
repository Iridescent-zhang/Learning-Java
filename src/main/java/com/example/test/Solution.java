package com.example.test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

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
    public static void main(String[] args) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
//        int intValue = 42;
//        long longValue = 12345675589L;
//        float floatValue = 3.14159f;
//        double doubleValue = 2.71828;
//        char charValue = 'A';
//        String stringValue = "Hello, World!";
//
//        // 格式化输出
//        System.out.printf("intValue: %d\n", intValue);
//        System.out.printf("longValue: %d\n", longValue);
//        System.out.printf("floatValue: %.2f\n", floatValue); // 保留两位小数
//        System.out.printf("doubleValue: %.3f\n", doubleValue); // 保留三位小数
//        System.out.printf("charValue: %c\n", charValue);
//        System.out.printf("stringValue: %s\n", stringValue);

//        // 复合格式
//        System.out.printf("Formatted output: intValue=%d, floatValue=%.2f, stringValue=%s\n",
//                intValue, floatValue, stringValue);

        arr = new long[1024 * 1024][];
        for (int i = 0; i < 1024 * 1024; i++) {
            arr[i] = new long[8];
            for (int j = 0; j < 8; j++) {
                arr[i][j] = 0L;
            }
        }
        long sum = 0L;
        long marked = System.currentTimeMillis();
        for (int i = 0; i < 1024 * 1024; i+=1) {
            for(int j =0; j< 8;j++){
                sum = arr[i][j];
            }
        }
        System.out.println("Loop times:" + (System.currentTimeMillis() - marked) + "ms");

        marked = System.currentTimeMillis();
        for (int i = 0; i < 8; i+=1) {
            for(int j =0; j< 1024 * 1024;j++){
                sum = arr[j][i];
            }
        }
        System.out.println("Loop times:" + (System.currentTimeMillis() - marked) + "ms");
        Thread.yield();
        LongAdder longAdder = new LongAdder();

        Socket socket = new Socket("", 123);
        socket.getInputStream().read();

    }
}
