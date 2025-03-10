package com.example.interview;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview
 * @ClassName : .java
 * @createTime : 2025/3/6 12:00
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.*;

/**
 * key 检测异常（Checked Exception）和非检测异常（Unchecked Exception）
 * 1、检测异常（Checked Exception）：
 *   检测异常是 key 需要在编译时被检查的异常，表示程序员必须显式处理这些异常（例如：输入 / 输出异常、SQL 异常等）。为了处理这些异常，程序员需要：
 *      在方法签名中声明 throws 该异常。
 *      或者在方法内使用 try-catch 块捕获该异常。
 *   key 其实就是有一些方法在方法签名上 throws 了异常，那我们用了这个方法就要显式地处理它，这是写代码时就要做好的，所以叫 “检测异常”
 * 2、非检测异常（Unchecked Exception）
 *   非检测异常包括 RuntimeException 及其子类，表示在运行时可能会发生的异常，例如：ArithmeticException（除零）、NullPointerException 等。--
 *   --与检测异常不同，这些异常不需要在方法签名中声明，它们往往表示编程中的一些逻辑错误或者错误操作。
 *   key 它们是运行时发生的，是意想不到的，按自然就叫“非检测异常”了
 *
 * Java 异常类层次结构
 * - Throwable
 *   - Exception
 *     - RuntimeException   只有 RuntimeException 及其子类被归类为非检测异常。
 *
 * 为什么 ArithmeticException 并不继承 ExecutionException，我们却能通过 catch (ExecutionException e) 捕获它？
 * ExecutionException 是包裹在 ExecutorService.submit 方法执行 Callable 返回的 Future.get() 方法中的异常。--
 * --在 Future.get() 方法中，key 任何在 Callable.call() 中抛出的异常将被捕获并重新封装成 ExecutionException。比如 new ExecutionException(new ArithmeticException());
 */
public class exceptionExample {

    public static void main(String[] args) throws FileNotFoundException {
        // key 方法体中可能会抛出 IOException 检测异常，我们显式处理它
//        FileReader reader = new FileReader("file.txt");

        // key 方法体中可能会抛出 ArithmeticException 非检测异常，但我们原先不知道
//        int result = 3/0;

        ExecutorService executor = Executors.newFixedThreadPool(5);
        Future<Integer> future = executor.submit(new myCallable());
        try {
            Integer ans = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            String message1 = e.getMessage();
            System.out.println("message1 = " + message1);
            String message = e.getCause().getMessage();
            System.out.println("message = " + message);

        }
    }
}

class myCallable implements Callable<Integer> {
    /**
     * key Java 重写规则
     * 原签名 V call() throws Exception;
     * 这是因为 Java 的覆写规则支持这一行为，在重写方法时，我们可以选择不声明抛出原接口方法中声明的异常。具体规则如下：
     * 1、可以抛出更少的异常：可以不抛出原方法声明的异常。
     * 2、可以抛出原方法声明的异常的子类。
     * 3、不能抛出新的异常或者比原方法更大的异常范围。
     * key 重写方法时跟访问限制符一样都有一定的限制吧
     */
    @Override
    public Integer call(){
//        if (true) throw new RuntimeException();
        int a = 3/0;
        return 123;
    }
}
