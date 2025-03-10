package com.example.interview.thread;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.concurrent
 * @ClassName : .java
 * @createTime : 2025/2/23 12:17
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class CreateThread {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        MyThread MyThread = new MyThread();
//        MyThread.start();
//        try {
//            MyThread.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        // 匿名重写 Runnable 的 run 方法
//        Thread bbb = new Thread(() -> {
//            System.out.println("BBB");
//            long id = Thread.currentThread().getId();
//            System.out.println("id = " + id);
//        });
//        bbb.start();

//        Thread ccc = new Thread(new MyRunnable());
//        ccc.start();

//        MyCallable myCallable = new MyCallable();
//        FutureTask<Integer> futureTask = new FutureTask<>(myCallable);
//        Thread thread = new Thread(futureTask);
//        thread.start();
//
//        try {
//            Integer integer = futureTask.get();
//            System.out.println("integer = " + integer);
//        } catch (ExecutionException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        ExecutorService executor = Executors.newFixedThreadPool(10);

        FutureTask<Integer> futureTask = new FutureTask<>(new MyCallable());
        executor.execute(futureTask);
        Integer integer = futureTask.get();

        Future<?> future1 = executor.submit(new MyRunnable());
        Future<Integer> submit = executor.submit(new MyCallable());
        Future<?> submitRun = executor.submit(() -> {
            System.out.println("submit = " + submit);
        });

        // keypoint CompletableFuture 是 Future 的增强版，支持编制任务的链式调用、完成回调、更强的异常处理和组合异步任务等功能。
        /**
         * supplyAsync 异步计算返回有结果的 future
         */
        CompletableFuture<Integer> future  = CompletableFuture.supplyAsync(() -> {
            System.out.println("submitRun = " + submitRun);
            return 1/0;
        }, executor);

        /**
         * a function that accepts one argument and produces a result.
         */
        CompletableFuture<Double> doubleCompletableFuture = future.thenApply(result -> {
            System.out.println("result = " + result);
            return (double) (100 * 100);
        }).exceptionally(e->{
            System.out.println("e.getCause() = " + e.getCause());
            return 0.5;
        });

        executor.shutdown();
    }
}

class MyThread extends Thread {
    @Override
    public void run(){
        System.out.println("AAA");
        String name = this.getName();   // 用this 就能拿到自己的名字
        System.out.println("name = " + name);
    }

    public static void main(String[] args) {
        Thread newThread = new Thread(new MyThread());
        newThread.start();
    }
}

class MyRunnable implements Runnable{
    private int state = 0;
    private Object object;
    @Override
    public void run() {
//        int a = 1/0;  无法抛出异常
        String name = Thread.currentThread().getName();
        System.out.println("name = " + name);
    }
}

/**
 * 可以有返回值 可以抛出异常
 */
class MyCallable implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
//        int a = 1/0;
        System.out.println("callable 运行");
        return 1;
    }
}
