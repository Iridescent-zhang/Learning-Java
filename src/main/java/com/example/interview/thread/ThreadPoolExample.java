package com.example.interview.thread;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.thread
 * @ClassName : .java
 * @createTime : 2025/3/5 23:45
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 线程池是 key 对象池设计模式的一种实现。对象池持有一组预留的对象，这些对象可以用于需要时的快速反复利用，即借用 - 归还模型。
 * 生产者和消费者模式通过阻塞队列可以实现解耦。生产者将数据放入队列中，消费者从队列中取数据进行处理，不需要直接交互。
 *
 * key Executors 是使用线程池很重要的工具类，创建常见线程池和线程工厂都要使用它
 * key ThreadPoolExecutor 是创建自定义线程池的关键类，那些拒绝策略就是它里面的内部类，还有自定义的定时线程池 ScheduledThreadPoolExecutor
 * key 错错错，所有线程池都是用 ThreadPoolExecutor 创建的，只是 Executors 工具类创建的几种提前定义的线程池使用 ExecutorService 接值而已，因为 ThreadPoolExecutor 也有实现 ExecutorService 接口
 */
public class ThreadPoolExample {
    public static void main(String[] args){
        /**
         * 完全自定义的线程池可以通过 ThreadPoolExecutor 类来实现。ThreadPoolExecutor 构造方法的七个参数如下：
         * corePoolSize：线程池中保留的核心线程数，即使空闲也不会回收。
         * maximumPoolSize：线程池中允许创建的最大线程数。
         * keepAliveTime：当线程数大于核心线程数时，多余的空闲线程的存活时间。
         * unit：keepAliveTime 的时间单位。
         * workQueue：用来存放等待执行任务的阻塞队列。
         * threadFactory：用于创建新线程的工厂。
         * handler：拒绝任务时的处理策略。
         * key 这是构造函数，记住这个类 ThreadPoolExecutor
         */
        ExecutorService executor = new ThreadPoolExecutor(5,
                10,
                1000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10),
//                Executors.defaultThreadFactory(),     // 默认工厂，下面是自定义工厂
//                new ThreadFactory() {
//                    @Override
//                    public Thread newThread(Runnable r) {
//                        Thread thread = new Thread(r);
//                        thread.setName("self define threadPollExecutor thread");
//                        return thread;
//                    }
//                },
                (r)->{
                    Thread thread = new Thread(r);
                    thread.setName("self define threadPollExecutor thread");
                    return thread;
                },
                new ThreadPoolExecutor.AbortPolicy());

        /**
         * 自定义线程池 ThreadPoolExecutor 的拒绝策略
         * AbortPolicy 抛出异常（默认），即使反馈程序运行状态
         * CallerRunsPolicy 直接在调用线程（提交这个任务的线程）执行任务，需要让所有任务都执行完毕，适合计算密集型任务
         * DiscardOldestPolicy 丢弃队列最前面的任务，然后重新提交被拒绝的任务
         */
        ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();
//        ThreadPoolExecutor.CallerRunsPolicy callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
//        ThreadPoolExecutor.DiscardOldestPolicy discardOldestPolicy = new ThreadPoolExecutor.DiscardOldestPolicy();
//        ThreadPoolExecutor.DiscardPolicy discardPolicy = new ThreadPoolExecutor.DiscardPolicy();

        /**
         * 阻塞队列的选择及特点
         * BlockingQueue 是一个接口，常用的实现类有以下几种：
         * 1、ArrayBlockingQueue 有界队列，基于数组实现。先进先出（FIFO）原则
         * 2、LinkedBlockingQueue：可以选择有界或无界队列，基于链表实现。 key 不指定容量时为无界队列，适用于生产者 - 消费者场景。
         * 3、SynchronousQueue 每个插入操作必须等待相应的移除操作，才能继续进行。特点：没有容量，适用于传递性队列，适合需要直接处理的任务。缓存线程池就用的这个，新任务到来时创建新线程
         * 4、PriorityBlockingQueue：无界队列，具有优先级的任务队列，而不是 FIFO。
         */
//        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100);
//        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
//        BlockingQueue<Runnable> queue = new SynchronousQueue<>();
//        BlockingQueue<Runnable> queue = new PriorityBlockingQueue<>();

        /**
         * 线程池创建，注意这四个常见线程池的命名
         */
//        ExecutorService executor = Executors.newFixedThreadPool(5);
//        ExecutorService executor = Executors.newCachedThreadPool();
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

