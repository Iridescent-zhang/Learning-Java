package com.example.interview.javaBase.jvm;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.jvm
 * @ClassName : .java
 * @createTime : 2025/3/6 18:26
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * key jvm 命令 查看内存、查看线程状态？
 *
 * jps命令用于展示当前 JVM 进程的 PID 及其启动类信息。比如每个在跑的 jar 包都是一个 JVM 进程
 * jps -l
 *
 * jstat JVM 统计监视工具，可以监视 GC 及内存使用情况。命令可以查看 JVM 内存各种统计信息。
 * jstat -gc <pid> 1000    # 查看堆内存使用情况    其中 <pid> 是 JVM 所在程序的进程 ID，1000 表示每秒刷新一次。
 *
 * jstack 命令用于查看所有线程的线程栈信息，一般用于排查死锁问题、长时间等待、过多线程等问题，但同样可以看到线程内存的使用情况。
 * jstack -l 4940 > thread_dump.txt
 *
 * key jcmd是一个多功能的工具，可以获取各种信息。
 * # 查看所有JVM进程
 * jcmd
 *
 * # 查看垃圾回收信息
 * jcmd 4940 GC.run
 *
 * # 查看系统属性
 * jcmd 4940 VM.system_properties
 *
 * # 查看系统的总体性能指标
 * jcmd 4940 VM.system_properties
 *
 * ------------------------------------
 *
 * -XX:ThreadStackSize 或简写 -Xss 用于设置每个 Java 线程的栈大小。这个参数定义了线程栈的内存空间，根据你的实际情况来调整这个值。
 * java -Xss512k -jar yourapp.jar
 *
 * jconsole：Java 监视和管理控制台，是一个可视化工具，用于监视和管理 Java 应用程序。可以查看线程状况。
 * jvisualvm：同样，VisualVM 不仅用于内存监控，也可用于线程分析，能够直观地看到各个线程的状态和活动情况。
 *
 * key 先记住：
 * jps    查看当前所有 Java 进程
 * jstat -gc pid      查看该进程的内存使用情况
 * jstack -l pid      查看该线程状态
 *
 * --------------------------------------------------------
 *
 * jstack [option] <pid>：打印指定进程 ID（PID）的 Java 线程栈信息。
 *   -l：key 显示锁的附加信息，如果线程拥有或者等待某个锁，会显示出具体的锁对象信息。
 * 使用这个命令我们一般关注的部分信息：
 * 1、线程 ID 和状态：包括线程的名字、优先级、key 线程状态（如 RUNNABLE, BLOCKED, WAITING 等）。一般第一行就是这些
 * 2、key 线程栈跟踪：包括当前线程执行的代码行。  然后一大堆栈信息就是这个
 * 3、key 锁信息（可选）：如果指定了 -l 选项，显示线程持有的锁和等待的锁。对于排查死锁、线程阻塞等问题特别有用。
 * 看看 ENDB 的 main 线程，很清楚：
 *     java.lang.Thread.State: RUNNABLE
 *     表示线程 key 正在执行 socket 的 accept 操作，也就是说该线程处于等待新连接的状态，堆栈信息中的各个方法和行号提供了程序中的调用路径，可以帮助定位代码中的问题。
 *     at sun.nio.ch.Net.accept(java.base@17.0.13/Native Method)
 *     at sun.nio.ch.NioSocketImpl.accept(java.base@17.0.13/NioSocketImpl.java:760)
 *
 * --------------------------------------------------------
 * jstat -gc 命令输出详解
 * 各列的数据代表不同的 JVM 堆和垃圾回收相关指标，详细解释如下：
 * S0C (Survivor space 0 capacity): Survivor 区 0 的容量（单位为 KB）。
 * S1C (Survivor space 1 capacity): Survivor 区 1 的容量（单位为 KB）。
 * S0U (Survivor space 0 utilization): Survivor 区 0 的使用量（单位为 KB）。
 * S1U (Survivor space 1 utilization): Survivor 区 1 的使用量（单位为 KB）。
 * EC (Eden space capacity): Eden 区的容量（单位为 KB）。
 * EU (Eden space utilization): Eden 区的使用量（单位为 KB）。
 * OC (Old space capacity): Old 区（老年代）的容量（单位为 KB）。
 * OU (Old space utilization): Old 区（老年代）的使用量（单位为 KB）。
 * MC (Metaspace capacity): Metaspace（元空间）的容量（单位为 KB）。
 * MU (Metaspace utilization): Metaspace（元空间）的使用量（单位为 KB）。
 * CCSC (Compressed class space capacity): 压缩类空间的容量（单位为 KB）。
 * CCSU (Compressed class space utilization): 压缩类空间的使用量（单位为 KB）。
 * YGC (Number of young generation GC events): Young 代（新生代）GC 发生的次数。
 * YGCT (Young generation garbage collection time): Young 代 GC 消耗的时间（单位为秒）。
 * FGC (Number of full GC events): Full GC 发生的次数。
 * FGCT (Full garbage collection time): Full GC 消耗的时间（单位为秒）。
 * CGC (Number of concurrent GC events): 并发 GC 发生的次数。
 * CGCT (Concurrent garbage collection time): 并发 GC 消耗的时间（单位为秒）。
 * GCT (Total garbage collection time): GC 总消耗时间（单位为秒）。
 *
 * ---------------------
 *
 * key Jmap 是 JDK 自带的堆分析工具 Java Memory Map，可以通过此工具打印出某个 Java 进程内存内的所有对象大小和数量；
 * 建议在测试环境中使用 jmap -histo:live 命令查询，执行此命令会触发一次Full GC
 * jmap -histo:live 20881 | grep QueryPartnerImpl
 * 1354:             2             80  com.meituan.trip.mobile.hermes.sal.meilv.impl.QueryPartnerImpl
 *
 * 配合 Jhat 是JDK自带的堆分析工具Java Heap Analyse Tool，可以将堆中的对象以HTML的形式显示出来，包括对象的数量、大小等，默认端口7000。
 *
 * 笔者在项目中添加 -XX:NativeMemoryTracking=detail   JVM参数重启项目，使用命令jcmd pid VM.native_memory detail查看到的内存分布如下：
 *
 *
 *
 * -------------------
 *
 * 内存泄漏通常是由于某些对象无法被垃圾回收，导致堆内存不断增长。通过分析堆内存（Heap Dump），可以找到内存泄漏的根源。
 * 生成堆内存快照：
 *      jmap -dump:live,format=b,file=heap_dump.hprof <pid>
 *      live 存活对象   format是二进制
 * 主要关注的信息：
 * Histogram 直方图：查看哪些类的实例数量异常多。
 * Dominator Tree：查看哪些对象占用了最多的内存。
 * 引用链（Reference Chain）：查看这些对象是如何被引用的，找到无法被回收的原因。
 * GC Roots：查看对象的根引用链，找出哪些对象无法被回收。
 *
 * 假设发现 com.example.MyClass 的实例数量异常多，可以通过以下步骤分析：
 * 在 Eclipse MAT 中打开 heapdump.hprof。
 * 查看 Histogram，找到 com.example.MyClass。
 * 查看 Dominator Tree，找出这个类的哪些 MyClass 实例占用了大量内存。
 * 查看 引用链，找出这些实例的根引用链，确定为什么无法被回收。
 */

/**
 * GC
 * 在 JDK 17 中，默认的垃圾收集器是 G1（Garbage-First）。G1 是一种面向低延迟的垃圾收集器，适合大内存和多核 CPU 的场景。
 * java -XX:+UseG1GC -jar your-application.jar     -XX:+UseG1GC：显式启用 G1 垃圾收集器。
 *  G1 的其他常用参数
 * -XX:MaxGCPauseMillis=200：设置最大 GC 停顿时间（默认 200ms）。【最大 GC 暂停时间】
 * -XX:G1HeapRegionSize=32m：设置 G1 的堆区域大小。            【应该是 G1 堆进行分区的大小】
 * -XX:InitiatingHeapOccupancyPercent=45：设置触发并发 GC 的堆占用百分比。       【初始化堆占用百分比：应该是到达这个百分比之后会进行并发 GC】
 */
public class jvmCmdExample {
    public static void main(String[] args) {
    }
}
