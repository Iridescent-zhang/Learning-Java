package com.example.interview.tcpWebServer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : lczhang
 * @version : 1.0
 * @Project : Learning-Project
 * @Package : com.example.interview.tcp
 * @ClassName : .java
 * @createTime : 2025/1/6 12:32
 * @Email : lczhang93@gmail.com
 * @Website : https://iridescent-zhang.github.io
 * @Description :
 */
import java.io.*;
import java.net.*;

public class SimpleWebServer {
    public static void main(String[] args) {
        // 监听端口12345
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Web server started, listening on port " + port);

            while (true) {
                // 接受客户端连接
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    // 处理请求
                    handleRequest(clientSocket);
                } catch (IOException e) {
                    System.err.println("Error handling client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    private static void handleRequest(Socket clientSocket) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter output = new PrintWriter(clientSocket.getOutputStream());

        // 读取HTTP请求头
        String requestLine = input.readLine();
        System.out.println("Request: " + requestLine);

        while (true) {
            String headerLine = input.readLine();
            if (headerLine == null || headerLine.isEmpty()) {
                break;
            }
            System.out.println(headerLine);
        }

        // 生成HTTP响应
        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                "<html><body><h1>Hello, World!</h1></body></html>";

        // 发送响应
        output.print(httpResponse);
        output.flush();

        // 关闭连接
        input.close();
        output.close();
    }
}
