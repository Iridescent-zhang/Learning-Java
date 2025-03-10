package com.example.interview.concurrent;

import java.util.concurrent.locks.*;
/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.concurrent
 * @ClassName : .java
 * @createTime : 2025/2/23 13:34
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class printABCABCABC {

    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition ACondition = lock.newCondition();
    private static final Condition BCondition = lock.newCondition();
    private static final Condition CCondition = lock.newCondition();
    private static final int MAX = 8;
    private static int state = 0;

    public static void main(String[] args) throws InterruptedException {

        Thread threadA = new Thread(()->{
            lock.lock();
            while (state <=MAX) {
                if (state %3 == 0) {
                    System.out.print("A");
//                    System.out.println(Thread.currentThread().getName() + ": " + Thread.currentThread().getState() + ": " + "A");
                    state++;
                    BCondition.signal();
                }else {
                    try{
                        ACondition.await();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // keypoint 注意这里还要把 B 再唤醒下
            BCondition.signal();
            lock.unlock();
        }, "ThreadA");

        Thread threadB = new Thread(()->{
            lock.lock();
            while (state <=MAX) {
                if (state %3 == 1) {
                    System.out.print("B");
//                    System.out.println(Thread.currentThread().getName() + ": " + Thread.currentThread().getState() + ": " + "B");
                    state++;
                    CCondition.signal();
                }else {
                    try{
                        BCondition.await();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            lock.unlock();
        }, "ThreadB");

        Thread threadC = new Thread(()->{
            lock.lock();
            while (state <=MAX) {
                if (state %3 == 2) {
                    System.out.print("C");
//                    System.out.println(Thread.currentThread().getName() + ": " + Thread.currentThread().getState() + ": " + "C");
                    state++;
                    ACondition.signal();
                }else {
                    try{
                        CCondition.await();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            lock.unlock();
        }, "ThreadC");

        // 进入就绪状态，等待被调度
        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();

        System.out.println("\r\n" + "state = " + state);
    }
}
