package com.example.interview.ioStream;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.ioStream
 * @ClassName : .java
 * @createTime : 2025/1/6 16:15
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class WriterExample {
    public static void main(String[] args) {
        try (OutputStream fileOutputStream = new FileOutputStream("output.txt");
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
             FileWriter fileWriter = new FileWriter("output.txt");
             PrintWriter printWriter = new PrintWriter(fileWriter);) {

            // TODO OutputStreamWriter 就和 InputStreamReader 对应的，实际上 FileWriter 就是 OutputStreamWriter 的子类，并且 OutputStreamWriter 可以直接构造PrintWriter
//            outputStreamWriter.write("HELLO,WORLD!");

            // TODO PrintWriter是一个非常方便的类，用于输出格式化的文本内容。它比OutputStreamWriter更高级，可以直接输出各种数据类型（字符串、整数等），而不用进行额外的转换。
            printWriter.println("Hello, World!");
            printWriter.printf("Formatted number: %.2f%n", 123.4567);

            PrintWriter printWriter1 = new PrintWriter(fileOutputStream);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
