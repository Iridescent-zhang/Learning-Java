package com.example.interview.blockingQueue;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.blockingQueue
 * @ClassName : .java
 * @createTime : 2025/1/14 18:51
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerExample {
    public static void main(String[] args) {
        // 定义容量为10的阻塞队列
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);

        // 创建并启动生产者线程
        Thread producer = new Thread(new Producer(queue));
        producer.start();

        // 创建并启动消费者线程
        Thread consumer = new Thread(new Consumer(queue));
        consumer.start();
    }
}

// 生产者类
class Producer implements Runnable {
    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 20; i++) {
                System.out.println("put: " + i);
                queue.put(i); // 阻塞地放入元素
//                Thread.sleep(100); // 模拟生产时间
            }
            queue.put(-1); // 发送特殊标志位表示结束
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// 消费者类
class Consumer implements Runnable {
    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer item = queue.take(); // 阻塞地获取元素
                if (item == -1) {
                    break; // 结束消费
                }
                System.out.println("Consuming: " + item);
//                Thread.sleep(150); // 模拟消费时间
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
