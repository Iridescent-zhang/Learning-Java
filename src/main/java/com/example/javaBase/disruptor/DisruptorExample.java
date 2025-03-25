package com.example.javaBase.disruptor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.javaBase.disruptor
 * @ClassName : .java
 * @createTime : 2025/3/24 16:47
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Disruptor是英国外汇交易公司LMAX开发的一个高性能队列，研发的初衷是解决 内存队列 的延迟问题（不是Kafka这样的分布式队列）
 * 先来看一看常用的线程安全的内置队列
 队列	                有界性	            锁	        数据结构
 ArrayBlockingQueue	    bounded	            加锁	        arraylist
 LinkedBlockingQueue	optionally-bounded	加锁      	linkedlist
 ConcurrentLinkedQueue	unbounded	        无锁	        linkedlist   key 这个估计也很重要，用的 CAS
 *  通过不加锁的方式实现的队列都是无界的（无法保证队列的长度在确定的范围内）；而加锁的方式，可以实现有界队列。在稳定性要求特别高的系统中，为了防止生产者速度过快，导致内存溢出，只能选择有界队列；
 *  同时，为了减少Java的垃圾回收对系统性能的影响，会尽量选择array/heap格式的数据结构。这样筛选下来，符合条件的队列就只有ArrayBlockingQueue。
 *  ArrayBlockingQueue在实际使用过程中，会因为加锁和伪共享等出现严重的性能问题
 *
 * Cache是由很多个cache line组成的。每个cache line通常是64字节，并且它有效地引用主内存中的一块儿地址。一个Java的long类型变量是8字节，因此在一个缓存行中可以存8个long类型的变量。
 * CPU每次从主存中拉取数据时，会把相邻的数据也存入同一个cache line。【预读】
 * 在访问一个long数组的时候，如果数组中的一个值被加载到缓存中，它会自动加载另外7个。因此你能非常快的遍历这个数组。事实上，你可以非常快速的遍历在连续内存块中分配的任意数据结构。
 * key  这里有个 CacheLineEffect 例子
 *
 * 什么是 ArrayBlockingQueue 中的伪共享
 * ArrayBlockingQueue 有三个成员变量： - takeIndex：需要被取走的元素下标 - putIndex：可被元素插入的位置的下标 - count：队列中元素的数量
 * 这三个变量很容易放到一个缓存行中，但是之间修改没有太多的关联。所以每次修改，都会使之前缓存的数据失效，从而不能完全达到共享的效果。
 * 当生产者线程put一个元素到ArrayBlockingQueue时，putIndex会修改，从而导致消费者线程的缓存中的缓存行无效，需要从主存中重新读取。
 * 这种无法充分使用缓存行特性的现象，称为伪共享。  key  其实根据小林，就是两个线程同时都经常需要修改的数据被放在了一个缓存行里面
 * 对于伪共享，一般的解决方案是，增大数组元素的间隔使得由不同线程存取的元素位于不同的缓存行上，以空间换时间。
 *
 * 环形数组结构
 * 为了避免垃圾回收，采用数组而非链表。同时，数组对处理器的缓存机制更加友好。
 * 元素位置定位
 * 数组长度2^n，通过位运算，加快定位的速度。下标采取递增的形式。不用担心index溢出的问题。index是long类型，即使100万QPS的处理速度，也需要30万年才能用完。
 * 无锁设计
 * 每个生产者或者消费者线程，会先申请可以操作的元素在数组中的位置，申请到之后，直接在该位置写入或者读取数据。
 *
 * 生产者单线程写数据的流程比较简单：
 * 申请写入m个元素；
 * 若是有m个元素可以入，则返回最大的序列号。这儿主要判断是否会覆盖未读的元素；
 * 若是返回的正确，则生产者开始写入元素。
 *
 * 多个生产者
 * 多个生产者的情况下，会遇到“如何防止多个线程重复写同一个元素”的问题。Disruptor的解决方法是，key  每个线程获取不同的一段数组空间进行操作。这个通过CAS很容易达到。只需要在分配元素的时候，通过CAS判断一下这段空间是否已经分配出去即可。
 * 但是会遇到一个新问题：如何防止读取的时候，读到还未写的元素。
 * Disruptor在多个生产者的情况下，引入了一个与Ring Buffer大小相同的buffer：available Buffer。当某个位置写入成功的时候，便把availble Buffer相应的位置标记为写入成功。读取的时候，会遍历available Buffer，来判断元素是否已经就绪。
 *
 * 读数据
 * 生产者多线程写入的情况会复杂很多：
 * 申请读取到序号n；
 * 从reader cursor开始读取available Buffer，一直查到第一个不可用的元素，然后返回最大连续可读元素的位置；
 * 消费者读取元素。
 * 比如：读线程申请读取到下标从3到11的元素，判断writer cursor>=11。然后开始读取availableBuffer，从3开始，往后读取，发现下标为7的元素没有生产成功，于是WaitFor(11)返回6，然后，消费者读取下标从3到6共计4个元素。
 *
 * 生产者的等待策略
 * 暂时只有休眠1ns。
 * LockSupport.parkNanos(1);
 *
 * 消费者的等待策略
 * BlockingWaitStrategy	    加锁      	CPU资源紧缺，吞吐量和延迟并不重要的场景
 *
 * Log4j 2相对于Log4j 1最大的优势在于多线程并发场景下性能更优。该特性源自于Log4j 2的异步模式采用了Disruptor来处理。 在Log4j 2的配置文件中可以配置WaitStrategy，默认是Timeout策略。
 * 美团在公司内部统一推行日志接入规范，要求必须使用Log4j 2，使普通单机QPS的上限不再只停留在几千，极高地提升了服务性能。
 *
 * 无论是 Disruptor 中的基于数组实现的缓冲区 RingBuffer，还是生产者，消费者，都有各自独立的 Sequence，
 * key  Disruptor 中的 Sequence 也是用的 【前面七个 long，后面七个 long，保证无论怎样两个实例中的关键 value 都不会放在同一个 cache line 里面】
 * Sequence 采用缓存行填充的方式对long类型加了一层包装，用以代表事件的序号，防止不同的 Sequence 之间的CPU缓存伪共享(Flase Sharing)问题。
 *
 * Disruptor在自己的接口里面添加了对于Java 8 Lambda的支持。
 * 大部分 Disruptor 中的接口都符合 Functional Interface 的要求（也就是在接口中仅仅有一个方法）。
 * 所以在 Disruptor 中，可以广泛使用 Lambda 来代替自定义类。
 *
 * key  在构造Disruptor对象，有几个核心的要点：
 * 1：事件工厂(Event Factory)定义了如何实例化事件(Event)，Disruptor 通过 EventFactory 在 RingBuffer 中预创建 Event 的实例。
 * 2：ringBuffer这个数组的大小，一般根据业务指定成2的指数倍。
 * 3：消费者线程池，事件的处理是在构造的线程池里来进行处理的。
 * 4：key  指定等待策略，Disruptor 定义了 com.lmax.disruptor.WaitStrategy 接口用于抽象 Consumer 如何等待Event事件。
 *  BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
 *  SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小【比如生产日志场景】，适合用于异步日志类似的场景；
 *  YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；。
 *
 * Disruptor实现高性能主要体现了去掉了锁，采用 CAS 算法，同时内部通过环形队列实现有界队列。
 * 1、环形数据结构
 * 数组元素不会被回收，避免频繁的 GC，所以，为了避免垃圾回收，采用数组而非链表。
 * 同时，数组对处理器的缓存机制更加友好。
 * 2、元素位置定位
 * 数组长度 2^n，通过位运算，加快定位的速度。
 * 3、下标采取递增的形式。不用担心 index 溢出的问题。
 * index是long类型，即使100万QPS的处理速度，也需要30万年才能用完。【应该是用索引对数组大小取模】
 * 4、无锁设计
 * key  采用 CAS 无锁方式，保证线程的安全性
 * 每个生产者或者消费者线程，会先申请可以操作的元素在数组中的位置，申请到之后，直接在该位置写入或者读取数据。整个过程通过原子变量 CAS，保证操作的线程安全。
 * 5、属性填充：
 * 通过添加额外的无用信息，避免伪共享问题
 *
 * Disruptor和BlockingQueue比较:
 * BlockingQueue: FIFO队列.生产者Producer向队列中发布publish一个事件时,消费者Consumer能够获取到通知.如果队列中没有消费的事件,消费者就会被阻塞,直到生产者发布新的事件
 * Disruptor可以比BlockingQueue做到更多:
 * Disruptor队列中同一个事件可以有多个消费者【发布订阅】,消费者之间既可以并行处理,也可以形成依赖图相互依赖,按照先后次序进行处理
 * Disruptor可以预分配用于存储事件内容的内存空间
 * Disruptor使用极度优化和无锁的设计实现极高性能的目标
 * key 如果你的项目有对性能要求高，对延迟要求低的需求，并且需要一个无锁的有界队列，来实现生产者/消费者模式，那么Disruptor是你的不二选择。
 *
 * Disruptor使用环形队列的优势：
 * Disruptor框架是一个使用 CAS 操作的内存队列，与普通的队列不同，是基于数组实现的环形队列，无论是生产者向缓冲区里提交任务，还是消费者从缓冲区里获取任务执行，都使用 CAS 操作。
 * 为什么用环形的数组作为队列？ 1、 因为创建普通双端队列时使用两个指针head和tail来管理这个队列，head和tail指针变量常常在同一个缓存行中，多线程修改同一缓存行中的变量就容易出现伪共享问题。
 *          而环形队列的一个特点就是只有一个指针，只通过一个指针来实现出列和入列操作。
 *          2、数组不像链表，Java会定期回收链表中一些不再引用的对象，而数组不会出现空间的新分配和回收问题，减少了系统对内存空间管理的压力。
 *
 * Disruptor 它可以用来作为高性能的有界内存队列， 适用于两大场景：
 * 生产者消费者场景：最常用的场景就是“生产者-消费者”场景，对场景的就是“一个生产者、多个消费者”的场景，并且要求顺序处理。
 * 发布订阅 场景：Disruptor也可以认为是观察者模式的一种实现， 实现发布订阅模式。
 */

