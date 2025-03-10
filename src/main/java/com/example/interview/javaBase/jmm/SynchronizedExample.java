package com.example.interview.javaBase.jmm;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.jmm
 * @ClassName : .java
 * @createTime : 2025/2/26 22:48
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * key 为什么叫监视器锁？
 * synchronized 在 Java 中被称为监视器锁（Monitor Lock），这是因为它依赖一个称为监视器的机制，来保证在同一时刻只有一个线程可以执行同步代码块中的代码。这个名称来源于早期计算机科学中的 “监视器” 概念，它用来管理并发访问共享数据。
 * synchronized 用于方法或者代码块时，JVM 会采用一种基于对象监视器（Monitor）管控进入和退出同步代码的策略，这些都是通过对象头（Object Header）和 Monitor 来实现的。
 *
 * 对象头（Object Header）
 * 在 Java 中，每个对象在堆内存中都有一个对象头，包含了对象的元数据。对象头主要由两部分组成：
 *  Mark Word（标记字段）：它存储了对象的哈希码、GC 信息、锁标志位等。 key 当对象被锁定时，Mark Word 会存储锁信息。锁标志位：是否是偏向锁
 *  Class Metadata Address（类型指针）：指向对象的类元数据。 key 也很关键
 *
 * synchronized 的底层机制涉及到三种锁状态和升级过程：
 * 无锁状态（Unlocked）     初始状态下，所有对象都是无锁状态。在这个状态下，Mark Word 存储了对象的哈希码等信息。
 * 偏向锁状态（Biased Locking）   当一个线程第一次进入 synchronized 块时，Java 会尝试给这个线程分配 "偏向锁"。偏向锁的意思是说该锁会偏向于最先获得它的线程，直到另一个线程尝试获取它时才会撤销偏向锁。
 *          key 在偏向锁状态下，Mark Word 会记录持有这个锁的线程 ID。
 * 轻量级锁状态（Lightweight Locking）      key 依赖于线程堆栈中的锁记录（Lock Record）和对象头中的 Mark Word。当一个线程试图获取轻量级锁时，会将 Mark Word 复制到锁记录中，并使用 CAS 操作尝试更新对象头的 Mark Word 以指向锁记录。
 *          若成功，则持有轻量级锁。该机制主要用于解决大多数情况下的锁竞争。
 * 重量级锁状态（Heavyweight Locking）      key 此时，锁信息将存储在对象监视器（Monitor）中，操作系统会不断挂起和恢复阻塞的线程。重量级锁主要用于应对大量并发竞争的情况。
 *
 * 偏向锁：Mark Word 存储了持有锁的线程 ID。
 * 轻量级锁：Mark Word 在 CAS 更新操作中指向线程堆栈中的 key 锁记录。锁记录通常是指在多线程程序运行过程中，每个线程正在等待或已经持有的锁的信息。拿不到锁线程会自旋（因为轻量）
 *      虚拟机首先将在当前线程的栈帧中建立一个名为锁记录（Lock Record）的空间，用于存储锁对象目前的Mark Word的拷贝，然后拷贝锁对象头中的Mark Word复制到锁记录中。
 *      拷贝成功后，虚拟机将使用CAS操作尝试将对象的Mark Word更新为指向Lock Record的指针，并将Lock Record里的owner指针指向对象的Mark Word。
 *      如果这个更新动作成功了，那么这个线程就拥有了该对象的锁，并且对象Mark Word的锁标志位设置为“00”，表示此对象处于轻量级锁定状态。
 * 重量级锁：Mark Word 指向一个互斥锁（Mutex Lock），该互斥锁由操作系统管理。（系统级别锁） key  拿不到锁线程阻塞
 *
 * key Monitor是线程私有的数据结构，每一个线程都有一个可用monitor record列表，同时还有一个全局的可用列表。每一个被锁住的对象都会和一个monitor关联，同时monitor中有一个Owner字段存放拥有该锁的线程的唯一标识，表示该锁被这个线程占用。
 * synchronized通过Monitor来实现线程同步，Monitor是依赖于底层的操作系统的Mutex Lock（互斥锁）来实现的线程同步。
 *
 * Java 中的每一个对象都可以用作一把锁，这种锁称为 Monitor 锁（即对象锁）。
 */
public class SynchronizedExample {

    public static int counter = 0;
    public void increment() {
        // 这里的synchronized(this)表示使用当前对象实例作为锁，同步该代码块。
        synchronized(this) {
            // 这段代码被锁保护
            counter++;
        }
    }

    // 当一个线程进入 increment 方法时，会自动获取对象实例的 Monitor 锁。在方法执行完毕或者抛出异常而退出时，释放该锁。
    public synchronized void synIncrement() {
        // 这段代码被锁保护
        counter++;
    }
}
