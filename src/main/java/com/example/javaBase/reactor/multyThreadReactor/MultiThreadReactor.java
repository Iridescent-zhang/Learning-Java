package com.example.javaBase.reactor.multyThreadReactor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.javaBase.reactor.multyThreadReactor
 * @ClassName : .java
 * @createTime : 2025/3/25 20:22
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

/**
 * 多线程 Reactor
 * 在单线程 Reactor 模式基础上，做如下改进：
 * （1）将 Handler 处理器的执行放入线程池，多线程进行业务处理。
 * （2）而对于 Reactor 而言，可以仍为单个线程。key  如果服务器为多核的 CPU，为充分利用系统资源，可以将 Reactor 拆分为两个线程。
 */
public class MultiThreadReactor {
    public static void main(String[] args) throws IOException {
        MthreadReactor mthreadReactor = new MthreadReactor(6666);
        Thread thread = new Thread(mthreadReactor);
        thread.run();
    }
}

class MthreadReactor implements Runnable {
    // subReactors 集合, 一个 selector 代表一个 subReactor
    Selector[] selectors = new Selector[2];
    int next = 0;
    final ServerSocketChannel serverSocket;

    MthreadReactor(int port) throws IOException {
        // Reactor 初始化
        selectors[0]=Selector.open();
        selectors[1]= Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        // 非阻塞
        serverSocket.configureBlocking(false);


        //分步处理,第一步,接收accept事件
        SelectionKey sk =
                serverSocket.register( selectors[0], SelectionKey.OP_ACCEPT);
        //attach callback object, Acceptor
        sk.attach(new Acceptor());
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                for (int i = 0; i < 2 ; i++){
                    selectors[i].select();
                    Set<SelectionKey> selected =  selectors[i].selectedKeys();
                    Iterator<SelectionKey> it = selected.iterator();

                    while (it.hasNext()) {
                        //Reactor负责dispatch收到的事件
                        dispatch(it.next());
                    }
                    selected.clear();
                }
            }
        } catch (IOException ex)
        { /* ... */ }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) (k.attachment());
        // 调用之前注册的 callback 对象
        if (r != null) {
            r.run();
        }
    }


    class Acceptor { // ...
        public synchronized void run() throws IOException {
            SocketChannel connection =
                    serverSocket.accept(); // 主 selector 负责accept
            if (connection != null) {
                new MthreadHandler(selectors[next], connection); // 选个 subReactor 去负责接收到的connection
            }
            if (++next == selectors.length) next = 0;
        }
    }

}

class MthreadHandler implements Runnable {
    final SocketChannel channel;
    final SelectionKey selectionKey;
    ByteBuffer input = ByteBuffer.allocate(256);
    ByteBuffer output = ByteBuffer.allocate(256);
    static final int READING = 0, SENDING = 1;
    int state = READING;


    ExecutorService pool = Executors.newFixedThreadPool(2);
    static final int PROCESSING = 3;

    MthreadHandler(Selector selector, SocketChannel c) throws IOException {
        channel = c;
        channel.configureBlocking(false);
        // Optionally try first read now
        selectionKey = channel.register(selector, 0);

        //将Handler作为callback对象
        selectionKey.attach(this);

        //第二步,注册Read就绪事件
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    boolean inputIsComplete() {
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


    synchronized void read() throws IOException {
        // ...
        channel.read(input);
        if (inputIsComplete()) {
            state = PROCESSING;
            // 使用线程 pool 异步执行
            pool.execute(new Processer());
        }
    }

    void send() throws IOException {
        channel.write(output);

        //write完就结束了, 关闭selectionKey
        if (outputIsComplete()) {
            selectionKey.cancel();
        }
    }

    synchronized void processAndHandOff() {
        process();
        state = SENDING;
        // or rebind attachment
        //process 完,开始等待write就绪
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    class Processer implements Runnable {
        public void run() {
            processAndHandOff();
        }
    }
}
