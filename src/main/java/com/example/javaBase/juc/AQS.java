package com.example.javaBase.juc;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.juc
 * @ClassName : .java
 * @createTime : 2025/3/11 13:19
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * AQS核心思想是，如果被请求的共享资源空闲，那么就将当前请求资源的线程设置为有效的工作线程（就是对这个资源来说，是当前工作线程），将共享资源设置为锁定状态；
 * 如果共享资源被占用，就需要一定的阻塞等待唤醒机制来保证锁分配。这个机制主要用的是CLH队列的变体实现的，将暂时获取不到锁的线程加入到队列中。
 * CLH：Craig、Landin and Hagersten队列，是单向链表，AQS中的队列是CLH变体的虚拟双向队列（FIFO），AQS是通过将每条请求共享资源的线程封装成一个节点来实现锁的分配。
 * AQS使用一个 Volatile 的 int 类型的成员变量【state】来表示同步状态，通过内置的 FIFO 队列来完成资源获取的排队工作，通过CAS完成对State值的修改。
 *
 * key 总的来说，AQS框架共分为五层，自上而下由浅入深，从AQS对外暴露的API到底层基础数据。
 * 当有自定义同步器接入时，只需重写第一层所需要的部分方法即可，不需要关注底层具体的实现流程。
 *
 * AQS中最基本的数据结构——Node，Node即为上面CLH变体队列中的节点。
 *      waitStatus	当前节点在队列中的状态： 比如 CANCELLED	为1，表示线程获取锁的请求已经取消了 ；CONDITION	为-2，表示节点在等待队列中，节点线程等待唤醒
 *      thread	表示处于该节点的线程
 * 接下来了解一下AQS的同步状态——State。AQS中维护了一个名为state的字段，意为同步状态，是由Volatile修饰的，用于展示当前临界资源的获锁情况。
 *      getState()	获取State的值，key 独占模式 state 只有 0（没被占有） 和 大于0（被重入的次数），而共享模式 state=n，每被拿一次锁就自减，state>0表示能拿到
 *      compareAndSetState(int expect, int update)	使用CAS方式更新State
 *
 * AQS提供了大量用于自定义同步器实现的Protected方法。自定义同步器实现的相关方法也只是为了通过修改 State 字段来实现多线程的独占模式或者共享模式。自定义同步器需要实现以下方法：
 *   独占：   tryAcquire(int arg)	独占方式。arg为获取锁的次数，尝试获取资源，成功则返回True，失败则返回False。
 *           tryRelease(int arg)	独占方式。arg为释放锁的次数，尝试释放资源，成功则返回True，失败则返回False。
 *   共享：   int tryAcquireShared(int arg)	共享方式。arg为获取锁的次数，尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
 *           boolean tryReleaseShared(int arg)	共享方式。arg为释放锁的次数，尝试释放资源，如果释放后允许唤醒后续等待结点返回True，否则返回False。 key 所谓共享是能被同时持有总共 n 次
 * AQS也支持自定义同步器同时实现独占和共享两种方式，如ReentrantReadWriteLock。
 * 而 ReentrantLock 是独占锁，所以实现了 tryAcquire-tryRelease。
 *
 * 加锁：
 * 通过ReentrantLock的加锁方法Lock进行加锁操作。
 * 会调用到内部类Sync的Lock方法，而它根据ReentrantLock初始化选择的公平锁和非公平锁，执行相关内部类的Lock方法，本质上都会执行AQS的Acquire方法。
 * AQS的Acquire方法会执行tryAcquire方法，但是由于tryAcquire需要自定义同步器实现，因此执行了 ReentrantLock 中的 tryAcquire 方法，由于ReentrantLock是通过公平锁和非公平锁内部类实现的tryAcquire方法，因此会根据锁类型不同，执行不同的tryAcquire。
 * tryAcquire 是获取锁逻辑，key 获取失败后，会执行框架AQS的后续逻辑，跟ReentrantLock自定义同步器无关。
 *
 * 解锁：
 * 通过ReentrantLock的解锁方法Unlock进行解锁。
 * Unlock会调用内部类Sync的Release方法，该方法继承于AQS。
 * Release中会调用tryRelease方法，tryRelease需要自定义同步器实现，tryRelease只在 ReentrantLock 中的 Sync 实现，因此可以看出，释放锁的过程，并不区分是否为公平锁。
 * 释放成功后，所有处理由AQS框架完成，与自定义同步器无关。
 *
 * 所以 ReentrantLock 暴露给用户 lock unLock 方法，而内部是自定义了同步器实现方法 tryAcquire tryRelease
 *
 * ---------
 * 前面说了，如果获取锁失败，就会调用addWaiter加入到等待队列中去。这些是框架 AQS 的后续逻辑，跟ReentrantLock自定义同步器无关。
 * 线程加入等待队列
 *
 * Q：某个线程获取锁失败的后续流程是什么呢？
 * A：存在某种排队等候机制，线程继续等待，仍然保留获取锁的可能，获取锁流程仍在继续。
 *
 * Q：既然说到了排队等候机制，那么就一定会有某种队列形成，这样的队列是什么数据结构呢？
 * A：是CLH变体的FIFO双端队列。
 *
 * Q：处于排队等候机制中的线程，什么时候可以有机会获取锁呢？
 * A：可以详细看下2.3.1.3小节。
 *
 * Q：如果处于排队等候机制中的线程一直无法获取锁，需要一直等待么？还是有别的策略来解决这一问题？
 * A：线程所在节点的状态会变成取消状态，取消状态的节点会从队列中释放，具体可见2.3.2小节。
 *
 * Q：Lock函数通过Acquire方法进行加锁，但是具体是如何加锁的呢？
 * A：AQS的Acquire会调用tryAcquire方法，tryAcquire由各个自定义同步器实现，通过tryAcquire完成加锁过程。
 *
 * -------------------------
 * ReentrantLock的可重入性是AQS很好的应用之一
 * 从上面这两段都可以看到，有一个同步状态State来控制整体可重入的情况。State是Volatile修饰的，用于保证一定的可见性和有序性。
 * 接下来看State这个字段主要的过程：
 *  State初始化的时候为0，表示没有任何线程持有锁。
 *  当有线程持有该锁时，值就会在原来的基础上+1，同一个线程多次获得锁是，就会多次+1，这里就是可重入的概念。
 *  解锁也是对这个字段-1，一直到0，此线程对锁释放。
 *
 * 除了上边ReentrantLock的可重入性的应用，AQS作为并发编程的框架，为很多其他同步工具提供了良好的解决方案。
 * 同步工具	                同步工具与AQS的关联
 * ReentrantLock	        使用AQS保存锁重复持有的次数。当一个线程获取锁时，ReentrantLock记录当前获得锁的线程标识，用于检测是否重复获取，以及错误线程试图解锁操作时异常情况的处理。
 * Semaphore	            使用AQS同步状态来保存信号量的当前计数。tryRelease会增加计数，acquireShared会减少计数。
 * CountDownLatch       	使用AQS同步状态来表示计数。计数为0时，所有的Acquire操作（CountDownLatch的await方法）才可以通过。
 * ReentrantReadWriteLock	key 使用AQS同步状态中的16位保存写锁持有的次数，剩下的16位用于保存读锁的持有次数。
 * ThreadPoolExecutor	    key Worker（线程池中的工作线程）利用 AQS 同步状态实现对独占线程变量的设置（tryAcquire和tryRelease）。线程池中的线程是独占的，这是靠 AQS 实现的
 */
public class AQS {
}
