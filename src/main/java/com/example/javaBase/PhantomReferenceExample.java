package com.example.javaBase;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase
 * @ClassName : .java
 * @createTime : 2025/3/11 17:30
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * 虚引用是 Java 四种引用类型中最弱的一种，主要用于跟踪对象被垃圾回收的状态，通常与 ReferenceQueue 结合使用。
 *
 * 1、虚引用的特点
 * 虚引用无法通过 get() 方法获取对象（总是返回 null）。
 * key 虚引用主要用于对象被回收时的通知机制。
 * 虚引用必须与 ReferenceQueue 结合使用。
 * 2. 虚引用的使用场景
 * 通过虚引用，我们可以追踪对象的销毁过程，并在对象即将被回收时进行一些必要的清理工作，比如释放资源、记录日志等。典型的应用场景包括内存管理和对象销毁监控。
 *
 * 当对象被垃圾回收时，虚引用会被加入 ReferenceQueue。
 */
public class PhantomReferenceExample {
    public static void main(String[] args) {
        Object obj = new Object();
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        PhantomReference<Object> phantomRef = new PhantomReference<>(obj, queue);

        // 虚引用的 get() 方法总是返回 null
        System.out.println("Phantom reference get: " + phantomRef.get());

        // 清除强引用
        obj = null;

        // 触发 GC
        System.gc();

        // 检查 ReferenceQueue 是否有虚引用
        PhantomReference<?> ref = (PhantomReference<?>) queue.poll();
        if (ref != null) {
            // 当对象被垃圾回收时，虚引用会被加入 ReferenceQueue，所以 poll 出来不为 null
            System.out.println("Object has been garbage collected");
        } else {
            System.out.println("Object is still alive");
        }
    }
}
