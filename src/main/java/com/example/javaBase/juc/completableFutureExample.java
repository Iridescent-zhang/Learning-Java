package com.example.javaBase.juc;

import com.google.common.util.concurrent.*;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.juc
 * @ClassName : .java
 * @createTime : 2025/3/9 21:40
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 两个主要特性：
 * 可组合：可以将多个依赖操作通过不同的方式进行编排，例如CompletableFuture提供 thenCompose、thenCombine 等各种then开头的方法，这些方法就是对“可组合”特性的支持。
 * 以及 异步特性。
 *
 * CompletableFuture是由Java 8引入的，在Java8之前我们一般通过Future实现异步。
 * key Future用于表示异步计算的结果，只能通过阻塞或者轮询的方式获取结果，而且不支持设置回调方法，Java 8之前若要设置回调一般会使用 guava 的 ListenableFuture，回调的引入又会导致臭名昭著的回调地狱（下面的例子会通过ListenableFuture的使用来具体进行展示）。
 * CompletableFuture对Future进行了扩展，key 可以通过设置回调的方式处理计算结果，同时也支持组合操作，支持进一步的编排，同时一定程度解决了回调地狱的问题。
 *
 * 举例：下面将举例来说明，我们对比ListenableFuture、CompletableFuture来实现异步的差异。假设有三个操作step1、step2、step3存在依赖关系，其中step3的执行依赖step1和step2的结果。
 */
public class completableFutureExample {
    public static void main(String[] args) {
        // key 显然，CompletableFuture的实现更为简洁，可读性更好。
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行step 1");
            return "step1 result";
        }, executor);
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行step 2");
            return "step2 result";
        });
        cf1.thenCombine(cf2, (result1, result2) -> {
            System.out.println(result1 + " , " + result2);
            System.out.println("执行step 3");
            return "step3 result";
        }).thenAccept(result3 -> System.out.println(result3));
    }
}

/**
 * 盘一下 CompletableFuture 的原理：
 * CompletableFuture实现了两个接口（如上图所示）：Future、CompletionStage。Future表示异步计算的结果，key CompletionStage用于表示异步执行过程中的一个步骤/阶段（Stage），
 * key 这个步骤可能是由另外一个CompletionStage触发的，随着当前步骤的完成，也可能会触发其他一系列 CompletionStage 的执行。
 * 从而我们可以根据实际业务对这些步骤进行多样化的编排组合，CompletionStage 接口正是定义了这样的能力，我们可以通过其提供的 thenApply、thenCompose 等函数式编程方法来组合编排这些步骤。
 * key 看来 CompletionStage（步骤） 才是异步编排的关键
 *
 * 使用CompletableFuture也是构建依赖树的过程。一个CompletableFuture的完成会触发另外一系列依赖它的CompletableFuture的执行：或者说前者完成了才允许后者的进行。
 *
 * 举例：其中包括CF1\CF2\CF3\CF4\CF5共5个步骤，并描绘了这些步骤之间的依赖关系，每个步骤可以是一次RPC调用、一次数据库操作或者是一次本地方法调用等，在使用CompletableFuture进行异步化编程时，图中的每个步骤都会产生一个CompletableFuture对象，最终结果也会用一个CompletableFuture来进行表示。
 * 根据CompletableFuture依赖数量，可以分为以下几类：零依赖、一元依赖、二元依赖和多元依赖。
 *
 * 1、零依赖：CompletableFuture的创建  （或者说依赖树的 root 的创建）
 *      首先发起两个异步调用CF1、CF2，主要有三种方式：
 * 2、一元依赖：依赖一个CF
 *      CF3，CF5分别依赖于CF1和CF2，这种对于单个 CompletableFuture 的依赖可以通过 thenApply、thenAccept、thenCompose 等方法来实现
 *      thenApply、thenCompose 传入一个 Function<T, R> {R apply(T t)} 它可以接上一个 Stage 的运行结果，然后自己执行出一个结果
 *      thenAccept 接一个 Comsumer {void accept(T t)} 只消耗不产出
 * 3、二元依赖：依赖两个CF
 *      CF4同时依赖于两个CF1和CF2，这种二元依赖可以通过 thenCombine 等回调来实现
 *      thenCombine 接收一个 BiFunction{R apply(T t, U u);}
 * 4、多元依赖：依赖多个CF
 *      整个流程的结束依赖于三个步骤CF3、CF4、CF5，这种多元依赖可以通过 allOf 或 anyOf 方法来实现，区别是当需要多个依赖全部完成时使用allOf，当多个依赖中的任意一个完成即可时使用anyOf
 *
 * key CompletableFuture原理
 * CompletableFuture中包含两个字段：result和stack。result用于存储当前CF的结果，stack（Completion）表示当前CF完成后需要触发的依赖动作（Dependency Actions），去触发依赖它的CF的计算，
 * 依赖动作可以有多个（表示有多个依赖它的CF），以栈（Treiber stack）的形式存储，stack表示栈顶元素。
 *
 * 这种方式类似“观察者模式”，依赖动作（Dependency Action）都封装在一个单独Completion子类中。
 * 下面是Completion类关系结构图。CompletableFuture中的每个方法都对应了图中的一个Completion的子类，Completion本身是观察者的基类
 *
 *  被观察者
 * 每个CompletableFuture都可以被看作一个被观察者，其内部有一个Completion类型的链表成员变量stack，用来存储注册到其中的所有观察者。当被观察者执行完成后会弹栈stack属性，依次通知注册到其中的观察者。上面例子中步骤fn2就是作为观察者被封装在UniApply中。
 * 被观察者CF中的result属性，用来存储返回结果数据。这里可能是一次RPC调用的返回值，也可能是任意对象，在上面的例子中对应步骤fn1的执行结果。
 *
 * 观察者
 * CompletableFuture支持很多回调方法，例如thenAccept、thenApply、exceptionally等，这些方法接收一个函数类型的参数f，生成一个Completion类型的对象（即观察者），并将入参函数f赋值给Completion的成员变量fn，
 *  然后检查当前CF是否已处于完成状态（即result != null），如果已完成直接触发fn，否则将观察者Completion加入到CF的观察者链stack中，此时再次尝试触发（双重检查），如果被观察者未执行完则其执行完毕之后通知触发。
 *
 * 这里仍然以thenApply为例来说明一元依赖的流程：
 * 将观察者Completion注册到CF1，此时CF1将Completion压栈。
 * 当CF1的操作运行完成时，会将结果赋值给CF1中的result属性。（此时结果不为空）
 * 依次弹栈，通知观察者尝试运行。
 * Q1：在观察者注册之前，如果CF已经执行完成，并且已经发出通知，那么这时观察者由于错过了通知是不是将永远不会被触发呢 ？
 * A1：不会。在注册时检查依赖的CF是否已经完成。如果未完成（即result == null）则将观察者入栈，如果已完成（result != null）则直接触发观察者操作。
 * Q2：在”入栈“前会有”result == null“的判断，这两个操作为非原子操作，CompletableFufure的实现也没有对两个操作进行加锁，完成时间在这两个操作之间，观察者仍然得不到通知，是不是仍然无法触发？
 * A2：不会。入栈之后再次检查CF是否完成，如果完成则触发。（双重检查）
 * Q3：当依赖多个CF时，观察者会被压入所有依赖的CF的栈中，每个CF完成的时候都会进行，那么会不会导致一个操作被多次执行呢 ？如下图所示，即当CF1、CF2同时完成时，如何避免CF3被多次触发。
 * A3：CompletableFuture的实现是这样解决该问题的：观察者在执行之前会先通过CAS操作设置一个状态位，将status由0改为1。如果观察者已经执行过了，那么CAS操作将会失败，取消执行。
 * 通过对以上3个问题的分析可以看出，CompletableFuture在处理并行问题时，全程无加锁操作，极大地提高了程序的执行效率。
 *
 * 不看了不看了，还得再回来看
 */
