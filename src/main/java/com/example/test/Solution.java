package com.example.test;

import io.netty.channel.nio.NioEventLoop;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectableChannel;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.test
 * @ClassName : .java
 * @createTime : 2025/1/16 16:30
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
class Solution {
    final int a = 5;
    static  long[][] arr;
    public static void main(String[] args) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("cores = " + cores);
        int activeThreadCount = ForkJoinPool.commonPool().getActiveThreadCount();
        System.out.println("activeThreadCount = " + activeThreadCount);
        int poolSize = ForkJoinPool.commonPool().getPoolSize();
        System.out.println("poolSize = " + poolSize);
        int parallelism = ForkJoinPool.commonPool().getParallelism();
        System.out.println("parallelism = " + parallelism);

        ByteBuffer allocate = ByteBuffer.allocate(100);
        File file = new File("");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        FileChannel channel = randomAccessFile.getChannel();
        channel.read(allocate);
    }
}
