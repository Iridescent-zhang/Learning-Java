package com.example.interview.concurrent.oddEven;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.concurrent
 * @ClassName : .java
 * @createTime : 2025/2/23 12:49
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

// 两线程打印奇数偶数
public class PrintOddEven {
    private static final Object lock = new Object();
    private static int count = 1;
    public static void main(String[] args) {
        Thread oddThread = new Thread(()->{
            synchronized(lock) {
                while(count<=10) {
                    if (count%2==1) {
                        String name = Thread.currentThread().getName();
                        System.out.println(name + " : " + count);
                        count++;
                        lock.notify();
                    }else {
                        try {
                            lock.wait();
                        }catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }, "oddThread");
        Thread evenThread = new Thread(()->{
            synchronized (lock) {
                while (count<=10) {
                    if (count%2==0) {
                        String name = Thread.currentThread().getName();
                        System.out.println(name + " : " + count);
                        count++;
                        lock.notify();
                    }else {
                        try {
                            lock.wait();
                        }catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }, "evenThread");

        oddThread.start();
        evenThread.start();
        try {
            oddThread.join();
            evenThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("count = " + count);
    }
}
