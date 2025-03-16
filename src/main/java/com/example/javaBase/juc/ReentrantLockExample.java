package com.example.javaBase.juc;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.juc
 * @ClassName : .java
 * @createTime : 2025/2/25 22:22
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
    private final ReentrantLock lock = new ReentrantLock(false); // true for fair, false for non-fair

    public void doSomething() {
        lock.lock();
        try {
            // critical section
            System.out.println(Thread.currentThread().getName() + " 获取了锁!");
            Thread.sleep(1000); // 模拟操作
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + " 释放了锁!");
        }
    }

    public static void main(String[] args) {
        ReentrantLockExample example = new ReentrantLockExample();
        Runnable doSomething = example::doSomething;

        Thread t1 = new Thread(doSomething, "线程1");
        Thread t2 = new Thread(doSomething, "线程2");
        Thread t3 = new Thread(doSomething, "线程3");

        t1.start();
        t2.start();
        t3.start();
    }
}
