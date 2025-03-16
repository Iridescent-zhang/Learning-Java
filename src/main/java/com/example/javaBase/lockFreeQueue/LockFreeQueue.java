package com.example.javaBase.lockFreeQueue;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.lockFreeQueue
 * @ClassName : .java
 * @createTime : 2025/3/5 12:25
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.util.concurrent.atomic.AtomicReference;

/**
 * 无锁队列（Lock-Free Queue）可以通过原子操作实现，常见是基于 CAS（Compare-and-Swap）操作。Java 中的java.util.concurrent包下的 ConcurrentLinkedQueue 就是一个典型的无锁队列实现。以下是基于链表实现的基本无锁队列思路：
 */
public class LockFreeQueue<T> {
    private final AtomicReference<Node<T>> head;
    private final AtomicReference<Node<T>> tail;

    public LockFreeQueue() {
        Node<T> dummy = new Node<>(null);
        head = new AtomicReference<>(dummy);
        tail = new AtomicReference<>(dummy);
    }

    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        while (true) {
            Node<T> currentTail = tail.get();
            Node<T> tailNext = currentTail.next;
            if (currentTail == tail.get()) {
                if (tailNext == null) {
                    if (currentTail.next.compareAndSet(null, newNode)) {
                        tail.compareAndSet(currentTail, newNode);
                        return;
                    }
                } else {
                    tail.compareAndSet(currentTail, tailNext);
                }
            }
        }
    }

    public T dequeue() {
        while (true) {
            Node<T> currentHead = head.get();
            Node<T> currentTail = tail.get();
            Node<T> headNext = currentHead.next;
            if (currentHead == head.get()) {
                if (currentHead == currentTail) {
                    if (headNext == null) {
                        return null;
                    }
                    tail.compareAndSet(currentTail, headNext);
                } else {
                    T item = headNext.item;
                    if (head.compareAndSet(currentHead, headNext)) {
                        return item;
                    }
                }
            }
        }
    }
}

class Node<T> {
    T item;
    Node<T> next;

    Node(T item) {
        this.item = item;
        this.next = null;
    }

    boolean compareAndSet(Node oldNode, Node newNode) {
        return true;
    }
}
