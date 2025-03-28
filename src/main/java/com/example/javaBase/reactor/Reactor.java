package com.example.javaBase.reactor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.javaBase.reactor
 * @ClassName : .java
 * @createTime : 2025/3/25 18:10
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 实际上的Reactor模式，是基于Java NIO的，在他的基础上，抽象出来两个组件——Reactor和Handler两个组件：
 * （1）Reactor：负责响应 IO 事件，当检测到一个新的事件，将其发送给相应的 Handler 去处理；新的事件包含 key  连接建立就绪、读就绪、写就绪等。
 * （2）Handler: 将自身（handler）与事件绑定，负责事件的处理，完成 channel 的读入，完成处理业务逻辑后，负责将结果写出 channel。
 *
 * 6.1. 优点
 * 1）响应快，不必为单个同步时间所阻塞，虽然 Reactor 本身依然是同步的；
 * 2）编程相对简单，可以最大程度的避免复杂的多线程及同步问题，并且避免了多线程/进程的切换开销；
 * 3）可扩展性，可以方便的通过增加 Reactor 实例个数来充分利用CPU资源；
 * 4）可复用性，reactor框架本身与具体事件处理逻辑无关，具有很高的复用性；
 *
 * 6.2. 缺点
 * 1）相比传统的简单模型，Reactor增加了一定的复杂性，因而有一定的门槛，并且不易于调试。
 * 2）Reactor模式需要底层的 Synchronous Event Demultiplexer 支持，比如 Java 中的 Selector 支持，操作系统的 select 系统调用支持，如果要自己实现 Synchronous Event Demultiplexer 可能不会有那么高效。
 * 3）Reactor 模式在 IO 读写数据时还是在同一个线程中实现的，即使使用多个 Reactor 机制的情况下.
 * 那些共享一个 Reactor 的 Channel 如果出现一个长时间的数据读写，会影响这个 Reactor 中其他Channel的相应时间，比如在大文件传输时，IO操作就会影响其他Client的相应时间，【感觉可以把 reactor 当作是 selector】
 * 因而对这种操作，使用传统的Thread-Per-Connection或许是一个更好的选择，或者此时使用改进版的Reactor模式如Proactor模式。
 */
public class Reactor {
}
