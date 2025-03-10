package com.example.interview.javaBase.jmm;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.jmm
 * @ClassName : .java
 * @createTime : 2025/2/26 22:08
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * key Java 内存模型（Java Memory Model，简称 JMM）中的 "happen-before" 关系是用来定义多线程环境中操作的可见性和顺序性的一种规则。简单地说，它描述了一个线程的某个操作如何能够在另一个线程中可见。
 *
 * happen-before 规则
 * 掌握 happen-before 关系能够帮助你判断在多线程环境中，一个操作的结果是否对另一个操作可见。
 */
public class HappenBefore {
    private static final Object lock = new Object();
    volatile static boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        /**
         * 1. 程序顺序规则（Program Order Rule）
         *      在一个线程内，按代码顺序，前面的操作 happen-before 后面的操作。key 注意关键在一个线程内的代码是有序的
         *      在同一个线程中，操作 1（a = 1） happen-before 操作 2（b = 2）。
         */
        int a = 1;  // 操作1
        int b = 2;  // 操作2

        /**
         * 2. 监视器锁规则（Monitor Lock Rule）
         *      一个线程的对监视器锁的释放 happen-before 随后的另一个线程对同一个监视器锁的获取。key 在锁没释放之前另一个线程是不能拿到锁的
         *      线程 A 对 lock 的释放 happen-before 线程 B 对相同 lock 的获取。因此，如果操作 1 修改了某个变量，那么线程 B 在执行操作 2 时能够看到这个修改。
         *      key 当然跟 synchronized 的特性也有关，进入同步块时它会从主内存读值，退出同步块时把值直接写入主内存，这是 synchronized 很重要的特性，别忘了，也就是它能有可见性和原子性
         */
        // 线程A
        synchronized (lock) {
            // 操作1
            a = 5;
        }
        // 线程B
        synchronized (lock) {
            // 操作2
            a = 10;
        }

        /**
         * 3. volatile 变量规则（Volatile Variable Rule）
         *      一个线程对 volatile 变量的写操作 happen-before 随后的线程对同一个 volatile 变量的读操作。
         *      线程 A 对 flag 的写操作 happen-before 线程 B 随后对 flag 的读操作。这意味着如果线程 A 将 flag 设置为 true，线程 B 读取到的也将是 true。
         */
        // 线程A
        flag = true;  // 操作1
        // 线程B
        if (flag) {  // 操作2
            // 执行某些操作
        }

        /**
         * 4. 传递性（Transitivity）
         *      如果操作 A happen-before 操作 B，且操作 B happen-before 操作 C，那么操作 A happen-before 操作 C。
         *      操作 1 happen-before 操作 2，操作 2 happen-before 操作 3，因此操作 1 happen-before 操作 3。这意味着，线程 C 在读取变量 c 时，能够看到线程 A 对变量 a 的写操作。
         */
        // 线程A
        a = 1;  // 操作1
        // 线程B
        if (a == 1) {
            b = 2; // 操作2
        }
        // 线程C
        if (b == 2) {
            int c = 3; // 操作3
        }

        /**
         * 5. Thread 启动规则（Thread Start Rule）
         *      一个线程的 Thread.start() 调用 happen-before 该线程的每一个动作。
         *      操作 2（调用 t.start()） happen-before 操作 1（线程 t 中的任何操作）。
         */
        Thread t = new Thread(() -> {
            // 操作1
        });
        t.start();  // 操作2

        /**
         * 6. Thread 终止规则（Thread Termination Rule）
         *      一个线程的所有操作 happen-before 另一个线程从该线程的 join() 调用成功返回。
         *      操作 1（线程 t 中的所有操作） happen-before 操作 2（join()的返回）。
         */
        Thread tt = new Thread(() -> {
            // 操作1
        });
        tt.start();
        tt.join();  // 操作2

    }
}
