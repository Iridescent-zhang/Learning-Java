package com.example.javaBase.juc;

import com.google.common.util.concurrent.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.juc
 * @ClassName : .java
 * @createTime : 2025/3/9 22:03
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * key Future用于表示异步计算的结果，只能通过阻塞或者轮询的方式获取结果，而且不支持设置回调方法，Java 8之前若要设置回调一般会使用 guava 的 ListenableFuture，
 * 回调的引入又会导致臭名昭著的回调地狱（下面的例子会通过ListenableFuture的使用来具体进行展示）。
 *
 * ListenableFutureTask是 Guava 库中的一个类，它实现了 ListenableFuture 接口所以可以被监听。我们用它来包装Callable任务
 * 【FutureTask 本来就是用来包装 Callable 任务然后提交给线程池的】，随后将其提交给线程池执行。
 *
 * listenableFutureTask.addListener 方法接受一个 Runnable (就是 listener)监听器对象和一个执行器（Executor）。（注意 Executor 是一个绝决子接口，练 ExecutorService 都是继承它的）
 * 在任务完成后，监听器会被触发，进而执行我们定义的Runnable逻辑。
 * 这里使用 MoreExecutors.directExecutor() 确保监听器在调用线程中立即执行。
 *
 * ---
 *
 * ListenableFutureTask实现了ListenableFuture接口，扩展了 JDK 自带的FutureTask，并添加了监听能力。
 * key 主要用途：允许为异步任务添加监听器，以便在任务完成时执行特定的动作。
 *
 * MoreExecutors.directExecutor()
 * 方法定义：MoreExecutors.directExecutor() key 返回一个执行器，它在调用线程中立即执行提交的 Runnable。
 * 主要用途：避免线程切换的开销，在不需要异步执行的情况下使用。
 *
 * FutureCallback
 * 接口定义：FutureCallback 是一个带有两个方法（onSuccess和onFailure）的接口，允许在任务完成或失败时分别执行不同操作。
 * 主要用途：提供细粒度的控制，可以针对成功或失败的结果执行特定逻辑。
 */
public class ListenableFutureExample {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        Callable<String> callable = () -> {
            Thread.sleep(1000);
            return "Task Completed!";
        };

        ListenableFutureTask<String> listenableFutureTask = ListenableFutureTask.create(callable);
        executor.execute(listenableFutureTask);

        // Add a listener to the ListenableFuture
        listenableFutureTask.addListener(() -> {
            try {
                System.out.println("Listener: " + listenableFutureTask.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, MoreExecutors.directExecutor());

        // Using Futures.addCallback
        Futures.addCallback(
                listenableFutureTask,
                new FutureCallback<String>() {
                    public void onSuccess(String result) {
                        System.out.println("Callback: " + result);
                    }
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                },
                MoreExecutors.directExecutor()
        );

        executor.shutdown();
    }
}
