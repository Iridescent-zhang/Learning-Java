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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 优雅停机（Graceful Shutdown）是指在程序终止时，key 不会突然中断正在执行的任务，并在终止之前适当地完成清理工作。
 * 如果你的服务组件实现了 DisposableBean 接口，该接口中的 destroy 方法会在 Spring 上下文关闭时被调用
 *
 * 学习线程池 Shutdown 和 ShutdownNow
 * 1、调用 shutdown() 方法后，线程池将停止接受新任务，但已经提交到线程池中的任务 key （包括正在执行的和等待执行的任务）都会继续运行，直到它们完成。
 * key  但是注意，主线程调用 shutdown() 不会阻塞着等待所有已提交的任务完成：指的是调用 shutdown() 方法的主线程不会阻塞等待线程池中所有任务的完成。换句话说，调用 shutdown() 后，程序会立即继续执行后续代码，不会等待线程池中的任务全部结束。
 *
 * shutdown () 后：一旦调用 shutdown()，线程池会进入关闭状态，不再接受新的任务。正在执行的任务会继续运行，直到完成。
 * 程序继续执行：即使调用 shutdown() 后，主线程不会阻塞等待已提交的任务完成。程序会立即执行 System.out.println("Shutdown called");。
 * awaitTermination()：为了确保所有已提交的任务完成，可以调用 awaitTermination() 来阻塞主线程，直到线程池中的任务全部执行完毕，或者等待超时。
 *
 * ---
 * shutdownNow() 方法
 * shutdownNow() 方法用于立即关闭线程池，采取的措施更加激烈：
 * 试图停止所有正在执行的任务。key 【通过中断的方式，所以当然也需要任务自身去处理中断】
 * 结束等待队列中的所有尚未开始执行的任务。 【任务队列中的任务】
 * key  返回这些还尚未执行过的任务列表。
 *
 * shutdownNow() 立即停止线程池，但不能保证所有任务都会停止，需要任务处理被中断的状态。
 */
public class GracefulShutdownDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        /**
         * key  我们关掉程序的本质就是 发送信号【除非是直接断电这种】
         * 在 Java 中，可以通过 Runtime.getRuntime().addShutdownHook() 方法添加一个钩子线程，捕获系统发送的 SIGTERM 或 SIGINT 信号，并在接收到这些信号时完成资源释放等清理工作。
         *
         * SIGTERM (信号编号：15)：key 请求进程终止。它是一个可以被捕获和处理的信号，因此进程可以在接收到这个信号后进行清理工作，比如关闭文件、释放资源等。
         * kill -15 <pid> # 发送 SIGTERM 信号给指定的进程
         * kill <pid>     # 默认情况下，kill 命令发送 SIGTERM 信号
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
                 * key 这个程序运行完没有自动停止的原因是因为 ExecutorService 未被正常关闭，在所有提交的任务执行完之后，ExecutorService 会继续等待新的任务，保持运行状态。
                 * 对于使用线程池（例如 ExecutorService）的程序，需要在停机前关闭线程池并等待任务完成。可以使用 shutdown 方法配合 awaitTermination 方法实现。
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

class ShutDown{
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(() -> {
            System.out.println("Task 1 is running");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Task 1 was interrupted");
            }
            System.out.println("Task 1 is completed");
        });

        executorService.submit(() -> {
            System.out.println("Task 2 is running");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Task 2 was interrupted");
            }
            System.out.println("Task 2 is completed");
        });

        executorService.shutdown();  // key  停止接收新任务，但已提交任务会继续执行，并且主线程不会阻塞等待那些任务完成，主线程继续往下走
        System.out.println("Shutdown called");

        // 使用 awaitTermination 等待所有任务完成
        if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
            System.out.println("Not all tasks completed within the timeout");
        } else {
            System.out.println("All tasks completed");
        }
    }
}

class ShutDownNow{
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        executorService.submit(() -> {
            System.out.println("Task 1 is running");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Task 1 was interrupted");
            }
        });

        executorService.submit(() -> {
            System.out.println("Task 2 is running");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Task 2 was interrupted");
            }
        });

        List<Runnable> UnExecutedTasks = executorService.shutdownNow();  // 立即关闭线程池，尝试中断所有正在执行的任务
        System.out.println("Thread pool is shut down immediately and returned unfinished tasks");

        UnExecutedTasks.forEach(task -> System.out.println("UnExecuted task: " + task));
    }
}