package com.example.interview.sendListFromAtoB.tryAgain;

import com.example.interview.netty.EchoClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.sendListFromAtoB.tryAgain
 * @ClassName : .java
 * @createTime : 2024/12/30 16:38
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class Receiver {
    public static ListNode receiver(int port) {
        ListNode head = null;
        try(ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {

            head = (ListNode)objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return head;
    }

    public static void main(String[] args) {
        ListNode head = receiver(6000);

        while (head!=null) {
            System.out.println("head = " + head.val);
            head = head.next;
        }
    }
}
