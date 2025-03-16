package com.example.javaBase.juc;
import java.util.concurrent.*;
/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.collection
 * @ClassName : .java
 * @createTime : 2025/2/25 20:37
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * CopyOnWriteArrayList 是 Java 提供的一个线程安全的 List 实现。
 * key 本质上，它在每次修改（如添加、修改、删除）时都会创建该 ArrayList 底层数组的新副本，因此读操作与写操作可以并发进行而不会互相干扰。
 * 内部存储：CopyOnWriteArrayList key 使用一个 volatile 关键字修饰的数组变量来存储数据：
 * 所有读操作（如 get、size 等）直接操作内存中的当前数组副本。这些操作不需要加锁，因为 volatile 保证了数组引用的可见性。
 * 所有写操作（如 add、set、remove 等）会首先复制当前数组（复制的时候会上锁），修改复制的数组，然后将引用指向新的数组。
 *      CopyOnWriteArrayList 使用 ReentrantLock 来确保只有一个线程可以进行修改操作，从而保证线程安全。
 * private transient volatile Object[] array; 而且底层数组是 volatile 的
 */
public class CopyOnWriteArrayListExample {
    private CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<Integer>();

    public void addElement(int element) {
        list.add(element);
        System.out.println("Added: " + element);
    }

    public void readElements() {
        for (int element : list) {
            System.out.println("Read: " + element);
        }
    }

    public static void main(String[] args) {
        CopyOnWriteArrayListExample example = new CopyOnWriteArrayListExample();
        // 启动写线程
        Thread writerThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                example.addElement(i);
                try {
                    Thread.sleep(100); // 模拟延迟
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // 启动读线程
        Thread readerThread = new Thread(() -> {
            try {
                Thread.sleep(50); // 模拟延迟，确保写线程已经开始
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            example.readElements();
        });

        writerThread.start();
        readerThread.start();

        try {
            writerThread.join();
            readerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
