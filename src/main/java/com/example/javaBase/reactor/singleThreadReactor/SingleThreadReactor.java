package com.example.javaBase.reactor.singleThreadReactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.javaBase.reactor.SingleThreadReactor
 * @ClassName : .java
 * @createTime : 2025/3/25 19:00
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 最简单的单 Reactor 单线程模型。Reactor线程是个多面手，负责多路分离套接字，Accept新连接，并分派请求到 Handler 处理器中。
 * 【关键】 key  Reactor和 Handler 处于一条线程执行。
 *
 * 单线程模式的缺点:
 * 1、 当其中某个 handler 阻塞时，会导致其他所有的 client 的 handler 都得不到执行， 并且更严重的是， handler 的阻塞也会导致整个服务不能接收新的 client 请求(因为 acceptor 也被阻塞了)。key  Reactor和 Handler 处于一条线程执行。
 *     因为有这么多的缺陷， 因此单线程 Reactor 模型用的比较少。这种单线程模型不能充分利用多核资源，所以实际使用的不多。
 * 2、因此，单线程模型仅仅适用于 handler 中业务处理组件能快速完成的场景。
 */
public class SingleThreadReactor {
    public static void main(String[] args) throws IOException {
        Reactor reactor = new Reactor(6666);
        Thread thread = new Thread(reactor);
        thread.run();
    }
}

class Reactor implements Runnable{
    final Selector selector;
    final ServerSocketChannel serverSocket;

    Reactor(int port) throws IOException {
        //Reactor初始化
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        //非阻塞
        serverSocket.configureBlocking(false);

        //分步处理,第一步,接收accept事件
        SelectionKey sk =
                serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        //attach callback object, Acceptor   key  这个 Acceptor 同样也是绑定到这个 SelectionKey，专门用来对这个通道的 ACCEPT 事件进行处理
        sk.attach(new Acceptor());
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                // 选择一组键，其相应的通道已为 I/O 操作做准备
                selector.select();

                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();

                while (it.hasNext()) {
                    // Reactor 负责 dispatch 收到的事件
                    dispatch((SelectionKey) (it.next()));
                }
                selected.clear();
            }
        } catch (IOException ex)
        { /* ... */ }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) (k.attachment());
        //调用之前注册的 callback 对象  key  这里应该既可以是 Acceptor 也可以是 Handler
        if (r != null) {
            r.run();
        }
    }

    // inner class
    class Acceptor implements Runnable {
        public void run() {
            try {
                SocketChannel channel = serverSocket.accept();
                if (channel != null)  // key  每次有新连接进来都会创建一个 Handler
                    new Handler(selector, channel);
            } catch (IOException ex)
            { /* ... */ }
        }
    }
}

/**
 * SelectionKey：它表示一个通道与选择器（Selector）之间的注册关系。每个 SelectionKey 包含若干兴趣集。
 * channel.register(selector, 感兴趣符号);
 * register 方法：该方法把通道注册到选择器上，同时设置它的兴趣集，即这个通道希望关注的事件类型。
 * register 方法的第二个参数是该通道感兴趣的操作的集合。可以设置为以下常量之一或它们的组合：
 * SelectionKey.OP_CONNECT - 连接就绪事件   1 << 2
 * SelectionKey.OP_ACCEPT - 接收就绪事件    1 << 1
 * SelectionKey.OP_READ - 读就绪事件   OP_READ = 1 << 0
 * SelectionKey.OP_WRITE - 写就绪事件       1 << 3【瞎写】
 */
class Handler implements Runnable{
    final SocketChannel channel;
    final SelectionKey sk;
    ByteBuffer input = ByteBuffer.allocate(256);
    ByteBuffer output = ByteBuffer.allocate(256);
    static final int READING = 0, SENDING = 1;
    int state = READING;


    Handler(Selector selector, SocketChannel c) throws IOException{
        channel = c;
        c.configureBlocking(false);
        // key 示例中设置为 0，表示初始注册时没有特定的兴趣事件。这样做的目的是为了在注册完通道到选择器后，马上进行特定的初始化操作或检查。
        sk = channel.register(selector, 0);

        // key  将这个 Handler 作为 callback 对象
        // key  也就是将 Handler 与 SelectionKey 绑定，而 SelectionKey 又和对应的 Channel 有紧密联系，Channel 对某些事件(比如读、写)感兴趣，所以这个 Handler 就专门负责这个事件的处理
        sk.attach(this);

        // 第二步,注册 Read 就绪事件  key  使用 interestOps 方法来明确地设置感兴趣的事件，这里是 OP_READ，即通道准备好读操作时选择器会通知。
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    boolean inputIsComplete(){
        /* ... */
        return false;
    }

    boolean outputIsComplete() {
        /* ... */
        return false;
    }

    void process() {
        /* ... */
        return;
    }

    public void run() {
        try {
            if (state == READING) {
                read();
            }
            else if (state == SENDING) {
                send();
            }
        } catch (IOException ex)
        { /* ... */ }
    }

    void read() throws IOException {
        channel.read(input);
        if (inputIsComplete()) {

            process();

            state = SENDING;
            // Normally also do first write now

            //第三步,接收 write 就绪事件
            sk.interestOps(SelectionKey.OP_WRITE);
        }
    }

    void send() throws IOException {
        channel.write(output);

        //write完就结束了, 关闭 selectionKey
        if (outputIsComplete()) {
            sk.cancel();
        }
    }
}
