package com.example.interview.ioStream;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.ioStream
 * @ClassName : .java
 * @createTime : 2025/1/6 15:59
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class ReaderExample {
    public static void main(String[] args) {
        try (InputStream fileInputStream = new FileInputStream("input.txt");
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);) {

            // TODO InputStreamReader是一个将字节流转换成字符流的桥梁。它的构造函数需要一个InputStream（即一个文件输入流FileInputStream或套接字输入流）。通过这个类，字节可以被读取并解释为字符。
//            char[] chars = new char[50];
//            inputStreamReader.read(chars);
//            System.out.println(new String(chars));

//            int data = inputStreamReader.read();
//            while (data!=-1) {
//                System.out.println((char) data);
//                data = inputStreamReader.read();
//            }

            // TODO bufferedReader 在 inputStreamReader 的基础上提供了读取文本行的功能，而不只是逐个字符读取。
//            String line = bufferedReader.readLine();
//            while (line != null) {
//                System.out.println("line = " + line);
//                line = bufferedReader.readLine();
//            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
