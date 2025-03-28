package com.example.javaBase.reactor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.javaBase.reactor
 * @ClassName : .java
 * @createTime : 2025/3/28 17:35
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Channel类型
 * 通道总是基于缓冲区Buffer来读写。和 BufferExample 绑定的
 *
 * 下面列出Java NIO中最重要的集中Channel的实现：
 * （1）FileChannel
 * （2）DatagramChannel
 * （3）SocketChannel
 * （4）ServerSocketChannel
 *
 * 四种通道的说明如下：
 * FileChannel用于文件的数据读写。
 * DatagramChannel用于UDP的数据读写。
 * SocketChannel用于TCP的数据读写。
 * ServerSocketChannel允许我们监听TCP链接请求，每个请求会创建会一个SocketChannel。
 *
 * FileChannel
 * FileChannel 是操作文件的Channel，我们可以通过 FileChannel 从一个文件中读取数据，也可以将数据写入到文件中。
 *
 * SocketChannel
 * 有两种Socket通道，一个是客户端的SocketChannel，一个是负责服务器端的Socket通道ServerSocketChannel。SocketChannel与OIO中的Socket类对应，ServerSocketChannel对应于OIO中的ServerSocket类
 *
 * ServerSocketChannel
 * ServerSocketChannel 顾名思义，是用在服务器为端的，可以监听客户端的 TCP 连接
 *
 * DatagramChannel 是用来处理 UDP 连接的.
 *
 */
public class NIOExample {
    public static void main(String[] args) throws IOException {
        // key 只能阻塞模式
        // 操作一：打开 FileChannel通道
        RandomAccessFile aFile = new RandomAccessFile("test.txt", "rw");
        FileChannel fileChannel = aFile.getChannel();
        // 操作二：读取数据
        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = fileChannel.read(buf);
        // 操作三：写入数据
        buf.clear();
        String newData = "New String to write to file..." + System.currentTimeMillis();
        buf.put(newData.getBytes());
        buf.flip();
        while(buf.hasRemaining())
        {
            fileChannel.write(buf);
        }
        // 操作四：关闭  当我们对 FileChannel 的操作完成后，必须将其关闭。
        fileChannel.close();
        // 操作五：强制刷新磁盘  FileChannel的force()方法将所有未写入的数据从通道刷新到磁盘中。在你调用该force()方法之前，出于性能原因，操作系统可能会将数据缓存在内存中，因此您不能保证写入通道的数据实际上写入磁盘。
        fileChannel.force(true);

        // key 阻塞模式
        // 操作一：创建  这个是客户端的创建。当一个服务器端的ServerSocketChannel 接受到连接请求时，也会返回一个 SocketChannel 对象。
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("example.com", 80));
        // 操作二：读取  如果 read()返回 -1，那么表示连接中断了.
        buf = ByteBuffer.allocate(48);
        bytesRead = socketChannel.read(buf);
        // 操作三：写入数据
        newData = "New String to write to file..." + System.currentTimeMillis();
        buf = ByteBuffer.allocate(48);
        buf.clear();
        buf.put(newData.getBytes());   // 在 buf 中放入数据
        buf.flip();
        while(buf.hasRemaining()) {
            socketChannel.write(buf);
        }
        // 操作四：关闭
        socketChannel.close();

        // key 我们可以设置 SocketChannel 为异步模式，这样我们的 connect，read，write 都是异步的了.
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 80));
        // 在异步模式中，或许连接还没有建立，socketChannel.connect 方法就返回了，因此我们不断的自旋，检查当前是否是连接到了主机。
        while(! socketChannel.finishConnect() ){
            //wait，or do something else...
            break;
        }
        // 操作二：非阻塞读写  在异步模式下，读写的方式是一样的，但在读取时，因为是异步的，因此我们必须检查 read 的返回值，来判断当前是否读取到了数据.

        // key ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        while(true){
            // 我们可以使用ServerSocketChannel.accept()方法来监听客户端的 TCP 连接请求，accept()方法会阻塞，直到有连接到来，当有连接时，这个方法会返回一个 SocketChannel 对象:
            SocketChannel retSocketChannel = serverSocketChannel.accept();
            if(socketChannel != null){
                //do something with socketChannel...
                break;
            }
        }
        serverSocketChannel.close();
    }
}
