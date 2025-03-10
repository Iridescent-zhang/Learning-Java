package com.example.interview.javaBase.lambda;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.javaBase.lambda
 * @ClassName : .java
 * @createTime : 2025/3/1 23:02
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * 在 Java 8 中，引入了方法引用（Method References），key 是一种更简洁的 lambda 表达式，它允许用一种简洁的方式实现函数式接口。Consumer 就是一个函数式接口@FunctionalInterface，它只有一个抽象方法 void accept(T t)
 * 你所见到的 IHandler::handler 可以作为 Consumer 或者 Runnable 的情况，实际上利用了方法引用的灵活性，这是因为方法引用可以 key 根据上下文提供的函数式接口的签名来确定。
 *
 * 方法引用有四种形式：
 * 静态方法引用 - ClassName::staticMethodName
 * 实例方法引用 - instance::instanceMethodName
 * 特定对象的实例方法引用 - ClassName::instanceMethodName
 * 构造方法引用 - ClassName::new
 *
 * key 底层原理是方法引用会根据上下文（即函数式接口）来推导最终需要实现的抽象方法。例如：
 * 当 IHandler::handler 被用作 Consumer<IHandler> 时，它需要实现 void accept(IHandler t)，它相当于 handler -> handler.handler()。
 * 当 myHandler::handler 被用作 Runnable 时，它需要实现 void run()，它相当于 () -> myHandler.handler()。
 *
 * 方法引用是 lambda 表达式的一种简化形式，可以根据上下文自动匹配需要实现的方法。当你将方法引用 IHandler::handler 用于不同的函数式接口时，编译器会自动推断出适当的 lambda 表达式并进行替换。
 */
interface IHandler {
    void handler();

    static void method(){
        System.out.println("method");
    }
}

class HandlerA implements IHandler {

    @Override
    public void handler() {
        System.out.println("执行 HandlerA");
    }
}

class HandlerB implements IHandler {

    @Override
    public void handler() {
        System.out.println("执行 HandlerB");
    }
}

class LambdaExample {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<IHandler> iHandlers = new ArrayList<>(Arrays.asList(new HandlerA(), new HandlerB()));
        /**
         * key 应该属于 特定对象的实例方法引用 - ClassName::instanceMethodName
         *
         * 等效代码为：handlers.forEach(handler -> handler.handler());
         */
        iHandlers.forEach(IHandler::handler);
        iHandlers.forEach(each -> {
            each.handler();
        });
        System.out.println();

        /**
         * key 这样是不行的：Runnable handler = IHandler::handler;   Consumer<IHandler> handler = IHandler::handler;
         * IHandler::handler 应该还是属于 instance::instanceMethodName，可以用来 consumer 消耗每个 IHandler 的实例对象
         * 接口 IHandler::handler 这样是生成 Consumer，不能生成 Runnable
         */
//        Runnable handler = IHandler::handler;
        Consumer<IHandler> handler = IHandler::handler;

        /**
         * 只有实例 handlerA::handler 才能生成 Runnable runnable = handlerA::handler;
         * myHandler::handler 被视为一个 Runnable 等效代码为：
         * Runnable runnable = new Runnable() {
         *     @Override
         *     public void run() {
         *         myHandler.handler();
         *     }
         * };
         */
        HandlerA myHandler  = new HandlerA();
        Runnable runnable = myHandler ::handler;
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();

        /**
         * 接口的静态方法是能作为 Runnable 的，现在懂了吧，能 Run 的才能作为 Runnable
         */
        Runnable method = IHandler::method;
    }
}



