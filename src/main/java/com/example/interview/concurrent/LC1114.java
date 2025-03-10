package com.example.interview.concurrent;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.concurrent
 * @ClassName : .java
 * @createTime : 2025/2/26 10:43
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Foo {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition conditionB = lock.newCondition();
    private final Condition conditionC = lock.newCondition();
    private int cnt = 1;  // key 使用实例变量而不是静态变量，因为 leetcode 调用是会创建不同的实例，如果是静态的话这些实例共用这个变量就死循环了

    public Foo() {}

    public void first(Runnable printFirst) throws InterruptedException {
        lock.lock();
        try {
            // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run();
            cnt++;  // 增加计数器
            conditionB.signal();  // 通知第二个任务可以执行
        } finally {
            lock.unlock();
        }
    }

    public void second(Runnable printSecond) throws InterruptedException {
        lock.lock();
        try {
            // 如果计数器不为2，则等待
            while (cnt != 2) {
                conditionB.await();
            }
            // printSecond.run() outputs "second". Do not change or remove this line.
            printSecond.run();
            cnt++;  // 增加计数器
            conditionC.signal();  // 通知第三个任务可以执行
        } finally {
            lock.unlock();
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        lock.lock();
        try {
            // 如果计数器不为3，则等待
            while (cnt != 3) {
                conditionC.await();
            }
            // printThird.run() outputs "third". Do not change or remove this line.
            printThird.run();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                Foo foo = new Foo();
                StringBuilder sb = new StringBuilder();
                CountDownLatch latch = new CountDownLatch(3);
                // key 当你创建这个 lambda 表达式 () -> sb.append("second") 时，Java 实际上在背后做了一些工作。具体来说，它创建了一个类，该类实现了 Runnable 接口，并且将 sb 引用捕获为这个内部类的一个成员变量。
                Runnable second = () -> sb.append("second");

                // key 在 Java 中，lambda 表达式可以捕获外部作用域中的变量，而这种捕获实际上是在编译阶段通过生成匿名内部类来实现的。这个匿名内部类持有对捕获变量的引用，从而可以在不同的线程中共享和操作这些变量。
//                class MyRunnable implements Runnable {
//                    private StringBuilder sb;
//
//                    public MyRunnable(StringBuilder sb) {
//                        this.sb = sb;
//                    }
//
//                    @Override
//                    public void run() {
//                        sb.append("second");
//                    }
//                }
//                Runnable r = new MyRunnable(sb);

                Thread A = new Thread(() -> {
                    try {
                        foo.first(() -> sb.append("second"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                }, "threadA");

                Thread B = new Thread(() -> {
                    try {
                        foo.second(() -> sb.append("first"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                }, "threadB");

                Thread C = new Thread(() -> {
                    try {
                        foo.third(() -> {
                            sb.append("third");
                            latch.countDown();
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, "threadC");

                A.start();
                B.start();
                C.start();

                // 最后输出结果
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(sb.toString());
            }).start();
        }
    }
}