        /**
         * 线程池提交任务的 api
         * 向线程池提交任务可以通过 execute 和 submit 方法。两者的区别主要在于返回值和异常的处理方式。
         * 1、execute 方法：
         *  直接提交一个任务，无法获取任务的执行结果。
         *  如果任务执行中抛出异常，会直接抛出并且不会被捕获。
         *  void execute(Runnable command)
         * 2、submit 方法：
         *  提交一个任务，返回一个 Future 对象，可以通过这个对象获取任务执行结果。
         *  key 异常会被捕获并可以通过 Future 的 get 方法获取。
         *  有三种签名：
         *      Future<?> submit(Runnable task)
         *      Future<T> submit(Runnable task, T result)
         *      Future<T> submit(Callable<T> task)
         */
        executor.execute(()->{
            System.out.println("调用 execute 方法");
        });

        Future<String> future = executor.submit(() -> {
            System.out.println("调用 submit 方法传入 callable");
            return "submit - callable";
        });
        try {
            String s = future.get();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
            String message = e.getCause().getMessage();
            System.out.println("message = " + message);
        }

        // 别忘了可以用 FutureTask 包装 Callable 然后传入需要 Runnable 的地方，因为 FutureTask 实现了 Runnable 接口
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println("FutureTask 包装 Callable 传入需要 Runnable 的地方");
            return "FutureTask - Callable - Runnable";
        });
        executor.execute(futureTask);
        executor.submit(futureTask);

        /**
         * 线程池的关闭
         */
        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}

