package com.example.javaBase.udp;

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

public class UDPClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
        byte[] sendData = "ACK".getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
        socket.send(sendPacket); //发送ACK
    }
}
