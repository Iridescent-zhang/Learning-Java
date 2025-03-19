package com.example.javaBase.juc;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.javaBase.juc
 * @ClassName : .java
 * @createTime : 2025/3/18 21:06
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import java.util.concurrent.atomic.LongAdder;

/**
 * LongAdder 是 Java 并发库中的一个类，用于高效地进行计数操作以替代 AtomicLong，特别是在高并发的情况下。
 * 它通过分段锁和分段计数的方式减少竞争，提供更高的吞吐量。
 *
 * key LongAdder 内部使用了一个 Cell 数组，每一个 Cell 都包含一个独立的计数器。
 * 通过分段计数，多个线程可以同时操作不同的 Cell，减少了竞争。
 * 如果某个 Cell 被锁定，线程将尝试操作另一个 Cell。
 *
 * 分段锁
 * 启动时 LongAdder 初始化一个基本计数器和一个 Cell 数组。
 * 当并发线程增加到某个阈值时，将会动态分配 Cell 数组（或者扩展数组），每个 Cell 内部都持有自己的计数器。
 * 每个 Cell 通过 CAS 操作（Compare-And-Swap）独立增量。
 * 数组的长度和位置确定由哈希来计算，使得多线程尽可能地操作不同的 Cell。
 *
 * 计数增加
 * 当线程执行 add 操作时：
 * 首先尝试更新基本的计数器，如果失败 key 则尝试更新 Cell 数组中与当前线程哈希值相关联的 Cell。
 * key 如果 Cell 数组未初始化或所有 Cell 都被锁定，将尝试重新初始化或扩展 Cell 数组。
 *
 * 求和
 * sum 操作时：
 * 所有 Cell 及基本计数器的值都将求和以提供最终的计数结果。
 *
 * Striped64：class LongAdder extends Striped64
 * 分散热点，将value值分散到一个数组中，不同线程会命中到数组的不同槽中，各个线程只对自己槽中的那个值进行CAS操作，这样热点就被分散了，冲突的概率就小很多。
 * 如果要获取真正的long值，只要将各个槽中的变量值累加返回。
 */
public class LongAdderExample {
    public static void main(String[] args) {
        LongAdder longAdder = new LongAdder();
        for(int i = 0; i<10; i++) {
            Thread thread = new Thread(()->{
                for(int j = 0; j<50; j++) {
                    longAdder.increment();
//                    longAdder.add(1);
                }
            });
            thread.start();
        }
        System.out.println("longAdder = " + longAdder);
    }
}