// 定义事件
class LongEvent {
    private long value;

    public void setValue(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}

// 定义事件工厂
class LongEventFactory implements EventFactory<LongEvent> {
    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}

// 定义事件处理器
class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println("Event: " + event.getValue());
    }
}

// 定义生产者
class LongEventProducer {

    //一个 translator 可以看做一个事件初始化器，ringBuffer.publishEvent 方法会调用它
    private static final EventTranslatorOneArg<LongEvent, Long> TRANSLATOR = new EventTranslatorOneArg<LongEvent, Long>() {
                public void translateTo(LongEvent event, long sequence, Long data) {
                    event.setValue(data);
                }
            };

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 如何产生事件，然后发出事件呢？
     * onData 用来发布事件，每调用一次就发布一次事件
     * 通过从 环形队列中 获取 序号， 通过序号获取 对应的 事件对象， 将数据填充到 事件对象，再通过 序号将 事件对象 发布出去。
     */
    public void onData(ByteBuffer bb) {
        long sequence = ringBuffer.next(); // 获取下一个可用位置的序号，可以把 ringBuffer 看做一个事件队列，那么 next 就是得到下面一个事件槽
        try {
            // key  事件工厂(Event Factory)定义了如何实例化空事件(Event)，Disruptor 通过 EventFactory 在 RingBuffer 中预创建 Event 的实例。
            LongEvent event = ringBuffer.get(sequence); // 获取该序号对应的 空事件对象，用上面的索引，取出一个空的事件用于填充
            event.setValue(bb.getLong(0)); // 填充事件数据
        } finally {
            // 要使用 try/finnally 保证事件一定会被发布，如果不能发布事件，那么就会引起 Disruptor 状态的混乱，尤其是在多个事件生产者的情况下会导致事件消费者失速，从而不得不重启应用才能会恢复。
            ringBuffer.publish(sequence); // 通过 序号将 事件对象 发布出去。
//            ringBuffer.publishEvent(TRANSLATOR, data);   // Disruptor3.0以后 , 提供了事件转换器， 帮助填充 LongEvent 的业务数据
        }
    }
}

