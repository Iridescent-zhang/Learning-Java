package com.example.interview;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview
 * @ClassName : .java
 * @createTime : 2025/3/17 0:07
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * 大端序（Big-endian）和小端序（Little-endian）是两种在存储数据时，决定数据的字节顺序的方式，这在计算机体系结构中非常重要。
 * (1) 大端序（Big-endian）
 * 大端序是指数据的高位字节存储在低地址处，低位字节存储在高地址处。
 * 形象地说，大端序像是从左到右依次书写的顺序。
 * (2) 小端序（Little-endian）
 * 小端序是指数据的低位字节存储在低地址处，高位字节存储在高地址处。
 * 形象地说，小端序像是从右到左依次书写的顺序。
 *
 * 2. 举例说明
 * 假设有一个 32 位的整数 0x12345678（16 进制表示），以下展示大端序和小端序的存储方式：
 * (1) 大端序（Big-endian）
 * 内存地址从低到高：
 * 地址：  0x00    0x01    0x02    0x03
 * 数据：  0x12    0x34    0x56    0x78
 *
 * (2) 小端序（Little-endian）
 * 内存地址从低到高：
 * 地址：  0x00    0x01    0x02    0x03
 * 数据：  0x78    0x56    0x34    0x12
 *
 * 3. 端序应用场景
 * (1) 网络字节序
 * 网络字节序一般采用大端序（Big-endian），即数据从高位到低位进行传输。这也是为什么在网络编程中，经常需要将主机字节序（Host Byte Order）转换为网络字节序（Network Byte Order）。
 * (2) 主机字节序
 * 计算机的处理器在存储数据时可能使用不同的字节顺序。
 * Intel x86 体系结构通常使用小端序（Little-endian）。
 * PowerPC 和 SPARC 等体系结构通常使用大端序（Big-endian）。
 *
 * 4. 转换函数
 * 在 Java 和其他编程语言中，可以使用一些实用的转换函数来进行字节序之间的转换。
 * Java 的 java.nio.ByteBuffer 类提供了一些实用的方法来处理大端序和小端序的数据。
 */
public class Endian {
    public static void main(String[] args) {
        int value = 0x12345678;  // 32位整数，4字节

        // 大端序
        ByteBuffer bufferBigEndian = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        bufferBigEndian.putInt(value);
        byte[] bigEndianBytes = bufferBigEndian.array();
        System.out.println("Big-endian: " + bytesToHex(bigEndianBytes));

        // 小端序
        ByteBuffer bufferLittleEndian = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        bufferLittleEndian.putInt(value);
        byte[] littleEndianBytes = bufferLittleEndian.array();
        System.out.println("Little-endian: " + bytesToHex(littleEndianBytes));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}

class StringEncodingExample {
    public static void main(String[] args) {
        try {
            String str = "Hello, 世界!";

            // 将字符串转换为字节数组，使用 UTF-8 编码
            byte[] utf8Bytes = str.getBytes("UTF-8");
            // 打印字节数组
            System.out.print("UTF-8 编码字节数组: ");
            for (byte b : utf8Bytes) {
                System.out.printf("%02X ", b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
