package com.example.interview.concurrent.oddEven;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.concurrent.oddEven
 * @ClassName : .java
 * @createTime : 2025/2/23 19:19
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class TryReentrantLock {
    private static int count = 1;
    private static final int maxValue = 10;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition oddCondition = lock.newCondition();
    private static final Condition evenCondition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException{
        Thread oddThread = new Thread(()->{
            lock.lock();
            while (count<=maxValue) {
                if ((count&1)==1) {
                    System.out.println(Thread.currentThread().getName() + ": " + count++);
                    evenCondition.signal();
                }else {
                    try {
                        oddCondition.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            lock.unlock();
        }, "oddThread");
        Thread evenThread = new Thread(()->{
            lock.lock();
            while (count<=maxValue) {
                if ((count&1)==0) {
                    System.out.println(Thread.currentThread().getName() + ": " + count++);
                    oddCondition.signal();
                }else {
                    try {
                        evenCondition.await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            /**
             * 这里灰常关键，区别于 synchronized 退出代码块后会自动释放锁，这里不会自动释放锁（之前是我自己 await 了其他线程才能拿到锁），而最后的时候 count=11 已经退出循环了，所以我没释放，
             * 奇数线程即便被唤醒了也一直处在竞争锁的过程，无法去判断 while 条件，自然也就死机了
             */
            lock.unlock();
        }, "evenThread");

        oddThread.start();
        evenThread.start();

        oddThread.join();
        evenThread.join();

        System.out.println("count = " + count);
    }
}