// 使用 Disruptor 来设置生产者和消费者
public class DisruptorExample {
    public static void main(String[] args) {
        // key  消费者线程池，事件的处理是在构造的线程池里来进行处理的。
        ExecutorService executor = Executors.newCachedThreadPool();
        EventFactory<LongEvent> factory = new LongEventFactory();
        int bufferSize = 1024; // 指定 Ring Buffer 大小，一般根据业务指定成2的指数倍。

        // key  指定等待策略，Disruptor 定义了 com.lmax.disruptor.WaitStrategy 接口用于抽象 Consumer 如何等待Event事件。

        // 创建 Disruptor，分裂者（事件分发者）
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, executor);

        // 连接事件处理器（消费者 处理器）
        disruptor.handleEventsWith(new LongEventHandler());

        // 启动 Disruptor，开启 分裂者（事件分发）
        disruptor.start();

        // 获取 Ring Buffer
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventProducer producer = new LongEventProducer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; l < 100; l++) {
            bb.putLong(0, l);
            producer.onData(bb);
        }

        disruptor.shutdown();
        executor.shutdown();
    }
}

class CacheLineEffect {
    //考虑一般缓存行大小是64字节，一个 long 类型占8字节
    static  long[][] arr;

    public static void main(String[] args) {
        arr = new long[1024 * 1024][];
        for (int i = 0; i < 1024 * 1024; i++) {
            arr[i] = new long[8];
            for (int j = 0; j < 8; j++) {
                arr[i][j] = 0L;
            }
        }
        long sum = 0L;
        long marked = System.currentTimeMillis();
        for (int i = 0; i < 1024 * 1024; i+=1) {
            for(int j =0; j< 8;j++){
                sum = arr[i][j];
            }
        }
        System.out.println("Loop times:" + (System.currentTimeMillis() - marked) + "ms");

        marked = System.currentTimeMillis();
        for (int i = 0; i < 8; i+=1) {
            for(int j =0; j< 1024 * 1024;j++){
                sum = arr[j][i];
            }
        }
        System.out.println("Loop times:" + (System.currentTimeMillis() - marked) + "ms");
    }
}

