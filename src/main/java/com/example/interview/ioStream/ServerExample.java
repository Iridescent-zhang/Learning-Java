package com.example.interview.ioStream;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.ioStream
 * @ClassName : .java
 * @createTime : 2025/1/6 16:36
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class ServerExample {
    /**
     * TODO getInputStream 和 getOutputStream 方法是 Socket 类的方法，用于获取与客户端通信的输入输出字节流（InputStream和OutputStream对象）。
     * 要通过这两个对象进行通信，需要将它们包装成更高级的Reader或Writer对象。
     * 例如，通过getInputStream可以获得一个字节输入流，再使用InputStreamReader将其转化为字符流，还可进一步包装为BufferedReader从而按行读取：
     *
     * TODO ServerSocket 是服务端专用的监听 socket（需要绑定端口），当客户端建立连接时使用的是 serverSocket.accept() 返回的连接socket
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server started...");

            while (true) {
                try (   // 连接 socket
                        Socket clientSocket = serverSocket.accept();
                     // 输入流
                     InputStream inputStream = clientSocket.getInputStream();
                     InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                     // 输出流
                     OutputStream outputStream = clientSocket.getOutputStream();
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                     PrintWriter printWriter = new PrintWriter(outputStreamWriter);) {

                    String message = bufferedReader.readLine();
                    System.out.println("Received: " + message);
                    printWriter.println("EchoToClient: " + message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
