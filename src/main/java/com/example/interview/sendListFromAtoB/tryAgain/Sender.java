package com.example.interview.sendListFromAtoB.tryAgain;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.sendListFromAtoB.tryAgain
 * @ClassName : .java
 * @createTime : 2024/12/30 16:25
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class Sender {
    public static void sender(String host, int port, ListNode head) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {

            objectOutputStream.writeObject(head);
//            objectOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1, null);
        head.next = new ListNode(2, new ListNode(3,null));
        sender("127.0.0.1", 6000, head);
    }
}