package com.example.javaBase.juc;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.juc
 * @ClassName : .java
 * @createTime : 2025/2/25 21:13
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * key AbstractQueuedSynchronizer (AQS) 抽象队列同步器
 * AbstractQueuedSynchronizer（简称 AQS）是 Java 并发库中一个用于构建 锁和同步器 的框架。（ReentrantLock 和 读写锁是锁，是 juc.locks 下的，信号量和 CountDownLatch 都是 juc 包下的，它们是同步器）
 * key 它提供了一种队列机制，用于管理一个或多个线程等待锁或其他资源的获取。
 * AQS 是 java.util.concurrent.locks 包中的核心类，很多并发工具如 ReentrantLock、CountDownLatch、Semaphore、ReentrantReadWriteLock 等都基于 AQS 实现。
 *
 * AQS 的关键概念：
 * AQS 使用一个 FIFO 双向队列来管理等待的线程。当一个线程尝试获取锁但失败时，它会被放入这个队列。  key CLH 队列（CLH Queue）
 * 每个进入队列的线程都会被封装成一个节点（Node），保存线程信息和状态（例如：是否已经获取锁、是否应该被唤醒等）。
 * key 同步状态 (state)：AQS 还维护一个 volatile int state 变量，这个变量可以被子类用来表示共享资源的状态（例如：计数器、许可证、锁被当前线程持有的次数(对 ReentrantLock 来说)等）。
 * 比如 CountDownLatch 就用这个 state 来表示自己的 count 计数。(Synchronization control For CountDownLatch. Uses AQS state to represent count.)
 *
 * AQS 的操作方法:
 * 获取锁（acquire）：
 * 一个线程尝试获取锁，如果获取失败，它会被加入等待队列，并一直等待直到被其他线程唤醒。
 * acquire(int arg) 和 acquireShared(int arg) 是两个关键的获取方法，分别用于独占锁和共享锁。
 *
 * 释放锁（release）：
 * key 当一个线程释放锁时，会唤醒等待队列中第一个等待的线程。
 * release(int arg) 和 releaseShared(int arg) 是两个关键的释放方法，分别用于独占锁和共享锁。
 *
 * 独占模式和共享模式：
 * AQS 支持独占模式（exclusive）和共享模式（shared）。
 * 独占模式下，同一时刻只有一个线程可以持有锁。比如：ReentrantLock。
 * 共享模式下，多线程可以同时访问共享资源。比如：Semaphore。
 *
 * 再讲讲 CLH 队列
 * CLH 队列是一种通过链表实现的自旋锁队列，用于管理多个线程对临界区或者共享资源的访问。
 * CLH 队列是 AbstractQueuedSynchronizer (AQS) 实现的核心数据结构之一，它使用了一种 隐式链表 来跟踪那些试图获取锁的线程。
 * CLH 队列节点 (CLH Nodes)
 * key CLH 队列中的每个节点（Node）代表一个正在等待获取锁的线程。就是每个等待资源的线程被封装为 Node
 * 线程引用：指向当前节点所表示的线程。
 * key 状态：标记当前节点是等待状态、取消状态还是被唤醒状态。  区别 AOS 的另一个核心变量 state，这个是在节点里面的
 * 前驱节点、后驱节点：指向队列中前一个和后一个节点，这样形成一个双向链表。
 */
public class CountDownLatchExample {

    public static void main(String[] args) throws InterruptedException {
        int count = 3;
        CountDownLatch latch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            new Thread(new Worker(latch)).start();
        }

        System.out.println("等待所有工作线程完成...");
        latch.await();  // 主线程在此等待，当计数器为0时，继续执行
        System.out.println("所有工作线程已完成.");
    }
}

class Worker implements Runnable {
    private final CountDownLatch latch;

    Worker(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            System.out.println("正在执行任务 " + Thread.currentThread().getName());
            Thread.sleep((int) (Math.random() * 1000)); // 模拟任务执行
            System.out.println("任务 " + Thread.currentThread().getName() + " 完成");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            latch.countDown();  // 减少计数器
        }
    }
}
