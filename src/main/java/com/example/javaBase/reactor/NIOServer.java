package com.example.javaBase.reactor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview
 * @ClassName : .java
 * @createTime : 2025/2/11 22:42
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 默认的 Selector 是 LT 模式。
 * 若使用特定的 Selector 实现，比如EPollSelectorProvider（与操作系统 epoll 关联），通过SelectionKey的附加标记可以开启 ET 模式。在 Java NIOExample 中，直接使用selector.select()等 API 时默认是 LT 模式。
 *
 * LT 模式是绝大多数系统默认为模式，它在某个文件描述符（如套接字）变为 “就绪” 状态时，事件通知系统会不断通知你，直到你处理完成为止。
 * ET 模式只会在状态从未就绪变为就绪时通知一次。如果你没有处理完该事件，事件通知系统不会再次通知你，需要你进行额外的处理以确保不会丢失事件。
 *
 * 使用ET模式，你应该怎么处理网络事件？
 *
 * 这个例子：key  其实Java的 NIOExample 模式的Selector网络通讯，其实就是一个简单的Reactor模型。可以说是Reactor模型的朴素原型。
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        /**
         * 创建选择器
         * 在 Java NIOExample 的 Selector 使用中，ET（Edge Triggered，边缘触发）和 LT（Level Triggered，水平触发）是两种不同的事件通知模式。
         *
         * (LT) 水平触发：
         * 当 Selector 发现通道准备好进行 I/O 操作时，它会通知一个事件。只要条件满足，比如有数据可读，Selector 会不断地通知。所以只要不处理，多次 select 操作会返回同一个事件。
         *
         * (ET) 边缘触发
         * 当 Selector 发现通道准备好进行 I/O 操作时，它仅在状态变化的时候（边缘）通知一次事件，意味着必须在一个通知内处理所有的数据，因为不会再次通知同一事件，必须及时读取所有数据，否则会错过。
         *
         */
        Selector selector = Selector.open();

        // 打开监听信道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        // 非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 注册接收连接操作
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // keypoint 这个是表示系统一直在运行
        while (true) {
            // 选择一组键，其相应的通道已为 I/O 操作做准备
            selector.select();

            // 返回此选择器的已选择键集
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if (key.isAcceptable()) {
                    // 处理新接入的连接
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel = serverChannel.accept();
                    clientChannel.configureBlocking(false);
                    // 注册读和写操作，以边缘触发模式
                    clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                } else if (key.isReadable()) {
                    // 处理读操作
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    /**
                     * TODO 当使用 ET 模式时，需要特别小心处理，因为错过了就绪事件后可能导致挂起或者其它错误。通常我们会在循环中 【注意是这个 while (true)】 一直读取或者写入，直到所有数据处理完毕。
                     * TODO 在 ET 模式下，当通道变为可读（或可写）时，因为 Selector 只会通知一次，加上可能 clientChannel.read(buffer) 不能读完所有消息内容，并且必须在这次消息通知内处理完所有数据。
                     * 所以就加上 while(true) ，这就是 while(true) 循环的目的。即：单次通知不能完美预测传来了多少数据，需循环读取直到 read 返回 0 或 -1
                     *
                     * 通过这个 while 保证对每个 ready 的 key 处理完 I/O 操作，通过循环读取整个 buffer 确保数据被完全处理
                     */
                    while (true) {
                        int bytesRead = clientChannel.read(buffer);
                        if (bytesRead > 0) {
                            buffer.flip();
                            while (buffer.hasRemaining()) {
                                System.out.print((char) buffer.get());
                            }
                            buffer.clear();
                        } else if (bytesRead == 0) {
                            break;
                        } else { // bytesRead = -1
                            key.cancel();
                            clientChannel.close();
                            break;
                        }
                    }
                }
                iterator.remove();
            }
        }
    }
}
