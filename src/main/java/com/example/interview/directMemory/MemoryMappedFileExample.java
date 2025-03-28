package com.example.interview.directMemory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.DirectMemory
 * @ClassName : .java
 * @createTime : 2025/1/9 16:06
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MemoryMappedFileExample {
    public static void main(String[] args) throws Exception {
        //TODO Java 支持使用直接内存（也称为本地内存、堆外内存），这是通过 NIOExample（New Input/Output）库实现的。直接内存的优势在于它绕过了 JVM 堆内存，从而减少一次拷贝操作并提高性能。
        RandomAccessFile file = new RandomAccessFile("input.txt", "rw");
        FileChannel fileChannel = file.getChannel();

        // 将文件映射到内存
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileChannel.size());

        // 读取和写入数据
        while(mappedByteBuffer.hasRemaining()) {
            System.out.print((char)mappedByteBuffer.get());
        }

        // 关闭通道和文件
        fileChannel.close();
        file.close();
    }
}
