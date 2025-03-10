package com.example.interview.sendListFromAtoB;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.sendListFromAtoB
 * @ClassName : .java
 * @createTime : 2024/12/30 15:45
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    public static ListNode receiveList(int port) {
        ListNode head = null;
        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept();
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            head = (ListNode) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return head;
    }

    public static void main(String[] args) {
        // Receive the list at port 5000
        ListNode head = receiveList(5000);

        // Print the received list
        while (head != null) {
            System.out.print(head.value + " ");
            head = head.next;
        }
    }
}
