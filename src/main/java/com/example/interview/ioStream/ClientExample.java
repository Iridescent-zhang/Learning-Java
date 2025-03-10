package com.example.interview.ioStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.ioStream
 * @ClassName : .java
 * @createTime : 2025/1/6 16:52
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
public class ClientExample {
    public static void main(String[] args) {
        // TODO PrintWriter 用 OutputStream 构造或 OutputStreamWriter 构造都没问题，都能将字符流转为字节流，用 OutputStreamWriter 构造的话层次更清晰
        try (Socket socket = new Socket("localhost", 12345);  // 这里是要连接的服务器的ip和端口
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            output.println("Hello from Client!");
            output.println("");  // 空行表示HTTP请求头结束
            String response = input.readLine();
            System.out.println("Server response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
