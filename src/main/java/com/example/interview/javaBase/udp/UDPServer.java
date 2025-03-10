package com.example.interview.javaBase.udp;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.reliableUDP
 * @ClassName : .java
 * @createTime : 2025/3/5 12:33
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDP 协议本身是非可靠的，不保证数据传输的完整性和顺序性。要在 UDP 基础上实现可靠传输，可以借鉴 TCP 协议的机制：
 *  key 确认应答（ACK）：发送方发送数据包后，接收方接收到数据包后发送 ACK 确认消息，发送方仅在接收到确认消息后认为数据传输成功。
 *  key 重传机制：发送方设定一个超时时间，在超时后没有收到接收方的 ACK 确认，则重传数据包。
 *  key 序列号：每个数据包附带一个序列号，用于接收方排列正确的数据顺序，并检测丢失数据包（序列号相当于包的标识，没有序列号怎么知道丢的是哪个包）。
 *  key 窗口机制：可以实现滑动窗口机制，控制数据包的发送速率和流量控制。
 */
public class UDPServer {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        byte[] sendData = "Hello".getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 9876);
        socket.send(sendPacket);

        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket); //等待ACK
        String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
        if (response.equals("ACK")) {
            // 数据传输成功
        }
    }
}
