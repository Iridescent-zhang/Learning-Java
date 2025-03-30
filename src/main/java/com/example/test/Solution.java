package com.example.test;

import com.example.interview.ioStream.ReaderExample;
import io.netty.channel.nio.NioEventLoop;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectableChannel;
import java.util.*;
import java.util.concurrent.*;
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
    public static void main(String[] args) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, ExecutionException, InterruptedException {

        String replace = new String(new char[10]).replace("\0", "_");
        System.out.println("replace = " + replace);

        String str = "|sdhwjk|sdjhwa|";
        String[] split = str.split("\\|");
        String s = Arrays.toString(split);
        System.out.println("s = " + s);


        String s1 = str.replaceAll("\\|", "*");
        System.out.println("s1 = " + s1);

    }
}
