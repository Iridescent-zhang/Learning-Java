package com.example.javaBase.reactor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.javaBase.reactor
 * @ClassName : .java
 * @createTime : 2025/3/28 18:13
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Java NIO的核心组件包括：
 * （1）Channel(通道)
 * （2）Buffer(缓冲区)
 * （3）Selector(选择器)
 * 其中Channel和Buffer比较好理解 ，联系也比较密切，他们的关系简单来说就是：数据总是从通道中读到 buffer 缓冲区内，或者从 buffer 写入到通道中。
 *
 * 选择器（Selector） 是 Channel（通道）的多路复用器，Selector 可以同时监控多个 通道的 IO（输入输出） 状况。
 * Selector的作用是什么？
 * key  从底层来看，Selector提供了询问通道是否已经准备好执行某个I/O操作的能力。
 * Selector 就是使用单线程处理多个Channel。仅用单个线程来处理多个Channels的好处是，只需要更少的线程来处理通道。事实上，可以只用一个线程处理所有的通道，这样会大量的减少线程之间上下文切换的开销。
 * 【总结就是: Selector 用来管理多个通道的时间】
 *
 * 1.1.2. 可选择通道(SelectableChannel) 【netty 里面的通道就是继承 SelectableChannel 的】
 * 并不是所有的Channel，都是可以被Selector 复用的。比方说，FileChannel就不能被选择器复用。为什么呢？
 * 判断一个Channel 能被Selector 复用，有一个前提：判断他是否继承了一个抽象类SelectableChannel。如果继承了SelectableChannel，则可以被复用，否则不能。
 * SelectableChannel类提供了实现通道的可选择性所需要的公共方法。它是所有支持就绪检查的通道类的父类。
 * 所有socket通道，都继承了SelectableChannel类所以它们都是可选择的，包括从管道(Pipe)对象的中获得的通道。而 FileChannel类，没有继承SelectableChannel，因此是不可选通道。
 *
 * 通道和选择器 是多对多的关系
 * 一个通道可以被注册到多个选择器上，但对每个选择器而言只能被注册一次。
 * key  通道和选择器之间的关系，使用注册的方式完成。SelectableChannel可以被注册到 Selector 对象上，在注册的时候，需要指定通道的哪些操作，是 Selector 感兴趣的。
 * 【指定 Selector 感兴趣的关于这个通道的时间】
 *
 * 使用Channel.register（Selector sel，int ops）方法，将一个通道注册到一个选择器时。第一个参数，指定通道要注册的选择器是谁。第二个参数指定选择器需要查询的通道操作。
 * 可以供选择器查询的通道状态，从类型来分，包括以下四种：
 * （1）可读 : SelectionKey.OP_READ      OP就是操作的意思
 * （2）可写 : SelectionKey.OP_WRITE
 * （3）连接 : SelectionKey.OP_CONNECT
 * （4）接收 : SelectionKey.OP_ACCEPT
 * 【特别提醒的是，选择器查询的不是通道的操作，而是通道的某种允许操作的一种就绪状态。】【比如此通道的写操作就绪】
 * 什么是操作的就绪状态？
 * 一旦通道具备完成某个操作的条件，表示该通道的某个操作已经就绪，那就可以被 Selector 查询到，之后程序可以对通道进行对应的操作。
 * 比方说，某个 SocketChannel 通道可以连接到一个服务器，则处于"连接就绪"(OP_CONNECT)。
 * 再比方说，一个 ServerSocketChannel [服务器通道]准备好接收新来的连接，则处于"接收就绪"（OP_ACCEPT）状态。
 * 还比方说，一个有数据可读的通道，可以说是"读就绪"(OP_READ)。一个等待写数据的通道可以说是"写就绪"(OP_WRITE)。
 *
 * 1.1.4. 选择键(SelectionKey) 和 channel 是一一对应的
 * Channel 和 Selector 的关系确定好后，key [并且一旦通道处于某种就绪的状态，就可以被选择器查询到(当然前提是选择器对这个状态感兴趣)]。 太核心了
 * 这个工作，使用选择器 Selector 的 select（）方法完成。select方法的作用：对我里面的通道操作，进行(我感兴趣的)就绪状态的查询。
 * Selector可以不断的查询Channel中发生的操作的就绪状态。并且挑选感兴趣的操作就绪状态。一旦通道有操作的就绪状态达成，并且是Selector感兴趣的操作，就会被Selector选中，放入选择键集合中。
 * key  一个选择键，首先是包含了注册在 Selector 的通道操作的类型，比方说 SelectionKey.OP_READ。也包含了特定的通道与特定的选择器之间的注册关系。
 * 开发应用程序是，选择键是编程的关键。NIO的编程，就是根据对应的选择键，进行不同的业务逻辑处理。【比如每个选择键都有自己的 attachment】
 * 选择键的概念，有点儿像事件的概念。选择键和事件的关系是什么？
 * 一个选择键有点儿像监听器模式里边的一个事件，但是又不是。由于 Selector 不是事件触发的模式，而是主动去查询的模式，所以不叫事件 Event，而是叫 SelectionKey 选择键。
 *
 * 与Selector一起使用时，Channel必须处于非阻塞模式下，否则将抛出异常IllegalBlockingModeException。这意味着，FileChannel不能与Selector一起使用，因为FileChannel不能切换到非阻塞模式，而套接字相关的所有的通道都可以。
 * 另外，还需要注意的是：
 * 一个通道，并没有一定要支持所有的四种操作。比如服务器通道ServerSocketChannel支持Accept 接受操作，而SocketChannel客户端通道则不支持。可以通过通道上的validOps()方法，来获取特定通道下所有支持的操作集合。
 *
 * 通过Selector的select（）方法，可以查询出已经就绪的通道操作，这些就绪的状态集合，包存在一个元素是SelectionKey对象的Set集合中。
 * select()方法返回的int值，表示有多少通道已经就绪，更准确的说，是自前一次select方法以来到这一次select方法之间的时间段上，有多少通道变成就绪状态。
 * 通过调用Selector的selectedKeys()方法来访问已选择键集合，然后迭代集合的每一个选择键元素，根据就绪操作的类型，完成对应的操作
 * 下面是Selector几个重载的查询select()方法：
 * （1）select(): 阻塞到至少有一个通道在你注册的事件上就绪了。
 * （2）select(long timeout)：和select()一样，但最长阻塞事件为timeout毫秒。
 * （3）selectNow():非阻塞，立刻返回结果即使为0。
 */
