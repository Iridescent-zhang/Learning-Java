package com.example.interview.javaBase.jmm;
import java.util.concurrent.*;
/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.jmm
 * @ClassName : .java
 * @createTime : 2025/2/26 22:52
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * key volatile 关键字确保变量的可见性和有序性。
 * 1、可见性：对 volatile 的写会直接写入主存，读也是从主存中读，所以一个线程对 volatile 变量的写对其他线程来说立即可见
 * 2、有序性：禁止 CPU 对 volatile 变量的相关指令重排序优化
 *      这确保了前面对 volatile 变量的写操作不会被重排序到对 volatile 变量的(读或写)操作之后【写写屏障、写读屏障】
 *      前面对 volatile 变量的读操作也不会被重排序到 volatile 变量操作之后。？这句存疑
 *
 * key 一个线程对 volatile 变量的写操作 happen-before 随后的对同一个 volatile 变量的读操作。【这里的所谓先后是 CPU 调度时决定的，总的意思是调度确定后的顺序不允许变化了】
 * Java 内存模型（Java Memory Model，JMM）保证：如果线程 A 对 v 的写操作 happen-before（调度时决定 A 先于 B） 线程 B 对 v 的读操作，那么线程 B 看到的是线程 A 写入的最新值。（我不让你重排序，那你一定只能看到新值）
 *
 * 线程调度：实际的先读还是先写，由操作系统的线程调度决定，并非编程语言本身控制。因此，我们无法 100% 预言哪个会先发生。
 * key 所以 volatile 变量的规则是一种补充，防止你明明调度了写先执行，你还重排序了读先执行，打乱了我的预期
 * 规则保证：volatile 变量的规则保证，即便在两个线程同时 start 的情况下，如果确实某个线程的写 happen-before 另一个线程的读，那么如下：
 *      1.线程 A 写操作对 v 的值，新值对线程 B 立即可见。
 *      2.按 JMM 定义，这种变量操作是不会受到重排序影响的。
 *
 * 如果没有 volatile：
 * 可见性问题：一个线程对变量的修改，可能对另一个线程不可见。key 具体是因为每个线程可能有自己的缓存或寄存器会保留该变量的副本，而不一定去读取主内存的值。即使变量是静态的，没有 volatile 修饰，仍然不能保证即时可见。
 *  缓存一致性问题：多个线程同时操作的情况下，线程之间可能看不到彼此的变化，因为缓存一致性协议没有强制刷新主内存中共享变量的值。
 *
 * key volatile 是一种轻量级的同步机制。
 *
 * 在多线程编程中，依赖正确的同步原语，如 volatile 关键字、同步块或并发包中的类（如 AtomicBoolean、ReentrantLock 等），才能确保线程安全和数据一致性。这样可以避免潜在的不确定性和隐患。
 *
 * key Java 内存模型（JMM）在底层实现上使用内存屏障来保证有序性。volatile 各类操作在内存屏障上的反应如下：
 * JMM 内存屏障主要分 4 种：
 *  写之后的读屏障（StoreLoad Barrier）：
 *  通常插在两次不同类型操作之间，比如写后再读。确保写操作的影响被其他处理器看到，然后才允许后续操作继续。
 *  读之后的读屏障（LoadLoad Barrier）：
 *  确保一个 load 操作在同样的线程中的所有先前的读操作在它之前完成。
 *  写之后的写屏障（StoreStore Barrier）：
 *  确保在一个线程中的所有先前写操作在一个新的写操作之前完成。
 *  读之后的写屏障（LoadStore Barrier）：
 *  确保前一个 load 操作在同一个线程中的写操作前完成。
 * key 这里 读写 volatile 变量在前，其他所有读写操作在后（这里的后操作是对所有变量的读写，不只是针对 volatile 变量的）
 */
public class VolatileExample {
    private static volatile boolean flag = false;
    volatile static int a;

    public static void main(String[] args) throws InterruptedException {
        Thread writerThread = new Thread(() -> {
            flag = true;
            System.out.println("Writer Thread sets flag to true");
        });

        Thread readerThread = new Thread(() -> {
            while (!flag) {
                // busy-wait
                System.out.println("busy-wait");
            }
            System.out.println("Reader Thread sees flag as true");
        });
        readerThread.start();
        writerThread.start();

        writerThread.join();
        readerThread.join();

        System.out.println("-----------------------");

        int b;
        /**
         * 写之后写屏障（StoreStore Barrier）
         * 写 a 后，再写 b。确保写入的顺序不会颠倒，写 a 的结果在写 b 之前可见。
         */
        a = 1;               // Store
        b = 1;               // Store

        /**
         * 写之后读屏障（StoreLoad Barrier）
         * 写入 volatile 变量 a 后，需要确保写操作已经完成并被所有线程可见，读操作的结果才可靠。所以此处会插入 StoreLoad Barrier。
         */
        // 写 `a` 之后，需要一个 `StoreLoad Barrier`
        a = 1;               // Store
        b = a + 1;           // Load

        /**
         *  读之后读屏障（LoadLoad Barrier）
         *  读 a 后，再读 b。确保读 a 在读 b 之前完成并且对所有线程可见。
         */
        int x = a;           // Load
        int y = b;           // Load

        /**
         * 读之后写屏障（LoadStore Barrier）
         * 读 a 后，再写 b。确保读 a 的结果在写 b 之前可见，不存在因重排序影响的情况。
         */
        x = a;               // Load
        b = 1;               // Store

    }
}