/**
 * 在2G Hz，2核，8G内存, jdk 1.7.0_45 的运行环境下，使用了共享机制比没有使用共享机制，速度快了4倍左右。
 * 有专门的注解 @Contended（抗衡） 来避免伪共享，更优雅地解决问题。
 */
class FalseSharing implements Runnable{
    public final static long ITERATIONS = 500L * 1000L * 100L;
    private int arrayIndex = 0;

    private static ValuePadding[] longs;
    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(final String[] args) throws Exception {
        for(int i=1;i<10;i++){
            System.gc();
            final long start = System.currentTimeMillis();
            runTest(i);
            System.out.println("Thread num "+i+" duration = " + (System.currentTimeMillis() - start));
        }
    }

    private static void runTest(int NUM_THREADS) throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        longs = new ValuePadding[NUM_THREADS];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new ValuePadding();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
    }

    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = 0L;
        }
    }

    /**
     * 避免不同线程竞争访问相邻内存位置导致伪共享。这些多余的变量确保 value 不与其他缓存行中的变量共享相同的缓存行。
     * 前面七个 long，后面七个 long，保证无论怎样两个实例中的关键 value 都不会放在同一个 cache line 里面
     */
    public final static class ValuePadding {
        protected long p1, p2, p3, p4, p5, p6, p7;
        protected volatile long value = 0L;
        protected long p9, p10, p11, p12, p13, p14, p15;
    }

    /**
     * 没用 共享机制，无 padding 填充
     */
    public final static class ValueNoPadding {
        // protected long p1, p2, p3, p4, p5, p6, p7;
        protected volatile long value = 0L;
        // protected long p9, p10, p11, p12, p13, p14, p15;
    }
}