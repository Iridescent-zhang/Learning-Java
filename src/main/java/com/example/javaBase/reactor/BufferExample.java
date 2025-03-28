package com.example.javaBase.reactor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.javaBase.reactor
 * @ClassName : .java
 * @createTime : 2025/3/28 18:11
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Java NIOExample BufferExample
 * key  Buffer 是一个是 抽象类，位于java.nio包中，主要用作缓冲区。Buffer缓冲区本质上是一块可以 写入数据，然后可以从中读取数据的内存。
 * 这块内存被包装成 NIOExample BufferExample 对象，并提供了一组方法，用来方便的访问该块内存。【BufferExample 本质是可读写内存】 注意：Buffer是非线程安全类。
 *
 * 在 NIO 中主要有八种缓冲区类，分别如下: key  他们竟然全都是抽象类
 * ByteBuffer        // key  用的最多的了
 * CharBuffer
 * DoubleBuffer
 * FloatBuffer
 * IntBuffer
 * LongBuffer
 * ShortBuffer
 * MappedByteBuffer 继承自 ByteBuffer // key  RocketMQ主要通过MappedByteBuffer对文件进行读写操作
 * key  这些 Buffer 覆盖了能从 IO 中传输的所有的 Java 基本数据类型。其中MappedByteBuffer是专门用于内存映射的一种ByteBuffer
 *
 * key  原理
 * Buffer在内部也是利用 byte[] 作为内存缓冲区，只不过多提供了一些标记变量属性而已。当多线程访问的时候，可以清楚的知道当前数据的位置。
 * 有三个重要的标记属性：capacity、position、limit。
 * 除此之外，还有一个标记属性：mark，可以临时保持一个特定的 position，需要的时候，可以恢复到这个位置。
 * 1.1.1. capacity
 * 作为一个内存块，Buffer有一个固定的大小值，也叫 "capacity"。你只能往里写capacity个数据。一旦Buffer满了，就不能再写入。
 * capacity与缓存的数据类型相关。key  指的不是内存的字节的数量，而是写入的对象的数量。
 * 比如使用的是一个保存 double 类型的 Buffer（DoubleBuffer），写入的数据是double类型， 如果其 capacity 是100，那么我们最多可以写入 100个 double 数据.
 * capacity一旦初始化，就不能不会改变。原因是什么呢？
 * Buffer对象在初始化时，会按照capacity分配内部的内存。内存分配好后，大小就不能变了。
 * key  分配内存时，一般使用 Buffer 的抽象子类 ByteBuffer.allocate() 方法，[实际上是生成 ByteArrayBuffer 类]。？
 *
 * 1.1.2. position
 * position 表示当前的位置。position 在 Buffer 的两种模式下的值是不同的。
 * 读模式下的 position 的值为：
 * 当读取数据时，也是从 position 位置开始读。当将Buffer从写模式切换到读模式，position会被重置为 0。当从Buffer的position处读取数据时，position向前移动到下一个可读的位置。
 * 写模式下的 position 的值为：
 * 在写模式下，当写数据到Buffer中时，position表示当前的写入位置。初始的position值为0，position最大可为capacity – 1。
 * 每当一个数据（byte、long等）写到Buffer后， position会向后移动到下一个可插入数据的可写的位置。
 *
 * 1.1.3. limit
 * limit表示最大的限制。在Buffer的两种模式下，limit的值是不同的。
 * 读模式下的limit的值为：
 * 读模式下，Buffer的 limit 表示最多能从Buffer里读多少数据。当Buffer从写切换到读模式时，limit的值，设置成写模式的 position 值【因为刚写完嘛】，也就是写模式下之前写入的数量值。
 * 举一个简单的例子，说明一下读模式下的limit值：
 * 先向Buffer写数据，Buffer在写模式。每写入一个数据，position向后面移动一个位置，值加一。假定写入了5个数，当写入完成后，position的值为5。这时，就可以读取数据了。当开始读取数据时，Buffer切换到读模式。limit的值，先会被设置成写入数据时的position值。这里是5，表示可以读取的最大限制是5个数。
 * 写模式下的limit的值为：
 * limit表示可以写入的数据最大限制。在切换成写模式时，limit的值会被更改，设置成Buffer的capacity，即为Buffer的容量。
 *
 * 在Buffer的四个属性之间，有一个简单的数量关系，如下：
 * capacity>=limit>=position>=mark>=0
 * capacity	容量，即可以容纳的最大数据量；在缓冲区创建时被设定并且不能改变
 * limit	上界，缓冲区中当前数据量
 * position	位置，下一个要被读或写的元素的索引
 * mark    （位置标记）	调用mark(pos)来设置mark=pos，再调用reset()可以让position恢复到标记的位置即position=mark
 *
 * 实例，包含了从 Buffer 实例的 key  获取、写入、读取、重复读、标记和重置等一个系列操作。
 *
 * 总结一下，使用 NIO Buffer 的步骤如下:
 * 一：将数据写入到 Buffer 中；
 * 二：key  调用 Buffer.flip()方法，将 NIO Buffer 转换为读模式；
 * 三：从 Buffer 中读取数据；
 * 四：key  调用 Buffer.clear() 或 Buffer.compact()方法，将 Buffer 转换为写模式。
 */
public class BufferExample {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);
        // 调用allocate分配内存后，buffer 处于 写模式（往 byteBuffer 里写）
        System.out.println("byteBuffer.capacity() = " + byteBuffer.capacity());
        System.out.println("byteBuffer.position() = " + byteBuffer.position());
        System.out.println("byteBuffer.limit() = " + byteBuffer.limit());
        System.out.println("--------------------------");
        // Flips this buffer. The limit is set to the current position and then the position is set to zero.【切换为从 byteBuffer 读数据的模式了】
        // After a sequence of channel-read or put operations【往 byteBuffer 里写】
        // invoke this method to prepare for a sequence of channel-write or relative get operations【从 byteBuffer 里读出来】
        // key 转为读模式
        byteBuffer.flip();
        System.out.println("byteBuffer.position() = " + byteBuffer.position());
        System.out.println("byteBuffer.limit() = " + byteBuffer.limit());
        System.out.println("--------------------------");

        // key  除了新建的buffer是写模式，如何将buffer切换成写模式呢？调用 buffer.clear() 清空或 buffer.compact() 压缩方法，可以将 Buffer 转换为写模式。
        // key  可以通过buffer的put方法写入数据。put方法有一个要求，需要写入的数据类型与Buffer的类型一致。
        // 将没读的数据清空了不就可以是写模式了吗
        // key Clears this buffer. The position is set to zero, the limit is set to the capacity
        byteBuffer.clear();
        for (int i = 0; i < 10; i++) {
            byteBuffer.put((byte) 127);
        }
        System.out.println("byteBuffer.position() = " + byteBuffer.position());
        System.out.println("byteBuffer.limit() = " + byteBuffer.limit());
        System.out.println("--------------------------");

        byteBuffer.flip();
        for (int i = 0; i < 10; i++) {
            System.out.println("byteBuffer.get() = " + byteBuffer.get());
        }
        System.out.println("byteBuffer.position() = " + byteBuffer.position());
        System.out.println("byteBuffer.limit() = " + byteBuffer.limit());
        System.out.println("--------------------------");

        // 将 position 置零，The position is set to zero ，可以重新读或重新写
        byteBuffer.rewind();
        System.out.println("byteBuffer.position() = " + byteBuffer.position());
        System.out.println("byteBuffer.limit() = " + byteBuffer.limit());
        System.out.println("--------------------------");

        // Resets this buffer's position to the previously-marked position.
        byteBuffer.reset();

    }

}
