package com.example.javaBase;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase
 * @ClassName : .java
 * @createTime : 2025/3/4 12:49
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 优雅停机（Graceful Shutdown）是指在程序终止时，key 不会突然中断正在执行的任务，并在终止之前适当地完成清理工作。
 *
 * 确保你的服务组件实现了DisposableBean接口，该接口中的destroy方法会在 Spring 上下文关闭时被调用：
 */
public class GracefulShutdownDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        /**
         * 在 Java 中，可以通过Runtime.getRuntime().addShutdownHook方法添加一个钩子线程，捕获系统发送的 SIGTERM 或 SIGINT 信号，并在接收到这些信号时完成资源释放等清理工作。
         *
         * SIGTERM (信号编号：15)：key 请求进程终止。它是一个可以被捕获和处理的信号，因此进程可以在接收到这个信号后进行清理工作，比如关闭文件、释放资源等。
         * kill -15 <pid> # 发送SIGTERM信号给指定的进程
         * kill <pid>     # 默认情况下，kill命令发送SIGTERM信号
         *
         * SIGINT (信号编号：2)：key 中断进程，通常由用户从键盘上使用 Ctrl+C 发送。它可以被捕获、阻塞或忽略。
         *
         * SIGKILL (信号编号：9) key 强制终止进程。它不能被捕获、阻塞或忽略，进程无法进行任何清理工作。
         */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown Hook is running, shutting down executor...");
            executor.shutdown();
            try {
                /**
                 * key 这个程序运行完没有自动停止的原因是因为ExecutorService未被正常关闭，在所有提交的任务执行完之后，ExecutorService会继续等待新的任务，保持运行状态。
                 * 对于使用线程池（例如ExecutorService）的程序，需要在停机前关闭线程池并等待任务完成。可以使用shutdown方法配合awaitTermination方法实现。
                 */
                if (!executor.awaitTermination(6, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
                System.out.println("Executor shut down.");
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }));

        // Submit tasks to executor
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println("Task completed by " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        System.out.println("Application is running...");
        // Simulating long-running task
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Application is terminating...");
    }
}
