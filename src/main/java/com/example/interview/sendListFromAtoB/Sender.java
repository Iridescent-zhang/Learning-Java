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
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Sender {
    public static void sendList(ListNode head, String host, int port) {
        try (Socket socket = new Socket(host, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {

            oos.writeObject(head);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Example to create a list: 1 -> 2 -> 3
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);

        // Send the list to computer B
        sendList(head, "127.0.0.1", 5000); // Using localhost and port 5000 as an example
    }
}
