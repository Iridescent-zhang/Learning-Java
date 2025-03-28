package com.example.interview.directMemory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.DirectMemory
 * @ClassName : .java
 * @createTime : 2025/1/9 16:59
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIODirectBufferExample {
    // TODO NIOExample 库的一个重要特性之一就是对直接内存（直接缓冲区）的支持，即 ByteBuffer 的 allocateDirect 方法。
    /**
     * NIOExample 确实包括非阻塞 I/O（如 SelectableChannel 和 Selector），但它的核心内容还包括缓冲区（BufferExample），通道（Channel）和选择器（Selector）。在这里，我们主要关注的是缓冲区和通道，它们可以用于高效地处理 I/O 操作。
     * ByteBuffer 的 allocateDirect 方法用于分配一个直接字节缓冲区，它将内存分配在 JVM 堆外，减少数据拷贝操作，并且可以更高效地利用操作系统的 I/O 机制。
     */

    public static void main(String[] args) {
        RandomAccessFile file = null;
        FileChannel fileChannel = null;

        try {
            // 创建文件输入流
            file = new RandomAccessFile("input.txt", "r");
            fileChannel = file.getChannel();

            // 分配直接字节缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);

            // 此时bytesRead是写模式，然后写数据到bytesRead
            int bytesRead = fileChannel.read(byteBuffer);

            while (bytesRead != -1) {
                // 切换缓冲区为读模式
                byteBuffer.flip();

                while (byteBuffer.hasRemaining()) {
                    System.out.print((char) byteBuffer.get());
                }

                // 清空缓冲区，准备进行下一次读操作
                byteBuffer.clear();
                bytesRead = fileChannel.read(byteBuffer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileChannel != null) {
                    fileChannel.close();
                }
                if (file != null) {
                    file.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
