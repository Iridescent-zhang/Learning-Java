package com.example.javaBase.reactor.NIOdemo;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.javaBase.reactor.NIOdemo
 * @ClassName : .java
 * @createTime : 2025/3/28 21:29
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * （1）客户端发起的连接操作是异步的，可以通过在多路复用器注册 OP_CONNECT 等待后续结果，不需要像之前的客户端那样被同步阻塞。【实现客户端不阻塞】
 * （2）SocketChannel 的读写操作都是异步的，如果没有可读写的数据它不会同步等待，直接返回，这样 I/O 通信线程就可以处理其他的链路，不需要同步等待这个链路可用。
 * （3）线程模型的优化：由于 JDK 的 Selector 在 Linux 等主流操作系统上通过 epoll 实现，它没有连接句柄数的限制（只受限于操作系统的最大句柄数或者对单个进程的句柄限制），
 *          这意味着一个 Selector 线程可以同时处理成千上万个客户端连接，而且性能不会随着客户端的增加而线性下降。因此，它非常适合做高性能、高负载的网络服务器。
 */
public class SelectorDemo {
    static class Client {
        /**
         * 客户端
         */
        public static void testClient() throws IOException {
            InetSocketAddress address= new InetSocketAddress("localhost", 5555);

            // 1、获取通道（channel）
            SocketChannel socketChannel =  SocketChannel.open(address);
            // 2、切换成非阻塞模式
            socketChannel.configureBlocking(false);

            // 3、分配指定大小的缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put("hello world  hello world  hello world".getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);

            socketChannel.close();
        }

        public static void main(String[] args) throws IOException {
            testClient();
        }
    }

    static class Server {
        public static void testServer() throws IOException {

            // 1、获取Selector选择器
            Selector selector = Selector.open();

            // 2、获取通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 3.设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            // 4、绑定连接
            serverSocketChannel.bind(new InetSocketAddress(5555));

            // 5、将通道注册到选择器上,并注册的操作为：“接收”操作
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            // 6、采用轮询的方式，查询获取“准备就绪”的注册过的操作   key  select(): 阻塞到至少有一个通道在你注册的事件上就绪了。
            while (selector.select() > 0) {
                // 7、获取当前选择器中所有注册的选择键（“已经准备就绪的操作”）
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    // 8、获取“准备就绪”的事件
                    SelectionKey selectedKey = selectedKeys.next();

                    // 9、判断key是具体的什么事件
                    if (selectedKey.isAcceptable()) {
                        // 10、若接受的事件是 "接收就绪" 操作, key  就获取客户端连接  【实际上就是获取这个连接 channel，这个 channel 对客户端服务端来说是一样的】
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        // 11、切换为非阻塞模式
                        socketChannel.configureBlocking(false);
                        // 12、将该通道注册到selector选择器上
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                    else if (selectedKey.isReadable()) {
                        // 13、获取该选择器上的 "读就绪" 状态的通道    key  selectedKey.channel() 获取通道
                        SocketChannel socketChannel = (SocketChannel) selectedKey.channel();

                        // 14、读取数据
                        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
                        int length = 0;
                        // socketChannel.read(byteBuffer)) 是往 byteBuffer 写，对应的就是刚 allocate 的 byteBuffer 就是写模式
                        while ((length = socketChannel.read(byteBuffer)) != -1) {
                            // 转为 读模式
                            byteBuffer.flip();
                            System.out.println(new String(byteBuffer.array(), 0, length));
                            // 转为 写模式
                            byteBuffer.clear();
                        }
                        socketChannel.close();
                    }
                    // 15、移除选择键
                    selectedKeys.remove();
                }
            }
            // 7、关闭连接
            serverSocketChannel.close();
        }
        public static void main(String[] args) throws IOException {
            testServer();
        }
    }
}
