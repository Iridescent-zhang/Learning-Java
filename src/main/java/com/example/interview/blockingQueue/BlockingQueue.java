package com.example.interview.blockingQueue;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.blockingQueue
 * @ClassName : .java
 * @createTime : 2025/1/14 16:23
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition 并不是一个锁本身，而是与某个特定的 ReentrantLock 相关联，用来实现更加细粒度的线程间通信。
 * 通过它们你可以为特定的条件进行等待（await）和通知（signal）。每个 Condition 都和特定的锁一起使用，因此它们能在相应的锁上挂起和唤醒线程。
 *
 */
public class BlockingQueue<E> {
    private final Queue<E> queue = new LinkedList<>();
    private final int capacity;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public void put(E item) throws InterruptedException {
        lock.lock();
        try {
            /**
             * 这里已经有.await()进行等待，但还是使用 while 循环是为了处理虚假唤醒（spurious wakeup）和避免竞争条件。
             * 虚假唤醒是指线程在调用 Condition.await() 后，即使没有收到相应的通知（signal），也会有可能被唤醒。这是一种可能发生的但不容易预料的情况。而使用 while 循环可以确保唤醒以后，仍然会再次检查条件，以确保状态符合预期。
             * 还为了避免避免竞争条件：假如有多个线程在等待相同的条件变量并且由于某些原因它们几乎同时被唤醒，不使用 while 循环将导致不一致的状态。
             *      如果有多个等待的线程被唤醒，它们可能会同时尝试插入新元素，从而导致队列超出容量。
             *
             * 虚假唤醒是指线程在没有接收到明确的唤醒信号的情况下，从 wait() 或 await() 等等待状态中返回。这种现象在多线程编程中是有可能发生的。这意味着一个线程可能会在条件未真正满足的情况下被唤醒。
             *      虚假唤醒是操作系统层面的行为，是一些 JVM 实现中可能出现的情况，即线程会在某些情况下不需要明确的条件满足就被唤醒。
             *      尽管在大多数情况下这种现象不会频繁发生，但出于规范和安全性考虑，Java 文档和社区推荐在使用 wait() 或 await() 时总是重新检查条件。
             */
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.add(item);
            System.out.println("put:"+item);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            E item = queue.poll();
            System.out.println("poll:"+item);
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
}