public class SelectorExample {
    public static void main(String[] args) throws IOException {
        // 创建Selector ：Selector对象是通过调用静态工厂方法open()来实例化的
        // 1、获取Selector选择器
        // Selector的类方法open()内部是向SPI发出请求，通过默认的 SelectorProvider (选择器提供者)对象获取一个新的实例。
        // SPI 即Service Provider Interface ，字面意思就是：“服务提供者的接口”，我的理解是：专门提供给服务提供者或者扩展框架功能的开发者去使用的一个接口。
        Selector selector = Selector.open();

        // 将Channel注册到Selector  要实现Selector管理Channel，需要将channel注册到相应的Selector上
        // 2、获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 3.设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 4、绑定连接
        serverSocketChannel.bind(new InetSocketAddress(6666));

        // 5、将通道注册到选择器上,并制定监听事件为：“接收”事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
        while(keyIterator.hasNext()) {
            SelectionKey key = keyIterator.next();

            if(key.isAcceptable()) {
                // a connection was accepted by a ServerSocketChannel. 【对 ServerSocketChannel 来说是 accept 状态的就绪】
            } else if (key.isConnectable()) {
                // a connection was established with a remote server.【对 SocketChannel 来说是 connect 状态的就绪】
            } else if (key.isReadable()) {
                // a channel is ready for reading
            } else if (key.isWritable()) {
                // a channel is ready for writing
            }
            // 处理完成后，直接将选择键，从这个集合中移除，防止下一次循环的时候，被重复的处理。
            keyIterator.remove();

        }
    }
}
