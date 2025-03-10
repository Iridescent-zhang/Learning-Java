package com.example.test;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.test
 * @ClassName : .java
 * @createTime : 2025/2/13 13:24
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import org.apache.logging.log4j.ThreadContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Example {
    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        String traceId = UUID.randomUUID().toString();
        ThreadContext.put("traceId", traceId);

        // 进行日志输出或者其他操作...
        Object o = new Object();
        Class<?> aClass = o.getClass();

        ThreadContext.remove("traceId"); // 使用完后移除traceId避免污染其他线程
        StringBuilder sb = new StringBuilder();
        LinkedList<String> sub = new LinkedList<String>();

        Example example;
        Class<?> aClass1 = Class.forName(Example.class.getName());
        String name = Example.class.getName();
        System.out.println("name = " + name);
        Constructor<?> constructor = aClass1.getConstructor();
        Object o1 = constructor.newInstance();
        System.out.println("o1 = " + o1);
    }
}

interface AAA{

}

interface B{

}

class Main {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        // Producer
        Thread producer = new Thread(() -> {
            try {
                int value = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    queue.offer(value);
                    System.out.println("Produced " + value++);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });

        // Consumer
        Thread consumer = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Integer value = queue.poll();
                    System.out.println("Consumed " + value);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}