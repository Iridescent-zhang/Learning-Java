package com.example.javaBase.threadSecurity;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.threadSave
 * @ClassName : .java
 * @createTime : 2025/2/25 17:47
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * keypoint 多线程保证安全性的方式，下次一定要全说
 * 1. 使用同步块（Synchronized Block）
 * 2. 使用显式锁（Explicit Lock）比如 ReentrantLock
 * 3. 使用原子类（Atomic Classes）比如 AtomicInteger，这些类提供了一些原子操作的方法，不需要额外的同步。
 * 4. 使用并发集合（Concurrent Collections）如 ConcurrentHashMap、CopyOnWriteArrayList 等，已经对内部的并发访问进行了适当的处理，可以直接使用。
 * 5. 使用不可变对象（Immutable Objects）天然是线程安全的。
 * 6. 使用 ThreadLocal 变量，提供了线程本地变量，每个线程都有自己独立初始化的变量副本，从而避免了共享变量的竞争问题。
 * 7. 使用信号量（Semaphore）是一种更为复杂的锁机制，keypoint 用于限制访问资源的线程数量。例如，它可以允许最多 N 个线程同时访问某个资源。
 * 8. 使用读写锁（ReadWriteLock）允许多个线程同时读取数据，但在写入时只允许一个线程写入。
 * 9、CAS 是不是也不能忘  key  CAS（Compare-And-Swap）是一种用于实现无锁并发编程的技术，就说 Atomic 包中的类就好，例如 AtomicInteger, AtomicReference等，它们都有 compareAndSet()
 *      ABA 问题：CAS操作可能会遇到 ABA 问题，即一个变量在一次操作中值没有改变，但过程中可能经过其他值的变化。这可以通过使用版本号来解决，例如 AtomicStampedReference。
 */
public class Counter {
    private static AtomicInteger cnt = new AtomicInteger(2);
    private static int count = 0;
    private static final ReentrantLock lock = new ReentrantLock(true);  // true for fair, false for non-fair
    private static ConcurrentHashMap<String, Integer> hmap = new ConcurrentHashMap<>();
    private static final ThreadLocal<Integer> threadLocalCounter = ThreadLocal.withInitial(() -> 0);
    private static final Semaphore semaphore = new Semaphore(1);
    private static final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    // keypoint 定义一个 InheritableThreadLocal
    private static final InheritableThreadLocal<Integer> inheritableThreadLocal = new InheritableThreadLocal<Integer>(){
        protected Integer initialValue(){
            return 0;
        }

        protected Integer childValue(Integer parentValue) {
            // 子线程对继承下来的 InheritableThreadLocal 做了变换
            return parentValue+1;
        }
    };

    // keypoint 定义一个 TransmittableThreadLocal
    private static final TransmittableThreadLocal<String> transmittableThreadLocal = new TransmittableThreadLocal<>();

    public static void main(String[] args) {

        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }

        threadLocalCounter.set(threadLocalCounter.get()+1);
        Integer integer = threadLocalCounter.get();
        System.out.println("integer = " + integer);

        // 信号量
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            semaphore.release();
        }

        ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
        readLock.lock();
        readLock.unlock();
        rwLock.writeLock().lock();
        rwLock.writeLock().unlock();

        /**
         * keypoint InheritableThreadLocal 是 Java 提供的一种特殊版本的 ThreadLocal，它能够将父线程中的 ThreadLocal 变量自动传递给子线程。
         * 父线程与子线程：当一个子线程从父线程派生时，InheritableThreadLocal 会确保子线程继承父线程的 ThreadLocal 变量值。
         * 自定义子线程变量值：可以通过重写 InheritableThreadLocal 中的 childValue 方法，使子线程的变量值可以是父线程变量值的某种变换，而不仅仅是简单的复制。
         */
        inheritableThreadLocal.set(100); // 设置父线程的值
        Thread child = new Thread(()->{
            Integer integer1 = inheritableThreadLocal.get();
            System.out.println("Child Thread Value = " + integer1);
        }, "childThread");
        child.start();
        try {
            child.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Integer integer1 = inheritableThreadLocal.get();
        System.out.println("Parent Thread Value = " + integer1);

        /**
         * 在使用线程池（如 ThreadPoolExecutor）时，每次任务执行都会从线程池中获取一个线程，这导致了 InheritableThreadLocal 并不直接适用于线程池，因为线程可能在多次任务之间被重用。
         * TransmittableThreadLocal 是 Alibaba 开源的工具库，它使得线程池中的线程可以正确传递 ThreadLocal 参数。
         */
        transmittableThreadLocal.set("Parent Value");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Runnable task = ()->{
            System.out.println("Child Thread Value: " + transmittableThreadLocal.get());
        };
        for(int i=0; i<5; i++) {
            // keypoint 使用 TtlRunnable 来包装任务。这样可以确保线程池中的线程能继承父线程的 TransmittableThreadLocal 变量值。
            executorService.submit(TtlRunnable.get(task));
        }
        executorService.shutdown();
    }

    public synchronized void increment() {
        count++;
    }
}