class completableFuturePrinciple {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        // 1、key 使用 runAsync 或 supplyAsync 发起异步调用传入一个 Supplier，注意 Supplier<T> {T get()} 是有返回值无参数的，同 Callable<T> {V call() throws Exception} 只不过后者还能返回异常
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            return "result1";
        }, executor);
        // 2、CompletableFuture.completedFuture() 直接创建一个已完成状态的 CompletableFuture
        CompletableFuture<String> cf2 = CompletableFuture.completedFuture("result2");
        // 3、先初始化一个未完成的 CompletableFuture，然后通过 complete()、completeExceptionally()，完成该CompletableFuture
        CompletableFuture<String> cf = new CompletableFuture<>();
        cf.complete("success");

        // --------------
        CompletableFuture<String> cf3 = cf1.thenApply(result1 -> {
            //result1为CF1的结果
            //......
            return "result3";
        });
        CompletableFuture<String> cf5 = cf2.thenApply(result2 -> {
            //result2为CF2的结果
            //......
            return "result5";
        });

        // -----------------
        CompletableFuture<String> cf4 = cf1.thenCombine(cf2, (result1, result2) -> {
            //result1和result2分别为cf1和cf2的结果
            return "result4";
        });

        // ------------------
        CompletableFuture<Void> cf6 = CompletableFuture.allOf(cf3, cf4, cf5);
        // 这个 result 是一个额外的结果，是cf6的下一虚构阶段，相当于它一元依赖 cf6
        CompletableFuture<String> result = cf6.thenApply(v -> {
            //这里的join并不会阻塞，因为传给thenApply的函数是在CF3、CF4、CF5全部完成时，才会执行 。
            String result3 = cf3.join();
            String result4 = cf4.join();
            String result5 = cf5.join();
            //根据result3、result4、result5组装最终result;
            return "result";
        });
    }
}


/**
 * key ListenableFuture 回调地狱
 */
class ListenableFutureCallbackHell {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        ListeningExecutorService guavaExecutor = MoreExecutors.listeningDecorator(executor);
        ListenableFuture<String> future1 = guavaExecutor.submit(() -> {
            //step 1
            System.out.println("执行step 1");
            return "step1 result";
        });
        ListenableFuture<String> future2 = guavaExecutor.submit(() -> {
            //step 2
            System.out.println("执行step 2");
            return "step2 result";
        });
        ListenableFuture<List<String>> future1And2 = Futures.allAsList(future1, future2);

        Futures.addCallback(future1And2, new FutureCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                System.out.println(result);
                ListenableFuture<String> future3 = guavaExecutor.submit(() -> {
                    System.out.println("执行step 3");
                    return "step3 result";
                });
                // key 这应该就是传说中的回调地狱了
                Futures.addCallback(future3, new FutureCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                    }
                    @Override
                    public void onFailure(Throwable t) {
                    }
                }, guavaExecutor);
            }

            @Override
            public void onFailure(Throwable t) {
            }}, guavaExecutor);
    }
}