/**
 * 线程池原理 ThreadPoolExecutor类
 * 线程池（Thread Pool）是一种基于池化思想管理线程的工具，经常出现在多线程服务器中，如MySQL。
 * 线程过多会带来额外的开销，其中包括创建销毁线程的开销、调度线程的开销等等，线程池维护多个线程，等待监督管理者分配可并发执行的任务。
 * 这种做法，key 记住这个：一方面避免了处理任务时反复创建和销毁的线程开销，另一方面避免了线程数量膨胀导致的过分调度问题，保证了对内核的充分利用（对线程数量进行控制）。
 *
 * key 降低资源消耗：通过池化技术重复利用已创建的线程，降低线程创建和销毁造成的损耗。
 * 提高响应速度：任务到达时，无需等待线程创建即可立即执行。key 这点原来也很容易漏掉
 * key 很核心的点：提高线程的可管理性：线程是稀缺资源，如果无限制创建，不仅会消耗系统资源，还会因为线程的不合理分布导致资源调度失衡，降低系统的稳定性。使用线程池可以进行统一的分配、调优和监控。
 * 提供更多更强大的功能：线程池具备 key 可拓展性，允许开发人员向其中增加更多的功能。比如延时定时线程池 ScheduledThreadPoolExecutor（ThreadPoolExecutor 的子类），就允许任务延期执行或定期执行。
 *
 * 线程池解决的 key 核心问题就是资源管理问题(很好理解，一定记住)。在并发环境下，系统不能够确定在任意时刻中，有多少任务需要执行，有多少资源需要投入。这种不确定性将带来以下若干问题：
 * 1、频繁申请/销毁资源和调度资源，将带来额外的消耗，可能会非常巨大。
 * 2、对资源无限申请缺少抑制手段，易引发系统资源耗尽的风险。
 * 3、系统无法合理管理内部的资源分布（因为无法感知），会降低系统的稳定性。
 *
 * 为解决资源分配这个问题，线程池采用了“池化”（Pooling）思想。池化，顾名思义，是为了最大化收益并最小化风险，而将资源统一在一起管理的一种思想。
 * 在计算机领域中的表现为：统一管理IT资源，通过共享资源，使用户在低投入中获益。除去线程池，还有其他比较典型的几种使用策略包括：
 *
 * 内存池(Memory Pooling)：预先申请内存，提升之后申请分配内存的速度，减少内存碎片。
 * 连接池(Connection Pooling)：预先申请数据库连接，提升之后申请连接的速度，降低系统的开销。
 * 实例池(Object Pooling)：循环使用对象，减少资源在初始化和释放时的昂贵损耗。
 *
 * ---------------------- 原理
 * Executor <- ExecutorService <- AbstractExecutorService <-ThreadPoolExecutor
 * ThreadPoolExecutor 实现的顶层接口是 Executor，key 顶层接口 Executor 提供了一种思想：将任务提交和任务执行进行解耦。【原来这就是 Executor 接口带来的】
 * 用户无需关注如何创建线程，如何调度线程来执行任务，用户只需提供 Runnable 对象，将任务的运行逻辑提交到执行器(Executor)中，由Executor框架完成线程的调配和任务的执行部分。
 * ExecutorService接口增加了一些能力：（1）扩充执行任务的能力，补充可以为一个或一批异步任务生成Future的方法；（2）提供了管控线程池的方法，比如停止线程池的运行。
 * AbstractExecutorService则是上层的抽象类，将执行任务的流程串联了起来，保证下层只需关注一个执行任务的方法即可。
 * 最下层的实现类ThreadPoolExecutor实现最复杂的运行部分，ThreadPoolExecutor将会 key 一方面维护自身的生命周期，另一方面同时管理线程和任务，使两者良好的结合从而执行并行任务。
 *
 * 程池在内部实际上构建了一个生产者消费者模型，将线程和任务两者解耦
 * 线程池的运行主要分成两部分：任务管理、线程管理。任务管理部分充当生产者的角色，当任务提交后，线程池会判断该任务后续的流转：（1）直接申请线程执行该任务；（2）缓冲到队列中等待线程执行；（3）拒绝该任务。
 * 线程管理部分是消费者，它们被统一维护在线程池内，根据任务请求进行线程的分配，当线程执行完任务后则会继续获取新的任务去执行，最终当线程获取不到任务的时候，线程就会被回收。
 *
 * 接下来，我们会按照以下三个部分去详细讲解线程池运行机制：
 *
 * 1、线程池如何维护自身状态。
 * 2、线程池如何管理任务。
 * 3、线程池如何管理线程。
 *
 * 生命周期管理
 * 线程池运行的状态，伴随着线程池的运行，由内部来维护。线程池内部使用一个变量维护两个值：运行状态(runState)和有效线程数量 (workerCount)。
 * ctl这个AtomicInteger类型，它同时包含两部分的信息：线程池的运行状态 (runState) 和线程池内有效线程的数量 (workerCount)，高3位保存runState，低29位保存workerCount
 * shutdown 会接着处理阻塞队列的任务，shutdownNow是直接全部停了
 *
 * 任务执行机制:
 * 首先检测线程池运行状态，如果不是RUNNING，则直接拒绝，线程池要保证在RUNNING的状态下执行任务。
 * 如果workerCount < corePoolSize，则创建并启动一个线程来执行新提交的任务。
 * 如果workerCount >= corePoolSize，且线程池内的阻塞队列未满，则将任务添加到该阻塞队列中。
 * 如果workerCount >= corePoolSize && workerCount < maximumPoolSize，且线程池内的阻塞队列已满，则创建并启动一个线程来执行新提交的任务。
 * 如果workerCount >= maximumPoolSize，并且线程池内的阻塞队列已满, 则根据拒绝策略来处理该任务, 默认的处理方式是直接抛异常。
 *
 * 任务缓冲
 * 线程池的本质是对任务和线程的管理，而做到这一点最关键的思想就是将任务和线程两者解耦，不让两者直接关联，才可以做后续的分配工作。
 *
 * 任务申请
 * 线程需要从任务缓存模块中不断地取任务执行，帮助线程从阻塞队列中获取任务，实现线程管理模块和任务管理模块之间的通信。这部分策略由getTask方法实现
 *
 * Worker线程管理
 * 线程池为了掌握线程的状态并维护线程的生命周期，设计了线程池内的工作线程 Worker：class Worker extends AbstractQueuedSynchronizer implements Runnable，又是 AQS
 * Worker 这个工作线程类，实现了Runnable接口，并持有一个线程thread，一个初始化的任务firstTask。
 * thread 是在调用构造方法时通过 ThreadFactory 来创建的线程，可以用来执行任务；【难怪说可以用线程工厂来给线程命名】
 * firstTask 用它来保存传入的第一个任务，这个任务可以有也可以为null。如果这个值是非空的，那么线程就会在启动初期立即执行这个任务，也就对应核心线程创建时的情况；
 *      如果这个值是null，那么就需要创建一个线程去执行任务列表（workQueue）中的任务，也就是非核心线程的创建。
 *
 * 线程池需要管理线程的生命周期，需要在线程长时间不运行的时候进行回收。线程池使用一张 Hash 表去持有线程的引用，这样可以通过添加引用、移除引用这样的操作来控制线程的生命周期。
 * key 这个时候重要的就是如何判断线程是否在运行。
 * key Worker是通过继承AQS，使用 AQS 来实现独占锁这个功能。没有使用可重入锁 ReentrantLock，而是使用 AQS，为的就是实现不可重入的特性去反应线程现在的执行状态。【终于见到不允许冲入的锁了】
 * 1.lock方法一旦获取了独占锁，表示当前线程正在执行任务中。 2.如果正在执行任务，则不应该中断线程。
 * 3.如果该线程现在不是独占锁的状态，也就是空闲的状态，说明它没有在处理任务，这时可以对该线程进行中断。（然后就对他上锁？）
 * 4.线程池在执行shutdown方法或tryTerminate方法时会调用interruptIdleWorkers方法来中断空闲的线程，
 *      interruptIdleWorkers方法会使用tryLock方法来判断线程池中的线程 是否是空闲状态；如果线程是空闲状态则可以安全回收。
 *
 * Worker线程回收
 * 线程池中线程的销毁依赖JVM自动的回收，线程池做的工作是根据当前线程池的状态维护一定数量的线程引用，防止这部分线程被JVM回收，当线程池决定哪些线程需要回收时，只需要将其引用消除即可。
 * Worker被创建出来后，就会不断地进行轮询，然后获取任务去执行，核心线程可以无限等待获取任务，非核心线程要限时获取任务。
 *      当Worker无法获取到任务，也就是获取的任务为空时，循环会结束，Worker会主动消除自身在线程池内的引用。
 *
 *
 * Worker线程执行任务
 * 1.while循环不断地通过getTask()方法获取任务。 2.getTask()方法从阻塞队列中取任务。
 * 3.如果线程池正在停止，那么要保证当前线程是中断状态，否则要确认当前线程不是中断状态。 4.执行任务。 5.如果getTask结果为null则跳出循环，执行processWorkerExit()方法，销毁线程。
 *
 * ---------------
 * 线程池在业务中的实践
 * 线程池使用面临的核心的问题在于：线程池的参数并不好配置。一方面线程池的运行机制不是很好理解，配置合理需要强依赖开发人员的个人经验和知识；
 * 另一方面，线程池执行的情况和任务类型相关性较大，IO密集型和CPU密集型的任务运行起来的情况差异非常大。
 * 事故原因：该服务展示接口内部逻辑使用线程池做并行计算，由于没有预估好调用的流量，导致最大核心数设置偏小，大量抛出RejectedExecutionException，触发接口降级条件，
 * 事故原因：该服务处理请求内部逻辑使用线程池做资源隔离，由于队列设置过长，最大线程数设置失效，导致请求数量增加时，大量任务堆积在队列中，任务执行时间过长，最终导致下游服务的大量调用超时失败。
 *
 * Actor模型的应用实际上甚少，只在Scala中使用广泛，协程框架在Java中维护的也不成熟。这三者现阶段都不是足够的易用，也并不能解决业务上现阶段的问题。
 *
 * 线程池参数动态化
 * 尽管经过谨慎的评估，仍然不能够保证一次计算出来合适的参数，那么我们是否可以将修改线程池参数的成本降下来，这样至少可以发生故障的时候可以快速调整从而缩短故障恢复的时间呢
 * 基于这个思考，我们是否可以 key 将线程池的参数从代码中迁移到分布式配置中心上，实现线程池参数可动态配置和即时生效
 *
 * 动态化线程池的核心设计包括以下三个方面：
 * 简化线程池配置：线程池构造参数有8个，但是最核心的是3个：corePoolSize、maximumPoolSize，workQueue，它们最大程度地决定了线程池的任务分配和线程分配策略。考虑到在实际应用中我们获取并发性的场景主要是两种：
 * （1）并行执行子任务，提高响应速度。不应该用队列缓冲任务，应该使用比如同步队列，去快速创建线程执行任务
 * （2）并行执行大批次任务，提升吞吐量。这种情况下，应该使用有界队列，使用队列去缓冲大批量的任务，队列容量必须声明，防止任务无限制堆积。
 *  key 所以线程池只需要提供这三个关键参数的配置，并且提供两种队列的选择，就可以满足绝大多数的业务需求，Less is More。
 *
 * 参数可动态修改：为了解决参数不好配，修改参数成本高等问题。在Java线程池留有高扩展性的基础上，封装线程池，key 允许线程池监听同步外部的消息，根据消息进行修改配置。
 *    将线程池的配置放置在平台侧，允许开发同学简单的查看、修改线程池配置。
 *
 * 增加线程池监控：对某事物缺乏状态的观测，就对其改进无从下手。key 在线程池执行任务的生命周期添加监控能力，帮助开发同学了解线程池状态。
 *
 * -------------------
 * 动态化线程池提供如下功能：
 * 动态调参：支持线程池参数动态调整、界面化操作；包括修改线程池核心大小、最大核心大小、队列长度等；参数修改后及时生效。
 * 任务监控：可以看到线程池的任务执行情况、最大任务执行时间、平均任务执行时间
 * 负载告警：当线程池负载数达到一定阈值的时候会通过大象告知应用开发负责人。
 * 操作监控：创建/修改和删除线程池都会通知到应用的开发负责人。
 * 操作日志：可以查看线程池参数的修改记录，谁在什么时候修改了线程池参数、修改前的参数值是什么。
 *
 * JDK原生线程池ThreadPoolExecutor提供了如下几个public的setter方法，key 我们管理的是一个个 线程池实例
 * 以setCorePoolSize为方法例，在运行期线程池使用方调用此方法设置corePoolSize之后，线程池会直接覆盖原来的corePoolSize值，并且基于当前值和原始值的比较结果采取不同的处理策略。
 *      对于当前值小于当前工作线程数的情况，说明有多余的worker线程，此时会向当前idle的worker线程发起中断请求以实现回收，多余的worker在下次idel的时候也会被回收；
 *      对于当前值大于原始值且当前队列中有待执行任务，则线程池会创建新的worker线程来执行队列任务
 *   key 可以看到线程池实例内部会处理好当前状态做到平滑修改（太强了）
 *
 * 线程池监控：分为 负载监控 和 任务级监控 和 线程池实例监控
 * 线程池负载关注的核心问题是：基于当前线程池参数分配的资源够不够。
 * 线程池定义了“活跃度”这个概念，线程池活跃度计算公式为：线程池活跃度 = activeCount/maximumPoolSize。代表当活跃线程数趋向于maximumPoolSize的时候，代表线程负载趋高。
 * 也可以从两方面来看线程池的过载判定条件，一个是发生了Reject异常，一个是队列中有等待任务（支持定制阈值）
 *
 * 任务级监控
 * 传统的线程池应用场景中，线程池中的任务执行情况对于用户来说是透明的。
 * 任务实际执行的频率和时长对于用户来说没有一个直观的感受，很可能这两类任务不适合共享一个线程池，但是由于用户无法感知，因此也无从优化。
 * 动态化线程池内部实现了任务级别的埋点，且允许为不同的业务任务指定具有业务含义的名称，基于这个功能，用户可以看到线程池内部任务级别的执行情况，且区分业务（比如任务执行时间，失败率，执行数等）
 *
 * 线程池实例监控
 * 封装了运行时状态实时查看的功能，用户基于这个功能可以了解线程池的实时状态，比如当前有多少个工作线程，执行了多少个任务，队列中等待的任务数等等。
 *
 * -----------------
 * 对业务中使用线程池遇到的实际问题，我们曾回到支持并发性问题本身来思考有没有取代线程池的方案，也曾尝试着去追求线程池参数设置的合理性，
 * 但面对业界方案具体落地的复杂性、可维护性以及真实运行环境的不确定性，我们在前两个方向上可谓“举步维艰”。
 * 虽然本质上还是没有逃离使用线程池的范畴，但是在成本和收益之间，算是取得了一个很好的平衡。
 * 成本在于实现动态化以及监控成本不高，收益在于：在不颠覆原有线程池使用方式的基础之上，从降低线程池参数修改的成本以及多维度监控这两个方面降低了故障发生的概率。
 */

class ThreadPoolExecutorPrinciple{
    public static void main(String[] args) {
    }
}

/**
 * 详解顶层接口 Executor
 * Executor 接口定义了 key 一个能够执行提交的 Runnable 任务的对象。这个接口提供了一种将任务提交与任务如何执行的机制（包括线程使用、调度等细节）解耦的方法。
 * Executor 通常用于替代显式创建线程（异步）。例如，与其对每个任务调用new Thread(new RunnableTask()).start()，可以使用如下方式：
 * Executor executor = anExecutor();
 * executor.execute(new RunnableTask());
 *
 * ExecutorService 接口是一个更全面的接口。ThreadPoolExecutor 类提供了一个可扩展的线程池实现。Executors 类提供了创建这些 Executor 的便捷工厂方法。
 */
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
