package com.example.interview;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview
 * @ClassName : .java
 * @createTime : 2025/1/2 21:37
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class VolatileExample {
    private static volatile boolean flag = false;
    private static int a = 0;
    private static int b = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread writerThread = new Thread(() -> {
            a = 5;
            flag = true;
            b = 10;
        });

        Thread readerThread = new Thread(() -> {
            if(flag) {
                System.out.println("a: " + a);
                System.out.println("b: " + b);
            }
        });

        writerThread.start();
        readerThread.start();

        writerThread.join();
        readerThread.join();
    }
}
